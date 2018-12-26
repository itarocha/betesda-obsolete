package br.com.itarocha.betesda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.itarocha.betesda.model.HospedeLeito;

public interface HospedeLeitoRepository extends JpaRepository<HospedeLeito, Long> {

	/* funciona
	select   hl.* 
	from     hospede_leito hl
	where    hl.hospede_id in (select hpd.id from hospede hpd where hpd.hospedagem_id = 30)
	and      hl.data_entrada = (select max(outro.data_entrada) from hospede_leito outro where outro.hospede_id = hl.hospede_id)
	order by hl.data_entrada desc;
	*/
	/*
	 @Query(value = "SELECT hospedeLeito " + 
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
	*/ 
	@Query(value = "SELECT   hl.* " + 
			"FROM     hospede_leito hl " + 
			"WHERE    hl.hospede_id IN (SELECT hpd.id FROM hospede hpd WHERE hpd.hospedagem_id = :hospedagemId) " + 
			"AND      hl.data_entrada = (SELECT MAX(outro.data_entrada) FROM hospede_leito outro WHERE outro.hospede_id = hl.hospede_id) " + 
			"ORDER BY hl.data_entrada DESC", nativeQuery = true) 
	List<HospedeLeito> findUltimoByHospedagemId(@Param("hospedagemId") Long hospedagemId);
	
	// Retorna só um
	@Query(value = "SELECT   hl.* " + 
	"FROM     hospede_leito hl " + 
	"WHERE    hl.hospede_id = :hospedeId " +
	"AND      hl.data_entrada = (SELECT MAX(outro.data_entrada) FROM hospede_leito outro WHERE outro.hospede_id = hl.hospede_id) " + 
	"ORDER BY hl.data_entrada ", nativeQuery = true)
	List<HospedeLeito> findUltimoByHospedeId(@Param("hospedeId") Long hospedeId);
	
	
	@Query(value = "SELECT COUNT(*) FROM  hospede h WHERE h.baixado = 'N' AND h.hospedagem_id = :hospedagemId", nativeQuery = true)	
	Long countHospedesNaoBaixadosByHospedagemId(@Param("hospedagemId") Long hospedagemId);
}
