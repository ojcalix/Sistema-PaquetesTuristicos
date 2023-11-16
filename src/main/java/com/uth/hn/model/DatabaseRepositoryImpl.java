package com.uth.hn.model;
import java.io.IOException;

import com.uth.hn.data.PaquetesTuristicos;
import com.uth.hn.data.PaquetesTuristicosResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class DatabaseRepositoryImpl {
	private static DatabaseRepositoryImpl INSTANCE;
	private DatabaseClient client;
	
	private DatabaseRepositoryImpl(String url, Long timeout) {
		client = new DatabaseClient(url, timeout);
	}
	//Conexion a base de datos
	public static DatabaseRepositoryImpl getInstance(String url, Long timeout) {
		if(INSTANCE == null) {
			synchronized (DatabaseRepositoryImpl.class) {
				if(INSTANCE == null) {
					INSTANCE = new DatabaseRepositoryImpl(url, timeout);//como la bd esta en internet necesitamos un tiempo de espera estimado
				}
			}
		}
		return INSTANCE;
	}
	
	public PaquetesTuristicosResponse consultarPaquetesTuristicos() throws IOException {
		Call<PaquetesTuristicosResponse> call = client.getDatabase().consultarPaquetesTuristicos();
		Response<PaquetesTuristicosResponse> response = call.execute();//AQUI SE LLAMA A LA BASE DE DATOS
		if(response.isSuccessful()) {
			return response.body();
		}else {
			return null;
		}
	}
	public boolean crearPaquetesTuristicos(PaquetesTuristicos nuevo) throws IOException {
		Call<ResponseBody> call = client.getDatabase().crearPaquetesTuristicos(nuevo);
		Response<ResponseBody> response = call.execute();//AQUI SE LLAMA A LA BASE DE DATOS
		return response.isSuccessful();
	}
}
