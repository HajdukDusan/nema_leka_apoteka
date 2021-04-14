package com.team_08.ISAproj.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.team_08.ISAproj.model.*;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PregledDTO {
    private Long id;
    private LocalDateTime start;
    private Double cena;
    private LocalDateTime end;
    private String dijagnoza;
    private boolean pregledObavljen;
    private Long trajanje;

    // connections
    private KorisnikDTO pacijent;
    private ApotekaDTO apoteka;
    private Set<LekDTO> preporuceniLekovi;

    public PregledDTO(Pregled pregled) {
        this.id = pregled.getId();
        this.start = pregled.getVreme();
        this.cena = pregled.getCena();
        this.end = pregled.getKraj();
        this.dijagnoza = pregled.getDijagnoza();
        this.pregledObavljen = pregled.isPregledObavljen();
        this.pacijent = new KorisnikDTO(pregled.getPacijent());
        this.apoteka = new ApotekaDTO(pregled.getApoteka());
        this.trajanje = pregled.getTrajanje();
        this.preporuceniLekovi = new HashSet<LekDTO>();
        for (Lek l : pregled.getPreporuceniLekovi())
            this.preporuceniLekovi.add(new LekDTO(l));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public boolean isPregledObavljen() {
        return pregledObavljen;
    }

    public void setPregledObavljen(boolean pregledObavljen) {
        this.pregledObavljen = pregledObavljen;
    }

    public KorisnikDTO getPacijent() {
        return pacijent;
    }

    public void setPacijent(KorisnikDTO pacijent) {
        this.pacijent = pacijent;
    }

    public ApotekaDTO getApoteka() {
        return apoteka;
    }

    public void setApoteka(ApotekaDTO apoteka) {
        this.apoteka = apoteka;
    }

    public Set<LekDTO> getPreporuceniLekovi() {
        return preporuceniLekovi;
    }

    public void setPreporuceniLekovi(Set<LekDTO> preporuceniLekovi) {
        this.preporuceniLekovi = preporuceniLekovi;
    }

    public long getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(Long trajanje) {
        this.trajanje = trajanje;
    }
}
