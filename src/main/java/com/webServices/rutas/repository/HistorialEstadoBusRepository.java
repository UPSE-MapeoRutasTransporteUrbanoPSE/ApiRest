package com.webServices.rutas.repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.repository.query.Param;

import com.webServices.rutas.model.EstadoBus;
import com.webServices.rutas.model.EstadoBusTemporal;
import com.webServices.rutas.model.HistorialEstadoBus;

@ViewIndexed(designDoc = "historialEstadoBus", viewName = "all")
public interface HistorialEstadoBusRepository  extends CouchbaseRepository<HistorialEstadoBus, String>  {
	/**
	 * Obtener Id de Historiales segun una Fecha
	 * @param creadoEn - Fecha de Creacion del Historial
	 * @return Id  de Historiales.
	 */
	@Query("SELECT META(h).id AS _ID, META(h).cas AS _CAS "
			+ "FROM #{#n1ql.bucket} as h "
			+ "WHERE MILLIS_TO_STR(h.creadoEn ,'1111-11-11') = '#{#creadoEn}' AND h.#{#n1ql.filter}")
	List<HistorialEstadoBus> findByCreadoEn(@Param("creadoEn") String creadoEn);
	
	/**
	 * Obtener {@link EstadoBus} crecanos a una parada especificada. 
	 * @param lista - numero de ListaEstado.
	 * @param meridian180condition - Meridiano
	 * @param loc_deg_lat - Latitud
	 * @param loc_deg_lon - Longitud
	 * @param SW_loc_rad_lat - Latutud en radianes Sureste.
	 * @param SW_loc_rad_lon - Longitud en radianes Sureste.
	 * @param NE_loc_rad_lat - Latutud en radianes Nortoeste.
	 * @param NE_loc_rad_lon - Longitud en radianes Nortoeste.
	 * @param distance - Radio de distancia.
	 * @param idHistorial - Id del historial
	 * @return Lista de {@link EstadoBusTemporal}
	 */
	@Query("SELECT m.#{#lista}[ARRAY_POSITION(m.#{#lista},er)-1].posicionActual as posicionAnterior, ARRAY_POSITION(m.#{#lista},er)  as idx, m.placa, er.cantidadUsuarios,er.creationDate,"
					+ "er.velocidad,er.posicionActual, er.estadoPuerta,er.linea, "
					+ "META(m).id AS _ID, META(m).cas AS _CAS "+" FROM #{#n1ql.bucket} AS m UNNEST m.#{#lista} AS er WHERE " + 
    "(RADIANS(er.posicionActual.y) >=  #{#SW_loc_rad_lat}  and RADIANS(er.posicionActual.y) <= #{#NE_loc_rad_lat}) and " +
    "(RADIANS(er.posicionActual.x) >= #{#SW_loc_rad_lon} #{#meridian180condition} RADIANS(er.posicionActual.x) <= #{#NE_loc_rad_lon} ) AND " +
    " acos(sin( RADIANS( #{#loc_deg_lat} )) * sin (RADIANS(er.posicionActual.y)) + cos( RADIANS( #{#loc_deg_lat} )) " 
    + " * cos(RADIANS(er.posicionActual.y)) * cos (RADIANS(er.posicionActual.x) - RADIANS( #{#loc_deg_lon} ))) <= #{#distance}/6378  AND m.#{#n1ql.filter} AND meta(m).id = '#{#idHistorial}'")
	List<EstadoBusTemporal> findByListaEstadosInPosicionWithIn( @Param("lista") String lista,
																@Param("meridian180condition") String meridian180condition,
																@Param("loc_deg_lat") double loc_deg_lat, 
																@Param("loc_deg_lon") double loc_deg_lon,
																@Param("SW_loc_rad_lat") double SW_loc_rad_lat,
																@Param("SW_loc_rad_lon") double SW_loc_rad_lon,
																@Param("NE_loc_rad_lat") double NE_loc_rad_lat,
																@Param("NE_loc_rad_lon") double NE_loc_rad_lon,
																@Param("distance") double distance,
																@Param("idHistorial") String idHistorial);

	/**
	 * Obtener Historial de Bus por Fecha de cracion y Placa.
	 * @param fecha - Fecha de creación.
	 * @param p - Placa de Bus.
	 * @return {@link HistorialEstadoBus}
	 */
	Optional<HistorialEstadoBus> findByCreadoEnAndPlaca(Date fecha, String p);
	
	/**
	 * Preguntar si existe Historial de bus.
	 * @param placa - Placa de Bus
	 * @param creadoEn - Fecha de creación
	 * @return {@link Boolean}
	 */
	@Query("SELECT CASE WHEN count(c)> 0 THEN true ELSE false END "
			+ "FROM #{#n1ql.bucket} as c "
			+ "WHERE lower(c.placa) = lower('#{#placa}') AND c.creadoEn = #{#creadoEn} AND c.#{#n1ql.filter}")
	boolean existsByPlacaAndCreadoEn(@Param("placa") String placa,@Param("creadoEn") long creadoEn);
}