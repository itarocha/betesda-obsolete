package br.com.itarocha.betesda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.itarocha.betesda.model.Hospede;

public interface HospedeRepository extends JpaRepository<Hospede, Long> {

}
