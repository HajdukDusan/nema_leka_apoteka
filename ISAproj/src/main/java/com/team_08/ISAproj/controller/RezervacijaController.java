package com.team_08.ISAproj.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.team_08.ISAproj.dto.PregledDTO;
import com.team_08.ISAproj.dto.RezervacijaLekDTO;
import com.team_08.ISAproj.model.*;
import com.team_08.ISAproj.repository.FarmaceutRepository;
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

import com.team_08.ISAproj.dto.NarudzbenicaDTO;
import com.team_08.ISAproj.dto.RezervacijaDTO;
import com.team_08.ISAproj.service.ApotekaLekService;
import com.team_08.ISAproj.service.ApotekaService;
import com.team_08.ISAproj.service.EmailService;
import com.team_08.ISAproj.service.KorisnikService;
import com.team_08.ISAproj.service.LekService;
import com.team_08.ISAproj.service.NarudzbenicaService;
import com.team_08.ISAproj.service.PregledService;
import com.team_08.ISAproj.service.RezervacijaService;

import net.bytebuddy.asm.Advice.Local;

@RestController
@RequestMapping("/rezervacije")
public class RezervacijaController {
	
	@Autowired
	private RezervacijaService rezervacijaService;
	@Autowired
	private LekService lekService;
	@Autowired
	private ApotekaService apotekaService;
	@Autowired
	private KorisnikService korisnikService;
	@Autowired
	private ApotekaLekService apotekaLekService;
	@Autowired
	private EmailService sendEmailService;
	@Autowired
	private FarmaceutRepository farmaceutRepository;
	@Autowired
	private PregledService pregledService;

