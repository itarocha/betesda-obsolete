package br.com.itarocha.betesda.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.model.Encaminhador;
import br.com.itarocha.betesda.model.Entidade;
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
import br.com.itarocha.betesda.model.Logico;
import br.com.itarocha.betesda.model.Pessoa;
import br.com.itarocha.betesda.model.Quarto;
import br.com.itarocha.betesda.model.TipoHospede;
import br.com.itarocha.betesda.model.TipoServico;
import br.com.itarocha.betesda.model.TipoUtilizacaoHospedagem;
import br.com.itarocha.betesda.model.hospedagem.CellAndamento;
import br.com.itarocha.betesda.model.hospedagem.CellStatusHospedagem;
import br.com.itarocha.betesda.model.hospedagem.CellUtilizacao;
import br.com.itarocha.betesda.model.hospedagem.DiaHospedagem;
import br.com.itarocha.betesda.model.hospedagem.HospedagemHeaderInfo;
import br.com.itarocha.betesda.model.hospedagem.HospedagemMapa;
import br.com.itarocha.betesda.model.hospedagem.LeitoHeader;
import br.com.itarocha.betesda.model.hospedagem.MapaRetorno;
import br.com.itarocha.betesda.repository.DestinacaoHospedagemRepository;
import br.com.itarocha.betesda.repository.EncaminhadorRepository;
import br.com.itarocha.betesda.repository.EntidadeRepository;
import br.com.itarocha.betesda.repository.HospedagemRepository;
import br.com.itarocha.betesda.repository.HospedagemTipoServicoRepository;
import br.com.itarocha.betesda.repository.HospedeLeitoRepository;
import br.com.itarocha.betesda.repository.HospedeRepository;
import br.com.itarocha.betesda.repository.LeitoRepository;
import br.com.itarocha.betesda.repository.PessoaRepository;
import br.com.itarocha.betesda.repository.QuartoRepository;
import br.com.itarocha.betesda.repository.TipoHospedeRepository;
import br.com.itarocha.betesda.repository.TipoServicoRepository;
import br.com.itarocha.betesda.util.validation.ResultError;
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
	private EntidadeRepository entidadeRepo;
	
	@Autowired
	private EncaminhadorRepository encaminhadorRepo;
	
	@Autowired
	private HospedagemTipoServicoRepository hospedagemTipoServicoRepo;

	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public Hospedagem create(HospedagemVO model) throws ValidationException {
		Hospedagem hospedagem = null;

		LocalDate hoje = LocalDate.now();
			
		if (model.getDataEntrada().isAfter(hoje)) {
			throw new ValidationException(new ResultError().addError("*", 
					String.format("Data de Entrada não pode ser superior a data atual (%s)",fmt.format(hoje))));
		}
		
		if (model.getDataPrevistaSaida().isBefore(model.getDataEntrada())) {
			throw new ValidationException(new ResultError().addError("*", 
					String.format("Data Prevista de Saída não pode ser inferior a Data de Entrada (%s)",fmt.format(model.getDataEntrada()))));
		}
		
		hospedagem = new Hospedagem();
		
		Optional<Entidade> entidade = entidadeRepo.findById(model.getEntidadeId());
		hospedagem.setEntidade(entidade.get());
		model.setEntidade(entidade.get());
		
		Optional<Encaminhador> encaminhador = encaminhadorRepo.findById(model.getEncaminhadorId());
		hospedagem.setEncaminhador(encaminhador.get());
		model.setEncaminhador(encaminhador.get());
		
		hospedagem.setDataEntrada(model.getDataEntrada());
		hospedagem.setDataPrevistaSaida(model.getDataPrevistaSaida());
		
		Optional<DestinacaoHospedagem> dest = destinacaoHospedagemRepo.findById(model.getDestinacaoHospedagemId());
		hospedagem.setDestinacaoHospedagem(dest.get());
		model.setDestinacaoHospedagemDescricao(dest.get().getDescricao());
		
		TipoUtilizacaoHospedagem tu = TipoUtilizacaoHospedagem.valueOf(model.getTipoUtilizacao());
		hospedagem.setTipoUtilizacao(tu);
		hospedagem.setObservacoes(model.getObservacoes());
		
		hospedagem = hospedagemRepo.save(hospedagem);
		
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
		    	//TODO: Tem um código igual no transferir. Refatorar criar método
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
		return hospedagem;
	}
	
	public MapaRetorno buildMapaRetorno(LocalDate dataBase) {
		MapaRetorno retorno = new MapaRetorno();

		LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
		LocalDate dFim = dIni.plusDays(QTD_DIAS - 1);
		
		retorno.setDataIni(dIni);
		retorno.setDataFim(dFim);
		
		// Monta lista de leitos 
		StringBuilder sbLeitos = StrUtil.loadFile("/sql/leitos_header.sql");
		TypedQuery<LeitoHeader> qLeitos = em.createQuery(sbLeitos.toString(), LeitoHeader.class);
		List<LeitoHeader> leitos =  qLeitos.getResultList();
		
		// Hóspedes totais
		StringBuilder sbHospedeLeitos = StrUtil.loadFile("/sql/hospede_leito.sql");
		TypedQuery<HospedeLeitoVO> qHospedeLeito = em.createQuery(sbHospedeLeitos.toString(), HospedeLeitoVO.class)
				.setParameter("DATA_INI", dIni )
				.setParameter("DATA_FIM", dFim );
		List<HospedeLeitoVO> listaHospedes = qHospedeLeito.getResultList();
		
		// Hóspedes parciais
		StringBuilder sbHospedeLeitosParciais = StrUtil.loadFile("/sql/hospedes_parciais.sql");
		TypedQuery<HospedeLeitoVO> qHospedeLeitoParciais = em.createQuery(sbHospedeLeitosParciais.toString(), HospedeLeitoVO.class)
				.setParameter("DATA_INI", dIni )
				.setParameter("DATA_FIM", dFim )
				.setParameter("TIPO_UTILIZACAO", TipoUtilizacaoHospedagem.P);
		List<HospedeLeitoVO> hospedesParciais = qHospedeLeitoParciais.getResultList();
		 
		retorno.setLeitos(gerarListaLeitos(leitos, hospedesParciais));		
		
		for (HospedeLeitoVO hospede: hospedesParciais) {
			listaHospedes.add(hospede);
		}
		
		LocalDate hoje = LocalDate.now();

		// Popula os leitos ocupados no mapa
		for(HospedeLeitoVO hl : listaHospedes) {
			Hospedagem hospedagem = hl.getHospedagem(); 
			Pessoa p = hl.getHospede().getPessoa(); 
			
			int celulaIndex = 0;
			LocalDate dtmp = dIni;
			
			CellStatusHospedagem statusHospedagem = resolveStatusHospedagemNew(hoje, hospedagem.getDataPrevistaSaida(), hospedagem.getDataEfetivaSaida());
			
			LocalDate dataEntradaHospedagem = hospedagem.getDataEntrada();
			LocalDate dataSaidaHospedagem = hospedagem.getDataEfetivaSaida() == null ? hospedagem.getDataPrevistaSaida() : hospedagem.getDataEfetivaSaida(); 
			
			LocalDate dataEntradaLeito = null;
			LocalDate dataSaidaLeito = null;
			String key = null;
			String identificador = null;
			Long id = 0L;
			//Long hospedagemId = hospedagem.getId();
			CellUtilizacao utilizacao = CellUtilizacao.VAZIO;
			if ("T".equals(hl.getTipoUtilizacao())) {
				utilizacao = CellUtilizacao.TOTAL;
				dataEntradaLeito = hl.getHospedeLeito().getDataEntrada();
				dataSaidaLeito = hl.getHospedeLeito().getDataSaida();
				Integer quartoNumero = hl.getHospedeLeito().getQuarto().getNumero();
				Integer leitoNumero = hl.getHospedeLeito().getLeito().getNumero();
				key = makeLeitoKey(hl.getHospedeLeito().getLeito().getId(), quartoNumero, leitoNumero);
				id = hl.getHospedeLeito().getId(); //*
				identificador =  String.format("T%06d", id); // T000000 Total
			} else {
				utilizacao = CellUtilizacao.PARCIAL;
				dataEntradaLeito = hl.getHospedagem().getDataEntrada(); 
				dataSaidaLeito = hl.getHospedagem().getDataEfetivaSaida() == null ? hl.getHospedagem().getDataPrevistaSaida() : hl.getHospedagem().getDataEfetivaSaida(); 
				Integer quartoNumero = 9999;
				Integer leitoNumero = 9999;
				key = makeLeitoKey(hl.getHospede().getId(), quartoNumero, leitoNumero);
				id = hl.getHospede().getId();
				identificador =  String.format("P%06d", hl.getHospede().getId()); // P000000 Parcial
			}
			
			LeitoHeader lh = null;
			for (LeitoHeader _lh : retorno.getLeitos()) {
				if (_lh.getKey().equals( key )) {
					lh = _lh;
					break;
				} 
			}
			
			if (lh != null) {
				HospedagemMapa hm = new HospedagemMapa(QTD_DIAS);
				
				HospedagemHeaderInfo hhi = new HospedagemHeaderInfo();
				hhi.setHospedagemId(hospedagem.getId());
				hhi.setHospedeLeitoId(id);
				hhi.setHospedeId(hl.getHospede().getId());
				
				hhi.setId(id);
				hhi.setIdentificador(identificador);
				hhi.setBaixado(Logico.S.equals(hl.getHospede().getBaixado()));
				
				hhi.setDestinacaoHospedagemId(hospedagem.getDestinacaoHospedagem().getId());
				hhi.setDestinacaoHospedagemDescricao(hospedagem.getDestinacaoHospedagem().getDescricao());
				
				hhi.setDataEntrada(hospedagem.getDataEntrada());
				hhi.setDataPrevistaSaida(hospedagem.getDataPrevistaSaida());
				hhi.setDataEfetivaSaida(hospedagem.getDataEfetivaSaida());
				
				hhi.setLeitoDataEntrada(dataEntradaLeito);
				hhi.setLeitoDataSaida(dataSaidaLeito);
				
				hhi.setPessoaId(p.getId());
				hhi.setPessoaNome(p.getNome());
				hhi.setStatusHospedagem(statusHospedagem);
				hhi.setUtilizacao(utilizacao);

				if (CellUtilizacao.TOTAL.equals(utilizacao)) {
					hhi.setLeitoId(hl.getHospedeLeito().getLeito().getId());
					hhi.setLeitoNumero(hl.getHospedeLeito().getLeito().getNumero());
					hhi.setQuartoId(hl.getHospedeLeito().getQuarto().getId());
					hhi.setQuartoNumero(hl.getHospedeLeito().getQuarto().getNumero());
				}
				
				//hm.getDias()[0..6] : DiaHospedagem
				int firstIndex = -1;
				while (dtmp.compareTo(dFim) != 1) {
					CellAndamento andamento = CellAndamento.VAZIO;

					Boolean inicioHospedagem = (dataEntradaHospedagem.equals(dtmp)); 
					Boolean fimHospedagem = (dataSaidaHospedagem.equals(dtmp)); 

					Boolean inicioLeito = (dataEntradaLeito.equals(dtmp)); 
					Boolean fimLeito = (dataSaidaLeito.equals(dtmp)); 
					Boolean duranteLeito = (dtmp.isAfter(dataEntradaLeito) && dtmp.isBefore(dataSaidaLeito));

					if (duranteLeito) {
						andamento = CellAndamento.DURANTE;
					} else
					if (inicioLeito && fimHospedagem) {
						andamento = CellAndamento.VINDO_FIM;
					} else	
					if (inicioLeito && fimLeito) {
						andamento = CellAndamento.INDO_VINDO;
					} else	
					if (inicioHospedagem && inicioLeito) {
						andamento = CellAndamento.INICIO;
					} else
					if (!inicioHospedagem && inicioLeito) {
						andamento = CellAndamento.VINDO;
					} else
					if (fimHospedagem && fimLeito) {
							andamento = CellAndamento.FIM;
					} else	
					if (!fimHospedagem && fimLeito) {
							andamento = CellAndamento.INDO;
					}	
					
					DiaHospedagem dh = hm.getDias()[celulaIndex];
					dh.setAndamento(andamento);
					if (!CellAndamento.VAZIO.equals(andamento)) {
						dh.setIdentificador(identificador);
						dh.setAtendimento(true);
						if (firstIndex == -1) {
							dh.setFirstIndex(true);
							firstIndex = celulaIndex;
						} 
						//dh.setPossuiContinuidade( (CellAndamento.FIM.equals(andamento) || CellAndamento.INICIO_FIM.equals(andamento)) && hhi.getPossuiContinuidade());
						//dh.setPossuiContinuidade(hhi.getPossuiContinuidade());
						//dh.setContinuacao((CellAndamento.INICIO.equals(andamento) || CellAndamento.INICIO_FIM.equals(andamento)) && hhi.getContinuacao());
						// Provavelmente possuiContinuidade e setContinuacao não servem pra nada!
						dh.setPossuiContinuidade(hhi.getPossuiContinuidade());
						dh.setContinuacao(hhi.getContinuacao());
					}
					celulaIndex++;
					dtmp = dtmp.plusDays(1);
				} // end while
				
				retorno.getHospedagens().add(hhi);
				lh.getHospedagens().add(hm);	
			}
		}
		
		//List<HospedagemHeaderInfo> hospedagens = retorno.getHospedagens(); 
		// TODO: Criar campo calculado em hospedagens com NOME + ID + TIPO + IDENTIFICADOR ou NOME + ID + DATAENTRADA
		retorno.getHospedagens().sort((a, b) -> a.getPessoaNome().compareTo(b.getPessoaNome()) );
		
		// Dias
		LocalDate dtmp = dIni;
		while (dtmp.compareTo(dFim) != 1) {
			retorno.getDias().add(dtmp);
			dtmp = dtmp.plusDays(1);
		}
		
		// Dashboard
		atualizarDashBoard(retorno, leitos.size());
		
		return retorno;
	}

	public List<Long> getLeitosOcupadosNoPeriodo(LocalDate dataIni, LocalDate dataFim){
		
		return hospedeLeitoRepo.leitosNoPeriodo(dataIni, dataFim);
		
	}
	
	private void atualizarDashBoard(MapaRetorno mapaRetorno, Integer qtdLeitos) {
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
		
		for (int x = 0; x < QTD_DIAS; x++) {
			qtdLeitosTotais[x] = qtdLeitos;
		}

		for (LeitoHeader lh : mapaRetorno.getLeitos()) {
			for (HospedagemMapa hm: lh.getHospedagens()) {


				for (int i = 0; i < QTD_DIAS; i++) {
					//Cell cell = c.getCells()[i];
					DiaHospedagem dia = hm.getDias()[i];
					
					String identificador = dia.getIdentificador();
					
					HospedagemHeaderInfo hhi = null;
					for (HospedagemHeaderInfo _hhi: mapaRetorno.getHospedagens()) {
						if (_hhi.getIdentificador().equals(identificador)) {
							hhi = _hhi;
							break;
						}
					}
					
					if (hhi != null) {
						if (!CellUtilizacao.VAZIO.equals(hhi.getUtilizacao())) {
							qtdTotais[i]++;
						}
						if (CellUtilizacao.TOTAL.equals(hhi.getUtilizacao())) {
							qtdLeitosOcupados[i]++;
						}
						if (CellUtilizacao.PARCIAL.equals(hhi.getUtilizacao())) {
							qtdParciaisTotais[i]++;
							qtdParciaisPendentes[i] = qtdParciaisPendentes[i] + (CellStatusHospedagem.ABERTA.equals(hhi.getStatusHospedagem()) ? 1 : 0);
							qtdParciaisVencidos[i] = qtdParciaisVencidos[i] + (CellStatusHospedagem.VENCIDA.equals(hhi.getStatusHospedagem()) ? 1 : 0);
							qtdParciaisEncerrados[i] = qtdParciaisEncerrados[i] + (CellStatusHospedagem.ENCERRADA.equals(hhi.getStatusHospedagem()) ? 1 : 0);
						}
						qtdPendentes[i] = qtdPendentes[i] + (CellStatusHospedagem.ABERTA.equals(hhi.getStatusHospedagem()) ? 1 : 0);
						qtdVencidos[i] = qtdVencidos[i] + (CellStatusHospedagem.VENCIDA.equals(hhi.getStatusHospedagem()) ? 1 : 0);
						qtdEncerrados[i] = qtdEncerrados[i] + (CellStatusHospedagem.ENCERRADA.equals(hhi.getStatusHospedagem()) ? 1 : 0);
					}
				}
				
			}
		}
		
		mapaRetorno.setQtdTotais(qtdTotais);
		mapaRetorno.setQtdVencidos(qtdVencidos);
		mapaRetorno.setQtdPendentes(qtdPendentes);
		mapaRetorno.setQtdEncerrados(qtdEncerrados);

		mapaRetorno.setQtdParciaisTotais(qtdParciaisTotais);
		mapaRetorno.setQtdParciaisVencidos(qtdParciaisVencidos);
		mapaRetorno.setQtdParciaisPendentes(qtdParciaisPendentes);
		mapaRetorno.setQtdParciaisEncerrados(qtdParciaisEncerrados);
		
		mapaRetorno.setQtdLeitosTotais(qtdLeitosTotais);
		mapaRetorno.setQtdLeitosOcupados(qtdLeitosOcupados);
		for (int x = 0; x < QTD_DIAS; x++) {
			qtdLeitosLivres[x] = qtdLeitos - qtdLeitosOcupados[x];
		}
		mapaRetorno.setQtdLeitosLivres(qtdLeitosLivres);
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
		
		if (h == null) {
			return retorno;
		}
		
		for (HospedagemTipoServico hts: h.getServicos()) {
			TipoServico servico = hts.getTipoServico();
			retorno.getServicos().add(servico);
		}
		
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
	
	public void encerrarHospedagem(Long hospedagemId, LocalDate dataEncerramento) throws ValidationException {
		/*
		* hospedagem = getHospedagem(hospedagemId)
		* Condição: se hospedagem.status == aberta
		* Condição: dataEncerramento >= hospedagem.dataEntrada
		* hospedagemLeito = getUltimoHospedagemLeito(hospedagemId)
		* Para cada hospedeLeito - hospedagemLeito.setDataSaída(dataEncerramento)
		* hospedagem.setDataPrevistaSaida(dataEncerramento)
		*/
		Optional<Hospedagem> opt  = hospedagemRepo.findById(hospedagemId);
		if (opt.isPresent()) {
			
			Hospedagem h = opt.get();
			if ((h.getDataEfetivaSaida() != null)) {
				throw new ValidationException(new ResultError().addError("*", "Hospedagem deve ter status = emAberto"));
			}
	
			LocalDate hoje = LocalDate.now();
			
			if (dataEncerramento.isAfter(hoje)) {
				throw new ValidationException(new ResultError().addError("*", 
						String.format("Data de Encerramento não pode ser superior a data atual (%s)",fmt.format(hoje))));
			}

			if (TipoUtilizacaoHospedagem.P.equals(h.getTipoUtilizacao())) {
				if (h.getDataEntrada().isBefore(dataEncerramento)) {
					throw new ValidationException(new ResultError().addError("*", 
							String.format("Data de Encerramento deve ser igual ou superior a Data de Entrada (%s)", fmt.format(h.getDataEntrada())) ));
				} 
			} else {
				LocalDate dataMinima = hospedeLeitoRepo.ultimaDataEntradaByHospedagemId(hospedagemId);
				if (dataEncerramento.isBefore(dataMinima)) {
					throw new ValidationException(new ResultError().addError("*", 
							String.format("Data de Encerramento deve ser igual ou superior a Data de Entrada da última movimentação (%s)",fmt.format(dataMinima))));
				}
			}
			
			if (dataEncerramento.isAfter(h.getDataPrevistaSaida())) {
				throw new ValidationException(new ResultError().addError("*", 
						String.format("Data de Encerramento deve ser inferior a data Prevista de Saída (%s)",fmt.format(h.getDataPrevistaSaida()))));
			}
			
			List<HospedeLeito> hlToSave = new ArrayList<HospedeLeito>();
			
			if (TipoUtilizacaoHospedagem.T.equals(h.getTipoUtilizacao())) {
				List<Hospede> hospedes = h.getHospedes();
				for (Hospede hpd : hospedes) {

					List<HospedeLeito> listaHospedeLeito = hospedeLeitoRepo.findUltimoByHospedeId(hpd.getId());
					
					for (HospedeLeito hl : listaHospedeLeito) {
						if (Logico.N.equals(hpd.getBaixado())) {
							hlToSave.add(hl);
						}
					}
				}
			}

			for (HospedeLeito hl : hlToSave) {
				hl.setDataSaida(dataEncerramento);
				hospedeLeitoRepo.save(hl);
			}	
			h.setDataEfetivaSaida(dataEncerramento);
			hospedagemRepo.save(h);
			
		}
	} 
	
	public void baixarHospede(Long hospedeId, LocalDate dataBaixa) throws ValidationException{
		Optional<Hospede> hospedeOpt = hospedeRepo.findById(hospedeId);
		if (hospedeOpt.isPresent()) {
			Long hospedagemId = hospedeOpt.get().getHospedagem().getId();
			Hospede hospede = hospedeOpt.get();

			if ((Logico.S.equals(hospede.getBaixado())  )) {
				throw new ValidationException(new ResultError().addError("*", "Hóspede já está baixado"));
			}

			Optional<Hospedagem> opt = hospedagemRepo.findById(hospedagemId);
			if (opt.isPresent()) {
				
				Hospedagem h = opt.get();
				if ((h.getDataEfetivaSaida() != null)) {
					throw new ValidationException(new ResultError().addError("*", "Hospedagem deve ter status = emAberto"));
				}
				
				Long qtd = hospedeLeitoRepo.countHospedesNaoBaixadosByHospedagemId(hospedagemId);
				if (qtd <= 1) {
					throw new ValidationException(new ResultError().addError("*", "Para baixar hóspede é necessário ter pelo menos 2 hóspedes ativos"));
				}
				
				if (dataBaixa.isBefore(h.getDataEntrada())) {
					throw new ValidationException(new ResultError().addError("*", "Data de encerramento deve ser superior a data de entrada"));
				}
				
				List<HospedeLeito> listaHospedeLeito = hospedeLeitoRepo.findUltimoByHospedeId(hospedeId);
				for (HospedeLeito hl : listaHospedeLeito) {
					if (hl.getDataEntrada().isAfter(dataBaixa)) {
						throw new ValidationException(new ResultError().addError("*", "Existe movimentação com data ANTERIOR a data da baixa"));
					}
					
					hl.setDataSaida(dataBaixa);
					hospedeLeitoRepo.save(hl);
				}
				
				hospede.setBaixado(Logico.S);
			}
		}
		
	} 
	
	public void transferirHospede(Long hospedeId, Long leitoId, LocalDate dataTransferencia) throws ValidationException{
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Optional<Hospede> hospedeOpt = hospedeRepo.findById(hospedeId);
		if (hospedeOpt.isPresent()) {
			Long hospedagemId = hospedeOpt.get().getHospedagem().getId();
			Hospede hospede = hospedeOpt.get();

			if ((Logico.S.equals(hospede.getBaixado())  )) {
				throw new ValidationException(new ResultError().addError("*", "Hóspede já está baixado"));
			}

			Optional<Hospedagem> opt = hospedagemRepo.findById(hospedagemId);
			if (opt.isPresent()) {
				
				Hospedagem h = opt.get();
				
				if (!TipoUtilizacaoHospedagem.T.equals(h.getTipoUtilizacao())) {
					throw new ValidationException(new ResultError().addError("*", "Tipo de Utilização da Hospedagem deve ser Total"));
				} 
				
				if ((h.getDataEfetivaSaida() != null)) {
					throw new ValidationException(new ResultError().addError("*", "Hospedagem deve ter status = emAberto"));
				}
				
				List<BigInteger> hospedagens = hospedagensNoPeriodo(leitoId, dataTransferencia, h.getDataPrevistaSaida());
				
				for (BigInteger _hospedagemId : hospedagens) {
					Long value = hospedagemId.longValue(); 
					if (!value.equals(h.getId())) {
						throw new ValidationException(new ResultError().addError("*", "Este Leito já está em uso no período por outra Hospedagem"));
					}
				}

				if (dataTransferencia.isBefore(h.getDataEntrada())) {
					throw new ValidationException(new ResultError().addError("*", "Data de encerramento deve ser superior a data de entrada"));
				}
				
				if (dataTransferencia.isAfter(h.getDataPrevistaSaida())) {
					throw new ValidationException(new ResultError().addError("*", 
							String.format("Data de Transferência deve ser inferior a data Prevista de Saída (%s)",fmt.format(h.getDataPrevistaSaida()))));
				}
				
				LocalDate dataMinima = hospedeLeitoRepo.ultimaDataEntradaByHospedagemId(hospedagemId);
				dataMinima = dataMinima.plusDays(1);
				if (dataTransferencia.isBefore(dataMinima)) {
					throw new ValidationException(new ResultError().addError("*", 
							String.format("Data de Transferência deve ser igual ou superior a Data de Entrada da última movimentação (%s)",fmt.format(dataMinima))));
				}
				
				List<HospedeLeito> listaHospedeLeito = hospedeLeitoRepo.findUltimoByHospedeId(hospedeId);
				for (HospedeLeito hl : listaHospedeLeito) {
					if (hl.getDataEntrada().isAfter(dataTransferencia)) {
						throw new ValidationException(new ResultError().addError("*", "Existe movimentação com data ANTERIOR a data da transferência"));
					}
					
					hl.setDataSaida(dataTransferencia.minusDays(1));
					hospedeLeitoRepo.save(hl);
				}
				
				// Inserir novo HospedeLeito com LeitoId, dataTransferencia até dataPrevistaSaida
		    	//Optional<Quarto> quarto = quartoRepo.findById(hvo.getAcomodacao().getQuartoId());
		    	Optional<Leito> leito = leitoRepo.findById(leitoId);

		    	if (leito.isPresent()) {
		    		Quarto q = leito.get().getQuarto();
		    		
		    		HospedeLeito hl = new HospedeLeito();
		    		hl.setHospede(hospede);
		    		hl.setDataEntrada(dataTransferencia);
		    		hl.setDataSaida(h.getDataPrevistaSaida());

		    		hl.setQuarto(q);

		    		hl.setLeito(leito.get());

		    		hospedeLeitoRepo.save(hl);
		    	}
			}
		}
		
	} 
	
	//TODO Implementar renovarHospedagem
	public void renovarHospedagem(Long hospedagemId, LocalDate dataRenovacao) {
		/*
		* Somente se hospedagem.status == aberta
		* hospedagem = getHospedagem(hospedagemId)
		* Condição: novaDataPrevistaSaida > hospedagem.dataPrevistaSaida
		* hospedagemLeito = getUltimoHospedagemLeito(hospedagemId)
		* Condição: Verificar se não existe hospedagemLeito em (hospedagemLeito.leito, hospedagem.dataPrevistaSaida + 1, novaDataPrevistaSaida) !!!
		* Para cada hospedeLeito, o último, hospedagemLeito.setDataSaída(novaDataPrevistaSaida)
		* hospedagem.setDataPrevistaSaida(novaDataPrevistaSaida)
		 */
		Optional<Hospedagem> opt  = hospedagemRepo.findById(hospedagemId);
		if (opt.isPresent()) {
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
			Hospedagem h = opt.get();
			if ((h.getDataEfetivaSaida() != null)) {
				throw new ValidationException(new ResultError().addError("*", "Hospedagem deve ter status = emAberto"));
			}
	
			if (!dataRenovacao.isAfter(h.getDataPrevistaSaida())) {
				throw new ValidationException(new ResultError().addError("*", 
						String.format("Data de Renovação deve ser igual ou superior a Data Prevista de Saída (%s)", fmt.format(h.getDataPrevistaSaida())) ));
			} 

			// Para cada leito (caso seja Total), verificar se ele está sendo utilizado no período (dataPrevistaSaida ~ dataRenovacao)
			
			List<HospedeLeito> hlToSave = new ArrayList<HospedeLeito>();
			
			if (TipoUtilizacaoHospedagem.T.equals(h.getTipoUtilizacao())) {
				List<Hospede> hospedes = h.getHospedes();
				for (Hospede hpd : hospedes) {

					List<HospedeLeito> listaHospedeLeito = hospedeLeitoRepo.findUltimoByHospedeId(hpd.getId());
					
					for (HospedeLeito hl : listaHospedeLeito) {
						if (Logico.N.equals(hpd.getBaixado())) {
							Long leitoId = hl.getLeito().getId();
							//System.out.println(hpd.getPessoa().getNome() + " - " + hl.getDataSaida() + " - " + hl.getQuarto().getNumero() + " - " + hl.getLeito().getNumero() +  " - Baixado? " + hpd.getBaixado());
							
							List<BigInteger> hospedagens = hospedagensNoPeriodo(leitoId, h.getDataPrevistaSaida().plusDays(1), dataRenovacao);
							
							for (BigInteger _hId : hospedagens) {
								Long value = _hId.longValue(); 
								if (!value.equals( hospedagemId )) {
									Integer quartoNumero = hl.getQuarto().getNumero();
									Integer leitoNumero = hl.getLeito().getNumero() ;

									throw new ValidationException(new ResultError().addError("*", String.format("O Leito %s do Quarto %s já está em uso no período por outra Hospedagem", leitoNumero, quartoNumero)));
								}
							}
							
							hlToSave.add(hl);
						}
					}
				}
			}

			for (HospedeLeito hl : hlToSave) {
				hl.setDataSaida(dataRenovacao);
				hospedeLeitoRepo.save(hl);
			}	
			h.setDataPrevistaSaida(dataRenovacao);
			hospedagemRepo.save(h);
		}
		
	}
	
	//TODO Implementar createNaoAtendimento
	public void createNaoAtendimento(Long hospedagemId, LocalDate dataNaoAtendimento) {
		
	}
	
	public void excluirHospedagem(Long id) {
		Optional<Hospedagem> opt = hospedagemRepo.findById(id);
		if (opt.isPresent()) {
			hospedagemRepo.delete(opt.get());
		}
	}
	
	//TODO Implementar removeNaoAtendimento
	public void removeNaoAtendimento(Long hospedagemId, Long naoAtendimentoId) {
		
	}
	
	private List<LeitoHeader> gerarListaLeitos(List<LeitoHeader> leitos, List<HospedeLeitoVO> hospedesParciais){
		
		List<LeitoHeader> retorno = new ArrayList<LeitoHeader>();
		
		for(LeitoHeader leito : leitos) {
			String key = makeLeitoKey(leito.getLeitoId(), leito.getQuartoNumero(), leito.getLeitoNumero()); 
			leito.setKey(key);
			retorno.add(leito);
		}

		for (HospedeLeitoVO hospede: hospedesParciais) {
			LeitoHeader leito = new LeitoHeader();
			leito.setLeitoId(0L);
			leito.setLeitoNumero(9999);
			leito.setQuartoId(0L);
			leito.setQuartoNumero(9999);
			leito.setKey(makeLeitoKey(hospede.getHospede().getId(), 9999, 9999)); 
			retorno.add(leito);
		}
		/*
		Map<String, Cell[]> retorno = mapa.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        */                
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

	public List<BigInteger> hospedagensNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT h.hospedagem_id ");
		sb.append("FROM   hospede_leito hl ");
		sb.append("INNER JOIN hospede h ");
		sb.append("ON         h.id = hl.hospede_id "); 
		sb.append("WHERE  hl.leito_id = :leitoId  "); 
		sb.append("AND    (((hl.data_entrada BETWEEN :dataIni AND :dataFim) OR (hl.data_saida BETWEEN :dataIni AND :dataFim))  "); 
		sb.append("OR     ((hl.data_entrada <= :dataIni) AND (hl.data_saida >= :dataFim)))  ");
		
		Query query = em.createNativeQuery(sb.toString());
		List<BigInteger> lista = (List<BigInteger>)query.setParameter("leitoId", leitoId)
								 .setParameter("dataIni", dataIni)
								 .setParameter("dataFim", dataFim)
								 .getResultList();
		
		return lista; 
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