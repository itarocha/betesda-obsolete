package br.com.itarocha.betesda.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RelatorioAtendimentos {
	
	private List<PlanilhaGeral> planilhaGeral;
	private List<CidadeQuantidade> rankingCidades;
	private List<ChaveValor> rankingEncaminhamentos;
	private List<AtividadeHospedagem> atividadesHospedagem;
	private List<PessoaEstatistica> listaPessoas;

	public RelatorioAtendimentos() {
		this.atividadesHospedagem = new ArrayList<>();
		this.listaPessoas = new ArrayList<>();
	}
	
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

	public void addAtividadeHospedagem(String titulo, List<ChaveValor> lista) {
		this.atividadesHospedagem.add(new AtividadeHospedagem(titulo, lista));
	}
	
	public List<AtividadeHospedagem> getAtividadesHospedagem() {
		return atividadesHospedagem;
	}

	public void setAtividadesHospedagem(List<AtividadeHospedagem> atividadesHospedagem) {
		this.atividadesHospedagem = atividadesHospedagem;
	}

	public Collection<PessoaEstatistica> getListaPessoas() {
		return listaPessoas;
	}
	
	public void setListaPessoas(List<PessoaEstatistica> listaPessoas) {
		
		/*
		listaPessoas.sort( (a, b) -> {
			if(a.getPessoaId().equals(b.getPessoaId())) {
				return a.getDataEntrada().compareTo(b.getDataEntrada());
			}
			return a.getPessoaId().compareTo(b.getPessoaId());
		});
	*/
		
		
		this.listaPessoas = listaPessoas;
	}
	
}
