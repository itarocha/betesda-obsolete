package br.com.itarocha.betesda.model.hospedagem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hospedagem", "classeIni", "classeFim", "idxFim", "dias"})
public class LinhaHospedagem {
	
	//private HospedeLeitoMapa hospedagem;

	private Long hospedagemId;
	private String identificador;
	private String nome;
	private String telefone;
	private String statusHospedagem;
	private Integer idxIni;
	private Integer idxFim;
	private String classeIni;
	private String classeFim;
	private String[] classes;
	private Integer[] dias;
	
	public LinhaHospedagem() {
		
	}
	
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Long getHospedagemId() {
		return hospedagemId;
	}

	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getStatusHospedagem() {
		return statusHospedagem;
	}

	public void setStatusHospedagem(String statusHospedagem) {
		this.statusHospedagem = statusHospedagem;
	}

	public Integer getIdxIni() {
		return idxIni;
	}
	
	public void setIdxIni(Integer idxIni) {
		this.idxIni = idxIni;
	}
	
	public Integer getIdxFim() {
		return idxFim;
	}
	
	public void setIdxFim(Integer idxFim) {
		this.idxFim = idxFim;
	}
	
	public String getClasseIni() {
		return classeIni;
	}
	
	public void setClasseIni(String classeIni) {
		this.classeIni = classeIni;
	}
	
	public String getClasseFim() {
		return classeFim;
	}
	
	public void setClasseFim(String classeFim) {
		this.classeFim = classeFim;
	}
	
	public String[] getClasses() {
		return classes;
	}

	public void setClasses(String[] classes) {
		this.classes = classes;
	}
	/*
	public HospedeLeitoMapa getHospedagem() {
		return hospedagem;
	}

	public void setHospedagem(HospedeLeitoMapa hospedagem) {
		this.hospedagem = hospedagem;
	}
	*/

	public Integer[] getDias() {
		return dias;
	}

	public void setDias(Integer[] dias) {
		this.dias = dias;
	}
}
