package com.team_08.ISAproj.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team_08.ISAproj.dto.LekDTO;
import com.team_08.ISAproj.dto.NarudzbenicaAdminDTO;
import com.team_08.ISAproj.dto.NarudzbenicaDTO;
import com.team_08.ISAproj.model.AdminApoteke;
import com.team_08.ISAproj.model.Apoteka;
import com.team_08.ISAproj.model.ApotekaLek;
import com.team_08.ISAproj.model.Korisnik;
import com.team_08.ISAproj.model.Lek;
import com.team_08.ISAproj.model.Narudzbenica;
import com.team_08.ISAproj.model.NarudzbenicaLek;
import com.team_08.ISAproj.service.ApotekaService;
import com.team_08.ISAproj.service.KorisnikService;
import com.team_08.ISAproj.service.LekService;
import com.team_08.ISAproj.service.NarudzbenicaService;

@RestController
@RequestMapping("/narudzbine")
public class NarudzbinaController {

	
	@Autowired
	private NarudzbenicaService narudzbenicaService;
	@Autowired
	private LekService lekService;
	@Autowired
	private ApotekaService apotekaService;
	@Autowired
	private KorisnikService korisnikService;
	@PostMapping(value="/admin")
	public ResponseEntity<List<NarudzbenicaAdminDTO>> dodajNarudzbenice(@RequestBody List<NarudzbenicaAdminDTO> narudzbenice){
		
		Narudzbenica n = new Narudzbenica();
		n.setApoteka(apotekaService.findOne(Long.parseLong(narudzbenice.get(0).getApotekaId())));
		n.setRokPonude(narudzbenice.get(0).getDatumNarudzbine());
		n.setPacijent(null);
		narudzbenicaService.saveNarudzbenica(n);
		for(NarudzbenicaAdminDTO nDTO: narudzbenice) {
			System.out.println("_________" + nDTO.getSifra());
			Lek l = lekService.findOneBySifra(nDTO.getSifra());
			NarudzbenicaLek nl = new NarudzbenicaLek(nDTO.getKolicina(), n, l);
			narudzbenicaService.saveNarudzbenicaLek(nl);

		}
		return new ResponseEntity<List<NarudzbenicaAdminDTO>>(narudzbenice,HttpStatus.OK);
		
	}
	@GetMapping(value="lekNarudzbina")
	public ResponseEntity<LekDTO> saveLek(@RequestParam String cookie,@RequestParam String sifra){
		Korisnik k = korisnikService.findUserByToken(cookie);
		if(k == null) {
			return new ResponseEntity<LekDTO>(HttpStatus.NOT_FOUND);
		}
		if(k instanceof AdminApoteke) {
    	
			AdminApoteke aa = (AdminApoteke) k;
			System.out.println(sifra + "____________");
			ApotekaLek al = lekService.addApotekaLek(sifra,aa.getApoteka().getId());
			System.out.println(al);
			if(al == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
				
			}
			return new ResponseEntity<LekDTO>(HttpStatus.CREATED);
		}
		return new ResponseEntity<LekDTO>(HttpStatus.NOT_FOUND);
	}
}
