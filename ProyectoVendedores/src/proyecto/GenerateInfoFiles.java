package proyecto;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * La clase GenerateInfoFiles se encarga de generar archivos de datos necesarios para simular
 * la información de ventas y de vendedores. Esto incluye archivos de productos, información
 * de vendedores y archivos de ventas para cada vendedor.
 */
public class GenerateInfoFiles {
    // Arreglos de nombres y apellidos para generar información de vendedores
    private static final String[] nombres = {"Juan", "Maria", "Pedro", "Luis", "Ana", "Carolina", "Carlos", "Daniela"};
    private static final String[] apellidos = {"Perez", "Gonzalez", "Rodríguez", "Lopez", "Martinez", "Garcia", "Castro"};

    // Arreglos de productos y precios para generar la lista de productos disponibles
    private static final String[] productos = {"Arroz", "Aceite", "Azucar", "Sal", "Cafe", "Leche", "Pan", "Huevos"};
    private static final double[] precios = {3500, 15000, 5000, 1200, 12000, 4000, 2500, 800};

    /**
     * Método principal que genera los archivos necesarios para la simulación.
     * 
     * @param args Argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        try {
            createProductsFile(productos.length);  // Crear archivo de productos
            createSalesManInfoFile(3);             // Crear archivo de información de vendedores
            generateSalesFiles();                  // Generar archivos de ventas para todos los vendedores
            System.out.println("Archivos generados correctamente.");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al generar los archivos.");
            e.printStackTrace();
        }
    }

    /**
     * Genera archivos de ventas para todos los vendedores listados en el archivo
     * "info_vendedores.txt".
     * 
     * @throws IOException Si ocurre un error al leer o escribir archivos.
     */
    public static void generateSalesFiles() throws IOException {
        // Leer los IDs de los vendedores desde el archivo "info_vendedores.txt"
        BufferedReader reader = new BufferedReader(new FileReader("info_vendedores.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] vendorInfo = line.split(";");
            long id = Long.parseLong(vendorInfo[1]);
            createSalesMenFile(5, "Vendedor", id);  // Generar archivo de ventas para cada vendedor
        }
        
        reader.close();
    }

    /**
     * Genera un archivo de ventas para un vendedor específico.
     * 
     * @param randomSalesCount Número de ventas aleatorias a generar.
     * @param name Nombre del vendedor (no se utiliza en este caso).
     * @param id ID del vendedor.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        FileWriter writer = new FileWriter("ventas_" + id + ".txt");
        Random rand = new Random();
        writer.write("CC;" + id + "\n"); // Escribir datos del vendedor
        for (int i = 0; i < randomSalesCount; i++) {
            int productId = rand.nextInt(productos.length);  // ID de producto aleatorio
            int quantitySold = rand.nextInt(50) + 1;  // Cantidad vendida aleatoria
            writer.write(productId + ";" + quantitySold + ";\n");
        }
        writer.close();
    }

    /**
     * Genera un archivo de productos disponibles con sus precios.
     * 
     * @param productsCount Número de productos a generar (debe ser igual al tamaño del arreglo de productos).
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void createProductsFile(int productsCount) throws IOException {
        FileWriter writer = new FileWriter("productos.txt");
        for (int i = 0; i < productsCount; i++) {
            writer.write(i + ";" + productos[i] + ";" + precios[i] + ";\n");
        }
        writer.close();
    }

    /**
     * Genera un archivo de información de vendedores con sus nombres y números de cédula.
     * 
     * @param salesmanCount Número de vendedores a generar.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        FileWriter writer = new FileWriter("info_vendedores.txt");
        Random rand = new Random();
        for (int i = 0; i < salesmanCount; i++) {
            String docType = "CC";  // Tipo de documento (Cédula de Ciudadanía)
            long docNumber = 10000000 + rand.nextInt(90000000);  // Número de cédula aleatorio
            String firstName = nombres[rand.nextInt(nombres.length)];
            String lastName = apellidos[rand.nextInt(apellidos.length)];
            writer.write(docType + ";" + docNumber + ";" + firstName + ";" + lastName + "\n");
        }
        writer.close();
    }
}
