package com.webServices.rutas.services;

import org.springframework.stereotype.Service;

import com.webServices.rutas.model.GlobalVariables;

@Service
public class ConfigService {
	private GlobalVariables globalVariables = new GlobalVariables();
	public String configSimulador(Integer horaIni, Integer horaFin, Integer tiempoAvance, Boolean validarTiempo) {
		if(horaIni != null){
			globalVariables.setHoraInicioSimulador(horaIni);
		}
		if(horaFin != null){
			globalVariables.setHoraFinalSimulador(horaFin);
		}
		if(tiempoAvance != null){
			globalVariables.setSecondSimulatorSave(tiempoAvance);
		}
		if(validarTiempo != null){
			globalVariables.setValidarSimulador(validarTiempo);
		}
		return "Parametros de SIMULADOR configurados...";
	}
	
	public String configExtra(String urlTrafic, Integer limiteEstadoBus) {
		if(urlTrafic != null){
			globalVariables.setUrlTraficBus(urlTrafic);;
		}
		if(limiteEstadoBus != null){
			globalVariables.setLimitListEstados(limiteEstadoBus);;
		}
		return "Paremetros EXTRAS configurados...";
	}
	
	public GlobalVariables getGlobalVariables() {
		return globalVariables;
	}

	public GlobalVariables getConfig() {
		return globalVariables;
	}

}