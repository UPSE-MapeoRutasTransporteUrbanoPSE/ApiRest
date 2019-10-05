package com.webServices.rutas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.Dimensional;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.geo.Circle;
import org.springframework.data.repository.query.Param;

import com.webServices.rutas.model.Parada;

/**
 * Repositorio de Parada.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@ViewIndexed(designDoc = "parada", viewName = "all")
public interface ParadaRepository extends CouchbaseRepository<Parada, String>{
	
	/**
	 * Buscar Parada por Id y estado activo.
	 * @param id - Id de Parada
	 * @return {@link Parada}
	 */
	@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND meta().id = '#{#id}' AND estado=true")
	Optional<Parada> findByIdAndEstadoIsTrue(@Param("id") String id);
	
	/**
	 * Buscar todas las Paradas.
	 * @return lista de {@link Parada}
	 */
	Optional<List<Parada>> findByEstadoIsTrue();
	
	/**
	 * Buscar Paradas en un radio Daro
	 * @param p - {@link Circle}
	 * @return Lista de {@link Parada}
	 */
	@Dimensional(designDocument = "spatialView_parada", spatialViewName = "spatialView_parada")
	Optional<List<Parada>> findByCoordenadaWithin(Circle p);
	
	/**
	 * Buscar las paradas pertenecientes a una Linea.
	 * @param linea - Linea de {@link Cooperativa}
	 * @return Lista de {@link Parada}
	 */
	@Query("SELECT t.*, META(t).id AS _ID, META(t).cas AS _CAS FROM #{#n1ql.bucket} AS p JOIN #{#n1ql.bucket} AS t ON KEYS ARRAY paradaId FOR paradaId IN p.listasParadas END where p.linea = '#{#linea}' AND (t.#{#n1ql.filter} or p.`_class` = \"com.webServices.rutas.model.Ruta\");")
	Iterable<Parada> findAllByLinea(@Param("linea") String linea);
	
	/**
	 * Preguntar si existe una Parada.
	 * @param nombre - Nombre de Parada
	 * @return {@link Boolean}
	 */
	@Query("SELECT CASE WHEN count(c)> 0 THEN true ELSE false END "
			+ "FROM #{#n1ql.bucket} as c "
			+ "WHERE lower(c.nombre) = lower('#{#nombre}') AND c.estado=true AND c.#{#n1ql.filter}")
	boolean existsByNombreAndEstadoIsTrue(@Param("nombre") String nombre);

	/**
	 * Buscar Parada por Nombre y Estado activo.
	 * @param nombre - Nombre de Parada
	 * @return - Paradas que coinciden en Nombre.
	 */
	Optional<Iterable<Parada>> findByNombreContainsAndEstadoIsTrue(String nombre);
}