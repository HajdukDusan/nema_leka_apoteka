package com.team_08.ISAproj.controller;

import java.util.List;

import com.team_08.ISAproj.dto.*;
import com.team_08.ISAproj.exceptions.CookieNotValidException;
import com.team_08.ISAproj.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team_08.ISAproj.service.ApotekaLekService;
import com.team_08.ISAproj.service.ApotekaService;
import com.team_08.ISAproj.service.EmailService;
import com.team_08.ISAproj.service.KorisnikService;
import com.team_08.ISAproj.service.LekService;
import com.team_08.ISAproj.service.NarudzbenicaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lekovi")
public class LekController {


	@Autowired
	private ApotekaLekService apotekaLekService;
    @Autowired
    private KorisnikService korisnikService;
	@Autowired
	private LekService lekService;
	@Autowired
	private ApotekaService apotekaService;
	@Autowired
	private EmailService sendEmailService;
	@Autowired
	private NarudzbenicaService narudzbenicaService;
	
	
//    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<LekDTO>> getLekovi1() {
//        List<ApotekaLek> apotekeLekovi = apotekaLekService.findAll();
//        if(apotekeLekovi == null) {
//        	return new ResponseEntity<List<LekDTO>>(HttpStatus.NOT_FOUND);
//        }
//        List<LekDTO> lekovi = new ArrayList<LekDTO>();
//        for(ApotekaLek a : apotekeLekovi) {
//        	lekovi.add(new LekDTO(a.getLek()));
//        }
//        return new ResponseEntity<List<LekDTO>>(lekovi, HttpStatus.OK);
//    }
	
	
	@GetMapping(value="/sviLek" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getSviLekovi(
			@RequestParam(required = false) String title,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "6") int size)
    {
		try {
	    	Pageable paging = PageRequest.of(page, size);
	    	
	    	Page<Lek> lekovi;
	    	lekovi = lekService.findAll(paging);
	
			List<LekDTO> lekoviDTO = new ArrayList<LekDTO>();
			for (Lek a : lekovi) {
				lekoviDTO.add(new LekDTO(a));
			}
			
			Map<String, Object> response = new HashMap<>();
			response.put("lekovi", lekovi);
			response.put("currentPage", lekovi.getNumber());
			response.put("totalItems", lekovi.getTotalElements());
			response.put("totalPages", lekovi.getTotalPages());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("")
	public ResponseEntity<Map<String, Object>> getLekovi(
			@RequestParam(required = false) String title,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "6") int size)
    {
		try {
	    	Pageable paging = PageRequest.of(page, size);
	    	
	    	Page<ApotekaLek> apotekeLekovi;
	    	if (title == null)
	    		apotekeLekovi = apotekaLekService.findAll(paging);
	        else
	        	apotekeLekovi = apotekaLekService.findByLekContaining(null, paging); //todo
	
			List<LekDTO> lekovi = new ArrayList<LekDTO>();
			for (ApotekaLek a : apotekeLekovi) {
				lekovi.add(new LekDTO(a));
			}
			
			Map<String, Object> response = new HashMap<>();
			response.put("lekovi", lekovi);
			response.put("currentPage", apotekeLekovi.getNumber());
			response.put("totalItems", apotekeLekovi.getTotalElements());
			response.put("totalPages", apotekeLekovi.getTotalPages());
			
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping(value="/aa" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getLekApoteka(@RequestParam(required = false) String title,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size, 
			@RequestParam String apotekaID,
	        @RequestParam(defaultValue = "naziv") String sort,
	        @RequestParam(defaultValue = "opadajuce") String smer) {
		
		List<Order> orders = new ArrayList<Order>();
		
    	Pageable paging = PageRequest.of(page, size);
    	Page<ApotekaLek> apotekeLekovi;
    	apotekeLekovi = apotekaLekService.findLekoviByApotekaIDSearch(Long.parseLong(apotekaID),"%" + title + "%", paging);
    	//apotekeLekovi = apotekaLekService.findLekoviByApotekaID(Long.parseLong(apotekaID), paging);
		if(apotekeLekovi == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		List<LekDTO> lekovi = new ArrayList<LekDTO>();
		for (ApotekaLek a : apotekeLekovi) {
			lekovi.add(new LekDTO(a));

		}
		Map<String, Object> response = new HashMap<>();
		response.put("lekovi", lekovi);
		response.put("currentPage", apotekeLekovi.getNumber());
		response.put("totalItems", apotekeLekovi.getTotalElements());
		response.put("totalPages", apotekeLekovi.getTotalPages());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping(value="/basic" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LekDTO>> getLekApoteka(@RequestParam String cookie) {
    	
		Korisnik k = korisnikService.findUserByToken(cookie);

        if(k == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(k instanceof AdminApoteke) {
        	AdminApoteke aa = (AdminApoteke) k;
    	List<ApotekaLek> apotekeLekovi = apotekaLekService.findOneByApoteka(aa.getApoteka().getId());
		if(apotekeLekovi == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		List<LekDTO> lekovi = new ArrayList<LekDTO>();
		for (ApotekaLek a : apotekeLekovi) {
			lekovi.add(new LekDTO(a.getLek()));
		}
		return new ResponseEntity<List<LekDTO>>(lekovi, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@GetMapping(value="/basic2" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LekDTO>> getLekByApotekaID(@RequestParam String apotekaID) {
    	
    	List<ApotekaLek> apotekeLekovi = apotekaLekService.findOneByApoteka(Long.parseLong(apotekaID));
		if(apotekeLekovi == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		List<LekDTO> lekovi = new ArrayList<LekDTO>();
		for (ApotekaLek a : apotekeLekovi) {
			lekovi.add(new LekDTO(a.getLek()));
		}
		return new ResponseEntity<List<LekDTO>>(lekovi, HttpStatus.OK);
	}
	//dodavanje leka
	@PostMapping(consumes = "application/json")
	public ResponseEntity<LekDTO> saveLek(@RequestBody LekDTO lekDTO){
		Korisnik k = korisnikService.findUserByToken(lekDTO.getCookie());
		if(k == null) {
			return new ResponseEntity<LekDTO>(HttpStatus.NOT_FOUND);
		}
		if(k instanceof AdminApoteke) {
    	
			AdminApoteke aa = (AdminApoteke) k;
    	
			Boolean check = lekService.saveLekApoteka(lekDTO,aa.getApoteka().getId().toString());
			if(check) {
				return new ResponseEntity<LekDTO>(lekDTO,HttpStatus.CREATED);
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
		}
		return new ResponseEntity<LekDTO>(HttpStatus.NOT_FOUND);
	}

	//uredjivanje leka
	@PutMapping(consumes = "application/json",produces = "application/json")
	public ResponseEntity<LekDTO> updateLek(@RequestBody LekDTO lekDTO){
		System.out.println(lekDTO.getCookie());
		Korisnik k = korisnikService.findUserByToken(lekDTO.getCookie());
		
		if (k == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);	
		}
		
		if(k instanceof AdminApoteke) {
			System.out.println(lekDTO.getCookie());
			AdminApoteke aa = (AdminApoteke) k;
			Lek l = lekService.findOneBySifra(lekDTO.getSifra());
			Apoteka a = apotekaService.findOne(aa.getApoteka().getId());
			ApotekaLek al = apotekaLekService.findOneBySifra(l,aa.getApoteka().getId());
			al.update(lekDTO);
			apotekaLekService.saveAL(al);
			return new ResponseEntity<LekDTO>(lekDTO,HttpStatus.OK);
		}
		return new ResponseEntity<LekDTO>(lekDTO,HttpStatus.NOT_FOUND);
	}
	//brisanje leka
	@DeleteMapping(value = "/deleteLek")
	public ResponseEntity<Void> deleteLek(@RequestParam String sifraLeka,@RequestParam String cookie){
		Korisnik k = korisnikService.findUserByToken(cookie);
		System.out.println(k);
		if (k == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);	
		}
		if(k instanceof AdminApoteke) {
			AdminApoteke aa = (AdminApoteke) k;
			Lek l = lekService.findOneBySifra(sifraLeka);
			if(l == null) {
				
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			apotekaLekService.removeBySifra(l.getId(),aa.getApoteka().getId());
			return new ResponseEntity<>(HttpStatus.OK);	
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);	
	}
	
	
	
	
	
	//narucivanje lekova
	@GetMapping(value="/narucivanje_lek" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getLekoveNaruci(@RequestParam("cookie") String cookie, 
			@RequestParam("apotekaID") String apotekaId, 
			@RequestParam(defaultValue = "15") int size,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String title,
	        @RequestParam(defaultValue = "naziv") String sort,
	        @RequestParam(defaultValue = "opadajuce") String smer){

		
		Pageable paging = PageRequest.of(page, size);
		Page<Lek> lekovi;
		// svi lekovi u bazi
		//lekovi = lekService.findAllSearch(paging,title);
		lekovi = lekService.findAll(paging);
		ApotekaLek al;
		LekDTO lekDTO;
		List<LekDTO> lekoviDTO = new ArrayList<LekDTO>();
		for(Lek l: lekovi) {
			//proveravamo da li postoji u apoteci
			al = apotekaLekService.findInApotekaLek(l.getId(),Long.parseLong(apotekaId));
			if(al == null) {
				lekDTO = new LekDTO(l);	
			}
			else {
				lekDTO = new LekDTO(al);
			}
			lekoviDTO.add(lekDTO);
		}	
		Map<String, Object> response = new HashMap<>();
		response.put("lekovi", lekoviDTO);
		response.put("currentPage", lekovi.getNumber());
		response.put("totalItems", lekovi.getTotalElements());
		response.put("totalPages", lekovi.getTotalPages());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
	}
	//dodavanje lekova iz baze
	@GetMapping(value = "/ostali")
	public ResponseEntity<Map<String, Object>> getOstaliLekovi(@RequestParam("cookie") String cookie, 
			@RequestParam("apotekaID") String apotekaId,
			@RequestParam(defaultValue = "6") int size,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String title,
	        @RequestParam(defaultValue = "naziv") String sort,
	        @RequestParam(defaultValue = "opadajuce") String smer){

		
		Pageable paging = PageRequest.of(page, size);
		Page<Lek> lekovi;
		// svi lekovi u bazi
		//lekovi = lekService.findAllSearch(paging,title);
		lekovi = lekService.test(paging,Long.parseLong(apotekaId));
		ApotekaLek al;
		LekDTO lekDTO;
		List<LekDTO> lekoviDTO = new ArrayList<LekDTO>();
		for(Lek l: lekovi) {
			al = apotekaLekService.findInApotekaLek(l.getId(),Long.parseLong(apotekaId));
			lekoviDTO.add(new LekDTO(l));
		}
		Map<String, Object> response = new HashMap<>();
		response.put("lekovi", lekoviDTO);
		response.put("currentPage", lekovi.getNumber());
		response.put("totalItems", lekovi.getTotalElements());
		response.put("totalPages", lekovi.getTotalPages());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
	}
	//narucivanje lekova
	@GetMapping(value="/narucivanje_lek_pacijant" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getLekoveNaruciPacijent(@RequestParam("cookie") String cookie, 
			@RequestParam("apotekaID") String apotekaId, 
			@RequestParam(defaultValue = "15") int size,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String title,
	        @RequestParam(defaultValue = "naziv") String sort,
	        @RequestParam(defaultValue = "opadajuce") String smer){

		
		Pageable paging = PageRequest.of(page, size);
		Page<Lek> lekovi;
		// svi lekovi u bazi
		//lekovi = lekService.findAllSearch(paging,title);
		Page<ApotekaLek> alLista = apotekaLekService.findLekoviByApotekaID(Long.parseLong(apotekaId), paging);
		List<LekDTO> lekoviDTO = new ArrayList<LekDTO>();
		for(ApotekaLek al: alLista) {
			lekoviDTO.add(new LekDTO(al));
		}	
		Map<String, Object> response = new HashMap<>();
		response.put("lekovi", lekoviDTO);
		response.put("currentPage", alLista.getNumber());
		response.put("totalItems", alLista.getTotalElements());
		response.put("totalPages", alLista.getTotalPages());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@PostMapping(value="/getAllByPacijentNotAllergic" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<LekDTO>> getAllByPacijentNotAllergic(
			@RequestBody Map<String, Object> body){

		String pretraga = (String) body.get("pretraga");
		Long idPacijenta = ((Number) body.get("idPacijenta")).longValue();
		String cookie = (String) body.get("cookie");
		int page = (int) body.get("page");
		int pageSize = (int) body.get("pageSize");
		List<String> vecPreporuceniSifre = (List<String>) body.get("vecPreporuceniSifre");
		vecPreporuceniSifre.add("");

		Page<Lek> lekovi = null;
		lekovi = lekService.getAllByPacijentNotAllergic(page, pageSize, idPacijenta, pretraga, vecPreporuceniSifre);
		if (lekovi == null)
			return new ResponseEntity<Page<LekDTO>>(Page.empty(), HttpStatus.OK);

		Page<LekDTO> lekoviDTO = lekovi.map(new Function<Lek, LekDTO>() {
			@Override
			public LekDTO apply(Lek l) {
				LekDTO lekDTO = new LekDTO(l);
				return lekDTO;
			}
		});
		return new ResponseEntity<Page<LekDTO>>(lekoviDTO, HttpStatus.OK);
	}
}
