package br.com.itarocha.betesda.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.itarocha.betesda.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.model.Encaminhador;
import br.com.itarocha.betesda.model.Entidade;
import br.com.itarocha.betesda.model.Hospedagem;
import br.com.itarocha.betesda.model.HospedagemFullVO;
import br.com.itarocha.betesda.model.HospedagemVO;
import br.com.itarocha.betesda.model.Hospede;
import br.com.itarocha.betesda.model.HospedeHospedagemVO;
import br.com.itarocha.betesda.model.HospedeLeito;
import br.com.itarocha.betesda.model.HospedeLeitoVO;
import br.com.itarocha.betesda.model.HospedeVO;
import br.com.itarocha.betesda.model.Leito;
import br.com.itarocha.betesda.model.LeitoVO;
import br.com.itarocha.betesda.model.Pessoa;
import br.com.itarocha.betesda.model.Quarto;
import br.com.itarocha.betesda.model.TipoHospede;
import br.com.itarocha.betesda.model.TipoUtilizacaoHospedagem;
import br.com.itarocha.betesda.model.hospedagem.Celula;
import br.com.itarocha.betesda.model.hospedagem.HospedagemHeader;
//import br.com.itarocha.betesda.model.hospedagem.HospedagemInfo;
import br.com.itarocha.betesda.model.hospedagem.LeitoOut;
import br.com.itarocha.betesda.model.hospedagem.MapaHospedagem;
import br.com.itarocha.betesda.repository.DestinacaoHospedagemRepository;
import br.com.itarocha.betesda.repository.HospedagemRepository;
import br.com.itarocha.betesda.repository.HospedeLeitoRepository;
import br.com.itarocha.betesda.repository.HospedeRepository;
import br.com.itarocha.betesda.repository.LeitoRepository;
import br.com.itarocha.betesda.repository.PessoaRepository;
import br.com.itarocha.betesda.repository.QuartoRepository;
import br.com.itarocha.betesda.repository.TipoHospedeRepository;
import br.com.itarocha.betesda.utils.LocalDateUtils;
import br.com.itarocha.betesda.utils.StrUtil;

@Service
@Transactional
public class HospedagemService {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private DestinacaoHospedagemRepository destinacaoHospedagemRepo;
	
	@Autowired
	private HospedagemRepository hospedagemRepo;
	
	@Autowired
	private PessoaRepository pessoaRepo;
	
	@Autowired
	private TipoHospedeRepository tipoHospedeRepo;
	
	@Autowired
	private QuartoRepository quartoRepo;
	
	@Autowired
	private LeitoRepository leitoRepo;
	
	@Autowired
	private HospedeLeitoRepository hospedeLeitoRepo;
	
	@Autowired
	private HospedeRepository hospedeRepo;
	
