package br.com.itarocha.betesda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.itarocha.betesda.model.DestinacaoHospedagem;

@Service
public interface DestinacaoHospedagemRepository extends JpaRepository<DestinacaoHospedagem, Long> {

}
