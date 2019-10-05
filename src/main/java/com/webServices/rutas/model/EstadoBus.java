package com.webServices.rutas.model;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.geo.Point;

/**
 * Representa el estado de un {@link Bus}
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
public class EstadoBus {
	
	/**
	 * Fecha de creacion del estado de Bus.
	 */
	@NotNull
	private Date creationDate;
	
	/**
	 * Velocidad del Bus.
	 */
	@NotNull
	@Max(value = 160,message = "La velocidad no debe exceder los 120 Km/H.")
	@Min(value = 0, message = "La velocidad no debe ser Menor a o Km/H.")
	private int velocidad;
	
	/**
	 * Cantidad de usuarios de un Bus.
	 */
	@Max(value = 75,message = "Bus excedio el limite Permitido.")
	private int cantidadUsuarios;
	
	/**
	 * Punto de Ubicació actual del Bus en coordenadas (X,Y)
	 */
	private Point posicionActual;
	
	/**
	 * Estado de la Puerta del Bus.
	 */
	private Boolean estadoPuerta;
	
	public EstadoBus(int velocidad, int cantidadUsuarios, Point posicionActual,Boolean estadoPuerta) {
		super();
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Guayaquil"));
		this.velocidad = velocidad;
		this.cantidadUsuarios = cantidadUsuarios;
		this.posicionActual = posicionActual;
		this.estadoPuerta = estadoPuerta;
		this.creationDate = now.getTime();
	}
	public EstadoBus(EstadoBus bus) {
		this.velocidad = bus.velocidad;
		this.cantidadUsuarios = bus.cantidadUsuarios;
		this.posicionActual = bus.posicionActual;
		this.estadoPuerta = bus.estadoPuerta;
		this.creationDate = bus.creationDate;
	}
	public EstadoBus() {
		super();
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Guayaquil"));
		this.creationDate = now.getTime();
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
	}
	public Boolean getEstadoPuerta() {
		return estadoPuerta;
	}
	public void setEstadoPuerta(Boolean estadoPuerta) {
		this.estadoPuerta = estadoPuerta;
	}
	@Override
	public String toString() {
		return "EstadoBus [creationDate=" + creationDate + ", velocidad=" + velocidad + ", cantidadUsuarios="
				+ cantidadUsuarios + ", posicionActual=" + posicionActual + ", estadoPuerta=" + estadoPuerta
				+ "]";
	}
	
}