	public Hospedagem create(HospedagemVO model) throws Exception {
		Hospedagem hospedagem = null;
		try {
			hospedagem = new Hospedagem();
			
			hospedagem.setDataEntrada(model.getDataEntrada());
			hospedagem.setDataPrevistaSaida(model.getDataPrevistaSaida());
			
			Optional<DestinacaoHospedagem> dest = destinacaoHospedagemRepo.findById(model.getDestinacaoHospedagemId());// em.find(DestinacaoHospedagem.class, model.getDestinacaoHospedagemId());
			hospedagem.setDestinacaoHospedagem(dest.get());
			model.setDestinacaoHospedagemDescricao(dest.get().getDescricao());
			
			TipoUtilizacaoHospedagem tu = TipoUtilizacaoHospedagem.valueOf(model.getTipoUtilizacao());
			hospedagem.setTipoUtilizacao(tu);
			hospedagem = hospedagemRepo.save(hospedagem);
			
			//em.persist(hospedagem);
			model.setId(hospedagem.getId()); 
			
			for (HospedeVO hvo: model.getHospedes()) {
				Hospede h = new Hospede();
				h.setHospedagem(hospedagem);
				
				Optional<Pessoa> p = pessoaRepo.findById(hvo.getPessoaId());
				// se p == null throw
				h.setPessoa(p.get());
				hvo.setPessoaId(p.get().getId());
				hvo.setPessoaNome(p.get().getNome());
				hvo.setPessoaDataNascimento(p.get().getDataNascimento());
				
				Optional<TipoHospede> th = tipoHospedeRepo.findById(hvo.getTipoHospedeId());
				h.setTipoHospede(th.get());
				hvo.setTipoHospedeDescricao(th.get().getDescricao());
				h = hospedeRepo.save(h);
				hvo.setId(h.getId());
				
				hospedagem.getHospedes().add(h);
				HospedeLeito hl = new HospedeLeito();
				hl.setHospede(h);
				hl.setDataEntrada(hospedagem.getDataEntrada());
				hl.setDataSaida(hospedagem.getDataPrevistaSaida());
				Optional<Quarto> quarto = quartoRepo.findById(hvo.getAcomodacao().getQuartoId());
				hvo.getAcomodacao().setQuartoNumero(quarto.get().getNumero());
				hl.setQuarto(quarto.get());
				Optional<Leito> leito = leitoRepo.findById(hvo.getAcomodacao().getLeitoId());
				hl.setLeito(leito.get());
				hvo.getAcomodacao().setLeitoNumero(leito.get().getNumero());
				
				hospedeLeitoRepo.save(hl);
				hvo.getAcomodacao().setId(hl.getId());
				
				h.getLeitos().add(hl);
			}
		} catch (Exception e) {
			throw e;
		}
		return hospedagem;
	}
	
