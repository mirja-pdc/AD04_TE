package entidad;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "monitor")
public class Monitor {
	
	// -- Atributos --
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String especialidad;

    // Relación OneToMany bidireccional con Actividad (Monitor es el lado inverso)
    // Un monitor puede dirigir varias actividades
    @OneToMany(mappedBy = "monitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actividad> actividades = new ArrayList<>();
    
    
    // -- Constructores --

    public Monitor() {}

    // Al constructor no se le pasa el id porque éste lo genera automáticamente la bd 
    public Monitor(String nombre, String especialidad) {
    	
        this.nombre = nombre;
        this.especialidad = especialidad;
        
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
    
    public String getEspecialidad() { 
    	
    	return especialidad; 
    	
    }
    
    public void setEspecialidad(String especialidad) { 
    	
    	this.especialidad = especialidad; 
    	
    }
    
    public List<Actividad> getActividades() { 
    	
    	return actividades; 
    	
    }
    
    public void setActividades(List<Actividad> actividades) { 
    	
    	this.actividades = actividades; 
    	
    }
    
    
    // -- toString() --

    // CORREGIDO POR GEMINI: Al haber una relación bidireccional entre Monitor y Actividad,
    // el toString() de Monitor no imprime actividades para no crear un bucle infinito:
    // "Si imprimes un Monitor con System.out.println(monitor), su toString() intentará 
    // imprimir su lista de actividades. Cada Actividad de esa lista intentará imprimir 
    // su monitor, el cual volverá a imprimir sus actividades... provocando un error 
    // de tipo StackOverflowError (bucle infinito)."
    @Override
    public String toString() {
        return "Monitor [id=" + id + ", nombre=" + nombre + ", especialidad=" + especialidad + "]";
    }
      
}