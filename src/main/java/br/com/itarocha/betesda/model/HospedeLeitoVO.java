package br.com.itarocha.betesda.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIgnoreProperties({"hospedexxxx", "hospedeLeitoxxxx"})
public class HospedeLeitoVO {

	//private Long hospedagemId;
	//private Long pessoaId;
	//private String pessoaNome;
	//private LocalDate dataEntrada;
	//private LocalDate dataEfetivaSaida;
	//private LocalDate dataPrevistaSaida;
	//private LocalDate dataEntradaLeito;
	//private LocalDate dataSaidaLeito;
	
	private Hospedagem hospedagem;
	private Hospede hospede;
	private HospedeLeito hospedeLeito;
	//private Quarto quarto;
	//private Leito leito;
	
	private Long leitoId;
	
	public HospedeLeitoVO() {}

	public HospedeLeitoVO(Hospedagem hospedagem, Hospede hospede, HospedeLeito hospedeLeito, Long leitoId) {
		this.hospedagem = hospedagem;
		this.hospede = hospede;
		this.hospedeLeito = hospedeLeito;
		this.leitoId = leitoId;
	}
	
	/*
	public HospedeLeitoVO(	Long hospedagemId, Long pessoaId, String pessoaNome, LocalDate dataEntrada, LocalDate dataEfetivaSaida, 
			LocalDate dataPrevistaSaida, LocalDate dataEntradaLeito, LocalDate dataSaidaLeito) {
		this.hospedagemId = hospedagemId;
		this.pessoaId = pessoaId;
		this.pessoaNome = pessoaNome;
		this.dataEntrada = dataEntrada;
		this.dataEfetivaSaida = dataEfetivaSaida;
		this.dataPrevistaSaida = dataPrevistaSaida;
		this.dataEntradaLeito = dataEntradaLeito;
		this.dataSaidaLeito = dataSaidaLeito;
	}
	*/
	/*
	public Long getHospedagemId() {
		return hospedagemId;
	}
	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}
	public Long getPessoaId() {
		return pessoaId;
	}
	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}
	public String getPessoaNome() {
		return pessoaNome;
	}
	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}
	public LocalDate getDataEntrada() {
		return dataEntrada;
	}
	public void setDataEntrada(LocalDate dataEntrada) {
		this.dataEntrada = dataEntrada;
	}
	public LocalDate getDataEfetivaSaida() {
		return dataEfetivaSaida;
	}
	public void setDataEfetivaSaida(LocalDate dataEfetivaSaida) {
		this.dataEfetivaSaida = dataEfetivaSaida;
	}
	public LocalDate getDataPrevistaSaida() {
		return dataPrevistaSaida;
	}
	public void setDataPrevistaSaida(LocalDate dataPrevistaSaida) {
		this.dataPrevistaSaida = dataPrevistaSaida;
	}
	public LocalDate getDataEntradaLeito() {
		return dataEntradaLeito;
	}
	public void setDataEntradaLeito(LocalDate dataEntradaLeito) {
		this.dataEntradaLeito = dataEntradaLeito;
	}
	public LocalDate getDataSaidaLeito() {
		return dataSaidaLeito;
	}
	public void setDataSaidaLeito(LocalDate dataSaidaLeito) {
		this.dataSaidaLeito = dataSaidaLeito;
	}
	*/

	public Hospede getHospede() {
		return hospede;
	}

	public void setHospede(Hospede hospede) {
		this.hospede = hospede;
	}

	/*
	public Quarto getQuarto() {
		return quarto;
	}


	public void setQuarto(Quarto quarto) {
		this.quarto = quarto;
	}


	public Leito getLeito() {
		return leito;
	}


	public void setLeito(Leito leito) {
		this.leito = leito;
	}
	*/

	public Hospedagem getHospedagem() {
		return hospedagem;
	}


	public void setHospedagem(Hospedagem hospedagem) {
		this.hospedagem = hospedagem;
	}


	public HospedeLeito getHospedeLeito() {
		return hospedeLeito;
	}


	public void setHospedeLeito(HospedeLeito hospedeLeito) {
		this.hospedeLeito = hospedeLeito;
	}

	public Long getLeitoId() {
		return leitoId;
	}

	public void setLeitoId(Long leitoId) {
		this.leitoId = leitoId;
	}
	
	
	
}
