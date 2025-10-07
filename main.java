import java.util.ArrayList;
import java.util.Scanner;

/**
 * ===========================================================
 * SISTEMA DE GESTIÓN DE HELADERÍAS
 * ===========================================================
 * Permite agregar, editar y eliminar heladerías, así como gestionar
 * los productos disponibles (helados y malteadas).
 */

// ------------------- CLASE PRINCIPAL -------------------
public class Main {
    public static void main(String[] args) {
        GestorSistema gestor = new GestorSistema();
        gestor.iniciarPrograma();
    }
}

// ------------------- CLASE GESTOR DEL SISTEMA -------------------
class GestorSistema {
    private ArrayList<Heladeria> heladerias = new ArrayList<>();
    private Scanner input = new Scanner(System.in);

    public void iniciarPrograma() {
        Menus menuPrincipal = new Menus(40, '*', '1');
        ArrayList<String> opciones = new ArrayList<>();

        opciones.add("Registrar nueva heladería");
        opciones.add("Mostrar heladerías");
        opciones.add("Acceder a una heladería");
        opciones.add("Modificar heladería");
        opciones.add("Eliminar heladería");
        opciones.add("Salir del sistema");

        boolean activo = true;
        while (activo) {
            int opcion = menuPrincipal.crearMenu(opciones, false);
            switch (opcion) {
                case 1 -> registrarHeladeria();
                case 2 -> mostrarHeladerias();
                case 3 -> accederHeladeria();
                case 4 -> modificarHeladeria();
                case 5 -> eliminarHeladeria();
                case 6 -> {
                    System.out.println("👋 Saliendo del sistema...");
                    activo = false;
                }
            }
        }
    }

    // ------------------- CRUD DE HELADERÍAS -------------------

    private void registrarHeladeria() {
        System.out.print("Ingrese ID de la heladería: ");
        int id = input.nextInt();
        input.nextLine();
        System.out.print("Ingrese dirección de la heladería: ");
        String direccion = input.nextLine();

        Heladeria nueva = new Heladeria(id, direccion);
        heladerias.add(nueva);
        System.out.println("✅ Heladería registrada correctamente.");
    }

    private void mostrarHeladerias() {
        if (heladerias.isEmpty()) {
            System.out.println("⚠ No existen heladerías registradas.");
            return;
        }
        System.out.println("📋 Lista de Heladerías:");
        for (Heladeria h : heladerias) {
            System.out.println("ID: " + h.id + " | Dirección: " + h.getDireccion());
        }
    }

    private Heladeria buscarHeladeria(int id) {
        for (Heladeria h : heladerias) {
            if (h.id == id) return h;
        }
        return null;
    }

    private void accederHeladeria() {
        System.out.print("Ingrese el ID de la heladería: ");
        int id = input.nextInt();
        input.nextLine();

        Heladeria seleccionada = buscarHeladeria(id);
        if (seleccionada == null) {
            System.out.println("❌ Heladería no encontrada.");
            return;
        }

        Menus menuHeladeria = new Menus(50, '-', '1');
        ArrayList<String> opciones = new ArrayList<>();

        opciones.add("Agregar helado");
        opciones.add("Ver helados");
        opciones.add("Agregar malteada");
        opciones.add("Ver malteadas");
        opciones.add("Volver al menú principal");

        boolean dentro = true;
        while (dentro) {
            int opcion = menuHeladeria.crearMenu(opciones, false);
            switch (opcion) {
                case 1 -> agregarHelado(seleccionada);
                case 2 -> mostrarHelados(seleccionada);
                case 3 -> agregarMalteada(seleccionada);
                case 4 -> mostrarMalteadas(seleccionada);
                case 5 -> dentro = false;
            }
        }
    }

    private void modificarHeladeria() {
        System.out.print("Ingrese el ID de la heladería a modificar: ");
        int id = input.nextInt();
        input.nextLine();

        Heladeria h = buscarHeladeria(id);
        if (h != null) {
            System.out.print("Ingrese nueva dirección: ");
            String direccion = input.nextLine();
            h.setDireccion(direccion);
            System.out.println("✏ Dirección actualizada correctamente.");
        } else {
            System.out.println("❌ Heladería no encontrada.");
        }
    }

    private void eliminarHeladeria() {
        System.out.print("Ingrese ID de la heladería a eliminar: ");
        int id = input.nextInt();
        input.nextLine();

        Heladeria h = buscarHeladeria(id);
        if (h != null) {
            heladerias.remove(h);
            System.out.println("🗑 Heladería eliminada correctamente.");
        } else {
            System.out.println("❌ Heladería no encontrada.");
        }
    }

    // ------------------- GESTIÓN DE PRODUCTOS -------------------

    private void agregarHelado(Heladeria h) {
        System.out.print("Ingrese ID del helado: ");
        int id = input.nextInt();
        System.out.print("Ingrese precio base: ");
        float precio = input.nextFloat();
        System.out.print("Tipo (1=cono, 2=vaso, 3=sundae): ");
        int tipo = input.nextInt();
        input.nextLine();

        ArrayList<String> sabores = new ArrayList<>();
        System.out.print("Sabor principal: ");
        sabores.add(input.nextLine());

        Helado nuevoHelado = new Helado(id, precio, sabores, tipo);

        boolean agregarMas = true;
        while (agregarMas) {
            System.out.print("¿Desea agregar un topping? (s/n): ");
            String resp = input.nextLine();
            if (resp.equalsIgnoreCase("s")) {
                System.out.print("Ingrese nombre del topping: ");
                nuevoHelado.addTopping(input.nextLine());
            } else {
                agregarMas = false;
            }
        }

        h.getListaHeladosDisponibles().add(nuevoHelado);
        System.out.println("🍦 Helado agregado con éxito.");
    }

