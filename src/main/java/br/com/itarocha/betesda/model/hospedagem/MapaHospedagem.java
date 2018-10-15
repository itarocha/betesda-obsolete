package br.com.itarocha.betesda.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

public class MapaHospedagem {

	private List<CelulaOut> celulas = new ArrayList<CelulaOut>();

	public List<CelulaOut> getCelulas() {
		return celulas;
	}

	public void setCelulas(List<CelulaOut> celulas) {
		this.celulas = celulas;
	}
	
	
}
