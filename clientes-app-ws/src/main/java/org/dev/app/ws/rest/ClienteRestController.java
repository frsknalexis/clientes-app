package org.dev.app.ws.rest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dev.app.ws.entity.Cliente;
import org.dev.app.ws.entity.Region;
import org.dev.app.ws.service.ClienteService;
import org.dev.app.ws.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(ClienteRestController.class);

	@Autowired
	@Qualifier("clienteService")
	private ClienteService clienteService;
	
	@Autowired
	@Qualifier("uploadFileService")
	private UploadFileService uploadFileService;
	
	@GetMapping("/clientes")
	List<Cliente> findAll() {
		List<Cliente> clientes = clienteService.findAll();
		return clientes;
	}
	
	@GetMapping("/cliente/page/{page}")
	Page<Cliente> index(@PathVariable(value = "page") Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return clienteService.findAll(pageable); 
	}
	
	@GetMapping("/cliente/{id}")
	ResponseEntity<?> getClienteById(@PathVariable(value = "id") Long id) {
		
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			cliente = clienteService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(cliente == null){
			response.put("mensaje", "El cliente con ID: ".concat(id.toString().concat(" no existe en la BD !")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@PostMapping("/cliente")
	ResponseEntity<?> createCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
		
		Cliente clienteResponse = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			
			/*
			List<String> errors = new ArrayList<String>();
			
			for(FieldError error : result.getFieldErrors()) {
				errors.add("El Campo '" + error.getField() + "' " + error.getDefaultMessage());
			}
			*/
			List<String> errors = result.getFieldErrors()
										.stream()
										.map(err -> {
											return "El Campo '" + err.getField() + "' " + err.getDefaultMessage();
										}).collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			clienteResponse = clienteService.save(cliente);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la BD !");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El Cliente ha sido creado con exito");
		response.put("cliente", clienteResponse);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/cliente/{id}")
	ResponseEntity<?> updateCliente(@PathVariable(value = "id") Long id, @Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteResponse = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
							.stream()
							.map(err -> {
								return "El Campo '" + err.getField() + "' " + err.getDefaultMessage();
							}).collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(clienteActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el Cliente con ID: ".concat(id.toString().concat(" no existe en la DB !")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setRegion(cliente.getRegion());
			clienteResponse = clienteService.save(clienteActual);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar al cliente en la BD !");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El Cliente ha sido actualizado con exito");
		response.put("cliente", clienteResponse);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/cliente/{id}")
	ResponseEntity<?> deleteCliente(@PathVariable(value = "id") Long id) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			
			Cliente cliente = clienteService.findById(id);
			String nombreFotoAnterior = cliente.getFoto();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			clienteService.delete(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar al Cliente de la BD !");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El Cliente ha sido eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/cliente/upload")
	ResponseEntity<?> uploadImagenCliente(@RequestParam(value = "archivo") MultipartFile archivo, 
			@RequestParam(value = "id") Long id) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		Cliente cliente = clienteService.findById(id);
		
		if(!archivo.isEmpty()) {
			String nombreArchivo = UUID.randomUUID().toString().concat("_").concat(archivo.getOriginalFilename().replace(" ", ""));
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			
			logger.info(rutaArchivo.toString());
			
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente ".concat(": ").concat(nombreArchivo));
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior = cliente.getFoto();
			
			if(nombreFotoAnterior != null && nombreArchivo.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			cliente.setFoto(nombreArchivo);
			Cliente clienteResponse = clienteService.save(cliente);
			response.put("cliente", clienteResponse);
			response.put("mensaje", "Haz subido correctamente la imagen".concat(" : ").concat(nombreArchivo));
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/image/{nombreFoto:.+}")
	ResponseEntity<Resource> verFoto(@PathVariable(value = "nombreFoto") String nombreFoto) {
		
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		logger.info(rutaArchivo.toString());
		Resource resource = null;
		
		try {
			resource = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		if(!resource.exists() && !resource.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();
			
			try {
				resource = new UrlResource(rutaArchivo.toUri()); 
			} catch(MalformedURLException e) {
				e.printStackTrace();
			}
			
			logger.error("Error no se pudo cargar la imagen ".concat(": ").concat(nombreFoto));
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(resource, headers , HttpStatus.OK);
	}
	
	@GetMapping("/clientes/regiones")
	List<Region> listarRegiones() {
		List<Region> regiones = clienteService.findAllRegiones();
		return regiones;
	}
}
