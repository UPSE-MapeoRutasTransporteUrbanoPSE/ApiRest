package com.webServices.rutas.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.repository.query.Param;

import com.webServices.rutas.model.Socio;
/**
 * Repositorio para Cooperativas
 * @author Davids Adrian Gonzalez Tigrero
 *
 */
@ViewIndexed(designDoc = "socio", viewName = "all")
public interface SocioRepository extends CouchbaseRepository<Socio, String>{
	
	/**
	 * Buscar {@link Cooperativa} por nombre y con estado activo.
	 * @param nombre - Nombre de cooperativa
	 * @return Lista de Cooperativas coincidentes.
	 */
	Optional<Iterable<Socio>> findByNombreContainsAndEstadoIsTrue(String nombre);
	
	/**
	 * Buscar {@link Cooperativa}s con estado Activo
	 * @return Lista de {@link Cooperativa}
	 */
	Optional<List<Socio>> findByEstadoIsTrue();
	
	/**
	 * Buscar cooperativas por Id y estado Activo.
	 * @param id - ID de Cooperativa
	 * @return {@link Cooperativa}
	 */
	@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND meta().id = '#{#id}' AND estado=true")
	Optional<Socio> findByIdAndEstadoIsTrue(@Param("id") String id);
	
	/**
	 * Preguntar si Existe Cooperativa.
	 * @param nombre - Nombre de Cooperativa
	 * @return {@link Boolean}
	 */
	@Query("SELECT CASE WHEN count(c)> 0 THEN true ELSE false END "
			+ "FROM #{#n1ql.bucket} as c "
			+ "WHERE lower(c.nombre) = lower('#{#nombre}') AND c.estado=true AND c.#{#n1ql.filter}")
	boolean existsByNombreAndEstadoIsTrue(@Param("nombre") String nombre);
}