	public MapaHospedagem getHospedagens(LocalDate dataBase) {
		Integer[] qtdTotais 			= {0,0,0,0,0,0,0};
		Integer[] qtdTotaisPendentes 	= {0,0,0,0,0,0,0};
		Integer[] qtdTotaisEncerradas 	= {0,0,0,0,0,0,0};

		// Parciais somente quando fizer o select dos sem leito
		Integer[] qtdParciais 			= {0,0,0,0,0,0,0};
		Integer[] qtdParciaisPendentes 	= {0,0,0,0,0,0,0};
		Integer[] qtdParciaisEncerradas = {0,0,0,0,0,0,0};
		
		Integer[] qtdLeitos 			= {0,0,0,0,0,0,0};
		Integer[] qtdLeitosOcupados 	= {0,0,0,0,0,0,0};
		Integer[] qtdLeitosLivres 		= {0,0,0,0,0,0,0};
		
		try {
			LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
			LocalDate dFim = dIni.plusDays(6);
			LocalDate hoje = LocalDate.now();
			/*
			System.out.println("-----------------------------");
			System.out.println(dIni + " até " + dFim);
			*/
			
			// Monta lista de leitos 
			StringBuilder sbLeitos = StrUtil.loadFile("/sql/leitos.sql");
			TypedQuery<LeitoVO> qLeitos = em.createQuery(sbLeitos.toString(), LeitoVO.class);
			List<LeitoVO> leitos = qLeitos.getResultList();
			for (int x = 0; x < 7; x++) {
				qtdLeitos[x] = leitos.size();
				qtdLeitosLivres[x] = leitos.size();
			}
			
			Map<String, Long[]> mapaLeitosNew = gerarMapaLeitosNewVazio(leitos, 7);

			StringBuilder sbHospedes = StrUtil.loadFile("/sql/hospedes_por_periodo.sql");
			TypedQuery<HospedeHospedagemVO> qHospedes = em.createQuery(sbHospedes.toString(), HospedeHospedagemVO.class)
															.setParameter("DATA_INI", dIni )
															.setParameter("DATA_FIM", dFim );
			List<HospedeHospedagemVO> hospedes = qHospedes.getResultList();
			/*
			System.out.println("-----------------------------");
			for (HospedeHospedagemVO h : hospedes) {
				System.out.println(h.getHospede().getPessoa().getNome() + " - hospedagemId = " + h.getHospedagem().getId() + " hospedeId = " + h.getHospede().getId() + " " + h.getHospedagem().getDataEntrada() + " até " + h.getHospedagem().getDataPrevistaSaida());
			}
			System.out.println("-----------------------------");
			*/
			// Monta lista de HospedeLeitoVO
			StringBuilder sbHospedeLeitos = StrUtil.loadFile("/sql/hospede_leito.sql");
			TypedQuery<HospedeLeitoVO> qHospedeLeito = em.createQuery(sbHospedeLeitos.toString(), HospedeLeitoVO.class)
					.setParameter("DATA_INI", dIni )
					.setParameter("DATA_FIM", dFim );
			List<HospedeLeitoVO> hospedeLeitos = qHospedeLeito.getResultList();
			//qtdLeitosOcupados = hospedeLeitos.size();
			//qtdLeitosLivres = qtdLeitos - qtdLeitosOcupados;
			
			List<HospedagemHeader> hospedagensHeaders = new ArrayList<HospedagemHeader>();

			// Popula os leitos ocupados no mapa
			for(HospedeLeitoVO hl : hospedeLeitos) {
				HospedeLeito hospedeLeito = hl.getHospedeLeito();
				Hospedagem hospedagem = hl.getHospedagem();
				Pessoa p = hl.getHospede().getPessoa(); 
				
				LocalDate dataEntrada = hl.getHospedeLeito().getDataEntrada(); 
				LocalDate dataSaida = hl.getHospedeLeito().getDataSaida(); 
				int celulaIndex = 0;
				LocalDate dtmp = dIni;
				
				String status = "aberta";
				if (hospedagem.getDataEfetivaSaida() != null) {
					status = "encerrada";
				} else if (hospedagem.getDataPrevistaSaida().isBefore(hoje) ) {
					status = "vencida";
				} else {
					status = "aberta";
				}
				
				HospedagemHeader hh = new HospedagemHeader(hospedeLeito.getId(), hospedagem.getId(), p.getId(), p.getNome(), status);
				hospedagensHeaders.add(hh);
				
				String key = makeLeitoKey(hl.getHospedeLeito().getLeito().getId(), hl.getHospedeLeito().getQuarto().getNumero(), hl.getHospedeLeito().getLeito().getNumero());
				while (dtmp.compareTo(dFim) != 1) {
					String tipo = "";
					if (dataEntrada.compareTo(dtmp) == 0) {
						tipo = "inicio";
					} else if (dataSaida.compareTo(dtmp) == 0) {
						tipo = "fim";
					} else if (dtmp.isAfter(dataEntrada) && dtmp.isBefore(dataSaida)) {
						tipo = "durante";
					}
					
					if (!"".equals(tipo)) {
						hh.getDias().add(new DiaHospedeLeito(celulaIndex, tipo));
						Long[] idias =  mapaLeitosNew.get(key);
						idias[celulaIndex] = hl.getHospedeLeito().getId();
						if (hh.getFirstIndex() == -1) {
							hh.setFirstIndex(celulaIndex);
						}

						qtdTotais[celulaIndex]++;
						qtdLeitosLivres[celulaIndex]--;
						if ("encerrada".equals(status)) {
							qtdTotaisEncerradas[celulaIndex]++;
						} else {
							qtdTotaisPendentes[celulaIndex]++;
						}
					}
					qtdLeitosOcupados[celulaIndex] = qtdTotais[celulaIndex];
					//qtdLeitosLivres[celulaIndex] = qtdLeitos[celulaIndex] - qtdLeitosOcupados[celulaIndex];
					celulaIndex++;
					dtmp = dtmp.plusDays(1);
				}
			}

			/*
			for (String s : mapaLeitosNew.keySet()) {
				Long[] dias = mapaLeitosNew.get(s);
				System.out.print(s + " ");
				for(Long i : dias) {
					System.out.print(String.format("%03d ",i));
				}
				System.out.println("");
			}
			*/
			
			MapaHospedagem mapaHospedagem = new MapaHospedagem(dIni, dFim);
			mapaHospedagem.setHospedagens(hospedagensHeaders);
			mapaHospedagem.setHospedes(hospedes);
			
			mapaHospedagem.setQtdTotais(qtdTotais);
			mapaHospedagem.setQtdTotaisPendentes(qtdTotaisPendentes);
			mapaHospedagem.setQtdTotaisEncerradas(qtdTotaisEncerradas);

			// Parciais somente quando fizer o select dos sem leito
			mapaHospedagem.setQtdParciais(qtdParciais);
			mapaHospedagem.setQtdParciaisPendentes(qtdParciaisPendentes);
			mapaHospedagem.setQtdParciaisEncerradas(qtdParciaisEncerradas);
			
			mapaHospedagem.setQtdLeitos(qtdLeitos);
			mapaHospedagem.setQtdLeitosOcupados(qtdLeitosOcupados);
			mapaHospedagem.setQtdLeitosLivres(qtdLeitosLivres);

			/*
			for (String key : mapaLeitos.keySet()) {
				CelulaOut celula = new CelulaOut(extractLeitoFromKey(key));
			
				Dia[] dias = mapaLeitos.get(key);
				for (Dia d : dias) {
					celula.getDias().add(d);
				}
				mapaHospedagem.getCelulas().add(celula);
			}
			*/
			for (String key : mapaLeitosNew.keySet()) {
				Celula celula = new Celula(extractLeitoFromKey(key), mapaLeitosNew.get(key));
				mapaHospedagem.getCelulas().add(celula);
			}
			
			// Dias
			LocalDate dtmp = dIni;
			while (dtmp.compareTo(dFim) != 1) {
				mapaHospedagem.getDias().add(dtmp);
				dtmp = dtmp.plusDays(1);
			}
			return mapaHospedagem;
			
		} finally {
		}
	}

