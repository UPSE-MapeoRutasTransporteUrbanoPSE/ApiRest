package com.webServices.rutas.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

/**
 * Representa la lista de calculos de Tiempos de buses en una ruta y entre paradas.
 * @author Davids Adrian Gonzalez Tigrero
 *
 */
@Document
public class TimeControlParada {
	@IdPrefix
	private String prefix = "timeControlParada";
	@Id @GeneratedValue(strategy = GenerationStrategy.UNIQUE,delimiter = "::")
	private String id;
	
	/**
	 * Linea de Cooperativa.
	 */
	@Field
	private String linea;
	
	/**
	 * Lista de Tiempos entre parada.
	 */
	@Field
	private List<BetweenParada> listTime;
	
	public TimeControlParada() {
		super();
	}
	public TimeControlParada(String linea) {
		super();
		this.linea = linea;
	}
	public TimeControlParada(String linea, List<Parada> paradas) {
		super();
		this.linea = linea;
		List<BetweenParada> listTime= new ArrayList<>();
		for(int i=0;i<paradas.size();i++) {
			if(i == paradas.size()-1) {
				listTime.add(new BetweenParada(paradas.get(i).getId(), paradas.get(0).getId()));
			}else {
				listTime.add(new BetweenParada(paradas.get(i).getId(), paradas.get(i+1).getId()));
			}
		}
		this.listTime = listTime;
	}
	public TimeControlParada(String id, String linea, List<BetweenParada> listTime) {
		super();
		this.id = id;
		this.linea = linea;
		this.listTime = listTime;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
	public List<BetweenParada> getListTime() {
		return listTime;
	}
	public void setListTime(List<BetweenParada> listTime) {
		this.listTime = listTime;
	}
	public TimeControlParada buscarParada1AndParada2(String parada1, String parada2, Long diff) {
		for(int i=0; i< this.listTime.size();i++) {
			if(this.listTime.get(i).getIdparada1().equals(parada1) && this.listTime.get(i).getIdparada2().equals(parada2)) {
				this.listTime.get(i).addListTiempo(diff);
				break;
			}
		}
		return this;
	}
	@Override
	public String toString() {
		return "TimeControlParada [id=" + id + ", linea=" + linea + ", listTime=" + listTime + "]";
	}
}
