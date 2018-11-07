package br.com.itarocha.betesda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.itarocha.betesda.model.HospedeLeito;

public interface HospedeLeitoRepository extends JpaRepository<HospedeLeito, Long> {

	 @Query("SELECT hospedeLeito " + 
	 		"FROM  HospedeLeito hospedeLeito " + 
	 		"INNER JOIN  hospedeLeito.hospede hospede " + 
	 		"INNER JOIN  hospede.hospedagem hospedagem " + 
	 		"WHERE (hospedagem.id = :hospedagemId) " + 
	 		"AND (hospedeLeito.dataEntrada = (SELECT MAX(outro.dataEntrada) " + 
	 		"                                 FROM HospedeLeito outro " + 
	 		"                                 WHERE (outro.hospede.id = hospede.id) " + 
	 		"                                 ) " + 
	 		"    ) " + 
	 		"ORDER BY hospedeLeito.id DESC")
		 List<HospedeLeito> findUltimoByHospedagemId(@Param("hospedagemId") Long hospedagemId);

}
