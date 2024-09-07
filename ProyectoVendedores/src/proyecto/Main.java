package proyecto;

import java.io.*;
import java.util.*;

/**
 * La clase Main procesa archivos de ventas y genera reportes de ventas y de productos.
 * Lee los archivos de ventas, calcula las ventas totales por vendedor y las cantidades vendidas
 * por producto, y genera reportes correspondientes en formato CSV.
 */
public class Main {
    /**
     * Método principal que coordina el procesamiento de archivos y la generación de reportes.
     * 
     * @param args Argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        try {
            Map<String, Double> salesByVendor = new HashMap<>();  // Mapa para almacenar ventas totales por vendedor
            Map<Integer, Integer> productsSold = new HashMap<>(); // Mapa para almacenar cantidades vendidas por producto

            // Procesar archivos de ventas
            processSalesFiles(salesByVendor, productsSold);

            // Generar los reportes
            generateSalesReport(salesByVendor);
            generateProductReport(productsSold);

            System.out.println("Reportes generados correctamente.");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al procesar los archivos.");
            e.printStackTrace();
        }
    }

    /**
     * Procesa los archivos de ventas y acumula la información de ventas por vendedor y
     * por producto.
     * 
     * @param salesByVendor Mapa para almacenar las ventas totales por vendedor.
     * @param productsSold  Mapa para almacenar las cantidades vendidas por producto.
     * @throws IOException Si ocurre un error al leer los archivos.
     */
    public static void processSalesFiles(Map<String, Double> salesByVendor, Map<Integer, Integer> productsSold) throws IOException {
        File folder = new File(".");  // Carpeta actual donde están los archivos
        Map<Integer, String[]> productData = loadProductData();  // Cargar datos de productos

        // Iterar sobre todos los archivos en la carpeta actual
        for (File file : folder.listFiles()) {
            if (file.getName().startsWith("ventas_")) {  // Solo procesar archivos que comienzan con "ventas_"
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine(); // Leer la primera línea (datos del vendedor)
                String[] vendorInfo = line.split(";");
                String vendorId = vendorInfo[1];  // Número de documento del vendedor

                // Leer las líneas restantes del archivo para procesar ventas
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    int productId = Integer.parseInt(parts[0]);
                    int quantitySold = Integer.parseInt(parts[1]);
                    double price = getProductPrice(productId, productData);  // Obtener el precio del producto

                    // Acumular ventas por vendedor
                    salesByVendor.put(vendorId, salesByVendor.getOrDefault(vendorId, 0.0) + (quantitySold * price));

                    // Acumular cantidades vendidas por producto
                    productsSold.put(productId, productsSold.getOrDefault(productId, 0) + quantitySold);
                }
                reader.close();
            }
        }
    }

    /**
     * Genera un archivo de reporte de ventas de vendedores en formato CSV.
     * 
     * @param salesByVendor Mapa que contiene las ventas totales por vendedor.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void generateSalesReport(Map<String, Double> salesByVendor) throws IOException {
        FileWriter writer = new FileWriter("reporte_vendedores.csv");

        salesByVendor.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())  // Ordenar de mayor a menor
            .forEach(entry -> {
                String vendorId = entry.getKey();
                double totalSales = entry.getValue();

                try {
                    writer.write(vendorId + ";" + totalSales + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        
        writer.close();
    }

    /**
     * Genera un archivo de reporte de productos vendidos en formato CSV.
     * 
     * @param productsSold Mapa que contiene las cantidades vendidas por producto.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void generateProductReport(Map<Integer, Integer> productsSold) throws IOException {
        FileWriter writer = new FileWriter("reporte_productos.csv");

        Map<Integer, String[]> productData = loadProductData(); // Cargar datos de productos (nombre, precio)
        
        productsSold.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .forEach(entry -> {
                int productId = entry.getKey();
                int quantitySold = entry.getValue();
                String[] productInfo = productData.get(productId); // Obtener nombre y precio

                if (productInfo == null) {
                    System.out.println("Advertencia: Producto con ID " + productId + " no encontrado.");
                    return;  // Si no se encuentra el producto, no lo procesamos.
                }

                String productName = productInfo[0];
                String productPrice = productInfo[1];

                try {
                    writer.write(productName + ";" + quantitySold + ";" + productPrice + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        writer.close();
    }

    /**
     * Obtiene el precio del producto desde el mapa de datos.
     * 
     * @param productId ID del producto.
     * @param productData Mapa que contiene la información de los productos.
     * @return Precio del producto.
     */
    public static double getProductPrice(int productId, Map<Integer, String[]> productData) {
        String[] productInfo = productData.get(productId);
        if (productInfo != null) {
            return Double.parseDouble(productInfo[1]);
        }
        System.out.println("Advertencia: Precio para el producto ID " + productId + " no encontrado. Usando precio temporal.");
        return 10000.0;  // Precio temporal si el producto no está en los datos
    }

    /**
     * Carga los datos de productos, incluyendo nombres y precios, desde un mapa.
     * 
     * @return Mapa con la información de los productos (ID, nombre, precio).
     */
    public static Map<Integer, String[]> loadProductData() {
        Map<Integer, String[]> products = new HashMap<>();
        products.put(0, new String[] {"Arroz", "3500"});
        products.put(1, new String[] {"Aceite", "15000"});
        products.put(2, new String[] {"Azucar", "5000"});
        products.put(3, new String[] {"Sal", "1200"});
        products.put(4, new String[] {"Cafe", "12000"});
        products.put(5, new String[] {"Leche", "4000"});
        products.put(6, new String[] {"Pan", "2500"});
        products.put(7, new String[] {"Huevos", "800"});
        // Puedes seguir añadiendo más productos aquí o cargarlos desde un archivo de productos reales

        return products;
    }
}
