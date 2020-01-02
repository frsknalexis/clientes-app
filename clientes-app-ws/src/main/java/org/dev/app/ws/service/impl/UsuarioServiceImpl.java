package org.dev.app.ws.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.dev.app.ws.entity.Usuario;
import org.dev.app.ws.repository.UsuarioRepository;
import org.dev.app.ws.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("usuarioService")
public class UsuarioServiceImpl implements UserDetailsService, UsuarioService {
	
	private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Autowired
	@Qualifier("usuarioRepository")
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioRepository.findByUsername(username);
		
		if(usuario == null) {
			logger.error("Error en el login, no existe el usuario: '" + username +  "' en el sistema");
			throw new UsernameNotFoundException("Error en el login, no existe el usuario: '" + username + "' en el sistema");
		}
		
		List<GrantedAuthority> authorities = usuario.getRoles().stream()
												.map(role -> {
													return new SimpleGrantedAuthority(role.getNombre());
												})
												.peek(authority -> {
													logger.info("Role: " + authority.getAuthority());
												})
												.collect(Collectors.toList());
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), 
					true, true, true, authorities);
	}

	@Override
	@Transactional
	public Usuario findByUsername(String username) {
		Usuario usuario = usuarioRepository.findByUsername(username);
		return usuario;
	}

}
