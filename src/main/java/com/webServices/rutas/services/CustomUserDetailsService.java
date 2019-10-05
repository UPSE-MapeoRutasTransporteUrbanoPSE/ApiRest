package com.webServices.rutas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.webServices.rutas.model.GlobalVariables;
import com.webServices.rutas.model.SegUsuario;
import com.webServices.rutas.repository.SegUsuarioRepository;
import com.webServices.rutas.repository.SocioRepository;

/**
 * Representa Servicios adicionales para Spring Security.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private SocioRepository socioRepository;
	/**
	 * Instancia a los Repositorios de Usuario
	 */
	@Autowired
	private SegUsuarioRepository segUsuarioRepository;
	
	/**
	 * Cargar Usuario a Spring Security.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		GlobalVariables global = new GlobalVariables();
		SegUsuario segUsuario = segUsuarioRepository.findByUsuarioOrEmail(username,username)
				.orElseThrow(() ->new ResponseStatusException(
				           HttpStatus.UNAUTHORIZED, "Usuario no Registrado."));
		global.setSocioAuth(socioRepository.findById(segUsuario.getIdSocio()).orElseThrow(() ->new ResponseStatusException(
		           HttpStatus.UNAUTHORIZED, "No existe SOCIO asociado a este Usuario Registrado.")));
		return User.withUsername(segUsuario.getUsuario())
                .password("{noop}"+segUsuario.getClave())
                .roles(segUsuario.getPerfil()).build();
	}
}