package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;

import br.com.itarocha.betesda.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.model.TipoUtilizacaoHospedagem;

public class HospedagemHeader {
	private String identificador;
	private Long id; // hospedeLeitoId ou hospedagemId
	private Long hospedagemId;
	private Long pessoaId;
	private String pessoaNome;
	private CellStatusHospedagem status;
	private Integer firstIndex;
	
	
	private LocalDate dataEntrada;
	private LocalDate dataEfetivaSaida;
	private LocalDate dataPrevistaSaida;
	
	private TipoUtilizacaoHospedagem tipoUtilizacao;
	private DestinacaoHospedagem destinacaoHospedagem;
	
	
	//private List<DiaHospedeLeito> dias = new ArrayList<DiaHospedeLeito>();
	
	public HospedagemHeader(String identificador, Long id, Long hospedagemId, Long pessoaId, String pessoaNome, CellStatusHospedagem status) {
		
		this.identificador = identificador;
		
		this.id = id;
		this.hospedagemId = hospedagemId;
		this.pessoaId = pessoaId;
		this.pessoaNome = pessoaNome;
		this.status = status;
		this.firstIndex = -1;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	public CellStatusHospedagem getStatus() {
		return status;
	}

	public void setStatus(CellStatusHospedagem status) {
		this.status = status;
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
	public TipoUtilizacaoHospedagem getTipoUtilizacao() {
		return tipoUtilizacao;
	}
	public void setTipoUtilizacao(TipoUtilizacaoHospedagem tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}
	public DestinacaoHospedagem getDestinacaoHospedagem() {
		return destinacaoHospedagem;
	}
	public void setDestinacaoHospedagem(DestinacaoHospedagem destinacaoHospedagem) {
		this.destinacaoHospedagem = destinacaoHospedagem;
	}
	/*
	public List<DiaHospedeLeito> getDias() {
		return dias;
	}
	public void setDias(List<DiaHospedeLeito> dias) {
		this.dias = dias;
	}
	*/
	public Integer getFirstIndex() {
		return firstIndex;
	}
	public void setFirstIndex(Integer firstIndex) {
		this.firstIndex = firstIndex;
	}
}
