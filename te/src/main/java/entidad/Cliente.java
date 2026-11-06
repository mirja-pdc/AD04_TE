package entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {
	
	// -- Atributos --
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String email;
    
    
    // -- Constructores --

    public Cliente() {}

    // Al constructor no se le pasa el id porque éste lo genera automáticamente la bd
    public Cliente(String nombre, String email) {
    	
        this.nombre = nombre;
        this.email = email;
        
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
    
    public String getEmail() { 
    	
    	return email; 
    	
    }
    
    public void setEmail(String email) { 
    	
    	this.email = email; 
    	
    }
    
    
    // -- toString() --

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", email=" + email + "]";
	}
    
}