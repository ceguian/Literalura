package com.aluracursos.Literalura.models;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "autores")
public class Autores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private Integer anoNacimiento;
    private Integer anoDeceso;
    @OneToMany(mappedBy = "autores", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libro;



    public Autores(){

    }

    public Autores(DatosAutores datosAutores) {
        this.nombre = datosAutores.nombre();
        this.anoNacimiento = (Integer) datosAutores.añoNacimiento();
        this.anoDeceso = (Integer) datosAutores.añoDeceso();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAñonacimiento() {
        return anoNacimiento;
    }

    public void setAñonacimiento(Integer añonacimiento) {
        this.anoDeceso = añonacimiento;
    }

    public Integer getAñoDeceso() {
        return anoDeceso;
    }

    public void setAñoDeceso(Integer añoDeceso) {
        this.anoDeceso = añoDeceso;
    }

    public List<Libro> getLibro() {
        return libro;
    }

    public void setLibro(List<Libro> libro) {
        libro.forEach(l->l.setAutores(this));
        this.libro = libro;
    }

    @Override
    public String toString() {
            return "\n" + "Nombre del Escritor: " + nombre + '\n' +
                    "Año de Nacimiento: " + anoNacimiento + '\n' +
                    "Año de Deceso: " + anoDeceso;
    }
}
