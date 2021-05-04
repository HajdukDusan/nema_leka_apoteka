package com.team_08.ISAproj.model;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "REZERVACIJA")
public class Rezervacija {
	@Id
	@SequenceGenerator(name="RezervacijaSeqGen", sequenceName = "RezervacijaSeq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RezervacijaSeqGen")
	private Long id;
	@Column(name = "ROK_PONUDE")
	private Date rokPonude;
	
	// connections
	@OneToMany(mappedBy = "rezervacija", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RezervacijaLek> lekovi = new HashSet<RezervacijaLek>();
	
	public void addRezervacijaLek(RezervacijaLek nl) {
		this.lekovi.add(nl);
	}
	
	public Set<RezervacijaLek> getLekovi() {
		return lekovi;
	}

	public void setLekovi(Set<RezervacijaLek> lekovi) {
		this.lekovi = lekovi;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	private Apoteka apoteka;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Pacijent pacijent;


	public Pacijent getPacijent() {
		return pacijent;
	}

	public void setPacijent(Pacijent pacijent) {
		this.pacijent = pacijent;
	}

	public Rezervacija(Date rokPonuda) {
		super();
		this.rokPonude = rokPonuda;
		this.lekovi = new HashSet<RezervacijaLek>();
	}

	public Rezervacija() {
		this.lekovi = new HashSet<RezervacijaLek>();
	}

	public Date getRokPonuda() {
		return rokPonude;
	}

	public void setRokPonuda(Date rokPonuda) {
		this.rokPonude = rokPonuda;
	}


	public Date getRokPonude() {
		return rokPonude;
	}

	public void setRokPonude(Date rokPonude) {
		this.rokPonude = rokPonude;
	}

	public Apoteka getApoteka() {
		return apoteka;
	}

	public void setApoteka(Apoteka apoteka) {
		this.apoteka = apoteka;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}