package com.webServices.rutas.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.collect.Iterables;
import com.webServices.rutas.model.GlobalVariables;
import com.webServices.rutas.model.Parada;
import com.webServices.rutas.model.Ruta;
import com.webServices.rutas.repository.ParadaRepository;
import com.webServices.rutas.repository.RutaRepository;

/**
 * Contiene los Servicios de {@link Parada} y realiza sus respectivas operaciones.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@Service
public class ParadaService {
	/**
	 * Instancia para el Repositorio de {@link Ruta}
	 * @see {@link RutaRepository}
	 */
	@Autowired
	private RutaRepository rutaRepository;
	
	/**
	 * Instancia para el Repositorio de {@link Parada}
	 * @see {@link ParadaRepository}
	 */
	@Autowired
	private ParadaRepository paradaRepository;
	
	/**
	 * Obtener lista de {@link Parada}
	 * @return Lista de {@link Parada}
	 */
	public Iterable<Parada> getAllParada(){
		return paradaRepository.findByEstadoIsTrue()
				.filter(a -> !a.isEmpty())
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existen Paradas Registrados."));
	}

	/**
	 * Obtener datos de una {@link Parada} entregando su respectivo ID.
	 * @param id - ID de la {@link Parada} que desee obtener los datos
	 * @return {@link Parada}
	 */
	public Parada getParada(String id) {
		return paradaRepository.findByIdAndEstadoIsTrue(id).orElseThrow(() -> new ResponseStatusException(
		           HttpStatus.NOT_FOUND, "No existe la Parada."));
	}
	
	/**
	 * Obtener datos de una {@link Parada} entregando su respectiva ID.
	 * Ignorando su estado eliminado
	 * @param id - ID de la {@link Parada} que desee obtener los datos
	 * @return {@link Parada}
	 */
	public Parada getParadaIgnoreEstado(String id) {
		return paradaRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existe Parada registrado con ID "+id+"."));
	}
	
	/**
	 * Obtener Lista de {@link Parada} Ignorando su estado Eliminado
	 * @return Lista de {@link Parada}
	 */
	public Iterable<Parada> getAllParadaIgnoreEstado(){
		return Optional.of((List<Parada>)paradaRepository.findAll())
				.filter(a -> !a.isEmpty())
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existen Paradas Registrados."));
	}
	
	/**
	 * Obtener datos de una {@link Parada} entregando su respectiva Nombre.
	 * @param nombre - Nombre de la {@link Parada} que desee obtener los datos
	 * @return {@link Parada}
	 */
	public Iterable<Parada> getParadaByNombre(String nombre) {
		return paradaRepository.findByNombreContainsAndEstadoIsTrue(nombre).filter(p ->Iterables.size(p)>0).orElseThrow(() -> new ResponseStatusException(
		           HttpStatus.NOT_FOUND, "No existe la Parada con el nombre "+nombre+"."));
	}
	
	/**
	 * Agregar {@link Parada}
	 * @param parada - {@link Parada} que desea guardar
	 * @return {@link Parada} agregado
	 */
	public Parada addParada(Parada parada) {
		if(paradaRepository.existsByNombreAndEstadoIsTrue(parada.getNombre()) || parada.getId() != null)
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "Ya existe Parada con el Nombre:  "+parada.getNombre()+".");
		else return paradaRepository.save(parada);
	}
	
	/**
	 * Actualiza datos de una {@link Parada}
	 * @param parada - {@link Parada} que desea actualizar sus datos
	 * @return {@link Parada} actualizada
	 */
	public Parada updateParada(Parada parada) {
		if(!paradaRepository.existsById(parada.getId()))
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "Parada no esta Registrada.");
		return paradaRepository.save(parada);
	}
	
	/**
	 * Elimina una {@link Parada}
	 * @param id - Id de la {@link Parada} a Eliminar
	 */
	public void deleteParada(String id) {
		Parada c = getParada(id);
		c.setEstado(false);
		Ruta r = rutaRepository.findByListaParadasContains(id).get();
		r.getListasParadas().removeIf(n -> n.equals(c.getId()));
		rutaRepository.save(r);
		paradaRepository.save(c);
	}
	
	/**
	 * Obtiene lista de {@link Parada} cercanas a un {@link Point} que pertenecen a una linea de {@link Cooperativa}
	 * @param punto - {@link Point}, Ubicación centrico. 
	 * @param longitud - Radio de alcance a Buscar las {@link Parada}
	 * @param linea - Linea de una {@link Cooperativa}
	 * @return Lista de {@link Parada} cercanas a un punto pertenecientes a una linea de una {@link Cooperativa}
	 */
	public List<Parada> getParadasCercanasRadio(Point punto,Double longitud,String linea){
		Circle circle = new Circle(punto,new Distance((longitud*GlobalVariables.coeficiente), Metrics.KILOMETERS));
		List<String> idsParadas = rutaRepository.findByNombreAndEstadoIsTrue(linea).orElseThrow(() -> new ResponseStatusException(
			       HttpStatus.NOT_FOUND, "No exsiste paradas para la Line "+linea+".")).getListasParadas();
		Optional<List<Parada>> par = paradaRepository.findByCoordenadaWithin(circle);
		par = Optional.of(par.get().stream()
		            .filter(e -> idsParadas.contains(e.getId()))
		            .collect(Collectors.toList()));
		return par.filter(a -> !a.isEmpty()).orElseThrow(() -> new ResponseStatusException(
			       HttpStatus.NOT_FOUND, "No exsiste paradas cercanas a su posicion de la linea "+linea+"."));
	}
	
	/**
	 * Obtiene lista de {@link Parada} cercanas a un {@link Point}.
	 * @param punto - {@link Point}, Ubicación centrico.
	 * @param longitud - Radio de alcance a Buscar las {@link Parada}
	 * @return Lista de {@link Parada} cercanas a un punto.
	 */
	public Iterable<Parada> getAllParadaCercanasRadio(Point punto,Double longitud){
		Circle circle = new Circle(punto,new Distance(longitud*GlobalVariables.coeficiente, Metrics.KILOMETERS));
		return paradaRepository.findByCoordenadaWithin(circle).filter(a-> !a.isEmpty()).orElseThrow(() -> new ResponseStatusException(
			       HttpStatus.NOT_FOUND, "No exsiste paradas cercanas."));
	}
	
	/**
	 * Elimina de manera permanente de la base de Datos una {@link Parada}
	 * @param id - ID del {@link Parada} a eliminar
	 */
	public void deleteParadaPhysical(String id) {
		if(paradaRepository.existsById(id)) {
			Ruta r = rutaRepository.findByListaParadasContains(id).get();
			r.getListasParadas().removeIf(n -> n.equals(id));
			rutaRepository.save(r);
			paradaRepository.deleteById(id);
		}
		else
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "No existe Parada con ID: "+id+".");
	}

	/**
	 * Elimina de manera Permanente todos las {@link Parada} registrados en la base de datos.
	 */
	public void deleteAllParadaPhysical() {
		List<Ruta> r = (List<Ruta>) rutaRepository.findAll();
		for(Ruta rt : r)
			rt.setListasParadas(null);
		paradaRepository.deleteAll();
	}

	/**
	 * Obtener lista de {@link Parada} que pertenecen a un {@link Ruta}
	 * @param linea - linea de la {@link Cooperativa}
	 * @return - Lista de {@link Parada}
	 */
	public Iterable<Parada> getAllParadaByLinea(String linea) {
		Ruta ruta = rutaRepository.findByNombreAndEstadoIsTrue(linea).orElseThrow(()->new ResponseStatusException(
					HttpStatus.NOT_FOUND, "No existe Linea "+linea+"."));
		Optional<List<Parada>> par = Optional.of((List<Parada>)paradaRepository.findAllById(ruta.getListasParadas()));
		par.get().removeIf(r -> !r.getEstado());
		return par.filter(a -> !a.isEmpty()).orElseThrow(() -> new ResponseStatusException(
			       HttpStatus.NOT_FOUND, "No exsiste paradas de la linea "+linea+"."));
	}
}