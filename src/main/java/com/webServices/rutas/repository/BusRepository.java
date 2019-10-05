package com.webServices.rutas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.repository.query.Param;

import com.webServices.rutas.model.Bus;

/**
 * Repositorio Especifico para Buses
 * @author Davids Adrian Gonzalez Tigrero
 */
@ViewIndexed(designDoc = "bus", viewName = "all")
public interface BusRepository extends CouchbaseRepository<Bus, String>{
	
	/**
	 * Buscar un Bus por placa y estado Activo.
	 * @param placa - Placa de Bus.
	 * @return {@link Bus}
	 */
	Optional<Bus> findByPlacaAndEstadoIsTrue(String placa);
	
	/**
	 * Buscar un Bus por placa.
	 * @param placa - Placa de Bus.
	 * @return {@link Bus}
	 */
	Optional<Bus> findByPlaca(String placa);
	
	/**
	 * Buscar todos los Buses cuyo estado es activo.
	 * @return lista de {@link Bus}
	 */
	Optional<List<Bus>> findByEstadoIsTrue();
	
	/**
	 * Buscar buses que pertenecen a una cooperativa.
	 * @param idC - Id de Cooperativa
	 * @return Lista de {@link Bus}
	 */
	Optional<List<Bus>> findByIdSocioAndEstadoIsTrue(String idC);
	
	/**
	 * Pregunta si existe un Bus por Placa.
	 * @param placa - Placa de {@link Bus}
	 * @return {@link Boolean}
	 */
	@Query("SELECT CASE WHEN count(c)> 0 THEN true ELSE false END "
			+ "FROM #{#n1ql.bucket} as c "
			+ "WHERE lower(c.placa) = lower('#{#nombre}') AND c.estado=true AND c.#{#n1ql.filter}")
	boolean existsByPlacaAndEstadoIsTrue(@Param("nombre") String placa);
}