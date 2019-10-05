package com.webServices.rutas.model;

import javax.validation.constraints.Email;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

@Document
public class SegUsuario {
	@IdPrefix
	private String prefix = "usuario";
	@Id @GeneratedValue(strategy = GenerationStrategy.UNIQUE,delimiter = "::")
	private String id;
	@Field
	private String idSocio;
	@Field
	private String perfil;
	@Field
	private String usuario;
	@Field
	private String clave;
	@Field
	private String movil;
	@Field
	@Email(message="Correo no valido")
	private String email;
	@Field
	private Boolean estado;
	public SegUsuario() {
		super();
		this.estado = true;
	}
	
	public SegUsuario(String idSocio,String perfil, String usuario, String clave, String movil,String email) {
		super();
		this.idSocio = idSocio;
		this.perfil = perfil;
		this.usuario = usuario;
		this.clave = clave;
		this.movil = movil;
		this.email = email;
		this.estado = true;
	}


	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String idSegPerfil) {
		this.perfil = idSegPerfil;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String nombre) {
		this.usuario = nombre;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public String getIdSocio() {
		return idSocio;
	}
	public void setIdSocio(String idSocio) {
		this.idSocio = idSocio;
	}

	@Override
	public String toString() {
		return "SegUsuario [id=" + id + ", nombre=" + usuario + ", clave=" + clave + "]";
	}
}
