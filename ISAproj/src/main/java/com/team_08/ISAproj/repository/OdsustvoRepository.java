package com.team_08.ISAproj.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team_08.ISAproj.model.Odsustvo;

@Repository
public interface OdsustvoRepository extends JpaRepository<Odsustvo, Long> {

	
	
	//SELECT o FROM ODSUSTVO o JOIN ZDRAVSTVENI_RADNIK zr on o.zdravstveniRadnik.id = zr.id JOIN DERMATOLOG_APOTEKA da on zr.id = da.dermatolog.id where da.apoteka.id = :ap_id
	@Query(value = "SELECT o FROM ODSUSTVO o JOIN ZDRAVSTVENI_RADNIK zr on o.zdravstveniRadnik.id = zr.id JOIN DERMATOLOG_APOTEKA da on zr.id = da.dermatolog.id where da.apoteka.id = :ap_id and o.status = :status")
	List<Odsustvo> findAllDermaOdsustvoApotekaId(@Param("ap_id") Long apotekaId,@Param("status") String status);
	@Query(value = "SELECT o FROM ODSUSTVO o JOIN ZDRAVSTVENI_RADNIK zr on o.zdravstveniRadnik.id = zr.id where zr.apoteka.id = :ap_id and o.status = :status")
	List<Odsustvo> findAllFarmaOdsustvoApotekaId(@Param("ap_id") Long apotekaId,@Param("status") String status);

	@Query(value = "SELECT o FROM ODSUSTVO o JOIN FETCH o.zdravstveniRadnik z where z.cookieTokenValue = :cookie and (:start < o.kraj and :end > o.pocetak)")
	Set<Odsustvo> fetchOdsustvaByZdravstveniRadnikCookieInDateRange(String cookie, LocalDateTime start, LocalDateTime end);
}
