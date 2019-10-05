package com.webServices.rutas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import com.webServices.rutas.model.SegUsuario;

/**
 * Repositorio de Usuario
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@ViewIndexed(designDoc = "segUsuario", viewName = "all")
public interface SegUsuarioRepository extends CouchbaseRepository<SegUsuario, String>{
	
	/**
	 * Buscar Usuarios con estado Activo.
	 * @return Lista de Usuarios.
	 */
	Optional<List<SegUsuario>> findByEstadoIsTrue();
	
	/**
	 * Buscar Usuario por Nombre o por Email.
	 * @param name - Nombre de Usuario
	 * @param email - Email de Usuario.
	 * @return Usuario.
	 */
	Optional<SegUsuario> findByUsuarioOrEmail(String name,String email);
	
	/**
	 * Buscar Usuario por ID y estado Activo
	 * @param id - ID de Usuario.
	 * @return Usuario.
	 */
	@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND meta().id = '#{#id}' AND estado=true")
	Optional<SegUsuario> findByIdAndEstadoIsTrue(String id);
}