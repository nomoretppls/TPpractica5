package es.ucm.fdi.tp.practica5.model;

import java.util.ArrayList;

public class Modelo {
	
	//El modelo guarda os observadores en una lista:
	ArrayList<ObservadorModelo> observadores;
	
	public void addObserver(ObservadorModelo ob){
		observadores.add(ob);
	}
	public void removeObserver(ObservadorModelo ob){
		observadores.remove(ob);
	}
	
	

}
