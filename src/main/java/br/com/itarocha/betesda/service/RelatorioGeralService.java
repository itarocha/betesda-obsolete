package br.com.itarocha.betesda.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.itarocha.betesda.model.TipoUtilizacaoHospedagem;
import br.com.itarocha.betesda.report.ChaveValor;
import br.com.itarocha.betesda.report.CidadeQuantidade;
import br.com.itarocha.betesda.report.CidadeUF;
import br.com.itarocha.betesda.report.HospedePermanencia;
import br.com.itarocha.betesda.report.PessoaEstatistica;
import br.com.itarocha.betesda.report.PlanilhaGeral;
import br.com.itarocha.betesda.report.RelatorioAtendimentos;
import br.com.itarocha.betesda.utils.StrUtil;

@Service
@Transactional /* todo (rollbackForClassName)*/
public class RelatorioGeralService {

	@Autowired
	private EntityManager em;
	
	public RelatorioAtendimentos buildPlanilhaGeral(LocalDate dataIni, LocalDate dataFim){
		
		RelatorioAtendimentos relatorio = new RelatorioAtendimentos();
		
		// Hóspedagens Totais
		StringBuilder sbHPTotal = StrUtil.loadFile("/sql/hospede_permanencia_total.sql");
		TypedQuery<HospedePermanencia> qHPTotal = em.createQuery(sbHPTotal.toString(), HospedePermanencia.class)
				.setParameter("DATA_INI", dataIni )
				.setParameter("DATA_FIM", dataFim );
		List<HospedePermanencia> listaHPTotal = qHPTotal.getResultList();

		// Hóspedagens Totais
		StringBuilder sbHPParcial = StrUtil.loadFile("/sql/hospede_permanencia_parcial.sql");
		TypedQuery<HospedePermanencia> qHPParcial = em.createQuery(sbHPParcial.toString(), HospedePermanencia.class)
				.setParameter("DATA_INI", dataIni )
				.setParameter("DATA_FIM", dataFim )
				.setParameter("TIPO_UTILIZACAO", TipoUtilizacaoHospedagem.P);
		
		List<HospedePermanencia> listaHPParcial = qHPParcial.getResultList();
		
		listaHPTotal.addAll(listaHPParcial);
		
		
		List<HospedePermanencia> listaOutros = new ArrayList<>();
		
		Map<String, HospedePermanencia> map = new HashMap<>();
		
		for (HospedePermanencia hp : listaHPTotal) {
			
			String key = String.format("%06d-%06d", hp.getPessoaId(), hp.getHospedagemId());
			
			if (!map.containsKey(key)) {
				map.put(key, hp);
			} else {
				HospedePermanencia hpOld = map.get(key);
				long dias = java.time.temporal.ChronoUnit.DAYS.between(hpOld.getDataSaida(), hp.getDataEntrada());
				
				if (dias > 1) {
					listaOutros.add(hpOld);
					
					map.put(key, hp);
				} else {
					if (hpOld.getDataEntrada().isBefore(hp.getDataEntrada())) {
						hp.setDataEntrada(hpOld.getDataEntrada());
					}
					if (hpOld.getDataSaida().isAfter(hp.getDataSaida())) {
						hp.setDataSaida(hpOld.getDataSaida());
					}
					map.put(key, hp);
				}
			}
			
		}
		
		List<HospedePermanencia> lstHospedePerm = new ArrayList(map.values());
		
		for(HospedePermanencia o : listaOutros) {
			lstHospedePerm.add(o);
		}
		
		lstHospedePerm.sort( (a, b) -> {
			if(a.getPessoaId().equals(b.getPessoaId())) {
				return a.getDataEntrada().compareTo(b.getDataEntrada());
			}
			return a.getPessoaId().compareTo(b.getPessoaId());
		});
		
		for(HospedePermanencia o : lstHospedePerm) {
			if (o.getDataEntrada().isBefore(dataIni)) {
				o.setDataEntrada(dataIni);
			}
			if (o.getDataSaida().isAfter(dataFim)) {
				o.setDataSaida(dataFim);
			}
			
			Long dias = java.time.temporal.ChronoUnit.DAYS.between(o.getDataEntrada(), o.getDataSaida()) + 1;
			o.setDiasPermanencia(dias.intValue() );
			
		}
		
		List<PlanilhaGeral> planilha = lstHospedePerm.stream().map( hpd -> {
			PlanilhaGeral pg = new PlanilhaGeral();

			pg.setPessoaId(hpd.getPessoaId());
			pg.setPessoaNome(hpd.getPessoa().getNome());
			pg.setPessoaCPF(hpd.getPessoa().getCpf());
			pg.setPessoaRG(hpd.getPessoa().getRg());
			pg.setPessoaEndereco(hpd.getPessoa().getEndereco() == null ? "" : hpd.getPessoa().getEndereco().semCidadeToString());
			pg.setPessoaTelefone(hpd.getPessoa().getTelefone());
			pg.setPessoaDataNascimento(hpd.getPessoa().getDataNascimento());
			pg.setCidadeOrigem(new CidadeUF(hpd.getPessoa().getEndereco().getCidade(), hpd.getPessoa().getEndereco().getUf().toString()));
			pg.setDataEntrada(hpd.getDataEntrada());
			pg.setDataSaida(hpd.getDataSaida());
			pg.setDiasPermanencia(hpd.getDiasPermanencia());
			pg.setEncaminhadorId(hpd.getEncaminhadorId());
			pg.setEncaminhadorNome(hpd.getHospedagem().getEntidade() == null ? null : hpd.getHospedagem().getEntidade().getNome());
			pg.setHospedagemId(hpd.getHospedagemId());
			pg.setTipoUtilizacao(hpd.getTipoUtilizacao());
			pg.setTipoHospede(hpd.getHospede() == null ? null : hpd.getHospede().getTipoHospede().getDescricao() );
			
			
			int idade = Period.between(pg.getPessoaDataNascimento(), pg.getDataEntrada()).getYears();
			pg.setPessoaIdade(idade);
			
			return pg;
		}).collect(Collectors.toList());
		
		
		// Ranking de cidades
		Map<CidadeUF, Integer> mapaCidades = new TreeMap<>();
		Map<String, Integer> mapTipoUtilizacao = new HashMap<>();
		
		
		for (PlanilhaGeral p : planilha) {
			if (mapaCidades.containsKey(p.getCidadeOrigem())) {
				Integer dias = mapaCidades.get(p.getCidadeOrigem());
				mapaCidades.put(p.getCidadeOrigem(), p.getDiasPermanencia() + dias );
			} else {
				mapaCidades.put(p.getCidadeOrigem(), p.getDiasPermanencia());
			}
			
			
			if (mapTipoUtilizacao.containsKey(p.getTipoUtilizacaoDescricao())) {
				Integer dias = mapTipoUtilizacao.get(p.getTipoUtilizacaoDescricao());
				mapTipoUtilizacao.put(p.getTipoUtilizacaoDescricao(), p.getDiasPermanencia() + dias );
			} else {
				mapTipoUtilizacao.put(p.getTipoUtilizacaoDescricao(), p.getDiasPermanencia());
			}
			 
		}

		List<CidadeQuantidade> rankingCidades = mapaCidades.entrySet().stream().map(temp -> {
			return new CidadeQuantidade(temp.getKey().getCidade(), temp.getKey().getUf(), temp.getValue());
		}).collect(Collectors.toList());
		
		rankingCidades.sort((a, b) -> b.getQuantidade().compareTo(a.getQuantidade()));
		
		
		// Ranking de Encaminhadores
		Map<String, Integer> mapaEncaminhadores = new TreeMap<>();
		
		Integer totalAtendimentos = 0; 
		for (PlanilhaGeral p : planilha) {
			if (mapaEncaminhadores.containsKey(p.getEncaminhadorNome())) {
				Integer dias = mapaEncaminhadores.get(p.getEncaminhadorNome());
				mapaEncaminhadores.put(p.getEncaminhadorNome(), p.getDiasPermanencia() + dias );
			} else {
				mapaEncaminhadores.put(p.getEncaminhadorNome(), p.getDiasPermanencia());
			}
			totalAtendimentos = totalAtendimentos + p.getDiasPermanencia();
		}
		
		List<ChaveValor> rankingEncaminhamentos = mapaEncaminhadores.entrySet().stream().map(temp -> {
			return new ChaveValor(temp.getKey(), temp.getValue());
		}).collect(Collectors.toList());
		rankingEncaminhamentos.sort((a, b) -> b.getQuantidade().compareTo(a.getQuantidade()));
		
		// ********************* NOVOS
		
		Map<Long, PessoaEstatistica> mapaPessoas = new TreeMap<>();
		planilha.stream().forEach(h -> {

			PessoaEstatistica p = new PessoaEstatistica();
			p.setId(h.getPessoaId());
			p.setNome(h.getPessoaNome());
			p.setFaixaEtaria(h.getPessoaFaixaEtaria());
			p.setCidadeOrigem(h.getPessoaCidadeOrigem());
			p.setCidadeOrigemUf(h.getPessoaCidadeOrigemUF());
			p.setEncaminhadorNome(h.getEncaminhadorNome());
			p.setTipoUtilizacaoDescricao(h.getTipoUtilizacaoDescricao());
			p.setTipoHospede(h.getTipoHospede());
			
			mapaPessoas.put(h.getPessoaId(), p);
		});
		
		List<ChaveValor> lstPessoasAtendimentos = new ArrayList<>();
		lstPessoasAtendimentos.add(new ChaveValor("Atendimentos Realizados", totalAtendimentos));
		lstPessoasAtendimentos.add(new ChaveValor("Pessoas Atendidas", mapaPessoas.size()));
		relatorio.addAtividadeHospedagem("PESSOAS E ATENDIMENTOS", lstPessoasAtendimentos);
		
		
		// ATENDIMENTOS
		List<ChaveValor> lstTipoUtilizacao = this.transforToList(mapTipoUtilizacao, true);
		relatorio.addAtividadeHospedagem("ATENDIMENTOS", lstTipoUtilizacao);
		
		// TIPO HÓSPEDE
		List<ChaveValor> lstTiposHospedes = this.buildMapaTiposHospedes(mapaPessoas.values());
		relatorio.addAtividadeHospedagem("TIPOS DE ATENDIMENTOS", lstTiposHospedes);
		
		// PESSOAS ATENDIDAS POR FAIXA ETÁRIA
		List<ChaveValor> lstFaixaEtaria = this.buildMapaFaixaEtaria(mapaPessoas.values());
		relatorio.addAtividadeHospedagem("PESSOAS ATENDIDAS POR FAIXA ETÁRIA", lstFaixaEtaria);
		
		// ENCAMINHAMENTOS
		List<ChaveValor> lstEncaminhador = this.buildMapaEncaminhadores(mapaPessoas.values()); 
		relatorio.addAtividadeHospedagem("ENCAMINHAMENTOS", lstEncaminhador);

		// CIDADE DE ORIGEM
		List<ChaveValor> lstCidades = this.buildMapaCidades(mapaPessoas.values());
		relatorio.addAtividadeHospedagem("CIDADE DE ORIGEM", lstCidades);
		
		
		// ***************************** END NOVOS
		
		relatorio.setPlanilhaGeral(planilha);
		relatorio.setRankingCidades(rankingCidades);
		relatorio.setRankingEncaminhamentos(rankingEncaminhamentos);
		
		List<PessoaEstatistica> lstPessoas = new ArrayList<>();
		lstPessoas.addAll(mapaPessoas.values());
		lstPessoas.sort((a, b) -> {
			return a.getNome().compareTo(b.getNome());
		});
		relatorio.setListaPessoas(lstPessoas);
		
		return relatorio;
	}

