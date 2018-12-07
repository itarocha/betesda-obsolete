package br.com.itarocha.betesda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.itarocha.betesda.model.HospedagemTipoServico;

public interface HospedagemServicoRepository extends JpaRepository<HospedagemTipoServico, Long> {

}
