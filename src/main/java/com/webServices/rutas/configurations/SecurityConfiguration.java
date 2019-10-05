package com.webServices.rutas.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.webServices.rutas.services.CustomUserDetailsService;
/**
 * Configuraciones basicas que necesitamos para habilitar la seguridad
 * en los Servicios Web
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	private static String REALM="MY_TEST_REALM";
	/**
	 * Instancia a los servicios  de UserDetails
	 */
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	/**
	 * Hace uso de la clase {@link CustomUserDetailsService} para la utentificacion de usuarios en los servicios web
	 * @see CustomUserDetailsService
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService);
	}
	
	/**
	 * Esta configuracion asegura que cualquier solicitud a estos servicios web 
	 * se autentifique con inicio de secion basado en autentificacion básica HTTP
	 */
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().fullyAuthenticated();
		http.httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint());
		http.csrf().disable();
	}
	
	/**
	 * Metodo que configura las respuesta en caso de malas credenciales.
	 * @see CustomBasicAuthenticationEntryPoint
	 * @return {@link CustomBasicAuthenticationEntryPoint}
	 */
	@Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
        return new CustomBasicAuthenticationEntryPoint();
    }
	
	/**
	 * Metodo que me permite ignorar cualquier recurso que yo desee.
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS,"/**");
	}
}
