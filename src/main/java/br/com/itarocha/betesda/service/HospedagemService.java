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
import br.com.itarocha.betesda.model.Hospedagem;
import br.com.itarocha.betesda.model.HospedagemFullVO;
import br.com.itarocha.betesda.model.HospedagemTipoServico;
import br.com.itarocha.betesda.model.HospedagemVO;
import br.com.itarocha.betesda.model.Hospede;
import br.com.itarocha.betesda.model.HospedeLeito;
import br.com.itarocha.betesda.model.HospedeLeitoVO;
import br.com.itarocha.betesda.model.HospedeVO;
import br.com.itarocha.betesda.model.Leito;
import br.com.itarocha.betesda.model.LeitoVO;
import br.com.itarocha.betesda.model.Pessoa;
import br.com.itarocha.betesda.model.Quarto;
import br.com.itarocha.betesda.model.TipoHospede;
import br.com.itarocha.betesda.model.TipoServico;
import br.com.itarocha.betesda.model.TipoUtilizacaoHospedagem;
import br.com.itarocha.betesda.model.hospedagem.Cell;
import br.com.itarocha.betesda.model.hospedagem.CellAndamento;
import br.com.itarocha.betesda.model.hospedagem.CellStatusHospedagem;
import br.com.itarocha.betesda.model.hospedagem.CellUtilizacao;
import br.com.itarocha.betesda.model.hospedagem.Celula;
import br.com.itarocha.betesda.model.hospedagem.HospedagemHeader;
import br.com.itarocha.betesda.model.hospedagem.LeitoOut;
import br.com.itarocha.betesda.model.hospedagem.MapaHospedagem;
import br.com.itarocha.betesda.repository.DestinacaoHospedagemRepository;
import br.com.itarocha.betesda.repository.HospedagemRepository;
import br.com.itarocha.betesda.repository.HospedagemTipoServicoRepository;
import br.com.itarocha.betesda.repository.HospedeLeitoRepository;
import br.com.itarocha.betesda.repository.HospedeRepository;
import br.com.itarocha.betesda.repository.LeitoRepository;
import br.com.itarocha.betesda.repository.PessoaRepository;
import br.com.itarocha.betesda.repository.QuartoRepository;
import br.com.itarocha.betesda.repository.TipoHospedeRepository;
import br.com.itarocha.betesda.repository.TipoServicoRepository;
import br.com.itarocha.betesda.utils.LocalDateUtils;
import br.com.itarocha.betesda.utils.StrUtil;

@Service
@Transactional /* todo (rollbackForClassName)*/
public class HospedagemService {

	private static final int QTD_DIAS = 7;
	
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
	
	@Autowired
	private TipoServicoRepository tipoServicoRepo;
	
	@Autowired
	private HospedagemTipoServicoRepository hospedagemTipoServicoRepo;

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
				
			    if ((hvo.getAcomodacao() != null) && (TipoUtilizacaoHospedagem.T.equals(hospedagem.getTipoUtilizacao())) ) {
			    	
			    	Optional<Quarto> quarto = quartoRepo.findById(hvo.getAcomodacao().getQuartoId());
			    	Optional<Leito> leito = leitoRepo.findById(hvo.getAcomodacao().getLeitoId());

			    	if (quarto.isPresent() && leito.isPresent()) {
			    		HospedeLeito hl = new HospedeLeito();
			    		hl.setHospede(h);
			    		hl.setDataEntrada(hospedagem.getDataEntrada());
			    		hl.setDataSaida(hospedagem.getDataPrevistaSaida());

			    		hvo.getAcomodacao().setQuartoNumero(quarto.get().getNumero());
			    		hl.setQuarto(quarto.get());

			    		hvo.getAcomodacao().setLeitoNumero(leito.get().getNumero());
			    		hl.setLeito(leito.get());

			    		hospedeLeitoRepo.save(hl);
			    		hvo.getAcomodacao().setId(hl.getId());
			    		
			    		h.getLeitos().add(hl);
			    	}
			    }
			    
