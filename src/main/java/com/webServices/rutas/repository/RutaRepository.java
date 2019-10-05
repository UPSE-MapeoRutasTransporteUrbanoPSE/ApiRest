package com.webServices.rutas.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.repository.query.Param;

import com.webServices.rutas.model.Ruta;

/**
 * Repositorio de Ruta
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@ViewIndexed(designDoc = "ruta", viewName = "all")
public interface RutaRepository extends CouchbaseRepository<Ruta, String>{
	
	/**
	 * Buscar Rutas con estado Activo.
	 * @return Lista de {@link Ruta}
	 */
	Optional<List<Ruta>> findByEstadoIsTrue();
	
	/**
	 * Buscar {@link Ruta} por Linea y Estado Activo.
	 * @param linea - Linea de Cooperativa.
	 * @return {@link Ruta}
	 */
	Optional<Ruta> findByNombreAndEstadoIsTrue(String linea);
	
	/**
	 * Buscar {@link Ruta} que contenga una Parada.
	 * @param idParada - ID de Parada
	 * @return {@link Ruta}
	 */
	@Query("#{#n1ql.selectEntity} where #{#n1ql.filter} AND ARRAY_CONTAINS(listasParadas, '#{#idParada}')")
	Optional<Ruta> findByListaParadasContains(@Param("idParada") String idParada);
	
	/**
	 * Preguntar si existe una Ruta segun una Linea de Cooperativa.
	 * @param linea - Linea de Cooperativa
	 * @return {@link Boolean}
	 */
	@Query("SELECT CASE WHEN count(c)> 0 THEN true ELSE false END "
			+ "FROM #{#n1ql.bucket} as c "
			+ "WHERE lower(c.nombre) = lower('#{#linea}') AND c.estado=true AND c.#{#n1ql.filter}")
	boolean existsByNombreAndEstadoIsTrue(@Param("linea") String linea);
	
	/**
	 * Preguntar si existe Parada en Linea
	 * @param linea - Linea de Cooperativa
	 * @param idParada - ID de Parada.
	 * @return {@link Boolean}
	 */
	@Query("SELECT ARRAY_CONTAINS(t.listasParadas, '#{#idParada}') " + 
			"FROM #{#n1ql.bucket} as t " + 
			"WHERE t.#{#n1ql.filter} AND t.nombre = '#{#linea}' " + 
			"LIMIT 1;")
	boolean existsByParadaInLinea(@Param("linea") String linea,@Param("idParada") String idParada);
}