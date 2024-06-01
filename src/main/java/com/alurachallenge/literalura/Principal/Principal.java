package com.alurachallenge.literalura.Principal;

import com.alurachallenge.literalura.model.*;
import com.alurachallenge.literalura.repository.AutorRepository;
import com.alurachallenge.literalura.repository.BookRepository;
import com.alurachallenge.literalura.service.ConsumoAPI;
import com.alurachallenge.literalura.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final String API_KEY = "?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private BookRepository repositorio;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private Optional<Libro> libroBuscado;

    public Principal(BookRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Agrega un libro por nombre 
                    2 - Consulta a un autor por su nombre
                    3 - Consulta el listado de todos los libros
                    4 - Consulta todos los autores registrados
                    5 - Consulta autores vivos en un periodo de tiempo
                    6 - Consulta libros por su idioma
                    7 - Consulta los 10 libros mas descargados
                    8 - Consulta un libro por nombre 
                    9 - Obtener autores de un libro
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    agregarLibro();
                    break;
                case 2:
                    consultarAutor();
                    break;
                case 3:
                    listaTodosLosLibros();
                    break;
                case 4:
                    listaAutoresRegistrados();
                    break;
                case 5:
                    consultaAutoresVivosPorPeriodo();
                    break;
                case 6:
                    consultarLibrosPorIdioma();
                    break;
                case 7:
                    top10LibrosMasDescargados();
                    break;
                case 8:
                    consultarLibroPorNombre();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                case 9:
                    obtenerAutoresPorLibro();
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void obtenerAutoresPorLibro() {

    }

    private void consultarLibroPorNombre() {
    }

    private void top10LibrosMasDescargados() {
    }

    private void consultarLibrosPorIdioma() {
    }

    private void consultaAutoresVivosPorPeriodo() {
    }

    private void listaAutoresRegistrados() {
    }

    private void listaTodosLosLibros() {
        libros = repositorio.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    private void consultarAutor() {
    }

    private void agregarLibro() {
        try {
            DatosLibro datos = getDatosLibro();
            if (datos != null) {
                // Verificar y persistir autores antes de crear el libro
                List<Autor> autores = datos.autores().stream()
                        .map(autor -> {
                            Optional<Autor> autorExistente = autorRepository.findByNameAndBirthYearAndDeathYear(autor.nombre(),autor.fechaDeNacimiento(),autor.fechaDeFallecimiento());
                            return autorExistente.orElseGet(() -> {
                                Autor nuevoAutor = new Autor(autor);
                                return autorRepository.save(nuevoAutor);
                            });
                        })
                        .collect(Collectors.toList());

                Libro libro = new Libro(datos);
                libro.setAutores(autores); // Asignar los autores verificados/persistidos al libro

                repositorio.save(libro);
                System.out.println("Libro guardado exitosamente: " + datos);
            } else {
                System.out.println("Libro no encontrado");
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("No puedes ingresar dos veces un mismo libro");
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado. Vuelva a intentar " + e);
        }
    }

    private DatosLibro getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar: ");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + API_KEY + nombreLibro.replace(" ", "%20"));
        System.out.println(json);

        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("Libro Encontrado");
            System.out.println(libroBuscado.get());
            return libroBuscado.get();
        } else {
            return null;
        }
    }

}

