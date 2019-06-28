package br.com.itarocha.betesda.report;

import java.util.List;

public class RelatorioAtendimentos {
	
	private List<PlanilhaGeral> planilhaGeral;
	private List<CidadeQuantidade> rankingCidades;
	private List<ChaveValor> rankingEncaminhamentos;
	
	public List<PlanilhaGeral> getPlanilhaGeral() {
		return planilhaGeral;
	}
	
	public void setPlanilhaGeral(List<PlanilhaGeral> planilhaGeral) {
		this.planilhaGeral = planilhaGeral;
	}
	
	public List<CidadeQuantidade> getRankingCidades() {
		return rankingCidades;
	}
	
	public void setRankingCidades(List<CidadeQuantidade> rankingCidades) {
		this.rankingCidades = rankingCidades;
	}
	
	public List<ChaveValor> getRankingEncaminhamentos() {
		return rankingEncaminhamentos;
	}
	
	public void setRankingEncaminhamentos(List<ChaveValor> rankingEncaminhamentos) {
		this.rankingEncaminhamentos = rankingEncaminhamentos;
	}

}
