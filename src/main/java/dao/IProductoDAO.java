package dao;

/**
 *
 * @author alfre
 */

import dao.DaoException;
import dao.EntityNotFoundException;
import java.util.List;
import model.Producto;
import org.bson.types.ObjectId;

public interface IProductoDAO {

    void create(Producto producto) throws DaoException;

    Producto findById(ObjectId id) throws EntityNotFoundException, DaoException;

    List<Producto> findAll() throws DaoException;

    void update(Producto producto) throws EntityNotFoundException, DaoException;

    void deleteById(ObjectId id) throws EntityNotFoundException, DaoException;

    void deleteAll() throws DaoException;

    List<Producto> findByNombre(String nombre) throws DaoException;

    List<Producto> findByCategoria(String categoria) throws DaoException;
}