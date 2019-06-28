package br.com.itarocha.betesda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.itarocha.betesda.model.Entidade;

public interface EntidadeRepository extends JpaRepository<Entidade, Long> {

}
