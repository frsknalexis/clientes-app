package org.dev.app.ws.rest;

import java.util.List;

import javax.validation.Valid;

import org.dev.app.ws.entity.Factura;
import org.dev.app.ws.entity.Producto;
import org.dev.app.ws.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:4200", "*" })
@RestController
@RequestMapping("/api/v1/factura")
public class FacturaRestController {

	@Autowired
	@Qualifier("clienteService")
	private ClienteService clienteService;
	
	@GetMapping("/factura/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	Factura getFacturaById(@PathVariable(value = "id") Long id) {
		Factura factura = clienteService.findFacturaById(id);
		return factura;
	}
	
	@DeleteMapping("/factura/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void deleteFactura(@PathVariable(value = "id") Long id) {
		clienteService.deleteFacturaById(id);
	}
	
	@GetMapping("/filtrarProductos/{termino}")
	@ResponseStatus(code = HttpStatus.OK)
	List<Producto> filtrarProductosByNombre(@PathVariable(value = "termino") String termino) {
		return clienteService.findProductoByNombre(termino);
	}
	
	@PostMapping("/crearFactura")
	@ResponseStatus(code = HttpStatus.CREATED)
	Factura crearFactura(@Valid @RequestBody Factura factura) {
		Factura facturaReturn = clienteService.saveFactura(factura);
		return facturaReturn;
	}
}
