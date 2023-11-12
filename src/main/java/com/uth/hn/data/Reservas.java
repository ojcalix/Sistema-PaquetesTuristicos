package com.uth.hn.data;

import java.time.LocalDate;

public class Reservas {
	private Integer idReserva;
	private String nombrePaquete;
    private String destino;
    private LocalDate precio;
    private String descripcion;
    private String fechaInicio;
    
    
    public Integer getIdReserva() {
		return idReserva;
	}
	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
	}
	public String getNombrePaquete() {
		return nombrePaquete;
	}
	public void setNombrePaquete(String nombrePaquete) {
		this.nombrePaquete = nombrePaquete;
	}
	
    public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}	
	
	
    public LocalDate getPrecio() {
		return precio;
	}
	public void setPrecio(LocalDate precio) {
		this.precio = precio;
	}
	
	
    public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
		
	public String getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
	
 
}