package com.webServices.rutas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.webServices.rutas.model.SegUsuario;
import com.webServices.rutas.repository.SegUsuarioRepository;

/**
 * Contiene los Servicios de {@link SegUsuario} y realiza sus respectivas operaciones.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@Service
public class SegUsuarioService {
	/**
	 * Instancia para el Repositorio de {@link SegUsuarioRepository}
	 * @see {@link SegUsuarioRepository}
	 */
	@Autowired
	private SegUsuarioRepository segUsuarioRepository;
	
	/**
	 * Obtener lista de {@link SegUsuario}
	 * @return Lista de {@link SegUsuario}
	 */
	public Iterable<SegUsuario> getAllSegUsuario(){
		return segUsuarioRepository.findByEstadoIsTrue()
				.filter(a -> !a.isEmpty())
				.orElseThrow(() ->new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existen Usuarios Registrados."));
	}
	
	/**
	 * Obtener datos de un {@link SegUsuario} entregando su respectivo ID.
	 * @param id - ID de {@link SegUsuario} que desee obtener los datos
	 * @return {@link SegUsuario}
	 */
	public SegUsuario getSegUsuario(String id) {
		return segUsuarioRepository.findByIdAndEstadoIsTrue(id)
				.orElseThrow(() -> new ResponseStatusException(
		           HttpStatus.NOT_FOUND, "No existe el Usuario."));
	}
	
	/**
	 * Obtener datos de un {@link SegUsuario} entregando su respectiva ID.
	 * Ignorando su estado eliminado
	 * @param id - ID de {@link SegUsuario} que desee obtener los datos
	 * @return {@link SegUsuario}
	 */
	public SegUsuario getSegUsuarioIgnoreEstado(String id) {
		return segUsuarioRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existe Usuario registrado con ID "+id+"."));
	}
	
	/**
	 * Obtener Lista de {@link SegUsuario} ignorando su estado Eliminado
	 * @return Lista de {@link SegUsuario}
	 */
	public List<SegUsuario> getAllSegUsuarioIgnoreEstado(){
		return Optional.of((List<SegUsuario>) segUsuarioRepository.findAll())
				.filter(a -> !a.isEmpty())
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existen Usuarios Registrados."));
	}
	
	/**
	 * Agregar {@link SegUsuario}
	 * @param segUsuario - {@link SegUsuario} que desea guardar
	 * @return {@link SegUsuario} agregado
	 */
	public SegUsuario addSegUsuario(SegUsuario segUsuario) {
		SegUsuario u = segUsuarioRepository.findByUsuarioOrEmail(segUsuario.getUsuario(),segUsuario.getEmail()).orElse(null);
		if(u == null)
			return segUsuarioRepository.save(segUsuario);
		else {
			if(segUsuario.getEmail().equals(u.getEmail()))
				throw new ResponseStatusException(
				           HttpStatus.CONFLICT, "Email Existente");
			else 
				throw new ResponseStatusException(
				           HttpStatus.CONFLICT, "Nombre de Usuario Existente");
		}
	}
	
	/**
	 * Actualiza datos de un {@link SegUsuario}
	 * @param segUsuario - {@link SegUsuario} que desea actualizar sus datos
	 * @return {@link SegUsuario} actualizado
	 */
	public SegUsuario updateSegUsuario(SegUsuario segUsuario) {
		if(!segUsuarioRepository.existsById(segUsuario.getId()))
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "Usuario no esta Registrado.");
		SegUsuario u = segUsuarioRepository.findByUsuarioOrEmail(segUsuario.getUsuario(),segUsuario.getEmail())
				.orElse(segUsuarioRepository.save(segUsuario));
		if(!segUsuario.getId().equals(u.getId())) {
			if(segUsuario.getEmail().equals(u.getEmail()))
				throw new ResponseStatusException(
				           HttpStatus.CONFLICT, "Email Existente");
			else 
				throw new ResponseStatusException(
				           HttpStatus.CONFLICT, "Nombre de Usuario Existente");
		}else return u;		
	}
	
	/**
	 * Elimina una {@link SegUsuario}
	 * @param id - ID de la {@link SegUsuario} a Eliminar
	 */
	public void deleteSegUsuario(String id) {
		SegUsuario u = getSegUsuario(id);
		u.setEstado(false);
		segUsuarioRepository.save(u);
	}
	
	/**
	 * Obtener datos de un {@link SegUsuario} entregando su Nombre y su Clave.
	 * @param usuario - Nombre de {@link SegUsuario} que desee obtener los datos
	 * @param clave - Clave del {@link SegUsuario}
	 * @return {@link SegUsuario}
	 */
	public SegUsuario getUsuarioByNombreAndPass(String usuario, String clave) {
		SegUsuario u = segUsuarioRepository.findByUsuarioOrEmail(usuario,usuario)
				.orElseThrow(()->new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "Usuario no encontrado"));
		if(u.getClave().equals(clave))
			return u;
		else
			throw new ResponseStatusException(
			       HttpStatus.CONFLICT, "Contraseña no valida");
		
	}
}