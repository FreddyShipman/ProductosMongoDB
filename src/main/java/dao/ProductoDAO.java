package dao;

/**
 *
 * @author alfre
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import config.MongoClientProvider;
import dao.IProductoDAO;
import dao.DaoException;
import dao.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Producto;
import org.bson.types.ObjectId;

public class ProductoDAO implements IProductoDAO {

    private final MongoCollection<Producto> coleccion;

    public ProductoDAO() {
        MongoClientProvider provider = MongoClientProvider.getInstance();
        this.coleccion = provider.getCollection("productos", Producto.class);
    }

    @Override
    public void create(Producto producto) throws DaoException {
        try {
            producto.setId(new ObjectId());
            Date now = new Date();
            producto.setCreatedAt(now);
            producto.setUpdatedAt(now);
            this.coleccion.insertOne(producto);
        } catch (Exception e) {
            throw new DaoException("Error al crear el producto", e);
        }
    }

    @Override
    public Producto findById(ObjectId id) throws EntityNotFoundException, DaoException {
        try {
            Producto producto = this.coleccion.find(Filters.eq("_id", id)).first();
            if (producto == null) {
                throw new EntityNotFoundException("Producto no encontrado con id: " + id);
            }
            return producto;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error al buscar el producto", e);
        }
    }

    @Override
    public List<Producto> findAll() throws DaoException {
        try {
            return this.coleccion.find().into(new ArrayList<>());
        } catch (Exception e) {
            throw new DaoException("Error al listar todos los productos", e);
        }
    }

    @Override
    public void update(Producto producto) throws EntityNotFoundException, DaoException {
        try {
            Date now = new Date();
            producto.setUpdatedAt(now);

            UpdateResult result = this.coleccion.updateOne(
                    Filters.eq("_id", producto.getId()),
                    Updates.combine(
                            Updates.set("nombre", producto.getNombre()),
                            Updates.set("precio", producto.getPrecio()),
                            Updates.set("stock", producto.getStock()),
                            Updates.set("proveedor", producto.getProveedor()),
                            Updates.set("categorias", producto.getCategorias()),
                            Updates.set("updatedAt", producto.getUpdatedAt())
                    )
            );

            if (result.getMatchedCount() == 0) {
                throw new EntityNotFoundException("Producto no encontrado para actualizar: " + producto.getId());
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error al actualizar el producto", e);
        }
    }

    @Override
    public void deleteById(ObjectId id) throws EntityNotFoundException, DaoException {
        try {
            DeleteResult result = this.coleccion.deleteOne(Filters.eq("_id", id));
            if (result.getDeletedCount() == 0) {
                throw new EntityNotFoundException("Producto no encontrado para eliminar: " + id);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error al eliminar el producto", e);
        }
    }

    @Override
    public void deleteAll() throws DaoException {
        try {
            this.coleccion.deleteMany(Filters.empty());
        } catch (Exception e) {
            throw new DaoException("Error al eliminar todos los productos", e);
        }
    }

    @Override
    public List<Producto> findByNombre(String nombre) throws DaoException {
        try {
            return this.coleccion.find(Filters.eq("nombre", nombre)).into(new ArrayList<>());
        } catch (Exception e) {
            throw new DaoException("Error al buscar por nombre", e);
        }
    }

    @Override
    public List<Producto> findByCategoria(String categoria) throws DaoException {
        try {
            return this.coleccion.find(Filters.eq("categorias", categoria)).into(new ArrayList<>());
        } catch (Exception e) {
            throw new DaoException("Error al buscar por categoria", e);
        }
    }
}