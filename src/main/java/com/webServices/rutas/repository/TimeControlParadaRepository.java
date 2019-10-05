package com.webServices.rutas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.repository.query.Param;

import com.webServices.rutas.model.BetweenParada;
import com.webServices.rutas.model.Parada;
import com.webServices.rutas.model.TimeControlParada;

/**
 * Repositorio de {@link TimeControlParada}
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@ViewIndexed(designDoc = "timeControlParada", viewName = "all")
public interface TimeControlParadaRepository extends CouchbaseRepository<TimeControlParada, String>{
	
	/**
	 * Buscar {@link TimeControlParada} por Linea
	 * @param linea - Linea de Cooperativa
	 * @return {@link TimeControlParada}
	 */
	Optional<TimeControlParada> findByLinea(String linea);
	
	/**
	 * Buscar {@link TimeControlParada} que contenga Id de Parada.
	 * @param idParada1 - ID de {@link Parada}
	 * @param linea - Linea de Cooperativa.
	 * @return Lista entre Parada.
	 */
	@Query("select "
			+ "ARRAY betweenParada FOR betweenParada IN m.listTime WHEN betweenParada.idparada1 = '#{#idP1}' END AS item "
			+ "from #{#n1ql.bucket} as m "
			+ "where linea='#{#linea}' AND m.#{#n1ql.filter}")
	List<BetweenParada> findBetweenParadaByIdParada1(@Param("idP1") String idParada1,@Param("linea") String linea);
}
