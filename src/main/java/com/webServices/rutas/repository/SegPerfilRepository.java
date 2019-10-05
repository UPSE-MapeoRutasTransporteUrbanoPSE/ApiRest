package com.webServices.rutas.repository;

import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import com.webServices.rutas.model.SegPerfil;
/**
 * Repositorio de Perfil.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@ViewIndexed(designDoc = "segPerfil", viewName = "all")
public interface SegPerfilRepository  extends CouchbaseRepository<SegPerfil, String>{
}