	public HospedagemFullVO getHospedagemPorHospedeLeitoId(Long hospedeLeitoId) {
		Hospedagem h = hospedagemRepo.findHospedagemByHospedeLeitoId(hospedeLeitoId);
		HospedagemFullVO retorno = new HospedagemFullVO();
		
		retorno.setId(h.getId());
		retorno.setEntidade(h.getEntidade());
		retorno.setEncaminhador(h.getEncaminhador());
		retorno.setDestinacaoHospedagem(h.getDestinacaoHospedagem());
		retorno.setDataEntrada(h.getDataEntrada());
		retorno.setDataPrevistaSaida(h.getDataPrevistaSaida());
		retorno.setDataEfetivaSaida(h.getDataEfetivaSaida());
		retorno.setTipoUtilizacao(h.getTipoUtilizacao());
		retorno.setObservacoes(h.getObservacoes());
		retorno.setHospedes(h.getHospedes());
		
		
		StringBuilder sbLeito = StrUtil.loadFile("/sql/leito_by_hospede_leito_id.sql");
		TypedQuery<LeitoVO> qLeitos = em.createQuery(sbLeito.toString(), LeitoVO.class);

		
		for (Hospede hospede: h.getHospedes()) {
			for (HospedeLeito hl : hospede.getLeitos()) {

				
				// buscar no banco...
				
				LeitoVO leito = qLeitos.setParameter("id", hl.getId()) .getSingleResult();

				hl.setQuartoNumero( leito.getQuartoNumero() );
				hl.setLeitoNumero( leito.getNumero() );
			}
		}
		return retorno;
	}
	
	
	//TODO Implementar transferencia
	public void transferirHospedagem(Long hospedagemId, Long leitoId, LocalDate dataApartir) {
		/*
		* hospedagem = getHospedagem(hospedagemId)
		* Condição: se hospedagem.status == aberta
		* Condição: Verificar se não existe hospedagemLeito em (leito, dataApartir, hospedagem.dataPrevistaSaida) !!!
		* hospedagemLeito = getUltimoHospedagemLeito(hospedagemId)
		* hospedagemLeito.setDataSaída(dataApartir -1)
		* Cria um novo HospedeLeito com hospedagemId, leito, dataApartir e dataSaida = hospedagem.dataPrevistaSaida
		*/
		
	}
	
