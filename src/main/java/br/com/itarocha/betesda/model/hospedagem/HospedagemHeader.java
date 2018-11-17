package br.com.itarocha.betesda.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

import br.com.itarocha.betesda.service.DiaHospedeLeito;

public class HospedagemHeader {
	
	private Long id; // hospedeLeitoId
	private Long hospedagemId;
	private Long pessoaId;
	private String pessoaNome;
	private String status;
	private Integer firstIndex;
	private List<DiaHospedeLeito> dias = new ArrayList<DiaHospedeLeito>();
	
	public HospedagemHeader(Long id, Long hospedagemId, Long pessoaId, String pessoaNome, String status) {
		this.id = id;
		this.hospedagemId = hospedagemId;
		this.pessoaId = pessoaId;
		this.pessoaNome = pessoaNome;
		this.status = status;
		this.firstIndex = -1;
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
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DiaHospedeLeito> getDias() {
		return dias;
	}
	public void setDias(List<DiaHospedeLeito> dias) {
		this.dias = dias;
	}
	public Integer getFirstIndex() {
		return firstIndex;
	}
	public void setFirstIndex(Integer firstIndex) {
		this.firstIndex = firstIndex;
	}
}
