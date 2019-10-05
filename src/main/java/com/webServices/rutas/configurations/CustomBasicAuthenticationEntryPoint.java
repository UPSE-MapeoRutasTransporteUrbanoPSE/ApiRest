package com.webServices.rutas.configurations;

import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
/**
 * Configuracion de BasicAuthenticationEntryPoint para devolver una respuesta en caso de que las
 * credenciales no sean válidas o falten. Dado que es una API REST, tiene sentido devolver alguna
 * respuesta y no redirigir al usuario a ninguna página de inicio de sesión.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint{
	@Override
    public void commence(final HttpServletRequest request, 
            final HttpServletResponse response, 
            final AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
        PrintWriter writer = response.getWriter();
        writer.println(authException.getMessage());
    }
	@Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("MY_TEST_REALM");
        super.afterPropertiesSet();
    }
}