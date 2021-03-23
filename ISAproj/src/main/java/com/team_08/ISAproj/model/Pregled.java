package com.team_08.ISAproj.model;

import java.sql.Date;
import java.time.Duration;
import java.util.ArrayList;

public class Pregled {
	
	private Date vreme;
	private Duration trajanje;
	private double cena;
	private Date kraj;
	private String dijagnoza;
	private boolean pregledObavljen;
	
	// connections
	private Dermatolog dermatolog;
	private Pacijent pacijent;
	private Apoteka apoteka;
	private ArrayList<Lek> preporuceniLekovi;
	
	// constructors
	public Pregled(Date vreme, Duration trajanje, double cena, Date kraj, String dijagnoza, boolean pregledObavljen,
			Dermatolog dermatolog, Pacijent pacijent, Apoteka apoteka, ArrayList<Lek> preporuceniLekovi) {
		super();
		this.vreme = vreme;
		this.trajanje = trajanje;
		this.cena = cena;
		this.kraj = kraj;
		this.dijagnoza = dijagnoza;
		this.pregledObavljen = pregledObavljen;
		this.dermatolog = dermatolog;
		this.pacijent = pacijent;
		this.apoteka = apoteka;
		this.preporuceniLekovi = preporuceniLekovi;
	}

	
	// getters and setters
	public Date getVreme() {return vreme;}
	public void setVreme(Date vreme) {this.vreme = vreme;}
	public Duration getTrajanje() {return trajanje;}
	public void setTrajanje(Duration trajanje) {this.trajanje = trajanje;}
	public double getCena() {return cena;}
	public void setCena(double cena) {this.cena = cena;}
	public Date getKraj() {return kraj;}
	public void setKraj(Date kraj) {this.kraj = kraj;}
	public String getDijagnoza() {return dijagnoza;}
	public void setDijagnoza(String dijagnoza) {this.dijagnoza = dijagnoza;}
	public boolean isPregledObavljen() {return pregledObavljen;}
	public void setPregledObavljen(boolean pregledObavljen) {this.pregledObavljen = pregledObavljen;}
	public Dermatolog getDermatolog() {return dermatolog;}
	public void setDermatolog(Dermatolog dermatolog) {this.dermatolog = dermatolog;}
	public Pacijent getPacijent() {return pacijent;}
	public void setPacijent(Pacijent pacijent) {this.pacijent = pacijent;}
	public Apoteka getApoteka() {return apoteka;}
	public void setApoteka(Apoteka apoteka) {this.apoteka = apoteka;}
	public ArrayList<Lek> getPreporuceniLekovi() {return preporuceniLekovi;}
	public void setPreporuceniLekovi(ArrayList<Lek> preporuceniLekovi) {this.preporuceniLekovi = preporuceniLekovi;}
	
}