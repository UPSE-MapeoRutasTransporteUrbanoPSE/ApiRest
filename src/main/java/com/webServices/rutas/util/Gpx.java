package com.webServices.rutas.util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.webServices.rutas.model.Parada;

import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Representa los Metodos usados para procesar el Archivo GPX.
 * @author Davids Adrian Gonzalez Tigrero
 * @version 1.0
 */
public class Gpx {
	/**
	 * Decodificar Archivo GPX y tomar Lista de Puntos que confrman la Ruta y sus Respectivas Paradas.
	 * @param is - Archivo GPX
	 * @return {@link HashMap} Lista de Puntos y Paradas.
	 */
     public static Map<String, Object> decodeGPX(byte[] is){
    	List<Point> ruta = new ArrayList<Point>();
     	List<Parada> paradas = new ArrayList<Parada>();
     	Map<String, Object> x = new HashMap<>();
     	DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(is));
            NodeList nodelist_trkpt = document.getElementsByTagName("trkpt");
            NodeList nodelist_wpt = document.getElementsByTagName("wpt");
            for(int i = 0; i < nodelist_trkpt.getLength(); i++){
            	NamedNodeMap attributes = nodelist_trkpt.item(i).getAttributes();
                ruta.add(new Point(Double.parseDouble(attributes.getNamedItem("lon").getTextContent()),
               		 			Double.parseDouble(attributes.getNamedItem("lat").getTextContent())));
            }
            for(int i = 0; i < nodelist_wpt.getLength(); i++){
                Node node = nodelist_wpt.item(i);
                NamedNodeMap attributes = node.getAttributes();
                NodeList datos = node.getChildNodes();
                String newName = "";
                String newImagen = "0";
                String newLatitude = attributes.getNamedItem("lat").getTextContent();
                String newLongitude = attributes.getNamedItem("lon").getTextContent();
                for(int j = 0; j < datos.getLength(); j++){
                	Node dat = datos.item(j);
                	String etq = dat.getNodeName();
                	if(etq.equals("name")) {
                		newName = dat.getFirstChild().getNodeValue();
                	}
                	if(etq.equals("extensions")) {
                		NodeList nodelist_ext = dat.getChildNodes();
                		NodeList nodelist_extch = nodelist_ext.item(1).getChildNodes();
                 		Node n = nodelist_extch.item(3);
                 		if(n != null) {
                 			newImagen = n.getTextContent();
                 		}
                 	}
                 }
                 paradas.add(new Parada(newName,
                		 	newImagen!="0"?newImagen:"",
                		 	new Point(  Double.parseDouble(newLongitude),
                		 				Double.parseDouble(newLatitude))));
             }
             x.put("ruta", douglasPeucker(ruta,0.000025));
             x.put("parada", paradas);
		} catch (SAXException e) {
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "Archivo no valido, posiblemente no sea Archivo GPX.");
		} catch (IOException e) {
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "Conflicto Interno Comuniquese con el Proveedor.");
		}catch (ParserConfigurationException e1) {
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "Conflicto Interno, Comuniquese con el Proveedor.");
		}
        return x;
     }
     
     /**
      * Algoritmo Douglas Pecker.
      * @param puntos - Puntos antes del algoritmo.
      * @param epsilon - Coeficiente epsilon. 
      * @return Lista de Puntos despues del Algoritmo.
      */
     public static List<Point> douglasPeucker(List<Point> puntos,double epsilon){
         int maxIndex=0;
         double maxDist=0.0;
         double distPerp;
         List<Point> izq,der=new ArrayList<Point>();
         List<Point> filtro=new ArrayList<Point>();
         if(puntos.size()<2)
         {
         	return puntos;
         }
         for(int i=1;i<puntos.size()-1;i++){
             distPerp=distanciaPerpendicular(puntos.get(i),puntos.get(0),puntos.get(puntos.size()-1));
             if(distPerp>maxDist){
                 maxIndex=i;
                 maxDist=distPerp;
             }
         }
         if(maxDist>=epsilon){
             izq=douglasPeucker(puntos.subList(0, maxIndex),epsilon);
             der=douglasPeucker(puntos.subList(maxIndex,puntos.size()),epsilon);
             filtro.addAll(izq);
             filtro.addAll(der);
         }else{
             filtro.add(puntos.get(0));
             filtro.add(puntos.get(puntos.size()-1));
         }
         return filtro;
     }
     public static double distanciaPerpendicular(Point punto,Point lineaInicio,Point lineaFin){
         double x=punto.getX();
         double y=punto.getY();
         double xi=lineaInicio.getX();
         double yi=lineaInicio.getY();
         double xf=lineaFin.getX();
         double yf=lineaFin.getY();
         double angulo=(yf-yi)/(xf-xi);
         double inter=yi-(angulo*xi);
         double result=Math.abs(angulo*x-y+inter)/Math.sqrt(Math.pow(angulo, 2)+1);
         return result;
     }
}