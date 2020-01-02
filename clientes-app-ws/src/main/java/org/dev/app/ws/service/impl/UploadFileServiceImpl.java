package org.dev.app.ws.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.dev.app.ws.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("uploadFileService")
public class UploadFileServiceImpl implements UploadFileService {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	
	private static final String DIRECTORIO_UPLOADS = "uploads";

	@Override
	public Resource cargarFile(String nombreFoto) throws MalformedURLException {
		
		Path rutaArchivo = getPath(nombreFoto);
		logger.info(rutaArchivo.toString());
		Resource resource = new UrlResource(rutaArchivo.toUri());
				
		if(!resource.exists() && !resource.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();
			resource = new UrlResource(rutaArchivo.toUri());
			logger.error("Error no se pudo cargar la imagen ".concat(": ").concat(nombreFoto));
		}
		return resource;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		
		String nombreArchivo = UUID.randomUUID().toString().concat("_").concat(archivo.getOriginalFilename().replace(" ", ""));
		Path rutaArchivo = getPath(nombreArchivo);
		logger.info(rutaArchivo.toString());
		
		Files.copy(archivo.getInputStream(), rutaArchivo);
		return nombreArchivo;
	}

	@Override
	public Boolean eliminar(String nombreFoto) {
		
		if(nombreFoto != null && nombreFoto.length() > 0) {
			Path rutaFotoAnterior = getPath(nombreFoto);
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		Path rutaArchivo = Paths.get(DIRECTORIO_UPLOADS).resolve(nombreFoto).toAbsolutePath();
		return rutaArchivo;
	}
}