	private List<ChaveValor> transforToList(Map<String, Integer> map, boolean byValue){
		List<ChaveValor> lst = map.entrySet().stream().map(temp -> {
			return new ChaveValor(temp.getKey(), temp.getValue() );
		}).collect(Collectors.toList());
		
		// Ordena inicialmente por chave
		lst.sort((a, b) -> a.getNome().compareTo(b.getNome()));
		
		if (byValue) {
			lst.sort((a, b) -> b.getQuantidade().compareTo(a.getQuantidade()));
		}
		
		return lst;
	}
	
	

	private List<ChaveValor> buildMapaFaixaEtaria(Collection<PessoaEstatistica> mapaPessoas) {
		Map<String, Integer> map = new HashMap<>();
		mapaPessoas.forEach(p -> {
			String faixaEtaria = p.getFaixaEtaria();
			if (map.containsKey(faixaEtaria)) {
				Integer qtd = map.get(faixaEtaria);
				map.put(faixaEtaria, ++qtd);
			} else {
				map.put(faixaEtaria, 1);
			}
		});
		
		return this.transforToList(map, true);
	}
	
	private List<ChaveValor> buildMapaEncaminhadores(Collection<PessoaEstatistica> mapaPessoas) {
		Map<String, Integer> map = new HashMap<>();
		mapaPessoas.forEach(p -> {
			String encaminhadorNome = p.getEncaminhadorNome();
			if (map.containsKey(encaminhadorNome)) {
				Integer qtd = map.get(encaminhadorNome);
				map.put(encaminhadorNome, ++qtd);
			} else {
				map.put(encaminhadorNome, 1);
			}
		});
		return this.transforToList(map, true);
	}
	
