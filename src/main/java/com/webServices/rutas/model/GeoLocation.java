package com.webServices.rutas.model;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;;
public class GeoLocation {
	private double rad_lat;
    private double rad_lon;
    private double deg_lat;
    private double deg_lon;
	private double EARTH_RADIUS = 6378;
	
	public GeoLocation(double rad_lat, double rad_lon, double deg_lat, double deg_lon) {
		super();
		this.rad_lat = rad_lat;
		this.rad_lon = rad_lon;
		this.deg_lat = deg_lat;
		this.deg_lon = deg_lon;
		this.check_values();
	}
	public GeoLocation(double deg_lat, double deg_lon) {
		super();
		this.rad_lat = Math.toRadians(deg_lat);
		this.rad_lon = Math.toRadians(deg_lon);
		this.deg_lat = deg_lat;
		this.deg_lon = deg_lon;
		this.check_values();
	}

	private void check_values() {
		double MIN_LAT  = Math.toRadians(-90);
		double MAX_LAT  = Math.toRadians (90);
		double MIN_LON  = Math.toRadians (-180);
		double MAX_LON  = Math.toRadians (180);
        if (this.rad_lat < MIN_LAT 
                || this.rad_lat > MAX_LAT 
                || this.rad_lon < MIN_LON 
                || this.rad_lon > MAX_LON) {
    			throw new ResponseStatusException(
    			           HttpStatus.NOT_FOUND, "Ilegal Arguments");
        }
	}
	public GeoLocation from_degrees(double deg_lat,double deg_lon) {
        double rad_Lat = Math.toRadians(deg_lat);
        double rad_Lon = Math.toRadians(deg_lon);
        return new GeoLocation(rad_Lat, rad_Lon, deg_lat, deg_lon);
	}
	public GeoLocation from_radians(double rad_lat,double rad_lon) {
        double deg_Lat = Math.toDegrees(rad_lat);
        double deg_Lon = Math.toDegrees(rad_lon);
        return new GeoLocation(rad_lat, rad_lon, deg_Lat, deg_Lon);
	}
	public double distance_to(GeoLocation other) {
		double radius = this.EARTH_RADIUS;
        return radius * Math.acos(
        		Math.sin(this.rad_lat) * Math.sin(other.rad_lat) +
        		Math.cos(this.rad_lat) * 
        		Math.cos(this.rad_lat) * 
        		Math.cos(this.rad_lon - other.rad_lon)
            );
	}
	
	public List<GeoLocation> bounding_locations(double distance) {
		/*
        Computes the bounding coordinates of all points on the surface
        of a sphere that has a great circle distance to the point represented
        by this GeoLocation instance that is less or equal to the distance argument.
        
        Param:
            distance - the distance from the point represented by this GeoLocation
                       instance. Must be measured in the same unit as the radius
                       argument (which is kilometers by default)
            
            radius   - the radius of the sphere. defaults to Earth's radius.
            
        Returns a list of two GeoLoations - the SW corner and the NE corner - that
        represents the bounding box.*/
		double MIN_LAT  = Math.toRadians(-90);
		double MAX_LAT  = Math.toRadians (90);
		double MIN_LON  = Math.toRadians (-180);
		double MAX_LON  = Math.toRadians (180);
		double radius = this.EARTH_RADIUS;
        if (radius < 0 || distance < 0)
        	throw new ResponseStatusException(
			           HttpStatus.NOT_FOUND, "Ilegal Arguments");
            
        // angular distance in radians on a great circle
        double rad_dist = distance/radius;
        
        double min_lat = this.rad_lat - rad_dist;
        double max_lat = this.rad_lat + rad_dist;
        double min_lon;
        double max_lon;
        
        if (min_lat > MIN_LAT || max_lat < MAX_LAT) {
            double delta_lon = Math.asin(Math.sin(rad_dist) / Math.cos(this.rad_lat));
            
            min_lon = this.rad_lon - delta_lon;
            if (min_lon < MIN_LON) {
                min_lon += 2 * Math.PI;
            }   
            max_lon = this.rad_lon + delta_lon;
            if (max_lon > MAX_LON) {
                max_lon -= 2 * Math.PI;
            }
        }
       // a pole is within the distance
        else {
            min_lat = Math.max(min_lat, MIN_LAT);
            max_lat = Math.min(max_lat, MAX_LAT);
            min_lon = MIN_LON;
            max_lon = MAX_LON;
        }
        return new ArrayList<GeoLocation>( Arrays.asList(this.from_radians(min_lat, min_lon),this.from_radians(max_lat, max_lon)));
	}
	public double getRad_lat() {
		return rad_lat;
	}
	public void setRad_lat(double rad_lat) {
		this.rad_lat = rad_lat;
	}
	public double getRad_lon() {
		return rad_lon;
	}
	public void setRad_lon(double rad_lon) {
		this.rad_lon = rad_lon;
	}
	public double getDeg_lat() {
		return deg_lat;
	}
	public void setDeg_lat(double deg_lat) {
		this.deg_lat = deg_lat;
	}
	public double getDeg_lon() {
		return deg_lon;
	}
	public void setDeg_lon(double deg_lon) {
		this.deg_lon = deg_lon;
	}
}
