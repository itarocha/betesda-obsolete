package br.com.itarocha.betesda.model.hospedagem;

public class Celula {
	
	private LeitoOut leito;
	private String[] dias;
	
	public Celula(LeitoOut leito, String[] dias) {
		this.leito = leito;
		this.dias = dias;
	}
	
	public LeitoOut getLeito() {
		return leito;
	}
	
	public void setLeito(LeitoOut leito) {
		this.leito = leito;
	}
	
	public String[] getDias() {
		return dias;
	}
	
	public void setDias(String[] dias) {
		this.dias = dias;
	}
	
}
