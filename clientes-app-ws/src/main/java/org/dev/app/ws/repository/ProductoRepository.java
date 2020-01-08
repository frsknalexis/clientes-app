package org.dev.app.ws.repository;

import java.util.List;

import org.dev.app.ws.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("productoRepository")
public interface ProductoRepository extends JpaRepository<Producto, Long> {

	@Query("select p from Producto p where p.nombre like %:termino%")
	List<Producto> findByNombre(@Param(value = "termino") String termino);
	
	List<Producto> findByNombreContainingIgnoreCase(String termino);
	
	List<Producto> findByNombreStartingWithIgnoreCase(String termino);
}
