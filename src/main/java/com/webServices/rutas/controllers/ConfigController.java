package com.webServices.rutas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webServices.rutas.model.GlobalVariables;
import com.webServices.rutas.services.ConfigService;

@RestController
@RequestMapping("config")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfigController {
	@Autowired
	private ConfigService configService;
	
	@PostMapping("/simulador")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public String setConfigSimulador( @RequestParam(required = false,value = "horaInicio") Integer horaIni,
								@RequestParam(value = "horaFin",required = false) Integer horaFin,
								@RequestParam(value = "tiempoAvance",required = false) Integer tiempoAvance,
								@RequestParam(value = "validarTiempo",required = false) Boolean validarTiempo){
		return configService.configSimulador(horaIni,horaFin,tiempoAvance,validarTiempo);
	}
	@PostMapping("/extra")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public String setConfigExtra(@RequestParam(required = false,value = "urlTrafic") String urlTrafic,
								@RequestParam(value = "limiteEstadoBus",required = false) Integer limiteEstadoBus){
		return configService.configExtra(urlTrafic,limiteEstadoBus);
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public GlobalVariables getConfig(){
		return configService.getConfig();
	}
	
}
