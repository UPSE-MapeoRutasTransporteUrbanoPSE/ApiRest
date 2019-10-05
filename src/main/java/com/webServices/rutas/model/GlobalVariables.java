package com.webServices.rutas.model;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Representa las Variables Globales a configuar para el correcto funcionamiento de este Sistema.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
public class GlobalVariables {
	
	private Socio socioAuth;
	
	/**
	 * Cooeficiente para exactitud de la busqueda GeoEspacial.
	 */
	public static final Double coeficiente = 57.304;
	
	/**
	 * Radio max para el Mapeo de {@link EstadoBus} cercanos a una parada dado en Km.
	 */
	public static final double radioMaxBusesToParada = 0.005;
	
	/**
	 * Hora final del simulador de {@link Bus} (es).
	 */
	private int horaFinalSimulador = 20;
	
	/**
	 * Hora de Inicio del simulador de {@link Bus} (es).
	 */
	private int horaInicioSimulador = 8;
	
	/**
	 * Tiempo de Persistencia para los {@link EstadoBus} del simulador.
	 */
	private int secondSimulatorSave = 5;
	
	/**
	 * Tiempo que se lanza el precalculo de {@link TimeControlParada}.
	 */
	public static final String timeScheduled  = "15 0 21 * * ?";
	
	/**
	 * En caso de Pruebas puedo colocar una fecha para recolectar {@link HistorialEstadoBus} en un dia especificandolo en formato yyyy-MM-dd.
	 */
	public static final String fechaNightCalculation  = "";
	
	/**
	 * Limite de {@link EstadoBus} * 3 que se guardaran en {@link HistorialEstadoBus}.
	 */
	private int limitListEstados = 3000;
	
	/**
	 * Inicio de simulador 1 de buses por dia.
	 */
	public static final String timeSimulator1 = "0 0 8 * * ?";
	
	/**
	 * Inicio de simulador 1 de buses por dia.
	 */
	public static final String timeSimulator2 = "0 21 8 * * ?";
	
	/**
	 * Inicio de simulador 1 de buses por dia.
	 */
	public static final String timeSimulator3 = "0 42 8 * * ?";
	
	/**
	 * Validar simulador de {@link Bus} segun el horario especificado.
	 * En caso de Pruebas colocar en falso
	 */
	private Boolean validarSimulador = true;
	
	/**
	 * Url Kibana para enbeber Tráfico de Buses
	 */
	private String urlTraficBus = "http://facsistel.upse.edu.ec:5601/app/kibana#/dashboard/8bba9120-c3a9-11e9-987e-e78c6a5f7065?_g=(refreshInterval%3A(pause%3A!f%2Cvalue%3A5000)%2Ctime%3A(from%3Anow-15m%2Cto%3Anow))";
	
	/**
	 * Verificar placa de un Bus segun el formato.
	 * @param placa - Placa de un {@link Bus}
	 * @return Placa de Bus Verificado.
	 */
	public static String confirmPlaca(String placa) {
		return placa.replace("-","")
					.toUpperCase();
	}
	
	/**
	 * Obtener instacia de la fecha actual sin tiempo.
	 * @return {@link Date}
	 */
	public static Date getFechaDMA() {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Guayaquil"));
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
		return now.getTime();
	}
	
	/**
	 * Obtener instancia de fecha y tiempo.
	 * @return {@link Date}
	 */
	public static Date getFecha() {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Guayaquil"));
		return now.getTime();
	}
	
	/**
	 * Validar tiempos en el Simulador de {@link Bus}
	 * @return {@link Boolean}
	 */
	public Boolean validateSimulator() {
		if(validarSimulador) {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Guayaquil"));
			if(now.get(Calendar.HOUR_OF_DAY) < horaFinalSimulador && now.get(Calendar.HOUR_OF_DAY) >= horaInicioSimulador) {
				return true;
			}else {
				throw new ResponseStatusException(
					       HttpStatus.CONFLICT, "Simulador fuera de tiempo.");
			}
		}else return true;
	}

	public int getHoraFinalSimulador() {
		return horaFinalSimulador;
	}

	public void setHoraFinalSimulador(int horaFinalSimulador) {
		this.horaFinalSimulador = horaFinalSimulador;
	}

	public int getHoraInicioSimulador() {
		return horaInicioSimulador;
	}

	public void setHoraInicioSimulador(int horaInicioSimulador) {
		this.horaInicioSimulador = horaInicioSimulador;
	}

	public int getSecondSimulatorSave() {
		return secondSimulatorSave;
	}

	public void setSecondSimulatorSave(int secondSimulatorSave) {
		this.secondSimulatorSave = secondSimulatorSave;
	}

	public Boolean getValidarSimulador() {
		return validarSimulador;
	}

	public void setValidarSimulador(Boolean validarSimulador) {
		this.validarSimulador = validarSimulador;
	}

	public String getUrlTraficBus() {
		return urlTraficBus;
	}

	public void setUrlTraficBus(String urlTraficBus) {
		this.urlTraficBus = urlTraficBus;
	}

	public int getLimitListEstados() {
		return limitListEstados;
	}

	public void setLimitListEstados(int limitListEstados) {
		this.limitListEstados = limitListEstados;
	}

	public Socio getSocioAuth() {
		return socioAuth;
	}

	public void setSocioAuth(Socio socioAuth) {
		this.socioAuth = socioAuth;
	}
	
	
	
}