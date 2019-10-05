package com.webServices.rutas.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParseException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonMappingException;
import com.couchbase.client.deps.io.netty.util.internal.ThreadLocalRandom;
import com.google.gson.Gson;
import com.webServices.rutas.model.BetweenParada;
import com.webServices.rutas.model.Bus;
import com.webServices.rutas.model.EstadoBus;
import com.webServices.rutas.model.EstadoBusTemporal;
import com.webServices.rutas.model.GlobalVariables;
import com.webServices.rutas.model.HistorialEstadoBus;
import com.webServices.rutas.model.Parada;
import com.webServices.rutas.model.Ruta;
import com.webServices.rutas.model.Socio;
import com.webServices.rutas.model.TimeControlParada;
import com.webServices.rutas.repository.BusRepository;
import com.webServices.rutas.repository.EstadoBusTemporalRepository;
import com.webServices.rutas.repository.HistorialEstadoBusRepository;
import com.webServices.rutas.repository.RutaRepository;
import com.webServices.rutas.repository.TimeControlParadaRepository;

/**
 * Contiene los Servicios de {@link Bus} y realiza sus respectivas operaciones.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
@Service
public class BusService {
	/**
	 * Instancia para los servicios de {@link Parada}
	 * @see {@link ParadaService}
	 */
	@Autowired
	private ParadaService paradaService;
	
	/**
	 * Instancia para los servicios de {@link Cooperativa}
	 * @see {@link SocioService}
	 */
	@Autowired
	private SocioService socioService;
	
	/**
	 * Instancia para el Repositorio de {@link TimeControlParada}
	 * @see {@link TimeControlParadaRepository}
	 */
	@Autowired
	private TimeControlParadaRepository timeControlParadaRepository;
	
	/**
	 * Instancia para el Repositorio de {@link HistorialEstadoBus}
	 * @see {@link HistorialEstadoBusRepository}
	 */
	@Autowired
	private HistorialEstadoBusRepository historialEstadoBusRepository;
	
	/**
	 * Instancia para el Repositorio de {@link Bus}
	 * @see {@link BusRepository}
	 */
	@Autowired
	private BusRepository busRepository;
	
	/**
	 * Instancia para el Repositorio de {@link Ruta}
	 * @see {@link ParadaService}
	 */
	@Autowired
	private RutaRepository rutaRepository;
	
	@Autowired
	private EstadoBusTemporalRepository estadoBusTemporalRepository;
	
	@Autowired
	private ConfigService configService;
	/**
	 * Obtener datos de un {@link Bus} entregando su respectiva placa.
	 * @param placa - Placa del {@link Bus} que desee obtener los datos
	 * @return {@link Bus}
	 */
	public Bus getBusByPlaca(String placa) {
		String p = GlobalVariables.confirmPlaca(placa);
		return busRepository.findByPlacaAndEstadoIsTrue(p)
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existe Bus registrado con la placa "+p+"."));
	}
	
	/**
	 * Obtener datos de un {@link Bus} entregando su respectiva ID.
	 * @param id - ID del {@link Bus} que desee obtener los datos
	 * @return {@link Bus}
	 */
	public Bus getBus(String id) {
		return busRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existe Bus registrado con el ID "+id+"."));
	}
	
	/**
	 * Obtener datos de un {@link Bus} entregando su respectiva placa.
	 * Ignorando su estado eliminado
	 * @param placa - Placa del {@link Bus} que desee obtener los datos
	 * @return {@link Bus}
	 */
	public Bus getBusIgnoreEstado(String placa) {
		String p = GlobalVariables.confirmPlaca(placa);
		return busRepository.findByPlaca(p)
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existe Bus registrado con la placa "+p+"."));
	}
	
	/**
	 * Obtener lista de {@link Bus}
	 * @return Lista de {@link Bus}
	 */
	public List<Bus> getAllBus(){
		return busRepository.findByEstadoIsTrue()
									.filter(a -> !a.isEmpty())
									.orElseThrow(() ->new ResponseStatusException(
									           HttpStatus.NOT_FOUND, "No existen Buses Registrados."));
	}
	
	/**
	 * Obtener Lista de {@link Bus} Ignorando su estado Eliminado
	 * @return Lista de {@link Bus}
	 */
	public List<Bus> getAllBusIgnoreEstado(){
		return Optional.of(((List<Bus>)busRepository.findAll()))
				.filter(a -> !a.isEmpty())
				.orElseThrow(() -> new ResponseStatusException(
				           HttpStatus.NOT_FOUND, "No existen Buses Registrados."));
	}
	
	/**
	 * Agregar {@link Bus}
	 * @param bus - {@link Bus} que desea guardar
	 * @return {@link Bus} agregado
	 */
	public Bus addBus(Bus bus) {
		if(busRepository.existsByPlacaAndEstadoIsTrue(bus.getPlaca()) && bus.getId() == null)
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "El bus con placas "+bus.getPlaca()+" ya se encuentra Registrado.");
		else return busRepository.save(bus);
	}

	/**
	 * Actualiza datos de un {@link Bus}
	 * @param bus - {@link Bus} que desea actualizar sus datos
	 * @return {@link Bus} actualizado
	 */
	public Bus updateBus(Bus bus) {
		if(bus.getId() != null) {
			if(!busRepository.existsById(bus.getId()))
				throw new ResponseStatusException(
				           HttpStatus.CONFLICT, "El bus con placas "+bus.getPlaca()+" no se encuentra Registrado.");
			else return busRepository.save(bus);
		}else
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "El bus con placas "+bus.getPlaca()+" no se encuentra Registrado.");
		
	}

	/**
	 * Elimina un {@link Bus}
	 * @param placa - Placa del {@link Bus} a Eliminar
	 */
	public void deleteBus(String placa) {
		String p = GlobalVariables.confirmPlaca(placa);
		Bus c = getBusByPlaca(p);
		c.setEstado(false);
		busRepository.save(c);
	}
	
	/**
	 * Obtener Listado de {@link Bus} pertenecientes a una {@link Cooperativa}
	 * @param idCooperativa - Id de la {@link Cooperativa}
	 * @return Listado de {@link Bus} de una {@link Cooperativa}
	 */
	public Iterable<Bus> getBusesByIdSocio(String idSocio){
		Socio c = socioService.getSocio(idSocio);
		return busRepository.findByIdSocioAndEstadoIsTrue(idSocio)
							.filter(a -> !a.isEmpty())
							.orElseThrow(()-> new ResponseStatusException(
							           HttpStatus.CONFLICT, "No existen Buses Registrados para la Cooperativa "+c.getNombre()+"."));
	}
	
	/**
	 * Retorna una lista de {@link EstadoBus} que ha tenido el {@link Bus} en el transcurso del dia.
	 * @param placa - Placa del {@link Bus} a consultar su {@link EstadoBus}
	 * @param fecha - Fecha que se desee consultar
	 * @return Lista de {@link EstadoBus}
	 */
	public List<EstadoBus> getHistorialEstadoBusAllByPlacaByFecha(String placa,Date fecha){
		String p = GlobalVariables.confirmPlaca(placa);
		if(busRepository.existsByPlacaAndEstadoIsTrue(p)) {
			HistorialEstadoBus h = historialEstadoBusRepository.findByCreadoEnAndPlaca(fecha,p)
					.orElseThrow(()->new ResponseStatusException(
					           HttpStatus.NOT_FOUND, "No existe Historial para el Bus con Placa "+p+"."));
				List<EstadoBus> allH = h.getListaEstados1();
				allH.addAll(h.getListaEstados2());
				allH.addAll(h.getListaEstados3());
				return allH;
		}else
			throw new ResponseStatusException(
			           HttpStatus.NOT_FOUND, "No existe el Bus con Placa "+p+".");
	}
	
	/**
	 * Obtener el ultimo {@link EstadoBus} del {@link Bus}
	 * @param placa - Placa del {@link Bus} a obtener el {@link EstadoBus}
	 * @return	{@link EstadoBus}
	 */
	public EstadoBusTemporal getEstadoActualBus(String placa) {
		String p = GlobalVariables.confirmPlaca(placa);
		if(busRepository.existsByPlacaAndEstadoIsTrue(p)) {
			return estadoBusTemporalRepository.findByPlaca(p)
				.filter(estado -> ifBusDisponible(estado.getCreationDate()))
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Bus no disponible por el momento."));
		}else
			throw new ResponseStatusException(
			           HttpStatus.NOT_FOUND, "No existe el Bus con Placa "+p+".");
	}
	
	public List<EstadoBusTemporal> getAllEstadoActualBus() {
		Optional<List<EstadoBusTemporal>> list = Optional.of((List<EstadoBusTemporal>)estadoBusTemporalRepository.findAll());
        list.get().removeIf(q -> !ifBusDisponible(q.getCreationDate()));
        return list.filter(a -> !a.isEmpty())
				.orElseThrow(() -> new ResponseStatusException(
								HttpStatus.NOT_FOUND, "Bus no disponible por el momento."));
	}

	/**
	 * Añade un nuevo {@link EstadoBus} en un respectivo Dia
	 * @param estadoBus - {@link EstadoBus} a guardar
	 * @param placa - Placa del {@link Bus}
	 * @param linea - Linea de {@link Cooperativa}
	 */
	public void updateEstadoBus(EstadoBus estadoBus,String placa,String linea) {
		placa = GlobalVariables.confirmPlaca(placa);
		if(rutaRepository.existsByNombreAndEstadoIsTrue(linea)) {
			if(busRepository.existsByPlacaAndEstadoIsTrue(placa)) {
				Bus bus = getBusByPlaca(placa);
				if(bus.getIdEstadoActualTemporal()==null) {
					EstadoBusTemporal ebt = estadoBusTemporalRepository.save(new EstadoBusTemporal(estadoBus,linea,placa));
					bus.setIdEstadoActualTemporal(ebt.getId());
				}else {
					EstadoBusTemporal ebt = estadoBusTemporalRepository.findById(bus.getIdEstadoActualTemporal()).orElse(new EstadoBusTemporal(estadoBus,linea,placa));
					ebt.updateEstadoBus(estadoBus,linea,placa);
					ebt = estadoBusTemporalRepository.save(ebt);
					bus.setIdEstadoActualTemporal(ebt.getId());
				}
				busRepository.save(bus);
				HistorialEstadoBus h;
				Date now = GlobalVariables.getFechaDMA();
				if(historialEstadoBusRepository.existsByPlacaAndCreadoEn(placa,now.getTime())) {
					h = historialEstadoBusRepository.findByCreadoEnAndPlaca(now,placa).get();
					if(h.getListaEstados1().size()<configService.getGlobalVariables().getLimitListEstados()) {
		        		h.getListaEstados1().add(estadoBus);
		        	}else {
		    			if(h.getListaEstados2().size()<configService.getGlobalVariables().getLimitListEstados()) {
		        			h.getListaEstados2().add(estadoBus);
		        		}else {
		        			if(h.getListaEstados3().size()<configService.getGlobalVariables().getLimitListEstados()) {
		        				h.getListaEstados3().add(estadoBus);
		        			}else {
		        				throw new ResponseStatusException(
		        					       HttpStatus.CONFLICT, "Se alcanzo el limite permitido de estados del bus en el Historial.");
		        			}
		        		}
		        	}
				}else {
					h = new HistorialEstadoBus();
		        	h.setPlaca(placa);
		        	h.setListaEstados1(new ArrayList<>(Arrays.asList(estadoBus)));
		        	h.setListaEstados2(new ArrayList<>());
		        	h.setListaEstados3(new ArrayList<>());
		        	h.setLinea(linea);
				}
				historialEstadoBusRepository.save(h);
			}else {
				throw new ResponseStatusException(
					       HttpStatus.NOT_FOUND, "No existe Bus con la placa "+placa+".");
			}
		}else {
			throw new ResponseStatusException(
				       HttpStatus.NO_CONTENT, "No existe ruta para la linea "+linea+".");
		}
	}
	/**
	 * Añade un nuevo {@link EstadoBus} en un respectivo dia pero entregando un cadena Json
	 * @param valor - Cadena Json del {@link EstadoBus}
	 * @param placa - Placa del {@link Bus}
	 * @param linea - Linea de {@link Cooperativa}
	 * @throws JsonParseException - Objeto no puede ser transformado a Json
	 * @throws JsonMappingException - El objeto Json no puede ser transformado a la Entidad
	 * @throws IOException - Errores Internos Desconocidos
	 * @deprecated Metodo no recomendable favor ver {@link BusService#updateEstadoBus(EstadoBus, String, String)}
	 * @see BusService#updateEstadoBus(EstadoBus, String, String)
	 */
	public void updateEstadoBusGET(String valor, String linea, String placa) throws JsonParseException, JsonMappingException, IOException {
        Gson mapper = new Gson();
		EstadoBus estadoBus = mapper.fromJson(valor, EstadoBus.class);
		updateEstadoBus(estadoBus, placa, linea);
	}
	
	/**
	 * Añade un nuevo {@link EstadoBus} en un respectivo dia pero entregando valores que representan este pero seguido de comas
	 * @param valor - datos del {@link EstadoBus} seguido de comas
	 * @param placa - Placa del {@link Bus}
	 * @deprecated Metodo no recomendable favor ver {@link BusService#updateEstadoBus(EstadoBus, String, String)}
	 * @see BusService#updateEstadoBus(EstadoBus, String, String)
	 */
	public void updateEstadoBusGETAlternative(String valor, String placa) {
		String[] attrib = valor.split(",");
		EstadoBus estadoBus = new EstadoBus(Integer.parseInt(attrib[0]),
										Integer.parseInt(attrib[1]),
										new Point(Double.parseDouble(attrib[2]),Double.parseDouble(attrib[3])),
										Boolean.parseBoolean(attrib[4]));
		String linea = attrib[5];
		updateEstadoBus(estadoBus, placa, linea);
	}

	/**
	 * Retorna link de redireccionamiento y muestra Mapas de Calor de Trafico de Buses en Tiempo Casi real
	 * @return {@link RedirectView}
	 */
	public RedirectView getTraficBus() {
		if(!configService.getGlobalVariables().getUrlTraficBus().equals("")) {
			return new RedirectView(configService.getGlobalVariables().getUrlTraficBus());
		}else
			throw new ResponseStatusException(
				       HttpStatus.NOT_FOUND, "No existe URL para Tráfico de Buses.");
	}
	
	/**
	 * Inicia la simulación de un {@link Bus} de una linea de una {@link Cooperativa}
	 * @param linea - Linea de {@link Cooperativa}
	 * @param placa - Placa de {@link Bus}
	 * @throws InterruptedException - Proceso interrumpido
	 */
	public void startSimulator(String linea, String placa) throws InterruptedException{
		Ruta rs = rutaRepository.findByNombreAndEstadoIsTrue(linea).orElseThrow(() -> new ResponseStatusException(
				       HttpStatus.CONFLICT, "No se puede iniciar el Simulador Linea no existente."));
		ThreadLocalRandom alea1 = ThreadLocalRandom.current();
		int count = alea1.nextInt(1, 4);
		boolean ban = true;
		while(ban) {
			ban = configService.getGlobalVariables().validateSimulator();
			Thread.sleep(configService.getGlobalVariables().getSecondSimulatorSave()*1000);
			EstadoBus b = new EstadoBus(alea1.nextInt(1, 4), alea1.nextInt(1, 4), rs.getListasPuntos().get(count), Math.random() < 0.5); 
			updateEstadoBus(b, placa,linea);
			count = count + alea1.nextInt(1, 4);
			if(count >= rs.getListasPuntos().size()) {
				count = alea1.nextInt(1, 4);
			}
		}
	}
	
	/**
	 * Calcula el tiempo que tarda en llegar los diferentes {@link Bus} de una linea de {@link Cooperativa} a una {@link Parada} especifica
	 * @param idParada - Id de una {@link Parada}
	 * @param linea - Linea de {@link Cooperativa}
	 * @return Placa de {@link Bus} con sus respectivos tiempos de llegada a la {@link Parada}
	 * @throws InterruptedException - Proceso Interrumpido
	 */
	public Map<String, Double> getCalculateTimeToStop(String idParada, String linea) throws InterruptedException {
		if(rutaRepository.existsByParadaInLinea(linea,idParada)) {
			TimeControlParada tcp = timeControlParadaRepository.findByLinea(linea).orElseThrow(() -> new ResponseStatusException(
				       HttpStatus.NOT_FOUND, "No exsiste historial de Tiempos a Paradas para la Line "+linea+"."));
			List<EstadoBusTemporal> listEstadoBus = getEstadoActualBusByLinea(linea);
			Map<String,Double> listTiempoBus = new HashMap<String, Double>();
			for(EstadoBusTemporal eb : listEstadoBus) {
				Double sumKm = 0.0;
				boolean banSerch =true;
				while(banSerch){
					sumKm = sumKm + 0.1;
					List<Parada> par;
					try {
						par = paradaService.getParadasCercanasRadio(eb.getPosicionActual(), sumKm, linea);
					} catch (Exception e) {
						par = null;
					}
					if(par!=null) {
						List<BetweenParada> validosParaEvaluar = findBetweenParadaForEvaluate(par,tcp);
						if(validosParaEvaluar == null) {
							banSerch =true;
						}else {
							for(int j=0;j<validosParaEvaluar.size();j++) {
								//evaluar puntos con la primera parada
								Parada p1 = paradaService.getParada(validosParaEvaluar.get(j).getIdparada1());
								//estado bus anterior
								double distanciaP1BusAnterior = p1.distance(eb.getPosicionAnterior(), "M");
								//estado bus actual
								double distanciaP1BusActual = p1.distance(eb.getPosicionActual(), "M");
								//evaluar puntos con la segunda parada
								Parada p2 = paradaService.getParada(validosParaEvaluar.get(j).getIdparada2());
								//estado bus anterior
								double distanciaP2BusAnterior = p2.distance(eb.getPosicionAnterior(), "M");
								//estado bus actual
								double distanciaP2BusActual = p2.distance(eb.getPosicionActual(), "M");
								if(distanciaP1BusActual>distanciaP1BusAnterior && distanciaP2BusActual<distanciaP2BusAnterior) {
									//BetweenParadas Valido para sacar su tiempo...
									//utilizare la posicion actual del bus
									double distanciaP1P2 = p1.distance(p2.getCoordenada(), "M");
									double porcentajeP2BusActual = distanciaP2BusActual*100/distanciaP1P2;//% para calcular el tiempo restante
									double tiempotarda = validosParaEvaluar.get(j).getTiempoPromedio()*porcentajeP2BusActual/100;
									boolean ban = true;
									String idParadaSiguente = p2.getId();
									while(ban) {
										ban = false;
										for(BetweenParada bParada : tcp.getListTime()) {
											if(idParadaSiguente.equals(bParada.getIdparada1())) {
												tiempotarda = tiempotarda + bParada.getTiempoPromedio();
												idParadaSiguente = bParada.getIdparada2();
												ban = true;
												break;
											}
										}
										if(idParadaSiguente.equals(idParada)) {
											ban = false;
											listTiempoBus.put(eb.getPlaca(), tiempotarda);
											banSerch =false;
										}
									}
								}else if(distanciaP1BusActual==distanciaP1BusAnterior || distanciaP2BusActual==distanciaP2BusAnterior){
									System.out.println("No se puede identificar... Buses en la misma posicion...");
								}
							}
						}
					}
				}
			}
			if(!listTiempoBus.isEmpty()) {
				return listTiempoBus;
			}else {
				throw new ResponseStatusException(
					       HttpStatus.NOT_FOUND, "No se pudo realizar la Estimacion de Tiempos.");
			}
		}else
			throw new ResponseStatusException(
				       HttpStatus.NOT_FOUND, "Parada no pertenece a la Linea "+ linea +".");
	}
	
	/**
	 * Evalua si las {@link Parada} cercanas a un {@link Bus} pueden servir para el calculo de tiempos de un bus.
	 * @param par - Lista de {@link Parada} cercanas a un {@link Bus}
	 * @param tcp - Lista de tiempos ( {@link TimeControlParada} ) entre {@link Parada} ya calculados Previamente
	 * @return Lista de Tiempos entre {@link Parada}
	 */
	public List<BetweenParada> findBetweenParadaForEvaluate(List<Parada> par, TimeControlParada tcp) {
		List<BetweenParada> validosParaEvaluar = null;
		for(Parada pAux:par) {//listas de paradas cercanas al bus
			for(BetweenParada oneBP:tcp.getListTime()) {
				if(oneBP.getIdparada1().equals(pAux.getId()) || oneBP.getIdparada2().equals(pAux.getId())) {
					if(validosParaEvaluar==null) {
						validosParaEvaluar = new ArrayList<BetweenParada>(Arrays.asList(oneBP));
					}else {
						boolean encontro = false;
						for(int x =0;x<validosParaEvaluar.size();x++) {
							if(validosParaEvaluar.get(x).equals(oneBP)) {
								encontro = true;
								break;
							}
						}
						if(encontro == false)
							validosParaEvaluar.add(oneBP);
					}
				}
			}
		}
		return validosParaEvaluar;
	}

	/**
	 * Obtiene el {@link EstadoBus} Actual de los {@link Bus} pertenecientes a una Linea de {@link Cooperativa}
	 * @param linea - Linea de la {@link Cooperativa}
	 * @return Lista de {@link EstadoBusTemporal} de los {@link Bus}
	 */
	public List<EstadoBusTemporal> getEstadoActualBusByLinea(String linea) {
		Optional<List<EstadoBusTemporal>> list = estadoBusTemporalRepository.findByLinea(linea);
        list.get().removeIf(q -> !ifBusDisponible(q.getCreationDate()));
        return list.filter(a -> !a.isEmpty())
				.orElseThrow(() -> new ResponseStatusException(
								HttpStatus.NOT_FOUND, "No existe Estado de Bus disponible para la linea  "+linea+"."));
	}

	/**
	 * Comprobar si un bus esta disponible en el momento.
	 * @param fechaUbicacion - Fecha a comprobar.
	 * @return Verdadero o Falso.
	 */
	public boolean ifBusDisponible(Date fechaUbicacion) {
		try {
			if(Math.abs(((GlobalVariables.getFecha().getTime() - fechaUbicacion.getTime())/1000)-5) > 5)
				return false; //no disponible
			else
				return true; //disponible
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Elimina de manera permanente de la base de Datos un {@link Bus}
	 * @param id - ID del {@link Bus} a eliminar
	 */
	public void deleteBusPhysical(String id) {
		if(busRepository.existsById(id))
			busRepository.deleteById(id);
		else
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "No existe Bus con ID "+id+".");
	}

	/**
	 * Elimina de manera Permanente todos los {@link Bus} registrados en la base de datos.
	 */
	public void deleteAllBusPhysical() {
		busRepository.deleteAll();
	}

	
}