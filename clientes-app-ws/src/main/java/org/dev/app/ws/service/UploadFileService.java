package org.dev.app.ws.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

	Resource cargarFile(String nombreFoto) throws MalformedURLException;
	
	String copiar(MultipartFile archivo) throws IOException;
	
	Boolean eliminar(String nombreFoto);
	
	Path getPath(String nombreFoto);
}
