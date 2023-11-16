package com.uth.hn.data;

public class PaquetesTuristicos {
	private Integer idPaquete;
	private String nombrePaquete;
    private String destino;
    private Float precio;
    private String descripcion;
    private Integer duracion;
    private Integer cupoPersonas;
    
    public Integer getIdPaquete() {
		return idPaquete;
	}
	public void setIdPaquete(Integer idPaquete) {
		this.idPaquete = idPaquete;
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
	
    public Float getPrecio() {
		return precio;
	}
	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	
	
    public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
		
    public Integer getDuracion() {
		return duracion;
	}
	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}

    public Integer getCupoPersonas() {
		return cupoPersonas;
	}
	public void setCupoPersonas(Integer cupoPersonas) {
		this.cupoPersonas = cupoPersonas;
	}
    
}