insert into dermatolog (id, username, password, ime, prezime, email_adresa) values (1, 'dusan', 'dusan', 'Dusan', 'Brkic', 'dusanbrk@gmail.com')


insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (1,'Marka Markovica 22, Subotica','Apoteka Markovic','opis apoteke',3.6)
insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (2,'Lazara Obilica 15, Beograd','Apoteka Djincic','opis apoteke',4.5)
insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (3,'Cara Lazara 103, Novi Sad','Apoteka Lazarevic','opis apoteke',2.2)
insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (4,'Zmaj Jovina 45, Sid','Apoteka Zmaj','opis apoteke',4.8)
insert into apoteka (id,adresa,naziv,opis,prosecna_ocena) values (5,'Matije Grupca 35, Novi Sad','Apoteka Antic','opis apoteke',3.2)

insert into pacijent (id,token,datum_rodjenja,email_adresa,ime,password,prezime,username,br_penala,poeni) values (1,'token','10.3.1999','mojEmail@gmail.com','dule','dule','dudulic','dule',0,10)

insert into admin_apoteke (id,username,password,ime,prezime,email_adresa,apoteka_id) values (1,'nikola','nikola','Nikola','Petrovic','nikola@gmail.com',1)
--insert into dermatolog_apoteka(id,cena,radno_vreme_kraj,radno_vreme_pocetak,apoteka_id,dermatolog_id) values (1,100.0,'2008-01-01 00:00:01','2008-01-01 00:00:01',1,1)