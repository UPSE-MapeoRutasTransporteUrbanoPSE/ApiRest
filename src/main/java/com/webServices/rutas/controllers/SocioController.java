package com.webServices.rutas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.webServices.rutas.model.Socio;
import com.webServices.rutas.services.SocioService;

/**
 * Clase que contiene los requestMapping de {@link Cooperativa} y los asocia a sus respectivos servicios en {@link SocioService}.
 * @author Davids Adrian Gonzalez Tigrero 
 * @see SocioService
 * @version 1.0
 */
@RestController
@RequestMapping("socios")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SocioController {
	
	/**
	 * Instancia de los Servicios para {@link Cooperativa}
	 */
	@Autowired
	private SocioService socioService;

	/**
	 * Metodo que Mapea "/cooperativas", RequestMethod es GET, se enlaza al servicio {@link SocioService#getAllCooperativa()}
	 * y retorna lista de {@link Cooperativa} registrados
	 * @return Lista de {@link Cooperativa}
	 * @see SocioService#getAllCooperativa()
	 */
	@GetMapping
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB') or hasRole('USER_MOVIL')")
	public List<Socio> getAllSocio(){
		return socioService.getAllSocio();
	}
	
	/**
	 * Metodo que Mapea "/cooperativas/ignoreEstado", RequestMethod es GET, se enlaza al servicio {@link SocioService#getAllCooperativaIgnoreEstado()} 
	 * y retorna lista de {@link Cooperativa} incluye eliminados logicamente.
	 * @return Lista de {@link Cooperativa} incluye eliminados logicamente.
	 * @see SocioService#getAllCooperativaIgnoreEstado()
	 */
	@GetMapping("/ignoreEstado")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public List<Socio> getAllSocioIgnoreEstado(){
		return socioService.getAllSocioIgnoreEstado();
	}
	
	/**
	 * Metodo que Mapea "/cooperativas/{id}", RequestMethod es GET, se enlaza al servicio {@link SocioService#getCooperativa(String)} 
	 * y retorna la {@link Cooperativa}
	 * @param id - Id de la {@link Cooperativa}
	 * @return {@link Cooperativa}
	 * @see SocioService#getCooperativa(String)
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public Socio getSocio(@PathVariable String id) {
		return socioService.getSocio(id);
	}
	
	/**
	 * Metodo que Mapea "/cooperativas/{id}/ignoreEstado", RequestMethod es GET, se enlaza al servicio {@link SocioService#getCooperativaIgnoreEstado(String)} 
	 * y retorna la {@link Cooperativa} ignorando su estado
	 * @param id - Id de la {@link Cooperativa}
	 * @return {@link Cooperativa}
	 * @see SocioService#getCooperativaIgnoreEstado(String)
	 */
	@GetMapping("/{id}/ignoreEstado")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public Socio getSocioIgnoreEstado(@PathVariable String id) {
		return socioService.getSocioIgnoreEstado(id);
	}
	
	/**
	 * Metodo que Mapea "/cooperativas/byNombre/{nombre}", RequestMethod es GET, se enlaza al servicio {@link SocioService#getCooperativaByNombre(String)} 
	 * y retorna la {@link Cooperativa}
	 * @param nombre - nombre de la {@link Cooperativa} que desee Informacion.
	 * @return {@link Cooperativa}
	 * @see SocioService#getCooperativaByNombre(String)
	 */
	@GetMapping("/byNombre/{nombre}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public Iterable<Socio> getSocioByNombre(@PathVariable String nombre) {
		return socioService.getSocioByNombre(nombre);
	}
	
	/**
	 * Metodo que Mapea "/cooperativas", RequestMethod es POST, se enlaza al servicio {@link SocioService#addCooperativa(Cooperativa)} 
	 * y retorna {@link Cooperativa} registrada
	 * @param cooperativa - {@link Cooperativa} a Registrar
	 * @return {@link Cooperativa} Registrada
	 * @see SocioService#addCooperativa(Cooperativa)
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public Socio addSocio(@RequestBody Socio socio) {
		return socioService.addSocio(socio);
	}
	
	/**
	 * Metodo que Mapea "/cooperativas", RequestMethod es PUT, se enlaza al servicio {@link SocioService#updateCooperativa(Cooperativa)}.
	 * Actualizar {@link Cooperativa}.
	 * @param cooperativa - {@link Cooperativa} a Actualizar
	 * @see SocioService#updateCooperativa(Cooperativa)
	 * @return {@link Cooperativa}
	 */
	@PutMapping
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public Socio updateSocio(@RequestBody Socio socio) {
		return socioService.updateSocio(socio);
	}
	
	/**
	 * Metodo que Mapea "/cooperativas/{id}", RequestMethod es DELETE, se enlaza al servicio {@link SocioService#deleteCooperativa(String)}.
	 * Eliminar una {@link Cooperativa}.
	 * @param id - Id de la {@link Cooperativa}
	 * @see SocioService#deleteCooperativa(String)
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(value=HttpStatus.OK, reason="Cooperativa eliminado con exito.")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public void deleteSocio(@PathVariable String id) {
		socioService.deleteSocio(id);
	}

	/**
	 * Metodo que Mapea "/cooperativas/{id}", RequestMethod es DELETE, se enlaza al servicio {@link SocioService#deleteCooperativaPhysical(String)}.
	 * Eliminar una {@link Cooperativa}.
	 * @param id - Id de la {@link Cooperativa}
	 * @see SocioService#deleteCooperativaPhysical(String)
	 */
	@DeleteMapping("/{id}/physical")
	@ResponseStatus(value=HttpStatus.OK, reason="Cooperativa eliminado con exito.")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public void deleteSocioPhysical(@PathVariable String id) {
		socioService.deleteSocioPhysical(id);
	}
	
	/**
	 * Metodo que Mapea "/cooperativas/{id}", RequestMethod es DELETE, se enlaza al servicio {@link SocioService#deleteAllCooperativaPhysical()}.
	 * Eliminar una {@link Cooperativa}.
	 * @see SocioService#deleteAllCooperativaPhysical()
	 */
	@DeleteMapping("/physical")
	@ResponseStatus(value=HttpStatus.OK, reason="Cooperativa eliminado con exito.")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public void deleteAllSocioPhysical() {
		socioService.deleteAllSocioPhysical();
	}
}