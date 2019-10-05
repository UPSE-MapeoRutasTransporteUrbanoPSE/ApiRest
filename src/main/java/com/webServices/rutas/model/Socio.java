package com.webServices.rutas.model;

import java.util.List;

public class Socio {
	private String id;
	private String cedula;
	private String nombre;
	private String apellido;
	private List<String> listIdBusesAsociados;
	private Boolean estado;
	public Socio(String id, String cedula, String nombre, String apellido, List<String> listIdBusesAsociados,
			Boolean estado) {
		super();
		this.id = id;
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.listIdBusesAsociados = listIdBusesAsociados;
		this.estado = estado;
	}
	public Socio() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public List<String> getListIdBusesAsociados() {
		return listIdBusesAsociados;
	}
	public void setListIdBusesAsociados(List<String> listIdBusesAsociados) {
		this.listIdBusesAsociados = listIdBusesAsociados;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
}
