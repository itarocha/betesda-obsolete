package br.com.itarocha.betesda.report;

import java.util.List;

public class AtividadeHospedagem {
	
	private String titulo;
	private List<ChaveValor> lista;
	
	public AtividadeHospedagem(String titulo, List<ChaveValor> lista) {
		this.titulo = titulo;
		this.lista = lista;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<ChaveValor> getLista() {
		return lista;
	}

	public void setLista(List<ChaveValor> lista) {
		this.lista = lista;
	}
	

}