	@GetMapping(value = "/proveriRezervaciju")
	public ResponseEntity<List<RezervacijaLekDTO>> proveriRezervaciju(
			@RequestParam String cookie,
			@RequestParam Long idRezervacije) {
		Farmaceut f = farmaceutRepository.findOneByCookieTokenValue(cookie);

		if (f == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		Rezervacija r = rezervacijaService.fetchRezervacijaWithLekoviByIdAndApotekaIdBeforeRok(idRezervacije,
				f.getApoteka().getId(), LocalDateTime.now().plusDays(1));
		if (r == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		List<RezervacijaLekDTO> rezervacijeLekoviDTO = r.getLekovi().stream().map(new Function<RezervacijaLek, RezervacijaLekDTO>() {
			@Override
			public RezervacijaLekDTO apply(RezervacijaLek rezervacijaLek) {
				return new RezervacijaLekDTO(rezervacijaLek);
			}
		}).collect(Collectors.toList());

		return new ResponseEntity<>(rezervacijeLekoviDTO, HttpStatus.OK);
	}


		@GetMapping(value = "/izdajLekove")
	public ResponseEntity<String> izdajLekove(@RequestParam String cookie,
											  @RequestParam Long idRezervacije){
		Farmaceut f = farmaceutRepository.findOneByCookieTokenValue(cookie);

		Rezervacija r = rezervacijaService.findRezervacijaByIdAndApotekaIdBeforeRok(idRezervacije,
				f.getApoteka().getId(), LocalDateTime.now().plusDays(1));
		if(r==null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		r.setPreuzeto(true);
		rezervacijaService.saveRezervacija(r);

		String body = "Poštovani, " + r.getPacijent().getIme() + "\n"
				+ "Vasa rezervacija sa jedinstveni brojem '" + r.getId() + "' je upravo prihvacena i lekovi su Vam izdati.\n" +
				"Srdacan pozdrav.";

		String title = "Potvrda izdavanja lekova rezervacije (ID:" + r.getId() + ")";

		final String body_tmp = body;

		try
		{
			Thread t = new Thread() {
				public void run()
				{
					sendEmailService.sendEmail(r.getPacijent().getEmailAdresa(), body_tmp, title);
				}
			};
			t.start();
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// rezervacija iz jedne apoteke
	@PostMapping(value="/pacijent")
	public ResponseEntity<List<RezervacijaDTO>> dodajRezervaciju(@RequestBody List<RezervacijaDTO> rezervacije){
		
		Rezervacija n = new Rezervacija();
		n.setApoteka(apotekaService.findOne(Long.parseLong(rezervacije.get(0).getApotekaId())));
		n.setRokPonude(rezervacije.get(0).getDatumRezervacije());
		
		Pacijent pacijent = (Pacijent) korisnikService.findUserByToken(rezervacije.get(0).getPacijent());
		n.setPacijent(pacijent);
		rezervacijaService.saveRezervacija(n);
		
		String body = "Poštovani, " + pacijent.getIme() + "\n" 
					+ "Vasa rezervacija se sastoji iz:\n";
		
		double ukupnaCena = 0;
		
		for(RezervacijaDTO nDTO: rezervacije) {
			
			Lek l = lekService.findOneBySifra(nDTO.getSifraLeka());
			RezervacijaLek rl = new RezervacijaLek(nDTO.getKolicina(), n, l);
			rezervacijaService.saveRezervacijaLek(rl);
			
			ApotekaLek apotekaLek = apotekaLekService.findInApotekaLek(l.getId(), Long.parseLong(rezervacije.get(0).getApotekaId()));
			
			apotekaLek.setKolicina(apotekaLek.getKolicina()-nDTO.getKolicina());
			apotekaLekService.saveAL(apotekaLek);
			
			
			double cena_leka = nDTO.getKolicina()*apotekaLek.getCena();
			ukupnaCena += cena_leka;
			body +=	"	- " + l.getNaziv() + " x " + nDTO.getKolicina() + "kom - " + cena_leka + "din. \n";

		}
		

		body += "Ukupna cena je: " + ukupnaCena + " dinara \n"
				+ "Rezervaciju možete pokupiti do datuma: " + rezervacije.get(0).getDatumRezervacije() + "\n\n"
				+ "Za sva dodatna pitanja obratite nam se na ovaj mejl.\n"
				+ "Srdačan pozdrav.";
		
		String title = "Potvrda Rezervacije Leka (ID:" + n.getId() + ")";
	
		String body_tmp = body;
		
		try
		{
			Thread t = new Thread() {
				public void run()
				{
					sendEmailService.sendEmail(pacijent.getEmailAdresa(), body_tmp, title);
				}
			};
			t.start();
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<List<RezervacijaDTO>>(rezervacije,HttpStatus.OK);
		
	}
	// Rezervacija jednog leka
	@GetMapping(value="/rezervacija-leka")
	public ResponseEntity<Void> receiveData(
			@RequestParam("sifra") String sifra,
			@RequestParam("kolicina") int kolicina,
			@RequestParam("istekRezervacije") String datum,
			@RequestParam("cookie") String cookie
			) throws ParseException
		{
		
			Lek lek = null;
			
			Pacijent k = (Pacijent) korisnikService.findUserByToken(cookie);
			
			ApotekaLek al = null;
			
			Long porudzbinaId = (long) 0;

			LocalDateTime dateTime = LocalDateTime.parse(datum);
			
			
			for (ApotekaLek a : apotekaLekService.findAll()) {
				if(sifra.equals(a.getLek().getSifra())) {
					lek = a.getLek();
					al = a;
					if(kolicina>al.getKolicina()) {
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
					al.setKolicina(al.getKolicina()-kolicina);
		
					apotekaLekService.saveAL(al);
					
					Rezervacija n = new Rezervacija();
					n.setRokPonude(dateTime);
					
					
					RezervacijaLek nl = new RezervacijaLek(kolicina, n,lek);
					
					n.addRezervacijaLek(nl);
					n.setApoteka(a.getApoteka());
					n.setPacijent(k);
					
					rezervacijaService.saveRezervacija(n);
					rezervacijaService.saveRezervacijaLek(nl);
					
					porudzbinaId = n.getId();
				}
			}
		
			if(lek == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		
			String body = "Poštovani, " + k.getIme() + "\n"
					+ "Rezervisali ste lek " + lek.getNaziv() + " x " + kolicina + "kom" +"\n"
					+ "Ukupna cena je: " + al.getCena()*kolicina + " dinara \n"
					+ "Rezervisani lek možete pokupiti do isteka rezervacije " + datum + "\n\n"
					+ "Za sva dodatna pitanja obratite nam se na ovaj mejl.\n"
					+ "Srdačan pozdrav.";
			
			String title = "Potvrda Rezervacije Leka (ID:" + porudzbinaId + ")";
		
			try
			{
				Thread t = new Thread() {
					public void run()
					{
						sendEmailService.sendEmail(k.getEmailAdresa(), body, title);
					}
				};
				t.start();
			}
			catch(Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	
	// dobavljanje rezervacija
	@GetMapping(value="/moje_rezervacije" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RezervacijaDTO>> getRezervacije(
			@RequestParam("cookie") String cookie)
	{
		

    	List<Rezervacija> n = rezervacijaService.findAllRezervacija();

    	List<RezervacijaDTO> rezervacije = new ArrayList<RezervacijaDTO>();
    	
    	Korisnik korisnik = korisnikService.findUserByToken(cookie);
    	
    	for(Rezervacija tmp : n) {
    		
    		if(tmp.getPacijent().getId().equals(korisnik.getId())) {
    		
	    		String lekovi = "";
	        	boolean first = true;

	    		for(RezervacijaLek rl : rezervacijaService.findAllRezervacijaLek()) {
	    			
	    			if(rl.getRezervacija().getId() == tmp.getId()) {
	    				
	    				if(!first) { lekovi += ", "; } else { first = false; }
	    				lekovi += rl.getLek().getNaziv() + " x " + rl.getKolicina() + " kom";
	    			}
	    		}
	    		
	    		rezervacije.add(new RezervacijaDTO(
	    				tmp.getId(),
	    				lekovi,
	    				tmp.getRokPonude(),
	    				tmp.getApoteka().getNaziv(),
	    				tmp.isPreuzeto()));
    		
    		}
    	}
    	
    	return new ResponseEntity<List<RezervacijaDTO>>(rezervacije, HttpStatus.OK);

		
	}
	
	// rezervacija iz jedne apoteke
	@GetMapping(value="/otkazi-rezervaciju")
	public ResponseEntity<Void> otkaziRezervaciju(@RequestParam String id_rezervacije){
		
			Rezervacija r = rezervacijaService.findRezervacijaByID(Long.parseLong(id_rezervacije));
			if(r==null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
	    	
			List<RezervacijaLek> rl = rezervacijaService.findRezervacijaLekByRezervacijaID(Long.parseLong(id_rezervacije));
			
			for(RezervacijaLek tmp: rl) {
				ApotekaLek al = apotekaLekService.findOneBySifra(tmp.getLek(), r.getApoteka().getId());
				al.setKolicina(al.getKolicina()+tmp.getKolicina());
				apotekaLekService.saveAL(al);
			}
			
	    	rezervacijaService.removeRezervacija(r.getId());
	    	
	    	return new ResponseEntity<>(HttpStatus.OK);
	}
	//izvestaji za rezervacije
    @GetMapping(value = "/lekIzvestaj")
    public ResponseEntity<Map<Integer,Integer>> lekIzvestaj(@RequestParam("cookie") String cookie,
    		@RequestParam("vremenskiPeriod") String vremenskiPeriod,
    		@RequestParam(required = false) int godina){
    	
    	Korisnik k = korisnikService.findUserByToken(cookie);
    	if(k == null) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	AdminApoteke a = (AdminApoteke) k;
    	Map<Integer,Integer> izvestaj = new HashMap<Integer,Integer>();	
    	
    	int suma;
    	if(vremenskiPeriod.equals("Mesecni")) {
    		List<Rezervacija> rezervacije = rezervacijaService.findAllRezervacijeFinishedYear(a.getApoteka().getId(),godina);
        	Map<LocalDateTime, List<Rezervacija>> data = rezervacije.stream().collect(Collectors.groupingBy(d -> d.getRokPonude().withDayOfMonth(1)));
        	System.out.println(data);
        	
        	for(int i = 1; i<13;i++) {
        		izvestaj.put(i, 0);
        	}
        	for (LocalDateTime date : data.keySet()) {
        		suma = 0;
        		for(Rezervacija r: data.get(date)) {
        			for(RezervacijaLek rl: r.getLekovi()) {
        				suma+=rl.getKolicina();
        			}
        		}
        		izvestaj.put(date.toLocalDate().getMonth().getValue(), suma);
        	}
    	}
    	else if(vremenskiPeriod.equals("Kvartalni")) {
    		List<Rezervacija> rezervacije  = rezervacijaService.findAllRezervacijeFinishedYear(a.getApoteka().getId(),godina);
    		Map<Object, List<Rezervacija>> data = rezervacije.stream().collect(Collectors.groupingBy(d -> d.getRokPonude().get(IsoFields.QUARTER_OF_YEAR)));
    		//System.out.print(data);
    		for(int i = 1;i<5;i++) {
    			izvestaj.put(i,0);
    		}
        	for (Object date : data.keySet()) {
        		suma = 0;
        		for(Rezervacija r: data.get(date)) {
        			for(RezervacijaLek rl: r.getLekovi()) {
        				suma+=rl.getKolicina();
        			}
        		}
        		izvestaj.put((Integer) date, suma);
        	}
    	}
    	else if(vremenskiPeriod.equals("Godisnji")) {
    		for(int i = 0;i<5;i++) {
    			List<Rezervacija> rezervacije  = rezervacijaService.findAllRezervacijeFinishedYear(a.getApoteka().getId(),2018+i);
            		suma = 0;
            		for(Rezervacija r: rezervacije) {
            			for(RezervacijaLek rl: r.getLekovi()) {
            				suma+=rl.getKolicina();
            			}
            		}
    			izvestaj.put(2018+i,suma);
    		}
    	}
    	return new ResponseEntity<Map<Integer,Integer>> (izvestaj, HttpStatus.OK);
    }
	@GetMapping(value="/prihodIzvestaj")
	public ResponseEntity<Map<LocalDate,Double>> prihodiIzvestaj(@RequestParam("cookie") String cookie,
    		@RequestParam("vremenskiPeriod") String vremenskiPeriod,
    		@RequestParam("pocetak") String pocetak,
    		@RequestParam("kraj") String kraj){
		
    	Korisnik k = korisnikService.findUserByToken(cookie);
    	if(k == null) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	AdminApoteke a = (AdminApoteke) k;
    	Map<LocalDate,Double> izvestaj = new HashMap<LocalDate,Double>();	
    	double suma;

    	LocalDateTime start = LocalDateTime.parse(pocetak, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    	LocalDateTime end = LocalDateTime.parse(kraj, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		List<Rezervacija> rezervacije = rezervacijaService.findAllRezervacijeFinishedDateRange(a.getApoteka().getId(),start,end);
    	Map<LocalDateTime, List<Rezervacija>> data = rezervacije.stream().collect(Collectors.groupingBy(d -> d.getRokPonude().withDayOfMonth(1)));
    	System.out.println(data);
    	
//    	for(int i = 1; i<13;i++) {
//    		izvestaj.put(i, 0.0);
//    	}
    	for (LocalDateTime date : data.keySet()) {
    		suma = 0.0;
    		for(Rezervacija r: data.get(date)) {
    			for(RezervacijaLek rl: r.getLekovi()) {
    				suma+=rl.getKolicina()*rl.getCena();
    			}
    		}
    		izvestaj.put(date.toLocalDate(), suma);
    	}
    	List<Pregled> pregledi = pregledService.findAllFromApotekaFinishedDateRange(a.getApoteka().getId(),start,end);
    	Map<LocalDateTime, List<Pregled>> data1 = pregledi.stream().collect(Collectors.groupingBy(d -> d.getVreme().withDayOfMonth(1)));
    	for (LocalDateTime date : data1.keySet()) {
			suma = 0.0;
			for(Pregled p: data1.get(date)) {
				suma+= p.getCena();
			}
    		if(izvestaj.containsKey(date.toLocalDate())) {
    			izvestaj.put(date.toLocalDate(), izvestaj.get(date.toLocalDate())+suma);
    		
    		}
    		else {
    			izvestaj.put(date.toLocalDate(),suma);
    		}
    	}    	
		return new ResponseEntity<Map<LocalDate,Double>> (new TreeMap<LocalDate,Double>(izvestaj), HttpStatus.OK);
		}
}