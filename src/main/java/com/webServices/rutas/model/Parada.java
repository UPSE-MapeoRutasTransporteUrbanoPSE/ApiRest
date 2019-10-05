package com.webServices.rutas.model;

import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;
import org.springframework.data.geo.Point;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
/**
 * Representa una Parada
 * @author Davids Adrian Gonzalez Tigrero
 */
public class Parada {
	@IdPrefix
	private String prefix = "parada";
	@Id @GeneratedValue(strategy = GenerationStrategy.UNIQUE,delimiter = "::")
	@Field
	private String id;
	
	/**
	 * Nombre de Parada
	 */
	@Field
	private String nombre;
	
	/**
	 * Url de Foto.
	 */
	@Field
	private String urlFoto;
	
	/**
	 * Posición de Parada.
	 */
	@Field
    private Point coordenada;
	@Field
    private Boolean estado;
	public Parada(String nombre, String urlFoto,Point coordenada) {
		super();
		this.nombre = nombre;
		this.urlFoto = urlFoto;
		this.coordenada = coordenada;
		this.estado = true;
	}
	public Parada() {
		super();
		this.estado = true;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrlFoto() {
		return urlFoto;
	}
	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}
	public Point getCoordenada() {
		return coordenada;
	}
	public void setCoordenada(Point coordenada) {
		this.coordenada = coordenada;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public double distance(Point farAway, String unit) {
		if ((farAway.getX() == coordenada.getX()) && (farAway.getY() == coordenada.getY())) {
			return 0;
		}
		else {
			double theta = coordenada.getX() - farAway.getX();
			double dist = Math.sin(Math.toRadians(coordenada.getY())) * Math.sin(Math.toRadians(farAway.getY())) + Math.cos(Math.toRadians(coordenada.getY())) * Math.cos(Math.toRadians(farAway.getY())) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			if (unit == "K") {
				dist = dist * 1.609344;
			} else if (unit == "N") {
				dist = dist * 0.8684;
			} else if (unit == "M") {
				dist = dist * 1.609344 * 1000;
			}

			return (dist);
		}
	}
	@Override
	public String toString() {
		return "Parada [id=" + id + ", nombre=" + nombre + ", urlFoto=" + urlFoto + ", coordenada=" + coordenada
				+ ", estado=" + estado + "]";
	}
	
}