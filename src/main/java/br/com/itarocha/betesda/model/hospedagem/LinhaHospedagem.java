package br.com.itarocha.betesda.model.hospedagem;

public class LinhaHospedagem {
	
	private HospedeLeitoMapa hospedagem;

	private String nome;
	private Integer idxIni;
	private Integer idxFim;
	private String classeIni;
	private String classeFim;
	private String[] classes;
	
	public LinhaHospedagem() {
		
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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

	public HospedeLeitoMapa getHospedagem() {
		return hospedagem;
	}

	public void setHospedagem(HospedeLeitoMapa hospedagem) {
		this.hospedagem = hospedagem;
	}
}
