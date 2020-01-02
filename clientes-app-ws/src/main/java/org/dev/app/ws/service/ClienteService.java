package org.dev.app.ws.service;

import java.util.List;

import org.dev.app.ws.entity.Cliente;
import org.dev.app.ws.entity.Factura;
import org.dev.app.ws.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

	List<Cliente> findAll();
	
	Page<Cliente> findAll(Pageable pageRequest);
	
	Cliente findById(Long id);
	
	Cliente save(Cliente cliente);
	
	void delete(Long id);
	
	List<Region> findAllRegiones();
	
	Factura findFacturaById(Long id);
	
	Factura saveFactura(Factura factura);
	
	void deleteFacturaById(Long id);
}
