package com.webServices.rutas.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;
import org.springframework.data.geo.Point;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
/**
 * Representa una Ruta de una Linea con sus respectivas paradas
 * @author Davids
 */
@Document
public class Ruta {
	@IdPrefix
	private String prefix = "ruta";
	@Id @GeneratedValue(strategy = GenerationStrategy.UNIQUE,delimiter = "::")
	private String id;
	
	/**
	 * Linea de Cooperativa.
	 */
	@NotNull(message = "Es necesario registrar Nombre.")
	@Field
	private String nombre;
	
	/**
	 * Lista de puntos que conforman la Ruta
	 */
	@Field
    private List<Point> listasPuntos;
	
	/**
	 * Lista de identificadores de Paradas de la Ruta.
	 */
	@Field
    private List<String> listasParadas;
	
	/**
	 * Lugares de Interes en una Ruta.
	 */
	@Field
	private List<String> lugaresInteres;
	
	@Field
	private String listPuntoString;
	
	@Field
	private String listParadaString;
	
	@Field
	private String color;
	
	@Field
	private Boolean estado; 
	
    public Ruta() {
    	this.estado = true;
    }
    public Ruta(String color, String nombre, List<Point> listasPuntos, List<String> listasParadas,List<String> lugaresInteres, String listParadaString, String listPuntoString) {
		super();
		this.color = color;
		this.listPuntoString = listPuntoString;
		this.listParadaString = listParadaString;
		this.nombre = nombre;
		this.listasPuntos = listasPuntos;
		this.listasParadas = listasParadas;
		this.lugaresInteres = lugaresInteres;
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
    public List<Point> getListasPuntos() {
        return listasPuntos;
    }
    public void setListasPuntos(List<Point> listasPuntos) {
        this.listasPuntos = listasPuntos;
    }

    public List<String> getListasParadas() {
		return listasParadas;
	}
	public void setListasParadas(List<String> listasParadas) {
		this.listasParadas = listasParadas;
	}
	public List<String> getLugaresInteres() {
		return lugaresInteres;
	}
	public void setLugaresInteres(List<String> lugaresInteres) {
		this.lugaresInteres = lugaresInteres;
	}
	public String getListPuntoString() {
		return listPuntoString;
	}
	public void setListPuntoString(String listPuntoString) {
		this.listPuntoString = listPuntoString;
	}
	public String getListParadaString() {
		return listParadaString;
	}
	public void setListParadaString(String listParadaString) {
		this.listParadaString = listParadaString;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
}