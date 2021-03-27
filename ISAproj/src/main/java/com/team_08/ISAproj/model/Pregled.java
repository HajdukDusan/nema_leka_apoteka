package com.team_08.ISAproj.model;

import javax.persistence.*;
import java.sql.Date;
import java.time.Duration;
import java.util.Set;

@Entity(name = "PREGLED")
public class Pregled {
	@Id
	@SequenceGenerator(name="PregledSeqGen", sequenceName = "PregledSeq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PregledSeqGen")
	private Long id;
	@Column(name = "VREME")
	private Date vreme;
	@Column(name = "TRAJANJE")
	private Duration trajanje;
	@Column(name = "CENA")
	private Double cena;
	@Column(name = "KRAJ")
	private Date kraj;
	@Column(name = "DIJAGNOZA")
	private String dijagnoza;
	@Column(name = "PREGLED_OBAVLJEN")
	private boolean pregledObavljen;
	
	// connections
	@ManyToOne(fetch = FetchType.EAGER)
	private Dermatolog dermatolog;
	@ManyToOne(fetch = FetchType.EAGER)
	private Pacijent pacijent;
	@ManyToOne(fetch = FetchType.EAGER)
	private Apoteka apoteka;
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Lek> preporuceniLekovi;
	
	// constructors
	public Pregled(Date vreme, Duration trajanje, double cena, Date kraj, String dijagnoza, boolean pregledObavljen,
			Dermatolog dermatolog, Pacijent pacijent, Apoteka apoteka, Set<Lek> preporuceniLekovi) {
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

	public Pregled() {

	}


	// getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
	public Set<Lek> getPreporuceniLekovi() {return preporuceniLekovi;}
	public void setPreporuceniLekovi(Set<Lek> preporuceniLekovi) {this.preporuceniLekovi = preporuceniLekovi;}
	
}
