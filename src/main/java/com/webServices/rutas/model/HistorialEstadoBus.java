package com.webServices.rutas.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;


/**
 * Representa el Historial de los Estados que ha tenido un Bus en un dia
 * @author Davids Adrian Gonzalez Tigrero
 */
@Document
public class HistorialEstadoBus {
	@IdPrefix
	private String prefix = "historialEstadoBus";
	@Id @GeneratedValue(strategy = GenerationStrategy.UNIQUE,delimiter = "::")
	private String id;
	
	/**
	 * Fecha de creación del Historial de Bus
	 */
	@Field
	@NotNull(message = "Debe ingreasar una fecha.")
	private Date creadoEn;
	
	/**
	 * Placa del {@link Bus}
	 */
	@NotNull(message = "Es necesario registrar un Numero de Placa.")
	@Size(max=7, min=6, message = "Debe contener de 6 a 7 Caracteres")
	@Field
	private String placa;
	@NotNull(message = "Es necesario registrar un Numero de Linea.")
	@Size(max=3, min=1, message = "Debe contener de 1 a 3 Caracteres")
	@Field
	private String linea;
	
	@Field
	private List<EstadoBus> listaEstados1;
	@Field
	private List<EstadoBus> listaEstados2;
	@Field
	private List<EstadoBus> listaEstados3;
	public HistorialEstadoBus(String placa, List<EstadoBus> listaEstados) {
		super();
		this.placa = GlobalVariables.confirmPlaca(placa);;
		this.listaEstados1 = listaEstados;
		this.creadoEn = GlobalVariables.getFechaDMA();
	}
	public HistorialEstadoBus() {
		super();
        this.creadoEn = GlobalVariables.getFechaDMA();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = GlobalVariables.confirmPlaca(placa);;
	}
	public List<EstadoBus> getListaEstados1() {
		return listaEstados1;
	}
	public void setListaEstados1(List<EstadoBus> listaEstados1) {
		this.listaEstados1 = listaEstados1;
	}
	public List<EstadoBus> getListaEstados2() {
		return listaEstados2;
	}
	public void setListaEstados2(List<EstadoBus> listaEstados2) {
		this.listaEstados2 = listaEstados2;
	}
	public List<EstadoBus> getListaEstados3() {
		return listaEstados3;
	}
	public void setListaEstados3(List<EstadoBus> listaEstados3) {
		this.listaEstados3 = listaEstados3;
	}
	public Date getCreadoEn() {
		return creadoEn;
	}
	public void setCreadoEn(Date creadoEn) {
		this.creadoEn = creadoEn;
	}
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
}