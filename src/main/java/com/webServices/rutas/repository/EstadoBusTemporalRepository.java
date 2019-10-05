package com.webServices.rutas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import com.webServices.rutas.model.EstadoBusTemporal;

/**
 * Repositorios para {@link EstadoBusTemporal}
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@ViewIndexed(designDoc = "estadoBusTemporal", viewName = "all")
public interface EstadoBusTemporalRepository  extends CouchbaseRepository<EstadoBusTemporal, String>{
	
	/**
	 * Buscar Estado Bus Temporal por Bus.
	 * @param placa - Placa de Bus
	 * @return - {@link EstadoBusTemporal}
	 */
	Optional<EstadoBusTemporal> findByPlaca(String placa);

	/**
	 * Buscar {@link EstadoBusTemporal} por Linea de cooperativa.
	 * @param linea - Linea de Cooperativa.
	 * @return Lista de {@link EstadoBusTemporal}
	 */
	Optional<List<EstadoBusTemporal>> findByLinea(String linea);
}