package br.com.itarocha.betesda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.itarocha.betesda.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
