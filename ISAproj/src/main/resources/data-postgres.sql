insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Brufen','bazinga 2mg','1','Piti jednom dnevno posle obroka')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Strepsils','lozenga 1.2mg+o.6mg, blister 2X8kom','2','Max 3 puta dnevno')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Gynaflor','vaginalna tableta; 1x 10exp8 CFU+0.03mg','3','Koristiti 2 puta')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Aspirin','oblozena tableta; 500mg','4','Max 2 puta dnevno')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Andol','tableta 75mg','5','Max 2 puta dnevno')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Viralex','tableta: 200mg','6','Max 2 puta dnevno')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Humira','rastvor za injekciju u napunjenom penu 50mg','7','Max 2 puta dnevno')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Aspirin','oblozena tableta; 1500mg','8','Max 2 puta dnevno')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Vaminolast','rastvor za infuziju','9','Max 2 puta dnevno')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Alpha D3','tableta za alfe','10','Max 2 puta dnevno')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Helex','oblozena tableta; 500mg','11','Max 2 puta dnevno')
insert into lek (id,naziv,sastav,sifra,uputstvo) values (nextval('lek_seq'),'Viagra','oblozena tableta; 40mg; 10kom','12','Africka sljiva')

insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (nextval('apoteka_seq'),'Marka Markovica 22, Subotica','Apoteka Markovic','opis apoteke',3.6)
insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (nextval('apoteka_seq'),'Lazara Obilica 15, Beograd','Apoteka Djincic','opis apoteke',4.5)
insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (nextval('apoteka_seq'),'Cara Lazara 103, Novi Sad','Apoteka Lazarevic','opis apoteke',2.2)
insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (nextval('apoteka_seq'),'Zmaj Jovina 45, Sid','Apoteka Zmaj','opis apoteke',4.8)
insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (nextval('apoteka_seq'),'Matije Grupca 35, Novi Sad','Apoteka Antic','opis apoteke',3.2)


insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'100','10','50','1','1')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'500','33','450','1','2')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'1000','4','800','1','3')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'800','140','450','2','4')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'1800','43','1450','2','5')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'80','200','45','3','6')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'1200','58','1200','4','7')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'600','15','670','4','8')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'700','23','770','4','9')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'300','74','340','4','10')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'320','5','300','5','11')
insert into apoteka_lek (id,cena,kolicina,stara_cena,apoteka_id,lek_id) values (nextval('apoteka_lek_seq'),'540','23','500','5','12')

--insert into dermatolog_apoteka(id,cena,radno_vreme_kraj,radno_vreme_pocetak,apoteka_id,dermatolog_id) values (1,100.0,'2008-01-01 09:00:01','2008-01-01 17:00:01',1,1)
insert into dermatolog (id, username, password, ime, prezime, email_adresa, token, first_login, adresa, grad, drzava, broj_telefona) values (nextval('korisnik_seq'), 'dusan', 'dusan', 'Dusan', 'Brkic', 'dusanbrk@gmail.com', 'dusan-dusan', false, 'Alekse Santica 14', 'Novi Sad', 'Kraljevina Srbija', '0640857676')
insert into pacijent (id,token,datum_rodjenja,email_adresa,ime,password,prezime,username,br_penala,poeni, adresa, grad, drzava, broj_telefona) values (nextval('korisnik_seq'),'token','10.3.1999','hajdukdusan99@gmail.com','Dusan','dule','Hajduk','dule',0,10, 'Tese Tesanovica 12', 'Sid', 'Republika Srbija', '0641212121')
insert into admin_apoteke (id,username,password,ime,prezime,email_adresa,first_login, apoteka_id, adresa, grad, drzava, broj_telefona) values (nextval('korisnik_seq'),'nikola','nikola','Nikola','Petrovic','nikola@gmail.com','false',1,'Milana Mice Petrovica 55', 'Kraljevo', 'Kraljevina Srbija', '0651212333')
insert into farmaceut (id, username, password, ime, prezime, email_adresa, token, first_login, adresa, grad, drzava, broj_telefona) values (nextval('korisnik_seq'), 'dzon', 'dzon', 'Dzon', 'Bosnic', 'johnbosnitch@gmail.com', 'dzon-dzon', false, 'The Chetnik immigration 133 Block 14/33', 'Toronto', 'Canada', '+1/250-5550199')

--dermatolog_apoteka
insert into dermatolog_apoteka(id,cena,radno_vreme_kraj,radno_vreme_pocetak,apoteka_id,dermatolog_id) values (1,100.0,'2008-01-01 17:00:01','2008-01-01 09:00:01',1,1)

