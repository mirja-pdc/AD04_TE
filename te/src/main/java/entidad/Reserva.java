package entidad;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
public class Reserva {
	
	// -- Atributos --
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_reserva")
    private LocalDateTime fechaReserva;

    @Column(name = "numero_plazas")
    private int numeroPlazas;

    @ManyToOne
    @JoinColumn(name = "actividad_id")
    private Actividad actividad;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    
    // -- Constructores --

    public Reserva() {}

    // Al constructor no se le pasa el id porque éste lo genera automáticamente la bd
    public Reserva(LocalDateTime fechaReserva, int numeroPlazas, Actividad actividad, Cliente cliente) {
        this.fechaReserva = fechaReserva;
        this.numeroPlazas = numeroPlazas;
        this.actividad = actividad;
        this.cliente = cliente;
    }

    
    // -- Getters y Setters
    
    public Long getId() { 
    	
    	return id; 
    	
    }
    
    // No hay setId() porque el id lo genera automáticamente la bd (AUTO_INCREMENT en SQL)
    
    public LocalDateTime getFechaReserva() { 
    	
    	return fechaReserva; 
    	
    }
    
    public void setFechaReserva(LocalDateTime fechaReserva) { 
    	
    	this.fechaReserva = fechaReserva; 
    	
    }
    
    public int getNumeroPlazas() { 
    	
    	return numeroPlazas; 
    	
    }
    
    public void setNumeroPlazas(int numeroPlazas) { 
    	
    	this.numeroPlazas = numeroPlazas; 
    	
    }
    
    public Actividad getActividad() { 
    	
    	return actividad; 
    	
    }
    
    public void setActividad(Actividad actividad) { 
    	
    	this.actividad = actividad; 
    	
    }
    
    public Cliente getCliente() { 
    	
    	return cliente; 
    	
    }
    
    public void setCliente(Cliente cliente) { 
    	
    	this.cliente = cliente; 
    	
    }
    
    
    // -- toString() --

	@Override
	public String toString() {
		return "Reserva [id=" + id + ", fechaReserva=" + fechaReserva + ", numeroPlazas=" + numeroPlazas
				+ ", actividad=" + actividad + ", cliente=" + cliente + "]";
	}
       
}