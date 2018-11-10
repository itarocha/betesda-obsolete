package br.com.itarocha.betesda.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

public class CelulaOut {
	
	private LeitoOut leito;
	private List<Dia> dias = new ArrayList<Dia>();
	
	public CelulaOut(LeitoOut leito) {
		this.leito = leito;
	}
	
	public LeitoOut getLeito() {
		return leito;
	}
	public void setLeito(LeitoOut leito) {
		this.leito = leito;
	}
	public List<Dia> getDias() {
		return dias;
	}
	public void setDias(List<Dia> dias) {
		this.dias = dias;
	}
	
}
