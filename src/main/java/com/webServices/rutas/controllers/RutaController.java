package com.webServices.rutas.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.webServices.rutas.model.Ruta;
import com.webServices.rutas.services.RutaService;

/**
 * Clase que contiene los requestMapping de {@link Ruta} y los asocia a sus respectivos servicios en {@link RutaService}.
 * @author Davids Adrian Gonzalez Tigrero 
 * @see RutaService
 * @version 1.0
 */
@RestController
@RequestMapping("rutas")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RutaController {
	
	/**
	 * Instancia de los Servicios para {@link Ruta}
	 */
	@Autowired
	private RutaService rutaService;
	
	/**
	 * Metodo que Mapea "/rutas", RequestMethod es GET, se enlaza al servicio {@link RutaService#getAllRuta()}
	 * y retorna la Lista de {@link Ruta} registrados
	 * @return Lista de Rutas
	 * @see RutaService#getAllRuta()
	 */
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB') or hasRole('USER_MOVIL')")
	@GetMapping
	public List<Ruta> getAllRuta(){
		return rutaService.getAllRuta();
	}
	
	/**
	 * Metodo que Mapea "/rutas/ignoreEstado", RequestMethod es GET, se enlaza al servicio {@link RutaService#getAllRutaIgnoreEstado()} 
	 * y retorna Lista de {@link Ruta} incluye eliminados logicamente.
	 * @return Lista de {@link Ruta} incluye eliminados logicamente.
	 * @see RutaService#getAllRutaIgnoreEstado()
	 */
	@GetMapping("/ignoreEstado")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public List<Ruta> getAllRutaIgnoreEstado(){
		return rutaService.getAllRutaIgnoreEstado();
	}
	
	/**
	 * Metodo que Mapea "/rutas/{linea}", RequestMethod es GET, se enlaza al servicio {@link RutaService#getRuta(String)} 
	 * y retorna la {@link Ruta}
	 * @param linea - Linea de la {@link Ruta}
	 * @return {@link Ruta}
	 * @see RutaService#getRuta(String)
	 */
	@GetMapping("/{linea}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB') or hasRole('USER_MOVIL')")
	public Ruta getRuta(@PathVariable String linea) {
		return rutaService.getRuta(linea);
	}
	
	/**
	 * Metodo que Mapea "/rutas/{id}/ignoreEstado", RequestMethod es GET, se enlaza al servicio {@link RutaService#getRutaIgnoreEstado(String)} 
	 * y retorna la {@link Ruta} ignorando su estado
	 * @param linea - Linea de la {@link Ruta}
	 * @return {@link Ruta}
	 * @see RutaService#getRutaIgnoreEstado(String)
	 */
	@GetMapping("/{linea}/ignoreEstado")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public Ruta getRutaIgnoreEstado(@PathVariable String linea) {
		return rutaService.getRutaIgnoreEstado(linea);
	}
	
	/**
	 * Metodo que Mapea "/rutas/archivoGPX", RequestMethod es POST, se enlaza al servicio {@link RutaService#addRutaWithGPX(MultipartFile, String)} 
	 * y retorna la {@link Ruta} Guardada mediante un archivo GPX.
	 * @param file - Archivo GPX de donde escogera la Ruta y sus Paradas
	 * @param linea - Linea a la que pertenece esta {@link Ruta}.
	 * @return {@link Ruta} Registrada
	 * @see RutaService#addRutaWithGPX(MultipartFile, String)
	 * @throws IOException - en caso de que el archivo no sea GPX
	 */
	@PostMapping("/archivoGPX")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public Ruta addRutaWithGPX(@RequestParam("file") MultipartFile file,	
							   @RequestParam("nombre") String nombre,
							   @RequestParam("color") String color) throws IOException {
		return rutaService.addRutaWithGPX(file, nombre,color);
	}
	
	/**
	 * Metodo que Mapea "/rutas/archivoGPX", RequestMethod es POST, se enlaza al servicio {@link RutaService#addRutaWithGPX(MultipartFile, String)} 
	 * y retorna la {@link Ruta} Guardada mediante un archivo GPX.
	 * @param file - Archivo GPX de donde escogera la Ruta y sus Paradas
	 * @param linea - Linea a la que pertenece esta {@link Ruta}.
	 * @return {@link Ruta} Registrada
	 * @see RutaService#addRutaWithGPX(MultipartFile, String)
	 * @throws IOException - en caso de que el archivo no sea GPX
	 */
	@PutMapping("/archivoGPX")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public Ruta updateRutaWithGPX(@RequestParam("file") MultipartFile file,
								  @RequestParam("nombre") String nombre,
								  @RequestParam("color") String color) throws IOException {
		return rutaService.updateRutaWithGPX(file, nombre,color);
	}
	
	/**
	 * Metodo que Mapea "/rutas", RequestMethod es POST, se enlaza al servicio {@link RutaService#addRuta(Ruta)} 
	 * y retorna {@link Ruta} registrada
	 * @param ruta - {@link Ruta} a Registrar
	 * @return {@link Ruta} Registrada
	 * @see RutaService#addRuta(Ruta)
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public Ruta addRuta(@RequestBody Ruta ruta) {
		return rutaService.addRuta(ruta);
	}
	
	/**
	 * Metodo que Mapea "/rutas", RequestMethod es PUT, se enlaza al servicio {@link RutaService#updateRuta(Ruta)}.
	 * Actualizar {@link Ruta}.
	 * @param ruta - {@link Ruta} a Actualizar
	 * @see RutaService#updateRuta(Ruta)
	 * @return {@link Ruta}
	 */
	@PutMapping
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public Ruta updateRuta(@RequestBody Ruta ruta) {
		return rutaService.updateRuta(ruta);
	}
	
	/**
	 * Metodo que Mapea "/rutas/{id}", RequestMethod es DELETE, se enlaza al servicio {@link RutaService#deleteRuta(String)}.
	 * Eliminar una {@link Ruta}.
	 * @param id - Id de {@link Ruta}
	 * @see RutaService#deleteRuta(String)
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(value=HttpStatus.OK, reason="Ruta eliminado con exito.")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('USER_WEB')")
	public void deleteRuta(@PathVariable String id) {
		rutaService.deleteRuta(id);
	}
	
	/**
	 * Metodo que Mapea "/rutas/{id}/physical", RequestMethod es DELETE, se enlaza al servicio {@link RutaService#deleteRutaPhysical(String)}.
	 * Eliminar una {@link Ruta} de la Base de Datos.
	 * @param id - ID de {@link Ruta}
	 * @see RutaService#deleteRutaPhysical(String)
	 */
	@DeleteMapping("/{id}/physical")
	@ResponseStatus(value=HttpStatus.OK, reason="Ruta eliminado con exito.")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public void deleteRutaPhysical(@PathVariable String id) {
		rutaService.deleteRutaPhysical(id);
	}
	
	/**
	 * Metodo que Mapea "/rutas/physical", RequestMethod es DELETE, se enlaza al servicio {@link RutaService#deleteAllRutaPhysical()}.
	 * Eliminar todos los {@link Ruta} de la Base de Datos.
	 * @see RutaService#deleteAllRutaPhysical()
	 */
	@DeleteMapping("/physical")
	@ResponseStatus(value=HttpStatus.OK, reason="Todos las Rutas eliminadas con exito.")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public void deleteAllRutaPhysical() {
		rutaService.deleteAllRutaPhysical();
	}
}