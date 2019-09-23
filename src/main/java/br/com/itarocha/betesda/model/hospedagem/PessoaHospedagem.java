package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"destinacaoHospedagemId","cidade", "dataUltimaEntrada"})
public class PessoaHospedagem {

	private Long id;
	private String nome;
	private String cidade;
	private String uf;
	private String cidadeUf;
	private Long hospedagemId;
	private Long hospedeId;
	private Long tipoHospedeId;
	private String tipoHospedeDescricao;
	private CellStatusHospedagem statusHospedagem;
	private Boolean baixado;
	private Long destinacaoHospedagemId;
	private String	destinacao;
	private String tipoUtilizacao;
	private String tipoUtilizacaoDescricao;
	private LocalDate dataEntradaHospedagem;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	private LocalDate dataSaidaHospedagem;

	private LocalDate dataPrimeiraEntrada;
	private LocalDate dataUltimaEntrada;
	
	private List<PessoaLeito> leitos = new ArrayList<>();
	
	public PessoaHospedagem() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidadeUf() {
		return cidadeUf;
	}

	public void setCidadeUf(String cidadeUf) {
		this.cidadeUf = cidadeUf;
	}

	public Long getHospedagemId() {
		return hospedagemId;
	}

	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}

	public Long getHospedeId() {
		return hospedeId;
	}

	public void setHospedeId(Long hospedeId) {
		this.hospedeId = hospedeId;
	}

	public Long getTipoHospedeId() {
		return tipoHospedeId;
	}

	public void setTipoHospedeId(Long tipoHospedeId) {
		this.tipoHospedeId = tipoHospedeId;
	}

	public String getTipoHospedeDescricao() {
		return tipoHospedeDescricao;
	}

	public void setTipoHospedeDescricao(String tipoHospedeDescricao) {
		this.tipoHospedeDescricao = tipoHospedeDescricao;
	}

	public CellStatusHospedagem getStatusHospedagem() {
		return statusHospedagem;
	}

	public void setStatusHospedagem(CellStatusHospedagem statusHospedagem) {
		this.statusHospedagem = statusHospedagem;
	}

	public Boolean getBaixado() {
		return baixado;
	}

	public void setBaixado(Boolean baixado) {
		this.baixado = baixado;
	}

	public Long getDestinacaoHospedagemId() {
		return destinacaoHospedagemId;
	}

	public void setDestinacaoHospedagemId(Long destinacaoHospedagemId) {
		this.destinacaoHospedagemId = destinacaoHospedagemId;
	}

	public String getDestinacao() {
		return destinacao;
	}

	public void setDestinacao(String destinacao) {
		this.destinacao = destinacao;
	}

	public String getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(String tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public String getTipoUtilizacaoDescricao() {
		return tipoUtilizacaoDescricao;
	}

	public void setTipoUtilizacaoDescricao(String tipoUtilizacaoDescricao) {
		this.tipoUtilizacaoDescricao = tipoUtilizacaoDescricao;
	}

	public LocalDate getDataEntradaHospedagem() {
		return dataEntradaHospedagem;
	}

	public void setDataEntradaHospedagem(LocalDate dataEntradaHospedagem) {
		this.dataEntradaHospedagem = dataEntradaHospedagem;
	}

	public LocalDate getDataPrevistaSaida() {
		return dataPrevistaSaida;
	}

	public void setDataPrevistaSaida(LocalDate dataPrevistaSaida) {
		this.dataPrevistaSaida = dataPrevistaSaida;
	}

	public LocalDate getDataEfetivaSaida() {
		return dataEfetivaSaida;
	}

	public void setDataEfetivaSaida(LocalDate dataEfetivaSaida) {
		this.dataEfetivaSaida = dataEfetivaSaida;
	}

	public LocalDate getDataSaidaHospedagem() {
		return dataSaidaHospedagem;
	}

	public void setDataSaidaHospedagem(LocalDate dataSaidaHospedagem) {
		this.dataSaidaHospedagem = dataSaidaHospedagem;
	}

	public LocalDate getDataPrimeiraEntrada() {
		return dataPrimeiraEntrada;
	}

	public void setDataPrimeiraEntrada(LocalDate dataPrimeiraEntrada) {
		this.dataPrimeiraEntrada = dataPrimeiraEntrada;
	}

	public LocalDate getDataUltimaEntrada() {
		return dataUltimaEntrada;
	}

	public void setDataUltimaEntrada(LocalDate dataUltimaEntrada) {
		this.dataUltimaEntrada = dataUltimaEntrada;
	}

	public List<PessoaLeito> getLeitos() {
		return leitos;
	}

	public void setLeitos(List<PessoaLeito> leitos) {
		this.leitos = leitos;
	}
	
}
