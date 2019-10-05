package com.webServices.rutas.model;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;
import org.springframework.data.geo.Point;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
/**
 * Representa el Estado de Bus temporal de un {@link Bus}.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
public class EstadoBusTemporal {
	@IdPrefix
	private String prefix = "estadoBusTemporal";
	@Id @GeneratedValue(strategy = GenerationStrategy.UNIQUE,delimiter = "::")
	private String id;
	/**
	 * Fecha de creación de un bus.
	 */
	@NotNull
	@Field
	private Date creationDate;
	
	/**
	 * Velocidad actual de un Bus.
	 */
	@NotNull
	@Max(value = 160,message = "La velocidad no debe exceder los 120 Km/H.")
	@Min(value = 0, message = "La velocidad no debe ser Menor a o Km/H.")
	@Field
	private int velocidad;
	
	/**
	 * Placa del Bus.
	 */
	@Field
	@NotNull(message = "Es necesario registrar un Numero de Placa.")
	@Size(max=7, min=6, message = "Debe contener de 6 a 7 Caracteres")
	private String placa;
	
	/**
	 * Cantidad de Usuarios de un Bus.
	 */
	@Max(value = 75,message = "Bus excedio el limite Permitido.")
	@Field
	private int cantidadUsuarios;
	
	/**
	 * Posición Actual de un Bus.
	 */
	@Field
	private Point posicionActual;
	
	/**
	 * Locacion de un Bus para ElasticSearch.
	 */
	@Field
	private Location location;
	
	/**
	 * Punto de Ubucación anterior de un Bus.
	 */
	@Field
	private Point posicionAnterior;
	
	/**
	 * Estado de la Puerta del Bus.
	 */
	@Field
	private Boolean estadoPuerta;
	
	@Field
	private int idx;
	
	/**
	 * Linea de un Bus.
	 */
	@Field
	private String linea;
	public EstadoBusTemporal(Date creationDate, int velocidad, int cantidadUsuarios, Point posicionActual,Boolean estadoPuerta,String linea, int idx) {
		super();
		this.velocidad = velocidad;
		this.cantidadUsuarios = cantidadUsuarios;
		this.posicionActual = posicionActual;
		this.estadoPuerta = estadoPuerta;
		this.creationDate = GlobalVariables.getFecha();
		this.location.setLat(posicionActual.getY());
		this.location.setLon(posicionActual.getX());
		this.linea = linea;
		this.idx = idx;
	}
	public EstadoBusTemporal(EstadoBusTemporal bus) {
		this.velocidad = bus.velocidad;
		this.cantidadUsuarios = bus.cantidadUsuarios;
		this.posicionActual = bus.posicionActual;
		this.estadoPuerta = bus.estadoPuerta;
		this.creationDate = GlobalVariables.getFecha();
		this.location = bus.getLocation();
		this.linea = bus.linea;
	}
	public EstadoBusTemporal() {
		super();
	}
	
	public EstadoBusTemporal(EstadoBus eb, String linea, String placa) {
		this.cantidadUsuarios = eb.getCantidadUsuarios();
		this.creationDate = GlobalVariables.getFecha();
		this.estadoPuerta = eb.getEstadoPuerta();
		this.linea = linea;
		this.placa = placa;
		this.posicionActual = eb.getPosicionActual();
		this.velocidad = eb.getVelocidad();
		this.location = new Location(eb.getPosicionActual().getX(), eb.getPosicionActual().getY());
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public int getVelocidad() {
		return velocidad;
	}
	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	public int getCantidadUsuarios() {
		return cantidadUsuarios;
	}
	public void setCantidadUsuarios(int cantidadUsuarios) {
		this.cantidadUsuarios = cantidadUsuarios;
	}
	public Point getPosicionActual() {
		return posicionActual;
	}
	public void setPosicionActual(Point posicionActual) {
		this.posicionActual = posicionActual;
		this.location.setLat(posicionActual.getY());
		this.location.setLon(posicionActual.getX());
	}
	public Point getPosicionAnterior() {
		return posicionAnterior;
	}
	public Boolean getEstadoPuerta() {
		return estadoPuerta;
	}
	public void setEstadoPuerta(Boolean estadoPuerta) {
		this.estadoPuerta = estadoPuerta;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
	public String getId() {
		return id;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	@Override
	public String toString() {
		return "EstadoBusTemporal [id=" + id + ", creationDate=" + creationDate + ", velocidad=" + velocidad
				+ ", cantidadUsuarios=" + cantidadUsuarios + ", posicionActual=" + posicionActual + ", estadoPuerta="
				+ estadoPuerta + ", idx=" + idx + ", linea=" + linea + "]";
	}
	public void updateEstadoBus(EstadoBus eb, String linea, String placa) {
		this.cantidadUsuarios = eb.getCantidadUsuarios();
		this.creationDate =	GlobalVariables.getFecha();
		this.estadoPuerta = eb.getEstadoPuerta();
		this.linea = linea;
		this.placa = placa;
		this.posicionAnterior = this.posicionActual; 
		this.posicionActual = eb.getPosicionActual();
		this.velocidad = eb.getVelocidad();
		this.location = new Location(eb.getPosicionActual().getX(), eb.getPosicionActual().getY());
	}
}