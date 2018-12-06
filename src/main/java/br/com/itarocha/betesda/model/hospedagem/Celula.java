package br.com.itarocha.betesda.model.hospedagem;

public class Celula {
	
	private LeitoOut leito;

	private Cell[] cells;
	
	public Celula(LeitoOut leito, Cell[] cells) {
		this.leito = leito;
		this.cells = cells;
	}
	
	public LeitoOut getLeito() {
		return leito;
	}
	
	public void setLeito(LeitoOut leito) {
		this.leito = leito;
	}
	
	public Cell[] getCells() {
		return cells;
	}
	
	public void setCells(Cell[] cells) {
		this.cells = cells;
	}
}
