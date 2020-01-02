package org.dev.app.ws.service;

import org.dev.app.ws.entity.Usuario;

public interface UsuarioService {

	Usuario findByUsername(String username);
}