			    if ((model.getServicos().length > 0) && (TipoUtilizacaoHospedagem.P.equals(hospedagem.getTipoUtilizacao())) ) {
			    	
			    	for (Long tipoServicoId : model.getServicos()) {
			    		Optional<TipoServico> ts = tipoServicoRepo.findById(tipoServicoId);
			    		if (ts.isPresent()) {
				    		HospedagemTipoServico servico = new HospedagemTipoServico();
				    		servico.setTipoServico(ts.get());
				    		servico.setHospedagem(hospedagem);
				    		hospedagemTipoServicoRepo.save(servico);
				    		hospedagem.getServicos().add(servico);
			    		}
			    	}
			    }	 
			    
			}
		} catch (Exception e) {
			throw e;
		}
		return hospedagem;
	}
	
	public MapaHospedagem getHospedagens(LocalDate dataBase) {
		try {
			LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
			LocalDate dFim = dIni.plusDays(QTD_DIAS - 1);
			
			// Monta lista de leitos 
			StringBuilder sbLeitos = StrUtil.loadFile("/sql/leitos.sql");
			TypedQuery<LeitoVO> qLeitos = em.createQuery(sbLeitos.toString(), LeitoVO.class);
			List<LeitoVO> leitos = qLeitos.getResultList();

			// Hóspedes parciais
			StringBuilder sbHospedeLeitosParciais = StrUtil.loadFile("/sql/hospedes_parciais.sql");
			TypedQuery<HospedeLeitoVO> qHospedeLeitoParciais = em.createQuery(sbHospedeLeitosParciais.toString(), HospedeLeitoVO.class)
					.setParameter("DATA_INI", dIni )
					.setParameter("DATA_FIM", dFim )
					.setParameter("TIPO_UTILIZACAO", TipoUtilizacaoHospedagem.P);
			List<HospedeLeitoVO> hospedesParciais = qHospedeLeitoParciais.getResultList();
			
			Map<String, Cell[]> mapaNew = gerarMapaNewVazio(leitos, hospedesParciais, QTD_DIAS);

			// Hóspedes totais
			StringBuilder sbHospedeLeitos = StrUtil.loadFile("/sql/hospede_leito.sql");
			TypedQuery<HospedeLeitoVO> qHospedeLeito = em.createQuery(sbHospedeLeitos.toString(), HospedeLeitoVO.class)
					.setParameter("DATA_INI", dIni )
					.setParameter("DATA_FIM", dFim );
			List<HospedeLeitoVO> listaHospedes = qHospedeLeito.getResultList();

			for (HospedeLeitoVO hospede: hospedesParciais) {
				listaHospedes.add(hospede);
			}
			
			List<HospedagemHeader> hospedagensHeaders = new ArrayList<HospedagemHeader>();

			LocalDate hoje = LocalDate.now();
			
			// Popula os leitos ocupados no mapa
			for(HospedeLeitoVO hl : listaHospedes) {
				Hospedagem hospedagem = hl.getHospedagem(); 
				Pessoa p = hl.getHospede().getPessoa(); 
				
				int celulaIndex = 0;
				LocalDate dtmp = dIni;
				
				CellStatusHospedagem statusHospedagem = resolveStatusHospedagemNew(hoje, hospedagem.getDataPrevistaSaida(), hospedagem.getDataEfetivaSaida());
				
				LocalDate dataEntrada = null;
				LocalDate dataSaida = null;
				String key = null;
				String identificador = null;
				Long id = 0L;
				Long hospedagemId = hospedagem.getId();
				CellUtilizacao utilizacao = CellUtilizacao.VAZIO;
				if ("T".equals(hl.getTipoUtilizacao())) {
					utilizacao = CellUtilizacao.TOTAL;
					dataEntrada = hl.getHospedeLeito().getDataEntrada();
					dataSaida = hl.getHospedeLeito().getDataSaida();
					Integer quartoNumero = hl.getHospedeLeito().getQuarto().getNumero();
					Integer leitoNumero = hl.getHospedeLeito().getLeito().getNumero();
					key = makeLeitoKey(hl.getHospedeLeito().getLeito().getId(), quartoNumero, leitoNumero);
					id = hl.getHospedeLeito().getId(); //*
					identificador =  String.format("T%06d", id); // T000000 Total
				} else {
					utilizacao = CellUtilizacao.PARCIAL;
					dataEntrada = hl.getHospedagem().getDataEntrada(); 
					dataSaida = hl.getHospedagem().getDataEfetivaSaida() == null ? hl.getHospedagem().getDataPrevistaSaida() : hl.getHospedagem().getDataEfetivaSaida(); 
					Integer quartoNumero = 9999;
					Integer leitoNumero = 9999;
					key = makeLeitoKey(hl.getHospede().getId(), quartoNumero, leitoNumero);
					id = hl.getHospede().getId();
					identificador =  String.format("P%06d", hl.getHospede().getId()); // P000000 Parcial
				}
				
				HospedagemHeader hh = new HospedagemHeader(identificador, id, hospedagem.getId(), p.getId(), p.getNome(), statusHospedagem);
				
				hh.setDataEntrada(hospedagem.getDataEntrada());
				hh.setDataEfetivaSaida(hospedagem.getDataEfetivaSaida());
				hh.setDataPrevistaSaida(hospedagem.getDataPrevistaSaida());
				hh.setTipoUtilizacao(hospedagem.getTipoUtilizacao()); //
				hh.setDestinacaoHospedagem(hospedagem.getDestinacaoHospedagem());
				
				hospedagensHeaders.add(hh);
				while (dtmp.compareTo(dFim) != 1) {
					CellAndamento andamento = CellAndamento.VAZIO;
					if (dataEntrada.compareTo(dtmp) == 0) {
						andamento = CellAndamento.INICIO;
					} else if (dataSaida.compareTo(dtmp) == 0) {
						andamento = CellAndamento.FIM;
					} else if (dtmp.isAfter(dataEntrada) && dtmp.isBefore(dataSaida)) {
						andamento = CellAndamento.DURANTE;
					}
					
					if (!CellAndamento.VAZIO.equals(andamento)) {
						//hh.getDias().add(new DiaHospedeLeito(celulaIndex, tipo));
						
						Cell[] xdias = mapaNew.get(key);
						xdias[celulaIndex].setId(identificador);
						xdias[celulaIndex].setHospedagemId(hospedagemId);
						xdias[celulaIndex].setAndamento(andamento);
						xdias[celulaIndex].setId(identificador);
						xdias[celulaIndex].setUtilizacao(utilizacao);
						xdias[celulaIndex].setStatus(statusHospedagem);
						
						if (hh.getFirstIndex() == -1) {
							hh.setFirstIndex(celulaIndex);
							xdias[celulaIndex].setFirstIndex(true);
						}
					}
					celulaIndex++;
					dtmp = dtmp.plusDays(1);
				}
			}
			
			MapaHospedagem mapaHospedagem = new MapaHospedagem(dIni, dFim);
			
			hospedagensHeaders.sort((a, b) -> a.getPessoaNome().compareTo(b.getPessoaNome()) );
			mapaHospedagem.setHospedagens(hospedagensHeaders);
			
			for (String key : mapaNew.keySet()) {
				Celula celula = new Celula(extractLeitoFromKey(key), mapaNew.get(key));
				mapaHospedagem.getCelulas().add(celula);
			}
			atualizarDashBoard(mapaHospedagem, leitos.size());
			
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
	
	private void atualizarDashBoard(MapaHospedagem mapaHospedagem, Integer qtdLeitos) {
		Integer[] qtdTotais 			= {0,0,0,0,0,0,0};
		Integer[] qtdVencidos 			= {0,0,0,0,0,0,0};
		Integer[] qtdPendentes 			= {0,0,0,0,0,0,0};
		Integer[] qtdEncerrados 		= {0,0,0,0,0,0,0};

		Integer[] qtdParciaisTotais		= {0,0,0,0,0,0,0};
		Integer[] qtdParciaisVencidos	= {0,0,0,0,0,0,0};
		Integer[] qtdParciaisPendentes 	= {0,0,0,0,0,0,0};
		Integer[] qtdParciaisEncerrados = {0,0,0,0,0,0,0};
		
		Integer[] qtdLeitosTotais		= {0,0,0,0,0,0,0};
		Integer[] qtdLeitosOcupados 	= {0,0,0,0,0,0,0};
		Integer[] qtdLeitosLivres 		= {0,0,0,0,0,0,0};

		for (Celula c : mapaHospedagem.getCelulas()) {
			for (int x = 0; x < QTD_DIAS; x++) {
				qtdLeitosTotais[x] = qtdLeitos;
			}
			for (int i = 0; i < QTD_DIAS; i++) {
				Cell cell = c.getCells()[i];
				if (!CellUtilizacao.VAZIO.equals(cell.getUtilizacao())) {
					qtdTotais[i]++;
				}
				if (CellUtilizacao.TOTAL.equals(cell.getUtilizacao())) {
					qtdLeitosOcupados[i]++;
				}
				if (CellUtilizacao.PARCIAL.equals(cell.getUtilizacao())) {
					qtdParciaisTotais[i]++;
					qtdParciaisPendentes[i] = qtdParciaisPendentes[i] + (CellStatusHospedagem.ABERTA.equals(cell.getStatus()) ? 1 : 0);
					qtdParciaisVencidos[i] = qtdParciaisVencidos[i] + (CellStatusHospedagem.VENCIDA.equals(cell.getStatus()) ? 1 : 0);
					qtdParciaisEncerrados[i] = qtdParciaisEncerrados[i] + (CellStatusHospedagem.ENCERRADA.equals(cell.getStatus()) ? 1 : 0);
				}
				qtdPendentes[i] = qtdPendentes[i] + (CellStatusHospedagem.ABERTA.equals(cell.getStatus()) ? 1 : 0);
				qtdVencidos[i] = qtdVencidos[i] + (CellStatusHospedagem.VENCIDA.equals(cell.getStatus()) ? 1 : 0);
				qtdEncerrados[i] = qtdEncerrados[i] + (CellStatusHospedagem.ENCERRADA.equals(cell.getStatus()) ? 1 : 0);
			}
		}
		
		mapaHospedagem.setQtdTotais(qtdTotais);
		mapaHospedagem.setQtdVencidos(qtdVencidos);
		mapaHospedagem.setQtdPendentes(qtdPendentes);
		mapaHospedagem.setQtdEncerrados(qtdEncerrados);

		mapaHospedagem.setQtdParciaisTotais(qtdParciaisTotais);
		mapaHospedagem.setQtdParciaisVencidos(qtdParciaisVencidos);
		mapaHospedagem.setQtdParciaisPendentes(qtdParciaisPendentes);
		mapaHospedagem.setQtdParciaisEncerrados(qtdParciaisEncerrados);
		
		mapaHospedagem.setQtdLeitosTotais(qtdLeitosTotais);
		mapaHospedagem.setQtdLeitosOcupados(qtdLeitosOcupados);
		for (int x = 0; x < QTD_DIAS; x++) {
			qtdLeitosLivres[x] = qtdLeitos - qtdLeitosOcupados[x];
		}
		mapaHospedagem.setQtdLeitosLivres(qtdLeitosLivres);
	}

	private CellStatusHospedagem resolveStatusHospedagemNew(LocalDate hoje, LocalDate dataPrevistaSaida, LocalDate dataEfetivaSaida) {
		CellStatusHospedagem status = CellStatusHospedagem.ABERTA;
		if (dataEfetivaSaida != null) {
			status = CellStatusHospedagem.ENCERRADA;
		} else if (dataPrevistaSaida.isBefore(hoje) ) {
			status = CellStatusHospedagem.VENCIDA;
		} else {
			status = CellStatusHospedagem.ABERTA;
		}
		return status;
	}

	public HospedagemFullVO getHospedagemPorHospedeLeitoId(Long hospedagemId) {
		Hospedagem h = hospedagemRepo.findHospedagemByHospedagemId(hospedagemId);
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
		
		CellStatusHospedagem status = resolveStatusHospedagemNew(LocalDate.now(), h.getDataPrevistaSaida(), h.getDataEfetivaSaida());
		retorno.setStatus(status);
		
		for (Hospede hospede: h.getHospedes()) {
			for (HospedeLeito hl : hospede.getLeitos()) {
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
	
	

	private LeitoOut extractLeitoFromKey(String key) {
		LeitoOut leito = new LeitoOut();
		leito.setKey("xxx");
		leito.setLeitoId(0L);
		leito.setQuartoNumero(0);
		leito.setLeitoNumero(0);
		String[] leitoArray = key.split("-");
		if (leitoArray.length == 3) {
			leito.setKey(key);
			leito.setQuartoNumero(Integer.parseInt(leitoArray[0]));
			leito.setLeitoNumero(Integer.parseInt(leitoArray[1]));
			leito.setLeitoId(Long.parseLong(leitoArray[2]));
		}
		return leito;
	}

	private Map<String, Cell[]> gerarMapaNewVazio(List<LeitoVO> leitos, List<HospedeLeitoVO> hospedesParciais, int numeroDias){
		Map<String, Cell[]> mapa = new HashMap<>();
		for(LeitoVO leito : leitos) {
			String key = makeLeitoKey(leito.getId(), leito.getQuartoNumero(), leito.getNumero()); 
			Cell[] cels = new Cell[numeroDias];
			for (int i = 0; i < numeroDias; i++) {
				cels[i] = new Cell();
			}
			mapa.put(key, cels);
		}

		for (HospedeLeitoVO hospede: hospedesParciais) {
			String key = makeLeitoKey(hospede.getHospede().getId(), 9999, 9999); 
			Cell[] cels = new Cell[numeroDias];
			for (int i = 0; i < numeroDias; i++) {
				cels[i] = new Cell();
			}
			mapa.put(key, cels);
		}
		
		Map<String, Cell[]> retorno = mapa.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		return retorno;
	}
	
	// quartoNumero - leitoNumero "000005-000007-000072" quarto 5, leito 7, leitoId = 72
	private String makeLeitoKey(Long leitoId, Integer quartoNumero, Integer leitoNumero) {
		return String.format("%06d-%06d-%06d", quartoNumero, leitoNumero, leitoId); 
	}
	
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
		
		return qtd <= 0; 
	}
	
}