package app;

/**
 *
 * @author alfre
 */

import dao.IProductoDAO;
import dao.DaoException;
import dao.ProductoDAO;
import java.util.Arrays;
import java.util.List;
import model.Producto;
import model.Proveedor;
import org.bson.types.ObjectId;

public class PruebasApp {

    public static void main(String[] args) {
        
        IProductoDAO productoDao = new ProductoDAO();
        ObjectId generatedId = null;

        try {
            System.out.println("--- Limpiando la base de datos ---");
            productoDao.deleteAll();

            // 1. Crear Producto
            System.out.println("\n--- Creando Producto ---");
            Proveedor prov = new Proveedor();
            prov.setNombre("ITSON");
            prov.setContacto("rectoriapotros.itson.edu.mx");
            prov.setPais("Mexico");

            Producto p = new Producto();
            p.setNombre("Laptop HP");
            p.setPrecio(18000.0);
            p.setStock(50);
            p.setProveedor(prov);
            p.setCategorias(Arrays.asList("Electronica", "Computo", "Escolar"));
            
            productoDao.create(p);
            generatedId = p.getId();
            System.out.println("Producto CREADO con ID: " + generatedId);

            // 2. Leer por ID
            System.out.println("\n--- Leyendo Producto por ID ---");
            Producto pLeido = productoDao.findById(generatedId);
            System.out.println("Producto encontrado: " + pLeido.getNombre());

            // 3. Actualizar
            System.out.println("\n--- Actualizando Producto ---");
            pLeido.setStock(45);
            pLeido.setPrecio(17500.0);
            productoDao.update(pLeido);
            
            Producto pActualizado = productoDao.findById(generatedId);
            System.out.println("Producto actualizado. Stock: " + pActualizado.getStock());
            System.out.println("Producto actualizado. Precio: " + pActualizado.getPrecio());

            // 4. Listar Todos
            System.out.println("\n--- Listando Todos los Productos ---");
            List<Producto> productos = productoDao.findAll();
            for (Producto prod : productos) {
                System.out.println("- " + prod.getNombre() + " (Stock: " + prod.getStock() + ")");
            }

            // 5. Eliminar
            System.out.println("\n--- Eliminando Producto ---");
            productoDao.deleteById(generatedId);
            System.out.println("Producto eliminado.");

            // 6. Verificar eliminación
            System.out.println("\n--- Verificando eliminacion (debe fallar) ---");
            try {
                productoDao.findById(generatedId);
            } catch (DaoException e) {
                System.out.println("Verificacion exitosa: " + e.getMessage());
            }

        } catch (DaoException e) {
            System.err.println("Ha ocurrido un error en el DAO:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ha ocurrido un error inesperado:");
            e.printStackTrace();
        }
    }
}