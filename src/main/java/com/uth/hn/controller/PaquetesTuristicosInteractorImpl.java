package com.uth.hn.controller;

import java.io.IOException;

import com.uth.hn.data.PaquetesTuristicos;
import com.uth.hn.data.PaquetesTuristicosResponse;
import com.uth.hn.model.DatabaseRepository;
import com.uth.hn.model.DatabaseRepositoryImpl;
import com.uth.hn.views.paquetesturisticos.PaquetesTuristicosViewModel;

public class PaquetesTuristicosInteractorImpl implements PaqueteaTuristicosInteractor{
	
	private DatabaseRepositoryImpl modelo;
	private PaquetesTuristicosViewModel vista;
	
	public PaquetesTuristicosInteractorImpl(PaquetesTuristicosViewModel view) {
		super();
		this.vista = view;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 30000L);
		
	}
	@Override
	public void consultarPaquetesTuristicos() {
		try {
			PaquetesTuristicosResponse respuesta = this.modelo.consultarPaquetesTuristicos();
			/*if(respuesta == null || respuesta.getItems() == null) {
				this.vista.mostrarMensajeError("No hay paquetes turisticos");
			}else {
			}*/		
			this.vista.mostarPaquetesTuristicosEnGrid(respuesta.getItems());
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void crearPaquetesTuristicos(PaquetesTuristicos nuevo) {
		try {
			boolean creado = this.modelo.crearPaquetesTuristicos(nuevo);
			/*if(creado == true) {
				this.vista.mostrarMensajeExito("Paquete Creado");
			}else {
				this.vista.mostrarMensajeError("Paquete Creado");
			}*/
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