--pacijenti
insert into pacijent (id,token,datum_rodjenja,email_adresa,ime,password,prezime,username,br_penala,poeni, adresa, grad, drzava, broj_telefona) values (nextval('korisnik_seq'),'token','10.3.1998','mojEmail12@gmail.com','Semir','osman','Osmanagic','osman',0,10, 'Tese Tesanovica 12', 'Sid', 'Republika Srbija', '0641212121')
insert into pacijent (id,token,datum_rodjenja,email_adresa,ime,password,prezime,username,br_penala,poeni, adresa, grad, drzava, broj_telefona) values (nextval('korisnik_seq'),'token','11.4.1999','mojEmail2@gmail.com','Miloslav','miloslav','Samardzic','miloslav',0,10, 'Tese Tesanovica 12', 'Sid', 'Republika Srbija', '0641212121')
insert into pacijent (id,token,datum_rodjenja,email_adresa,ime,password,prezime,username,br_penala,poeni, adresa, grad, drzava, broj_telefona) values (nextval('korisnik_seq'),'token','15.7.1955','mojEmail3@gmail.com','Miroljub','miroljub','Petrovic','miroljub',0,10, 'Tese Tesanovica 12', 'Sid', 'Republika Srbija', '0641212121')
insert into pacijent (id,token,datum_rodjenja,email_adresa,ime,password,prezime,username,br_penala,poeni, adresa, grad, drzava, broj_telefona) values (nextval('korisnik_seq'),'token','20.7.1944','mojEmail4@gmail.com','Tesa','tesa','Tesanovic','tesa',0,10, 'Tese Tesanovica 12', 'Sid', 'Republika Srbija', '0641212121')
insert into pacijent (id,token,datum_rodjenja,email_adresa,ime,password,prezime,username,br_penala,poeni, adresa, grad, drzava, broj_telefona) values (nextval('korisnik_seq'),'token','30.1.1988','mojEmail5@gmail.com','Slavisa','slavisa','Miljkovic','slavisa',0,10, 'Tese Tesanovica 12', 'Sid', 'Republika Srbija', '0641212121')

--pregledi
insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, 'Bude ga noci sajgonske.', '2021-04-13T12:00:01.0', true, 600000, '2021-04-13T11:45:01.0', '1', '1', '2')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('1', '1')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('1', '2')

insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, 'Pacijent ima vijetnamski sindrom.', '2021-04-13T13:00:01.0', true, 660000, '2021-04-13T12:45:01.0', '3', '1', '8')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('2', '3')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('2', '2')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('2', '4')

insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, 'Bude ga noci sajgonske.', '2021-04-13T12:00:01.0', true, 600000, '2021-04-15T11:45:01.0', '1', '1', '6')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('3', '1')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('3', '2')

insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, 'Pacijent ima vijetnamski sindrom.', '2021-04-14T13:00:01.0', true, 660000, '2021-04-14T12:45:01.0', '3', '1', '7')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('4', '3')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('4', '2')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('4', '4')

insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, 'Bude ga noci sajgonske.', '2021-04-14T15:00:01.0', true, 600000, '2021-04-14T14:45:01.0', '1', '1', '9')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('5', '1')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('5', '2')

insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, 'Pacijent ima vijetnamski sindrom.', '2021-04-19T13:00:01.0', true, 660000, '2021-04-19T12:45:01.0', '3', '1', '5')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('6', '3')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('6', '2')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('6', '4')

insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, 'Bude ga noci sajgonske.', '2021-04-21T12:00:01.0', true, 600000, '2021-04-21T11:45:01.0', '2', '1', '9')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('7', '1')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('7', '2')

insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, 'Pacijent ima vijetnamski sindrom.', '2021-04-27T13:00:01.0', true, 660000, '2021-04-27T12:45:01.0', '2', '1', '8')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('8', '3')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('8', '2')
insert into pregled_preporuceni_lekovi (pregled_id, preporuceni_lekovi_id) values ('8', '4')

insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), false, '', '2021-05-12T13:00:01.0', false, 0, '2021-05-12T12:45:01.0', '4', '1', '7')
insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, '', '2021-05-15T13:15:01.0', false, 0, '2021-05-15T12:45:01.0', '4', '1', '8')
insert into pregled (id, pregled_zakazan, dijagnoza, kraj, pregled_obavljen, trajanje, vreme, apoteka_id, zdravstveni_radnik_id, pacijent_id) values (nextval('pregled_seq'), true, '', '2021-05-16T13:00:01.0', false, 0, '2021-05-16T11:45:01.0', '1', '1', '9')