	//TODO Implementar encerrarHospedagem
	public void encerrarHospedagem(Long hospedagemId, LocalDate dataEncerramento) {
		/*
		* hospedagem = getHospedagem(hospedagemId)
		* Condição: se hospedagem.status == aberta
		* Condição: dataEncerramento >= hospedagem.dataEntrada
		* hospedagemLeito = getUltimoHospedagemLeito(hospedagemId)
		* hospedagemLeito.setDataSaída(dataEncerramento)
		* hospedagem.setDataPrevistaSaida(dataEncerramento)
		*/
		Optional<Hospedagem> opt  = hospedagemRepo.findById(hospedagemId);
		if (opt.isPresent()) {
			
			Hospedagem h = opt.get();
			if ((h.getDataEfetivaSaida() != null)) {
				System.out.println("Erro. Hospedagem deve ter status = emAberto"); 
				return;
			}
			
			if (dataEncerramento.isBefore(h.getDataEntrada())) {
				System.out.println("Erro. Data de encerramento deve ser superior a data de entrada"); 
				return;
			}

			List<HospedeLeito> listaHospedeLeito = hospedeLeitoRepo.findUltimoByHospedagemId(hospedagemId);
			for (HospedeLeito hl : listaHospedeLeito) {
				// System.out.println("hospede.Id = " + hl.getHospede().getId() + " Id = " + hl.getId() + " dataEntrada = "+hl.getDataEntrada());
				hl.setDataSaida(dataEncerramento);
				hospedeLeitoRepo.save(hl);
			}
			
			h.setDataEfetivaSaida(dataEncerramento);
			hospedagemRepo.save(h);
		}
	} 
	
	//TODO Implementar renovarHospedagem
	public void renovarHospedagem(LocalDate novaDataPrevistaSaida) {
		/*
		* Somente se hospedagem.status == aberta
		* hospedagem = getHospedagem(hospedagemId)
		* Condição: novaDataPrevistaSaida > hospedagem.dataPrevistaSaida
		* hospedagemLeito = getUltimoHospedagemLeito(hospedagemId)
		* Condição: Verificar se não existe hospedagemLeito em (hospedagemLeito.leito, hospedagem.dataPrevistaSaida + 1, novaDataPrevistaSaida) !!!
		* hospedagemLeito.setDataSaída(novaDataPrevistaSaida)
		* hospedagem.setDataPrevistaSaida(novaDataPrevistaSaida)
		 */
		
	}
	
	//TODO Implementar createNaoAtendimento
	public void createNaoAtendimento(Long hospedagemId, LocalDate dataNaoAtendimento) {
		
	}
	
	//TODO Implementar removeNaoAtendimento
	public void removeNaoAtendimento(Long hospedagemId, Long naoAtendimentoId) {
		
	}
	
	
	// quartoNumero - leitoNumero "000005-000007-000072" quarto 5, leito 7, leitoId = 72
	private String makeLeitoKey(Long leitoId, Integer quartoNumero, Integer leitoNumero) {
		return String.format("%06d-%06d-%06d", quartoNumero, leitoNumero, leitoId); 
	}

	private LeitoOut extractLeitoFromKey(String key) {
		LeitoOut leito = new LeitoOut();
		leito.setLeitoId(0L);
		leito.setQuartoNumero(0);
		leito.setLeitoNumero(0);
		String[] leitoArray = key.split("-");
		if (leitoArray.length == 3) {
			leito.setQuartoNumero(Integer.parseInt(leitoArray[0]));
			leito.setLeitoNumero(Integer.parseInt(leitoArray[1]));
			leito.setLeitoId(Long.parseLong(leitoArray[2]));
		}
		return leito;
	}

