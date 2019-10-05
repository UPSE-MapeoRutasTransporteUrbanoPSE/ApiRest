package com.webServices.rutas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.webServices.rutas.model.Bus;
import com.webServices.rutas.model.Socio;
import com.webServices.rutas.repository.BusRepository;
import com.webServices.rutas.repository.SocioRepository;

/**
 * Contiene los Servicios de {@link Cooperativa} y realiza sus respectivas operaciones.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@Service
public class SocioService {
	/**
	 * Instancia para el Repositorio de {@link Cooperativa}
	 * @see {@link SocioRepository}
	 */
	@Autowired
	private SocioRepository socioRepository;
	
	/**
	 * Instancia para el Repositorio de {@link Bus}
	 * @see {@link BusRepository}
	 */
	@Autowired
	private BusRepository busRepository;
	
	/**
	 * Obtener lista de {@link Cooperativa}
	 * @return Lista de {@link Cooperativa}
	 */
	public List<Socio> getAllSocio(){
		return socioRepository.findByEstadoIsTrue()
				.filter(a -> !a.isEmpty())
				.orElseThrow(() ->new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existen Cooperativas Registrados."));
	}
	
	/**
	 * Obtener datos de una {@link Cooperativa} entregando su respectivo ID.
	 * @param id - ID de la {@link Cooperativa} que desee obtener los datos
	 * @return {@link Cooperativa}
	 */
	public Socio getSocio(String id) {
		return socioRepository.findByIdAndEstadoIsTrue(id)
				.orElseThrow(() -> new ResponseStatusException(
		           HttpStatus.NOT_FOUND, "No existe la Cooperativa."));
	}
	
	/**
	 * Obtener datos de una {@link Cooperativa} entregando su respectiva ID.
	 * Ignorando su estado eliminado
	 * @param id - ID de la {@link Cooperativa} que desee obtener los datos
	 * @return {@link Cooperativa}
	 */
	public Socio getSocioIgnoreEstado(String id) {
		return socioRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existe Cooperativa registrado con ID "+id+"."));
	}
	
	/**
	 * Obtener datos de una {@link Cooperativa} entregando su respectiva Nombre.
	 * @param nombre - Nombre de la {@link Cooperativa} que desee obtener los datos
	 * @return {@link Cooperativa}
	 */
	public Iterable<Socio> getSocioByNombre(String nombre) {
		return socioRepository.findByNombreContainsAndEstadoIsTrue(nombre).orElseThrow(() -> new ResponseStatusException(
		           HttpStatus.NOT_FOUND, "No existe la Cooperativa con el nombre "+nombre+"."));
	}
	
	/**
	 * Obtener Lista de {@link Cooperativa} Ignorando su estado Eliminado
	 * @return Lista de {@link Cooperativa}
	 */
	public List<Socio> getAllSocioIgnoreEstado(){
		return Optional.of((List<Socio>) socioRepository.findAll())
				.filter(a -> !a.isEmpty())
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existen Cooperativas Registrados."));
	}
	
	/**
	 * Agregar {@link Cooperativa}
	 * @param cooperativa - {@link Cooperativa} que desea guardar
	 * @return {@link Cooperativa} agregado
	 */
	public Socio addSocio(Socio socio) {
		if(socioRepository.existsByNombreAndEstadoIsTrue(socio.getNombre()) || socio.getId()!=null)
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "Ya existe Cooperativa con el Nombre:  "+socio.getNombre()+".");
		else return socioRepository.save(socio);
	}
	
	/**
	 * Actualiza datos de una {@link Cooperativa}
	 * @param cooperativa - {@link Cooperativa} que desea actualizar sus datos
	 * @return {@link Cooperativa} actualizada
	 */
	public Socio updateSocio(Socio socio) {
		if(!socioRepository.existsById(socio.getId()))
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "Socio no esta Registrada.");
		return socioRepository.save(socio);
	}
	
	/**
	 * Elimina una {@link Cooperativa}
	 * @param id - ID de la {@link Cooperativa} a Eliminar
	 */
	public void deleteSocio(String id) {
		Socio c = getSocio(id);
		c.setEstado(false);
		List<Bus> b = busRepository.findByIdSocioAndEstadoIsTrue(c.getId()).get();
		b.stream().forEach(t -> t.setIdSocio(null));
		busRepository.saveAll(b);
		socioRepository.save(c);
	}
	
	/**
	 * Elimina de manera permanente de la base de Datos una {@link Cooperativa}
	 * @param id - ID del {@link Cooperativa} a eliminar
	 */
	public void deleteSocioPhysical(String id) {
		if(socioRepository.existsById(id)) {
			socioRepository.deleteById(id);
			List<Bus> b = busRepository.findByIdSocioAndEstadoIsTrue(id).get();
			b.stream().forEach(t -> t.setIdSocio(null));
			busRepository.saveAll(b);
		}else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "No existe Cooperativa con el id "+id+".");
		}
	}
	
	/**
	 * Elimina de manera Permanente todas las {@link Cooperativa} registrados en la base de datos.
	 */
	public void deleteAllSocioPhysical() {
		List<Bus> b = (List<Bus>)busRepository.findAll();
		b.stream().forEach(t -> t.setIdSocio(null));
		busRepository.saveAll(b);
		socioRepository.deleteAll();
	}
}
