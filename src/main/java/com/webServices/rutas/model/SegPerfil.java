package com.webServices.rutas.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
@Document
public class SegPerfil {
	@Id
	private String id;
	@Field
	private List<String> perfiles = new ArrayList<String>();
	
	public SegPerfil(String id, List<String> perfiles, Boolean estado) {
		super();
		this.id = id;
		this.perfiles = perfiles;
	}
	public SegPerfil() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getMyMap() {
		return perfiles;
	}
	public void setMyMap(List<String> perfiles) {
		this.perfiles = perfiles;
	}
	
}