	private Map<String, Long[]> gerarMapaLeitosNewVazio(List<LeitoVO> leitos, int numeroDias){
		Map<String, Long[]> mapa = new HashMap<>();
		for(LeitoVO leito : leitos) {
			String key = makeLeitoKey(leito.getId(), leito.getQuartoNumero(), leito.getNumero()); 
			Long[] cels = new Long[numeroDias];
			for (int i = 0; i < numeroDias; i++) {
				cels[i] = 0L;
			}
			mapa.put(key, cels);
		}
		Map<String, Long[]> retorno = mapa.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		return retorno;
	}
	/*
	private Map<String, Dia[]> gerarMapaLeitosVazio(List<LeitoVO> leitos, int numeroDias){
		Map<String, Dia[]> mapa = new HashMap<>();
		for(LeitoVO leito : leitos) {
			String key = makeLeitoKey(leito.getQuartoNumero(), leito.getNumero()); 
			Dia[] cels = new Dia[numeroDias];
			for (int i = 0; i < numeroDias; i++) {
				cels[i] = new Dia();
			}
			mapa.put(key, cels);
		}
		Map<String, Dia[]> retorno = mapa.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		return retorno;
	}
	*/
	
	public boolean leitoLivreNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(*) "); 
		sb.append("FROM   hospede_leito hl "); 
		sb.append("WHERE  hl.leito_id = :leitoId  "); 
		sb.append("AND    (((hl.data_entrada BETWEEN :dataIni AND :dataFim) OR (hl.data_saida BETWEEN :dataIni AND :dataFim))  "); 
		sb.append("OR     ((hl.data_entrada <= :dataIni) AND (hl.data_saida >= :dataFim)))  ");
		
		Query query = em.createNativeQuery(sb.toString());
		Long qtd = ((Number)query.setParameter("leitoId", leitoId)
								 .setParameter("dataIni", dataIni)
								 .setParameter("dataFim", dataFim)
								 .getSingleResult()).longValue();
		
		//int count = ((Number)qtd.get[0]).intValue();
		//System.out.println(qtd);
		return qtd <= 0; 
	}

	public boolean pessoaLivre(Long pessoaId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(*) ");
		sb.append("FROM   hospede h ");
		sb.append("INNER JOIN hospedagem hpd ");
		sb.append("ON    hpd.id = h.hospedagem_id ");
		sb.append("WHERE pessoa_id = :pessoaId ");
		sb.append("AND   hpd.data_efetiva_saida is null");
		
		Query query = em.createNativeQuery(sb.toString());
		Long qtd = ((Number)query.setParameter("pessoaId", pessoaId)
								 .getSingleResult()).longValue();
		
		//System.out.println("pessoaLivre.qtd = "+qtd);
		return qtd <= 0; 
	}
	
	
