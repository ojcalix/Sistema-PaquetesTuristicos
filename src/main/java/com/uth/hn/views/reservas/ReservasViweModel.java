package com.uth.hn.views.reservas;

import java.util.List;

import com.uth.hn.data.Reservas;

public interface ReservasViweModel {

	void mostrarReservasEnGrid(List<Reservas> items);
	void mostrarMensajeError(String mensaje);
	void mostrarMensajeExito(String mensaje);
}
