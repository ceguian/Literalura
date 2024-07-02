package com.aluracursos.Literalura.models;
import jakarta.persistence.*;
import java.util.NoSuchElementException;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String titulo;
    private int cuentaDescargas;
    @Enumerated(EnumType.STRING)
    private Idioma lenguajes;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Autores autores;

    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo= datosLibro.titulo();
        try{
            this.autores= datosLibro.autores().stream().map(Autores::new).toList().getFirst();
        }catch (NoSuchElementException e){
            this.autores = null;
        }
        this.lenguajes= Idioma.fromString(datosLibro.lenguajes().stream().toList());
        this.cuentaDescargas= datosLibro.cuentaDescargas();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getCuentaDescargas() {
        return cuentaDescargas;
    }

    public void setCuentaDescargas(int cuentaDescargas) {
        this.cuentaDescargas = cuentaDescargas;
    }

    public Autores getAutores() {
        return autores;
    }

    public void setAutores(Autores autores) {
        this.autores = autores;
    }

    public Idioma getLenguajes() {
        return lenguajes;
    }

    public void setLenguajes(Idioma lenguajes) {
        this.lenguajes = lenguajes;
    }

    @Override
    public String toString() {

        try{
            return  "Título: " + titulo + '\n' +
                    "Autor(es): " + autores.getNombre() + "\n" +
                    "Idiomas: " + lenguajes + "\n" +
                    "Número de Descargas: " + cuentaDescargas + "\n";
        }catch (NullPointerException e){
            return  "Título: " + titulo + '\n' +
                    "Autor(es): Desconocido" + '\n' +
                    "Idiomas: " + lenguajes + "\n" +
                    "Número de Descargas: " + cuentaDescargas + "\n";
        }



    }
}
