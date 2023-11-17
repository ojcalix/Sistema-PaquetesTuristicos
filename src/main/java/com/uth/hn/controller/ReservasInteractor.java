package com.uth.hn.controller;

import com.uth.hn.data.Reservas;

public interface ReservasInteractor {
	void consultarReservas();
	void crearReservas(Reservas nuevo);
}
