package com.webServices.rutas.model;

/**
 * Representa una Ubicacion de un {@link Bus} en tiempo real.
 * Clase extra usada para Mapas de Calor en ElasticSearch.
 * @author Davids Adrian Gonzalez Tigrero
 *
 */
public class Location {
	/**
	 * Longitud
	 */
	private double lon;
	
	/**
	 * Latitud
	 */
	private double lat;
	public Location(double lon, double lat) {
		super();
		this.lon = lon;
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
}
