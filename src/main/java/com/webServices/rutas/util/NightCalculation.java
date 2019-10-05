package com.webServices.rutas.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.webServices.rutas.model.Bus;
import com.webServices.rutas.model.EstadoBus;
import com.webServices.rutas.model.EstadoBusTemporal;
import com.webServices.rutas.model.GeoLocation;
import com.webServices.rutas.model.GlobalVariables;
import com.webServices.rutas.model.HistorialEstadoBus;
import com.webServices.rutas.model.Parada;
import com.webServices.rutas.model.TimeControlParada;
import com.webServices.rutas.repository.EstadoBusTemporalRepository;
import com.webServices.rutas.repository.HistorialEstadoBusRepository;
import com.webServices.rutas.repository.ParadaRepository;
import com.webServices.rutas.repository.TimeControlParadaRepository;
import com.webServices.rutas.services.BusService;
import com.webServices.rutas.services.ConfigService;

/**
 * Calculo de Tiempos entre paradas.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@Component
public class NightCalculation {
	
	/**
	 * Instancia de Repositorio de {@link HistorialEstadoBus}
	 */
	@Autowired
	HistorialEstadoBusRepository historialEstadoBusRepository;
	
	/**
	 * Instancia de Repositorio de {@link EstadoBusTemporal}
	 */
	@Autowired
	EstadoBusTemporalRepository estadoBusTemporalRepository;
	
	/**
	 * Instancia de Repositorio de {@link TimeControlParada}
	 */
	@Autowired
	TimeControlParadaRepository timeControlParadaRepository;
	
	/**
	 * Instancia de Repositorio de {@link Parada}
	 */
	@Autowired
	private ParadaRepository paradaRepository;
	
	/**
	 * Instancia a los Servicios de {@link Bus}
	 */
	@Autowired
	private BusService busService;
	
	@Autowired
	private ConfigService configService;
	
	/**
	 * Calculo de Tiempos entre paradas.
	 * @throws IOException - Error en tiempo de Ejecución.
	 */
	@Scheduled(cron=GlobalVariables.timeScheduled, zone="America/Guayaquil")
	public void timeBetweenStops() throws IOException{
		//TODO crear index para mayor velocidad de consulta
		List<HistorialEstadoBus> allHistorialEstadoBus = getHistorialDelDia();
		//Recorrer los historiales del los buses
        for(HistorialEstadoBus oneHistorial : allHistorialEstadoBus) {
        	HistorialEstadoBus op = historialEstadoBusRepository.findById(oneHistorial.getId()).get();
        	List<EstadoBus> listEstadosHistorial = op.getListaEstados1();
        	listEstadosHistorial.addAll(op.getListaEstados2());
        	listEstadosHistorial.addAll(op.getListaEstados3());
        	//Guardar temporalmente para poder evaluar por SpatialView
        	//buscar las paradas pertenecientes a la linea que esta haciendo el recorrido
        	List<Parada> paradasByLinea = (List<Parada>) paradaRepository.findAllByLinea(String.valueOf(op.getLinea()));
        	//Buscar TimeCoontrol para esta linea
        	TimeControlParada timeControlParada = timeControlParadaRepository.findByLinea(String.valueOf(op.getLinea()))
        											.orElse(new TimeControlParada(String.valueOf(op.getLinea()),paradasByLinea));
        	//Recorro las paradas
        	for(int i = 0; i <= paradasByLinea.size()-1; i++) {
        		Parada p = paradasByLinea.get(i);
        		//por cada parada pregunto si existen buses cercanos a menos de 3 metros a la redonda
        		List<EstadoBusTemporal> busesCercanos = new ArrayList<>();
        		for(int t=1;t<=3;t++){
        			List<EstadoBusTemporal> list = findBusesCercanos(p, op.getId(),"listaEstados"+String.valueOf(t));
        			if(t>1) {
        				int it=t-1;
        				list.forEach(s -> s.setIdx(s.getIdx()+(configService.getGlobalVariables().getLimitListEstados()*it)));
        			}
        			busesCercanos.addAll(list);
        		}
    			//obtengo la siguiente parada para comenzar a recorrer cada punto del historial hasta encontrar el menor
    			if(busesCercanos.size()!=0) {
    				Parada siguienteParada;
        			if((i+1) >= paradasByLinea.size())
        				siguienteParada = paradasByLinea.get(0);
    				else
    					siguienteParada = paradasByLinea.get(i+1);
        			//Si encuentra multiples buses cercanos a la parada recorre
        			for(EstadoBusTemporal e : busesCercanos) {
        				//obtiene su index para comenzar a evaluar de alli en adelante
        				int idxw = e.getIdx();
        				//Distancia inicial a la siguiente parada
        				double menorDistancia = siguienteParada.distance(e.getPosicionActual(), "M");
        				double otraDistancia;
        				//comienza a recorrer desde el index registrado
        				for(int j = idxw+1;j <=listEstadosHistorial.size()-1;j++) {
        					//consultar siguiente distancia en metros
        					otraDistancia = siguienteParada.distance(listEstadosHistorial.get(j).getPosicionActual(), "M");
        					if(otraDistancia < menorDistancia ) {
        						menorDistancia = otraDistancia;
        					}else {
        						Long diffInMillies = Math.abs(e.getCreationDate().getTime() - listEstadosHistorial.get(j).getCreationDate().getTime());
        					    Long diff = diffInMillies/1000;//segundos
        					    //Buscar si ya existen en timeControlParada
        					    if(diff != 0) {
        					    	//TODO Falta comprobar si ya existe un BetweenParada con mismo ip1 y idP2
        					    	timeControlParada = timeControlParada.buscarParada1AndParada2(p.getId(),siguienteParada.getId(),diff);
        					    }
        						break;
        					}
        				}
        			}
    			}
        	}
    		timeControlParada = timeControlParadaRepository.save(timeControlParada);
        }
	}
	
	/**
	 * Obtener Listas de Ids de los historiales por Dia.
	 * @return - Lista de Tiempos.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private List<HistorialEstadoBus> getHistorialDelDia() throws FileNotFoundException, UnsupportedEncodingException {
		String todayAsString;
        if(GlobalVariables.fechaNightCalculation.equals("")) {
        	Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Guayaquil"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            todayAsString = df.format(now.getTime());
        }else {
        	todayAsString = GlobalVariables.fechaNightCalculation;
        }
        return historialEstadoBusRepository.findByCreadoEn(todayAsString);
	}
	
	/**
	 * Buscar Lista de {@link EstadoBus} cercanos a una Parada.
	 * @param p - Parada 
	 * @param idHistorial - ID de Historial.
	 * @param listado - Buscar en Listado 1, 2, 3.
	 * @return Lista de {@link EstadoBusTemporal} cercanos a una Parada.
	 */
	private List<EstadoBusTemporal> findBusesCercanos(Parada p,String idHistorial,String listado) {
		double lat = p.getCoordenada().getY();
		double lon = p.getCoordenada().getX();
		GeoLocation loc = new GeoLocation(lat,lon);
		List<GeoLocation> SW_NE_LOC = loc.bounding_locations(GlobalVariables.radioMaxBusesToParada);
		String meridian180condition = (SW_NE_LOC.get(0).getRad_lon() > SW_NE_LOC.get(1).getRad_lon()) ? " OR " : " AND ";
		return historialEstadoBusRepository.findByListaEstadosInPosicionWithIn(listado,meridian180condition, loc.getDeg_lat(),  
																				loc.getDeg_lon(), SW_NE_LOC.get(0).getRad_lat(),  
																				SW_NE_LOC.get(0).getRad_lon(), SW_NE_LOC.get(1).getRad_lat(),
																				SW_NE_LOC.get(1).getRad_lon(), GlobalVariables.radioMaxBusesToParada,idHistorial);
	}
	
	/**
	 * Simulador Diario para la linea 11.
	 * @throws IOException - Error en Tiempo de Ejecución.
	 * @throws InterruptedException - Metodo Interrumpido.
	 */
	@Scheduled(cron=GlobalVariables.timeSimulator1, zone="America/Guayaquil")
	public void IniciarSimuladorLinea11() throws IOException, InterruptedException{
		busService.startSimulator("11", "ABC1234");
	}
	
	/**
	 * Simulador Diario para la linea 8.
	 * @throws IOException - Error en Tiempo de Ejecución.
	 * @throws InterruptedException - Metodo Interrumpido.
	 */
	@Scheduled(cron=GlobalVariables.timeSimulator2, zone="America/Guayaquil")
	public void IniciarSimuladorLinea8() throws IOException, InterruptedException{
			busService.startSimulator("8", "DEF5678");
	}
	
	/**
	 * Simulador Diario para la linea 7.
	 * @throws IOException - Error en Tiempo de Ejecución.
	 * @throws InterruptedException - Metodo Interrumpido.
	 */
	@Scheduled(cron=GlobalVariables.timeSimulator3, zone="America/Guayaquil")
	public void IniciarSimuladorLinea7() throws IOException, InterruptedException{
			busService.startSimulator("7", "GHI9012");
	}
}