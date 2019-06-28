package br.com.itarocha.betesda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.itarocha.betesda.model.SituacaoLeito;

public interface SituacaoLeitoRepository extends JpaRepository<SituacaoLeito, Long> {

	@Query("SELECT e FROM SituacaoLeito e ORDER BY e.descricao")
	List<SituacaoLeito> findAllOrderByDescricao();

}
