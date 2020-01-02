package org.dev.app.ws.repository;

import org.dev.app.ws.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("usuarioRepository")
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Usuario findByUsername(String username);
	
	@Query("select u from Usuario u where u.username = :username")
	Usuario findByUsername2(@Param(value = "username") String username);
	
}
