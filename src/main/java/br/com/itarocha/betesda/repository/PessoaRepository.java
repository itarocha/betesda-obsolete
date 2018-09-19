package br.com.itarocha.betesda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.itarocha.betesda.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
