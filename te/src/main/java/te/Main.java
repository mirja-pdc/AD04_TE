package te;

import entidad.Actividad;
import entidad.Cliente;
import entidad.Monitor;
import entidad.Reserva;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
    	
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("dbactivaPU");
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n--- GESTIÓN DE ACTIVIDADES - MENU ---");
            System.out.println("1. Crear monitor");
            System.out.println("2. Crear actividad");
            System.out.println("3. Buscar actividades disponibles");
            System.out.println("4. Crear cliente");
            System.out.println("5. Crear reserva");
            System.out.println("6. Cancelar reserva");
            System.out.println("7. Listar actividades");
            System.out.println("8. Salir");
            System.out.print("Elige una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                EntityManager em = emf.createEntityManager();

                switch (opcion) {
                    case 1:
                        crearMonitor(em, scanner);
                        break;
                    case 2:
                        crearActividad(em, scanner);
                        break;
                    case 3:
                        buscarActividades(em, scanner);
                        break;
                    case 4:
                        crearCliente(em, scanner);
                        break;
                    case 5:
                        crearReserva(em, scanner);
                        break;
                    case 6:
                        cancelarReserva(em, scanner);
                        break;
                    case 7:
                        listarActividades(em);
                        break;
                    case 8:
                        System.out.println("Saliendo de la aplicación...");
                        break;
                    default:
                        System.out.println("Opción no válida. Introduce un número del 1 al 8.");
                }
                em.close();

            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduce un número entero válido.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error en la aplicación: " + e.getMessage());
            }

        } while (opcion != 8);

        emf.close();
        scanner.close();
    }

    
    private static void crearMonitor(EntityManager em, Scanner scanner) {
    	
        System.out.print("Nombre del monitor: ");
        String nombre = scanner.nextLine();
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();

        if (nombre.trim().isEmpty() || especialidad.trim().isEmpty()) {
            System.out.println("Error: Los campos no pueden estar vacíos.");
            return;
        }

        em.getTransaction().begin();
        Monitor monitor = new Monitor(nombre, especialidad);
        em.persist(monitor);
        em.getTransaction().commit();

        System.out.println("¡Monitor creado con éxito! ID generado: " + monitor.getId());
    }
    

    private static void crearActividad(EntityManager em, Scanner scanner) {
    	
        System.out.print("Nombre de la actividad: ");
        String nombre = scanner.nextLine();
        System.out.print("Localidad: ");
        String localidad = scanner.nextLine();  
        System.out.print("Fecha y hora (formato dd/MM/yyyy HH:mm): ");
        String fechaStr = scanner.nextLine();   
        System.out.print("Plazas totales: ");
        int plazas = Integer.parseInt(scanner.nextLine());
        System.out.print("ID del monitor: ");
        Long monitorId = Long.parseLong(scanner.nextLine());

        if (plazas <= 0) {
            System.out.println("Error: Las plazas deben ser un número positivo.");
            return;
        }

        Monitor monitor = em.find(Monitor.class, monitorId);
        if (monitor == null) {
            System.out.println("Error: El monitor con ID " + monitorId + " no existe.");
            return;
        }

        try {
        	
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime fechaHora = LocalDateTime.parse(fechaStr, formatter);

            em.getTransaction().begin();
            Actividad actividad = new Actividad(nombre, localidad, fechaHora, plazas, monitor);
            em.persist(actividad);
            em.getTransaction().commit();

            System.out.println("¡Actividad creada con éxito! ID generado: " + actividad.getId());
            
        } catch (Exception e) {
        	
            if (em.getTransaction().isActive()) {
            	
            	em.getTransaction().rollback();
            }
            
            System.out.println("Error en el formato de fecha u operación: " + e.getMessage());
        }
    }
    

    private static void buscarActividades(EntityManager em, Scanner scanner) {
    	
        System.out.print("Localidad: ");
        String localidad = scanner.nextLine();
        System.out.print("Fecha (formato dd/MM/yyyy): ");
        String fechaStr = scanner.nextLine();
        System.out.print("Número de plazas necesarias: ");
        int plazasNecesarias = Integer.parseInt(scanner.nextLine());

        if (plazasNecesarias <= 0) {
            System.out.println("Error: Las plazas deben ser mayores que cero.");
            return;
        }
    
        try {
            // Convertir la fecha introducida al inicio y fin del día completo (00:00 a 23:59)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaBuscada = LocalDate.parse(fechaStr, dateFormatter);
            LocalDateTime inicioDia = fechaBuscada.atStartOfDay();
            LocalDateTime finDia = fechaBuscada.atTime(LocalTime.MAX);

            // Consulta HQL/JPQL parametrizada con los nombres exactos
            String jpql = "SELECT a FROM Actividad a WHERE LOWER(a.localidad) = LOWER(:loc) AND a.fechaHora BETWEEN :inicioDia AND :finDia AND a.plazasDisponibles >= :plazas ORDER BY a.fechaHora ASC";
            TypedQuery<Actividad> query = em.createQuery(jpql, Actividad.class);
            query.setParameter("loc", localidad);
            query.setParameter("inicioDia", inicioDia); 
            query.setParameter("finDia", finDia);       
            query.setParameter("plazas", plazasNecesarias);

            List<Actividad> lista = query.getResultList();

            if (lista.isEmpty()) {
                System.out.println("No se encontraron actividades disponibles para esa localidad, fecha y aforo.");
            } else {
                System.out.println("\n--- ACTIVIDADES DISPONIBLES ---");
                for (Actividad a : lista) {
                    System.out.println(a);
                }
            }
        } catch (Exception e) {
            System.out.println("Error en el formato de la fecha: " + e.getMessage());
        }
    }
    

    private static void crearCliente(EntityManager em, Scanner scanner) {
    	
        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (nombre.trim().isEmpty() || email.trim().isEmpty()) {
            System.out.println("Error: El nombre y el email no pueden estar vacíos.");
            return;
        }
        
        // Normalizar email: quitar espacios y pasar a minúsculas para comparar
        String emailNormalizado = email.trim().toLowerCase();
        
        // Comprobación sencilla del formato del correo
        if (!emailNormalizado.contains("@") || !emailNormalizado.contains(".")) {
            System.out.println("Error: El formato del correo electrónico no es válido.");
            return;
        }

        try {
        	
            // Comprobar si ya existe un cliente con ese mismo correo (ignorando mayúsculas/minúsculas)
            String jpql = "SELECT COUNT(c) FROM Cliente c WHERE LOWER(c.email) = :email";
            Long duplicados = em.createQuery(jpql, Long.class)
                           .setParameter("email", emailNormalizado)
                           .getSingleResult();

            if (duplicados > 0) {
                System.out.println("Error: Ya existe un cliente registrado con ese correo electrónico.");
                return;
            }

            // Si pasa las validaciones, procedemos a persistir
            em.getTransaction().begin();
            Cliente cliente = new Cliente(nombre.trim(), emailNormalizado);
            em.persist(cliente);
            em.getTransaction().commit();
            
            System.out.println("¡Cliente creado con éxito! ID generado: " + cliente.getId());
            
        } catch (Exception e) {
        	
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al crear el cliente: " + e.getMessage());
            
        }
        
    }
  

    private static void crearReserva(EntityManager em, Scanner scanner) {
    	
        System.out.print("ID del cliente: ");
        Long clienteId = Long.parseLong(scanner.nextLine());
        System.out.print("ID de la actividad: ");
        Long actividadId = Long.parseLong(scanner.nextLine());
        System.out.print("Número de plazas a reservar: ");
        int plazasReservar = Integer.parseInt(scanner.nextLine());

        if (plazasReservar <= 0) {
            System.out.println("Error: El número de plazas debe ser positivo.");
            return;
        }

        Cliente cliente = em.find(Cliente.class, clienteId);
        Actividad actividad = em.find(Actividad.class, actividadId);

        if (cliente == null || actividad == null) {
            System.out.println("Error: El cliente o la actividad no existen.");
            return;
        }

        if (actividad.getPlazasDisponibles() < plazasReservar) {
            System.out.println("Error: No hay suficientes plazas disponibles (Disponibles: " + actividad.getPlazasDisponibles() + ").");
            return;
        }

        // Transacción atómica: Guardar reserva y descontar plazas juntas
        em.getTransaction().begin();
        try {
        	
            actividad.setPlazasDisponibles(actividad.getPlazasDisponibles() - plazasReservar);
            Reserva reserva = new Reserva(LocalDateTime.now(), plazasReservar, actividad, cliente);
            em.persist(reserva);
            em.getTransaction().commit();
            System.out.println("¡Reserva creada con éxito! ID de Reserva: " + reserva.getId() + " | Plazas restantes en actividad: " + actividad.getPlazasDisponibles());
        
        } catch (Exception e) {
        	
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al procesar la reserva. Se han revertido los cambios: " + e.getMessage());
            
        }
        
    }
    

    private static void cancelarReserva(EntityManager em, Scanner scanner) {
    	
        System.out.print("ID de la reserva a cancelar: ");
        Long reservaId = Long.parseLong(scanner.nextLine());

        Reserva reserva = em.find(Reserva.class, reservaId);
        if (reserva == null) {
            System.out.println("Error: No existe ninguna reserva con ese ID.");
            return;
        }

        Actividad actividad = reserva.getActividad();
        int plazasDevueltas = reserva.getNumeroPlazas();

        // Transacción atómica: Devolver plazas y borrar reserva juntas
        em.getTransaction().begin();
        try {
        	
            actividad.setPlazasDisponibles(actividad.getPlazasDisponibles() + plazasDevueltas);
            // Se hace merge para actualizar las plazas disponibles tras cancelar la reserva 
            em.merge(actividad); 
            em.remove(reserva);
            em.getTransaction().commit();
            System.out.println("¡Reserva cancelada con éxito! Plazas recuperadas: " + plazasDevueltas + " | Plazas disponibles ahora: " + actividad.getPlazasDisponibles());
        
        } catch (Exception e) {
        	
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al cancelar la reserva. Se han revertido los cambios: " + e.getMessage());
        
        }
    
    }

    
    private static void listarActividades(EntityManager em) {
    	
        List<Actividad> lista = em.createQuery("SELECT a FROM Actividad a ORDER BY a.fechaHora ASC", Actividad.class).getResultList();

        if (lista.isEmpty()) {
            System.out.println("No hay actividades registradas.");
        } else {
            System.out.println("\n--- LISTADO DE ACTIVIDADES ---");
            for (Actividad a : lista) {
                System.out.println(a);
            }
        }
        
    }
    
}