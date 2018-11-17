package br.com.itarocha.betesda.model.hospedagem;

public class Celula {
	
	private LeitoOut leito;
	private Long[] dias;
	
	public Celula(LeitoOut leito, Long[] dias) {
		this.leito = leito;
		this.dias = dias;
	}
	
	public LeitoOut getLeito() {
		return leito;
	}
	public void setLeito(LeitoOut leito) {
		this.leito = leito;
	}
	public Long[] getDias() {
		return dias;
	}
	public void setDias(Long[] dias) {
		this.dias = dias;
	}
	
}
