package br.com.itarocha.betesda.model;

public enum EstadoCivil {

	S("Solteiro"),
	C("Casado"),
	D("Divorciado"),
	U("União Estável"),
	V("Viúvo");
	
	private String descricao;
	
	EstadoCivil(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
}
