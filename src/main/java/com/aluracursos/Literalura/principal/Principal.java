package com.aluracursos.Literalura.principal;
import com.aluracursos.Literalura.models.*;
import com.aluracursos.Literalura.repositories.LibroRepository;
import com.aluracursos.Literalura.sevice.ConsumoApi;
import com.aluracursos.Literalura.sevice.ConvierteDatos;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataIntegrityViolationException;


import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi conexionApi = new ConsumoApi();
    private final String URLBASE = "https://gutendex.com/books/";
    private ConvierteDatos convierteDatos = new ConvierteDatos();

    private List<DatosLibro> datosLibros = new ArrayList<>();

    private List<Libro> listaLibrosBuscados = new ArrayList<>();

    private String busqueda;

    private LibroRepository repositorio;

    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void mostrarMenu() throws JsonProcessingException {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Obtener lista de libros automática 
                    2 - Buscar autores o libros
                    3 - Libros buscados recientemente
                    4 - Autores de Libros buscados recientemente
                    5 - Buscar Autores vivos en un rango específico
                    6 - Buscar Libro dentro de la base de datos
                    7 - Listado de libro por Idioma
                                                                                              
                    0 - Salir
                    """;
            System.out.println(menu);
            try {
                opcion = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Intente de nuevo.");
            }

            switch (opcion) {
                case 1:
                    obtenerLibros();
                    break;
                case 2:
                    buscarAutor();
                    break;
                case 3:
                    listaDeLibrosBuscados();
                    break;
                case 4:
                    listaAutoresBuscados();
                    break;
                case 5 :
                    buscarAutoresPorRango();
                    break;
                case 6:
                    buscarTituloDB();
                    break;
                case 7:
                    listadoLibroPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    public void obtenerLibros() {
        System.out.println("Conectando con la base de datos...");
        for (int i = 1; i < 10; i++) {
            var json = conexionApi.obtenerDatos(URLBASE + "?page=" + i);
            var datos = convierteDatos.obtenerDatos(json, ListaLibros.class);
            datosLibros = new ArrayList<>(datos.results());
            for (DatosLibro datosLibro : datosLibros) {
                Libro libro = new Libro(datosLibro);
                listaLibrosBuscados.add(libro);
                try {
                    repositorio.save(libro);

                } catch (DataIntegrityViolationException _) {
                }
            }
        }
        System.out.println("Conexión completada... \n");
    }

    public void buscarAutor() throws JsonProcessingException {
        System.out.println("Ingresa el nombre del autor o libro que deseas buscar");
        var busqueda = scanner.nextLine();

        var json = conexionApi.obtenerDatos(URLBASE + "?search=" + busqueda.replace(" ", "+"));
        var datos = convierteDatos.obtenerDatos(json, ListaLibros.class);
        List<DatosLibro> libros;
        try {
            libros = new ArrayList<>(datos.results());
            if (libros.isEmpty()) {
                System.out.println("Sin resultados" + "\n");
            } else {
                List<Libro> resultado = new ArrayList<>(libros.stream().map(Libro::new).toList());

                for (Libro libro : resultado) {
                    System.out.println(libro);
                    listaLibrosBuscados.add(libro);
                    try {
                        repositorio.save(libro);
                    } catch (DataIntegrityViolationException _) {

                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void listaDeLibrosBuscados() {
        if (listaLibrosBuscados.isEmpty()) {
            System.out.println("No se ha realizado ninguna búsqueda.");
        } else {
            try{
                listaLibrosBuscados.sort(Comparator.comparing(Libro::getLenguajes));
                listaLibrosBuscados.forEach(System.out::println);
            }catch (NullPointerException e){
                listaLibrosBuscados.forEach(System.out::println);
            }
        }
    }

    public void listaAutoresBuscados() {
        if (listaLibrosBuscados.isEmpty()) {
            System.out.println("No se han realizado búsquedas.");
        } else {
            for (Autores autor : listaLibrosBuscados.stream().map(Libro::getAutores).toList()) {
                System.out.println(autor);
            }
        }
    }

    public void buscarAutoresPorRango(){
        System.out.println("Ingresa el rango a comenzar (Ej. 1990)");
        int rango1 = 0;
        int rango2= 0;
        try{
             rango1 = scanner.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Ingrese unicamente valores numéricos");
            Principal principal = new Principal(repositorio);
            principal.buscarAutoresPorRango();
        }
        System.out.println("ingresa el rango final (Ej. 2000)");
        try{
             rango2 = scanner.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Ingrese unicamente valores numéricos");
            Principal principal = new Principal(repositorio);
            principal.buscarAutoresPorRango();
        }

        var json = conexionApi.obtenerDatos(URLBASE + "?author_year_start=" + rango1 + "&author_year_end=" +rango2);
        var datos = convierteDatos.obtenerDatos(json, ListaLibros.class);

        datosLibros = new ArrayList<>(datos.results());
        for (DatosLibro datosLibro : datosLibros) {
            Libro libro = new Libro(datosLibro);
            System.out.println(libro.getAutores().toString());
            try {
                repositorio.save(libro);
            } catch (DataIntegrityViolationException _) {
            }
        }
    }

    public void buscarTituloDB(){
        System.out.println("Escribe el título del libro a buscar");
        var res = scanner.nextLine();
        Optional<Libro> libroBuscado = repositorio.findByTituloContainsIgnoreCase(res).stream().findFirst();
        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado\n" + libroBuscado.get());
        }else{
            System.out.println("Lo sentimos, el libro no se encuentra registrado en nuestra base de datos.");
        }
    }

    public void listadoLibroPorIdioma() throws JsonProcessingException {
        var mensaje = """
                Elija el Idioma a listar
                    1 - Inglés
                    2 - Español
                    
                    0 - Volver
                """;
        System.out.println(mensaje);
        var opcion = scanner.nextInt();
        List<String> librosIngles = new ArrayList<>();
        List<String> librosEspanol = new ArrayList<>();
        Idioma ingles ;
        Idioma espanol;
        switch (opcion){
            case 1:
                librosIngles.add("en");
                ingles = Idioma.fromString(librosIngles);
                List<Libro> listaIngles = (repositorio.findBylenguajes(ingles).stream().toList());
                System.out.println("Total de Libros en Inglés: " + listaIngles.size());
                for (Libro libro : listaIngles){
                    System.out.println(libro);
                }
                break;
            case 2:
                librosEspanol.add("es");
                espanol = Idioma.fromString(librosEspanol);
                List<Libro> listaEspanol = (repositorio.findBylenguajes(espanol).stream().toList());
                System.out.println("Total de Libros en Español: " + listaEspanol.size());
                for (Libro libro : listaEspanol){
                    System.out.println(libro);
                }
                break;
            case 0:
                Principal principal = new Principal(repositorio);
                principal.mostrarMenu();
                break;
            default:
                System.out.println("Opción no valida");
                break;
        }
    }

}
