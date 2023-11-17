package com.uth.hn.controller;

import java.io.IOException;

import com.uth.hn.data.Reservas;
import com.uth.hn.data.ReservasResponse;
import com.uth.hn.model.DatabaseRepositoryImpl;
import com.uth.hn.views.reservas.ReservasViweModel;

public class ReservasInteractorImpl implements ReservasInteractor{
	
	private DatabaseRepositoryImpl modelo;
	private ReservasViweModel vista;
	
	public ReservasInteractorImpl(ReservasViweModel view) {
		super();
		this.vista = view;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 30000L);
	}
	
	@Override
	public void consultarReservas() {
	    try {
	        ReservasResponse respuesta = this.modelo.consultarReservas();
	        if (respuesta == null || respuesta.getItems() == null) {
	            this.vista.mostrarMensajeError("No hay reservas que mostrar");
	        } else {
	            this.vista.mostrarReservasEnGrid(respuesta.getItems());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public void crearReservas(Reservas nuevo) {
		try {
			boolean creado = this.modelo.crearReservas(nuevo);
			if(creado == true) {
				this.vista.mostrarMensajeExito("Reservas creado exitosamente!");
			}else {
				this.vista.mostrarMensajeError("Hubo un problema al crear la reserva");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}

}
