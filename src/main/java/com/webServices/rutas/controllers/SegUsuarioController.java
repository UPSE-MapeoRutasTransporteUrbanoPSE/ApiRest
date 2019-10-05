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

import com.webServices.rutas.model.SegUsuario;
import com.webServices.rutas.services.SegUsuarioService;

/**
 * Clase que contiene los requestMapping de {@link SegUsuario} y los asocia a sus respectivos servicios en {@link SegUsuarioService}.
 * @author Davids Adrian Gonzalez Tigrero
 * @see SegUsuarioService
 * @version 1.0
 */
@RestController
@RequestMapping("usuarios")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SegUsuarioController {
	
	/**
	 * Instancia de los Servicios para {@link SegUsuario}
	 */
	@Autowired
	private SegUsuarioService segUsuarioService;
	/**
	 * Metodo que Mapea "/usuarios", RequestMethod es GET, se enlaza al servicio {@link SegUsuarioService#getAllSegUsuario()}
	 * y retorna datos de todos las {@link SegUsuario} registrados
	 * @return Lista de {@link SegUsuario}
	 * @see SegUsuarioService#getAllSegUsuario()
	 */
	@GetMapping
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public Iterable<SegUsuario> getAllUsuarios(){
		return segUsuarioService.getAllSegUsuario();
	}
	
	/**
	 * Metodo que Mapea "/usuarios/ignoreEstado", RequestMethod es GET, se enlaza al servicio {@link SegUsuarioService#getAllSegUsuarioIgnoreEstado()} 
	 * y retorna todos las {@link SegUsuario} incluye eliminados logicamente.
	 * @return Lista de {@link SegUsuario} incluye eliminados logicamente.
	 * @see SegUsuarioService#getAllSegUsuarioIgnoreEstado()
	 */
	@GetMapping("/ignoreEstado")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public List<SegUsuario> getAllReporteIgnoreEstado(){
		return segUsuarioService.getAllSegUsuarioIgnoreEstado();
	}
	
	/**
	 * Metodo que Mapea "/usuarios/{id}", RequestMethod es GET, se enlaza al servicio {@link SegUsuarioService#getSegUsuario(String)} 
	 * y retorna el {@link SegUsuario}
	 * @param id - Id del {@link SegUsuario} 
	 * @return {@link SegUsuario}
	 * @see SegUsuarioService#getSegUsuario(String)
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public SegUsuario getUsuario(@PathVariable String id) { 
		return segUsuarioService.getSegUsuario(id);
	}
	
	/**
	 * Metodo que Mapea "/usuarios/{id}/ignoreEstado", RequestMethod es GET, se enlaza al servicio {@link SegUsuarioService#getSegUsuarioIgnoreEstado(String)} 
	 * y retorna el {@link SegUsuario}
	 * @param id - Id del {@link SegUsuario} 
	 * @return {@link SegUsuario}
	 * @see SegUsuarioService#getSegUsuarioIgnoreEstado(String)
	 */
	@GetMapping("/{id}/ignoreEstado")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public SegUsuario getUsuarioIgnoreEstado(@PathVariable String id) { 
		return segUsuarioService.getSegUsuarioIgnoreEstado(id);
	}
	
	/**
	 * Metodo que Mapea "/usuarios", RequestMethod es POST, se enlaza al servicio {@link SegUsuarioService#addSegUsuario(SegUsuario)} 
	 * y retorna Datos de una {@link SegUsuario} registrado
	 * @param segUsuario - Datos del {@link SegUsuario} a Registrar
	 * @return {@link SegUsuario} Registrado
	 * @see SegUsuarioService#addSegUsuario(SegUsuario)
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_MOVIL')")
	public SegUsuario addSegUsuario(@RequestBody SegUsuario segUsuario) {
		return segUsuarioService.addSegUsuario(segUsuario);
	}
	
	/**
	 * Metodo que Mapea "/usuarios", RequestMethod es PUT, se enlaza al servicio {@link SegUsuarioService#updateSegUsuario(SegUsuario)}.
	 * Actualizar {@link SegUsuario}.
	 * @param segUsuario - {@link SegUsuario} a Actualizar
	 * @return {@link SegUsuario} Actualizado
	 * @see SegUsuarioService#updateSegUsuario(SegUsuario)
	 */
	@PutMapping
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_MOVIL')")
	public SegUsuario updateSegUsuario(@RequestBody SegUsuario segUsuario) {
		return segUsuarioService.updateSegUsuario(segUsuario);
	}
	
	/**
	 * Metodo que Mapea "/usuarios/{id}", RequestMethod es DELETE, se enlaza al servicio {@link SegUsuarioService#deleteSegUsuario(String)}.
	 * Eliminar un {@link SegUsuario}.
	 * @param id - Id del {@link SegUsuario}
	 * @see SegUsuarioService#deleteSegUsuario(String)
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(value = HttpStatus.OK, reason = "Usuario Eliminado Correctamente")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public void deleteSegUsuario(@PathVariable String id) {
		segUsuarioService.deleteSegUsuario(id);
	}
	
	/**
	 * Metodo que mapea "/usuarios/{usuario}/{clave}", RequestMethod es GET, se enlaza al servicio {@link SegUsuarioService#getUsuarioByNombreAndPass(String, String)}
	 * @param usuario - Nombre de Usuario
	 * @param clave - Clave del Usuario
	 * @return {@link SegUsuario}
	 */
	@GetMapping("/{usuario}/{clave}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB') or hasRole('USER_MOVIL')")
	public SegUsuario getUsuarioByNameAndPass(@PathVariable String usuario,@PathVariable String clave) {
		return segUsuarioService.getUsuarioByNombreAndPass(usuario,clave);
	}
}