package org.dev.app.ws.service.impl;

import java.util.Date;
import java.util.List;

import org.dev.app.ws.entity.Cliente;
import org.dev.app.ws.entity.Factura;
import org.dev.app.ws.entity.Region;
import org.dev.app.ws.repository.ClienteRepository;
import org.dev.app.ws.repository.FacturaRepository;
import org.dev.app.ws.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("clienteService")
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	@Qualifier("clienteRepository")
	private ClienteRepository clienteRepository;
	
	@Autowired
	@Qualifier("facturaRepository")
	private FacturaRepository facturaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		List<Cliente> clientes = clienteRepository.findAll();
		return clientes;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageRequest) {
		return clienteRepository.findAll(pageRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Region> findAllRegiones() {
		List<Region> regiones = clienteRepository.findAllRegiones();
		return regiones;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		return facturaRepository.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public Factura saveFactura(Factura factura) {
		return facturaRepository.save(factura);
	}
	
	@Override
	@Transactional
	public void deleteFacturaById(Long id) {
		facturaRepository.deleteById(id);
	}
}
