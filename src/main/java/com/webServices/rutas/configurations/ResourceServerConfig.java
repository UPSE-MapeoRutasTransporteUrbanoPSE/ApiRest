package com.webServices.rutas.configurations;

import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
/**
 * Configuracion de Recursos utilizados en Spring Security.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId("resource_id").stateless(false);
	}
}