package entidad;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "actividad")
public class Actividad {
	
	// -- Atributos --
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String localidad;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "plazas_totales")
    private int plazasTotales;

    @Column(name = "plazas_disponibles")
    private int plazasDisponibles;

    // Relación ManyToOne con Monitor (Actividad es la parte dueña de la relación)
    @ManyToOne
    @JoinColumn(name = "monitor_id")
    private Monitor monitor;

    
    // -- Constructores --
    
    public Actividad() {}
    
    // Al constructor no se le pasa el id porque éste lo genera automáticamente la bd
    public Actividad(String nombre, String localidad, LocalDateTime fechaHora, int plazasTotales, Monitor monitor) {
    	
        this.nombre = nombre;
        this.localidad = localidad;
        this.fechaHora = fechaHora;
        this.plazasTotales = plazasTotales;
        this.plazasDisponibles = plazasTotales; // Al crear la Actividad, disponibles = totales
        this.monitor = monitor;
        
    }

    
    // -- Getters y Setters --
    
    public Long getId() { 
    	
    	return id; 
    }
    
    // No hay setId() porque el id lo genera automáticamente la bd (AUTO_INCREMENT en SQL)
    
    public String getNombre() { 
    	
    	return nombre; 
    	
    }
    
    public void setNombre(String nombre) { 
    	
    	this.nombre = nombre; 
    	
    }
    
    public String getLocalidad() { 
    	
    	return localidad; 
    	
    }
   
    public void setLocalidad(String localidad) { 
    	
    	this.localidad = localidad; 
    	
    }
    
    public LocalDateTime getFechaHora() { 
    	
    	return fechaHora; 
    	
    }
    
    public void setFechaHora(LocalDateTime fechaHora) { 
    	
    	this.fechaHora = fechaHora; 
    	
    }
    
    public int getPlazasTotales() { 
    	
    	return plazasTotales; 
    	
    }
    
    public void setPlazasTotales(int plazasTotales) { 
    	
    	this.plazasTotales = plazasTotales; 
    	
    }
    
    public int getPlazasDisponibles() { 
    	
    	return plazasDisponibles; 
    	
    }
    
    public void setPlazasDisponibles(int plazasDisponibles) { 
    	
    	this.plazasDisponibles = plazasDisponibles; 
    	
    }
    
    public Monitor getMonitor() { 
    	
    	return monitor; 
    	
    }
   
    public void setMonitor(Monitor monitor) { 
    	
    	this.monitor = monitor; 
    	
    }
    
    
    // -- toString() --

    // Al haber una relación bidireccional entre Monitor y Actividad, para no crear un bucle infinito
    // el toString() de Actividad no imprime un objeto monitor completo, sino solo su nombre 
    // (ver comentario a toString() de Monitor)
	@Override
	public String toString() {
		return "Actividad [id=" + id + ", nombre=" + nombre + ", localidad=" + localidad + ", fechaHora=" + fechaHora
				+ ", plazasTotales=" + plazasTotales + ", plazasDisponibles=" + plazasDisponibles + ", monitor="
				+ monitor.getNombre() + "]";
	}  
    
}