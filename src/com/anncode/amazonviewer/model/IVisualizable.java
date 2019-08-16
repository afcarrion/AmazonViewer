package com.anncode.amazonviewer.model;

import java.util.Date;

public interface IVisualizable {
	
	/**
	 * Este metodo captura el tiempo exacto de visualización.
	 * @param dateI Es un objeto {@code Date} con el tiempo de inicio exacto
	 * @return Devuelve la fecha  y hora capturada. 
	 * */
	Date startToSee(Date dateI);
	
	/**
	 * Este metodo captura el tiempo de inicio y de fin de la visualziacion.
	 * @param dateI Es un objeto {@code Date} con el tiempo de inicio exacto
	 * @param dateF Es un objeto {@code Date} con el tiempo de final exacto
	 * */
	void stopToSee(Date dateI, Date dateF);
	
}