    private void mostrarHelados(Heladeria h) {
        if (h.getListaHeladosDisponibles().isEmpty()) {
            System.out.println("⚠ No hay helados disponibles.");
            return;
        }
        for (Helado helado : h.getListaHeladosDisponibles()) {
            System.out.println(
                "ID: " + helado.id +
                " | Precio: " + helado.getPrecio() +
                " | Sabores: " + helado.getSabores() +
                " | Toppings: " + helado.getToppings() +
                " | Precio Final: " + helado.calcularPrecio()
            );
        }
    }

    private void agregarMalteada(Heladeria h) {
        System.out.print("Ingrese ID de la malteada: ");
        int id = input.nextInt();
        System.out.print("Ingrese precio base: ");
        float precio = input.nextFloat();
        input.nextLine();

        ArrayList<String> sabores = new ArrayList<>();
        System.out.print("Sabor principal: ");
        sabores.add(input.nextLine());

        Malteada nueva = new Malteada(id, precio, sabores);
        h.getListaMalteadasDisponibles().add(nueva);
        System.out.println("🥤 Malteada agregada con éxito.");
    }

    private void mostrarMalteadas(Heladeria h) {
        if (h.getListaMalteadasDisponibles().isEmpty()) {
            System.out.println("⚠ No hay malteadas disponibles.");
            return;
        }
        for (Malteada m : h.getListaMalteadasDisponibles()) {
            System.out.println(
                "ID: " + m.getId() +
                " | Precio: " + m.getPrecio() +
                " | Sabores: " + m.getSabores()
            );
        }
    }
}

// ------------------- CLASE MENÚS -------------------
class Menus {
    private int ancho = 30;
    private char borde = '*';
    private char primerOpcion = '1';

    public Menus(int ancho, char borde, char primerOpcion) {
        this.ancho = ancho;
        this.borde = borde;
        this.primerOpcion = primerOpcion;
    }

    public int crearMenu(ArrayList<String> opciones, boolean centrado) {
        Scanner sc = new Scanner(System.in);
        boolean mostrar = true;
        int eleccion = 0;

        while (mostrar) {
            System.out.println(generarBorde());
            char idOpcion = primerOpcion;

            for (String texto : opciones) {
                String linea = idOpcion + ". " + texto;
                System.out.println(centrado ? centrar(linea) : linea);
                idOpcion++;
            }

            System.out.println(generarBorde());
            System.out.print("Seleccione una opción: ");
            eleccion = sc.nextInt();

            if (eleccion >= (primerOpcion - '0') && eleccion <= opciones.size()) {
                mostrar = false;
            } else {
                System.out.println("❌ Opción inválida, intente nuevamente.");
            }
        }
        return eleccion;
    }

    private String generarBorde() {
        return String.valueOf(borde).repeat(ancho);
    }

    private String centrar(String mensaje) {
        int espacios = (ancho - mensaje.length()) / 2;
        return " ".repeat(Math.max(0, espacios)) + mensaje;
    }
}

// ------------------- CLASES DE DATOS -------------------

class Helado {
    public int id;
    private float precio;
    private float costoTopping;
    private ArrayList<String> sabores;
    private ArrayList<String> toppings;
    private int tipo;

    public Helado(int id, float precio, ArrayList<String> sabores, int tipo) {
        this.id = id;
        this.precio = precio;
        this.sabores = sabores;
        this.tipo = tipo;
        this.toppings = new ArrayList<>();
        this.costoTopping = 500;
    }

    public float calcularPrecio() {
        return precio + (costoTopping * toppings.size());
    }

    public void addTopping(String topping) { toppings.add(topping); }
    public float getPrecio() { return precio; }
    public ArrayList<String> getSabores() { return sabores; }
    public ArrayList<String> getToppings() { return toppings; }
}

class Malteada {
    private int id;
    private float precio;
    private ArrayList<String> sabores;

    public Malteada(int id, float precio, ArrayList<String> sabores) {
        this.id = id;
        this.precio = precio;
        this.sabores = sabores;
    }

    public float calcularPrecio() { return precio; }
    public int getId() { return id; }
    public float getPrecio() { return precio; }
    public ArrayList<String> getSabores() { return sabores; }
}

class Heladeria {
    public int id;
    private String direccion;
    private ArrayList<Helado> heladosDisponibles;
    private ArrayList<Malteada> malteadasDisponibles;

    public Heladeria(int id, String direccion) {
        this.id = id;
        this.direccion = direccion;
        this.heladosDisponibles = new ArrayList<>();
        this.malteadasDisponibles = new ArrayList<>();
    }

    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getDireccion() { return direccion; }
    public ArrayList<Helado> getListaHeladosDisponibles() { return heladosDisponibles; }
    public ArrayList<Malteada> getListaMalteadasDisponibles() { return malteadasDisponibles; }
}
