package org.dev.app.ws.repository;

import java.util.List;

import org.dev.app.ws.entity.Cliente;
import org.dev.app.ws.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("clienteRepository")
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	@Query("from Region")
	List<Region> findAllRegiones();
}