	private List<ChaveValor> buildMapaCidades(Collection<PessoaEstatistica> mapaPessoas) {
		Map<String, Integer> map = new HashMap<>();
		mapaPessoas.forEach(p -> {
			String cidade = p.getCidadeOrigem().concat(" - ").concat(p.getCidadeOrigemUf());
			if (map.containsKey(cidade)) {
				Integer qtd = map.get(cidade);
				map.put(cidade, ++qtd);
			} else {
				map.put(cidade, 1);
			}
		});
		return this.transforToList(map, true);
	}
	
	private List<ChaveValor> buildMapaTiposHospedes(Collection<PessoaEstatistica> mapaPessoas) {
		Map<String, Integer> map = new HashMap<>();
		mapaPessoas.forEach(p -> {
			String tipoHospede = p.getTipoHospede();
			if (map.containsKey(tipoHospede)) {
				Integer qtd = map.get(tipoHospede);
				map.put(tipoHospede, ++qtd);
			} else {
				map.put(tipoHospede, 1);
			}
		});
		return this.transforToList(map, true);
	}

	private List<ChaveValor> buildMapaTiposUtilizacao(Collection<PessoaEstatistica> mapaPessoas) {
		Map<String, Integer> map = new HashMap<>();
		mapaPessoas.forEach(p -> {
			String tipoUtilizacao = p.getTipoUtilizacaoDescricao();
			if (map.containsKey(tipoUtilizacao)) {
				Integer qtd = map.get(tipoUtilizacao);
				map.put(tipoUtilizacao, ++qtd);
			} else {
				map.put(tipoUtilizacao, 1);
			}
		});
		return this.transforToList(map, true);
	}

}
