package br.com.itarocha.betesda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.itarocha.betesda.model.Hospedagem;

public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {
	
	@Query("SELECT  DISTINCT hospedagem " + 
			"FROM   Hospedagem hospedagem FETCH ALL PROPERTIES " +
			"WHERE  EXISTS( " +
			"				SELECT      hospedeLeito " + 			
			"				FROM        HospedeLeito hospedeLeito " +
			"				INNER JOIN  hospedeLeito.hospede hospede " +
			"				INNER JOIN  hospede.hospedagem hpd " +
			"				WHERE       hpd.id = hospedagem.id " +
			"				AND         hospedeLeito.id = :hospedeLeitoId " +
			") ")
	public Hospedagem findHospedagemByHospedeLeitoId(@Param("hospedeLeitoId") Long hospedeLeitoId);
	
	@Query("SELECT  hospedagem " + 
			"FROM   Hospedagem hospedagem FETCH ALL PROPERTIES " +
			"WHERE  hospedagem.id = :hospedagemId")
	public Hospedagem findHospedagemByHospedagemId(@Param("hospedagemId") Long hospedagemId);
}
