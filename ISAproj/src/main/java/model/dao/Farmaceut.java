package model.dao;

import java.util.ArrayList;

public class Farmaceut extends Korisnik {

	
	
	private double prosecnaOcena;
	private FarmaceutApoteka apoteke;
	public double getProsecnaOcena() {
		return prosecnaOcena;
	}

	public void setProsecnaOcena(double prosecnaOcena) {
		this.prosecnaOcena = prosecnaOcena;
	}

	public Farmaceut(double prosecnaOcena) {
		super();
		this.prosecnaOcena = prosecnaOcena;
	}
	
}