/*
  
	private Map<String, Dia[]> gerarMapa(LocalDate dIni, Integer numeroDias, List<LeitoVO> leitos){
		
		Map<String, Dia[]> mapa = new HashMap<>();
		LocalDate dFim = dIni.plusDays(numeroDias-1);
		
		for(LeitoVO leito : leitos) {
			Dia[] cels = new Dia[numeroDias];
			String key = makeLeitoKey(leito.getQuartoNumero(), leito.getNumero()); 

			LocalDate dtmp = dIni;
			int index = 0;
			while (dtmp.compareTo(dFim) != 1) {
				Dia cel = new Dia();
				cels[index] = cel;
				dtmp = dtmp.plusDays(1);
				index++;
			}
			mapa.put(key, cels);
		}
		Map<String, Dia[]> retorno = mapa.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		return retorno;
	}
 
  	
 */
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	private static class HospedagemResult {
		public BigInteger hospedagemId;
		public BigInteger destinacaoHospedagemId;
		public String tipoUtilizacao;
		public LocalDate dataEntrada;
		public LocalDate dataPrevistaSaida;
		public LocalDate dataEfetivaSaida;
		public LocalDate dataSaida;
		
		public HospedagemResult(BigInteger hospedagemId, BigInteger destinacaoHospedagemId, String tipoUtiliacao, LocalDate dataEntrada, 
				LocalDate dataPrevistaSaida, LocalDate dataEfetivaSaida, LocalDate dataSaida ){
			this.hospedagemId = hospedagemId;
			this.destinacaoHospedagemId = destinacaoHospedagemId;
			this.tipoUtilizacao = tipoUtiliacao;
			this.dataEntrada = dataEntrada;
			this.dataPrevistaSaida = dataPrevistaSaida;
			this.dataEfetivaSaida = dataEfetivaSaida;
			this.dataSaida = dataSaida;
		}
	}
	
	private static class LeitoResult {
		public BigInteger hospedagemId;
		public BigInteger hospedeId;
		public BigInteger pessoaId;
        public LocalDate dataEntrada;
        public LocalDate dataSaida;
        public BigInteger quartoId;
        public Integer quartoNumero;
        public BigInteger leitoId;
        public Integer leitoNumero;

        public LeitoResult(BigInteger hospedagemId, BigInteger hospedeId, BigInteger pessoaId, LocalDate dataEntrada, LocalDate dataSaida, 
        		BigInteger quartoId, Integer quartoNumero, BigInteger leitoId, Integer leitoNumero) {

        	this.hospedagemId = hospedagemId;
    		this.hospedeId = hospedeId;
    		this.pessoaId = pessoaId;
    		this.dataEntrada = dataEntrada;
    		this.dataSaida = dataSaida;
    		this.quartoId = quartoId;
    		this.quartoNumero = quartoNumero;
    		this.leitoId = leitoId;
    		this.leitoNumero = leitoNumero;
        }
	}
	
	private static class HospedeResult {
		public BigInteger hospedagemId;
		public BigInteger pessoaId;
        public String nome;
        public BigInteger tipoHospedeId;
        public String descricao;

        public HospedeResult(BigInteger hospedagemId, BigInteger pessoaId, String nome, BigInteger tipoHospedeId, String descricao) {
        	this.hospedagemId = hospedagemId; 
        	this.pessoaId = pessoaId;
        	this.nome = nome;
        	this.tipoHospedeId = tipoHospedeId;
        	this.descricao = descricao;
        }
	}
	*/
}



/*
SELECT      hl.quarto_id
, q.numero quarto_numero
, hl.leito_id
, l.numero leito_numero
, h.id hospedagem_id
, h.id hospede_id  
, h.destinacao_hospedagem_id
, dh.descricao destinacao_hospedagem_descricao
, h.tipo_utilizacao
, h.data_entrada
, h.data_prevista_saida
, h.data_efetiva_saida
, COALESCE(h.data_efetiva_saida, h.data_prevista_saida) data_saida
, hpd.pessoa_id
, hl.data_entrada data_entrada_leito
, hl.data_saida data_saida_leito
FROM        hospedagem h
INNER JOIN  hospede hpd
ON          hpd.hospedagem_id = h.id
INNER JOIN  hospede_leito hl
ON          hl.hospede_id = hpd.id
INNER JOIN  quarto q
ON          q.id = hl.quarto_id
INNER JOIN  leito l
ON          l.id = hl.leito_id
INNER JOIN  destinacao_hospedagem dh
ON          dh.id = h.destinacao_hospedagem_id
WHERE       ('2018-08-12' BETWEEN hl.data_entrada AND hl.data_saida 
OR           '2018-08-18' BETWEEN hl.data_entrada AND hl.data_saida)
ORDER BY    q.numero
, l.numero
*/


