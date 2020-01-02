package org.dev.app.ws.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_roles", schema = "public")
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6841220628133761119L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rol_id", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "nombre", unique = true, length = 20)
	private String nombre;

	public Role() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
