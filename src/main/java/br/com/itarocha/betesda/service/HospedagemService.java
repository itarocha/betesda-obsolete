package br.com.itarocha.betesda.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.itarocha.betesda.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.model.Hospedagem;
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
import br.com.itarocha.betesda.model.hospedagem.CelulaOut;
import br.com.itarocha.betesda.model.hospedagem.Dia;
import br.com.itarocha.betesda.model.hospedagem.HospedagemHeader;
import br.com.itarocha.betesda.model.hospedagem.HospedagemInfo;
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
	
	private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");


	public Hospedagem create(HospedagemVO model) throws Exception {
		Hospedagem hospedagem = null;
		try {
			hospedagem = new Hospedagem();
			
			hospedagem.setDataEntrada(model.getDataEntrada());
			hospedagem.setDataPrevistaSaida(model.getDataPrevistaSaida());
			
			Optional<DestinacaoHospedagem> dest = destinacaoHospedagemRepo.findById(model.getDestinacaoHospedagemId()) ;// em.find(DestinacaoHospedagem.class, model.getDestinacaoHospedagemId());
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
	
	/*
	http://localhost:8088/api/hospedagem/mapa
	Deve produzir um json nesse formato:
	{
		celulas : [
		{
			leito : {quartoNumero, leitoNumero}, 
			dias : [
				{data: "2018-08-12", hospedagem: {hospedagemId, hospedeId, pessoaId, inicio, durante, fim}}, 
				{data: "2018-08-13", hospedagem: null},
				{},  {},  {},  {},  {},  {}]
		}],
		pessoas : [{id, nome, tipoHospede...},{},{}],
		hospedagens : [{id, dataEntrada, dataPrevistaSaida, dataEfetivaSaida, tipoUtilizacao, entidade, encaminhador},{},{}],
		hospedesLeitos: [{id, dataEntrada, dataSaida},{},{}]
	}
	*/
	public MapaHospedagem getHospedagens(LocalDate dataBase) {
		try {
			LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
			LocalDate dFim = dIni.plusDays(6);
			
			// Monta lista de leitos 
			StringBuilder sbLeitos = StrUtil.loadFile("/sql/leitos.sql");
			TypedQuery<LeitoVO> qLeitos = em.createQuery(sbLeitos.toString(), LeitoVO.class);
			List<LeitoVO> leitos = qLeitos.getResultList();
			Map<String, Dia[]> mapaLeitos = gerarMapaLeitosVazio(leitos, 7);

			StringBuilder sbHospedes = StrUtil.loadFile("/sql/hospedes_por_periodo.sql");
			TypedQuery<HospedeHospedagemVO> qHospedes = em.createQuery(sbHospedes.toString(), HospedeHospedagemVO.class)
					.setParameter("DATA_INI", dIni )
					.setParameter("DATA_FIM", dFim );
			List<HospedeHospedagemVO> hospedes = qHospedes.getResultList();
			System.out.println("-----------------------------");
			for (HospedeHospedagemVO h : hospedes) {
				System.out.println(h.getHospede().getPessoa().getNome() + " - " + h.getHospedagem().getId() + " " + h.getHospedagem().getDataEntrada() + " até " + h.getHospedagem().getDataPrevistaSaida());
			}
			
			
			// Monta lista de HospedeLeitoVO
			StringBuilder sbHospedeLeitos = StrUtil.loadFile("/sql/hospede_leito.sql");
			TypedQuery<HospedeLeitoVO> qHospedeLeito = em.createQuery(sbHospedeLeitos.toString(), HospedeLeitoVO.class)
					.setParameter("DATA_INI", dIni )
					.setParameter("DATA_FIM", dFim );
			List<HospedeLeitoVO> hospedeLeitos = qHospedeLeito.getResultList();
			
			List<HospedagemHeader> hospedagensHeaders = new ArrayList<HospedagemHeader>();

			
			// Popula os leitos ocupados no mapa
			for(HospedeLeitoVO hl : hospedeLeitos) {
				Hospedagem hspd = hl.getHospedagem();
				Pessoa p = hl.getHospede().getPessoa(); 
				
				LocalDate dataEntrada = hl.getHospedeLeito().getDataEntrada(); 
				LocalDate dataSaida = hl.getHospedeLeito().getDataSaida(); 
				int celulaIndex = 0;
				LocalDate dtmp = dIni;
				// loop da primeira data até a última
				
				HospedagemHeader hh = new HospedagemHeader(hspd.getId(), p.getId(), dataEntrada, hspd.getDataPrevistaSaida(), hspd.getDataEfetivaSaida());
				hospedagensHeaders.add(hh);

				String key = makeLeitoKey(hl.getHospedeLeito().getQuarto().getNumero(), hl.getHospedeLeito().getLeito().getNumero());
				while (dtmp.compareTo(dFim) != 1) {
					Boolean inicio 	= (dataEntrada.compareTo(dtmp) == 0); 
					Boolean fim 	= (dataSaida.compareTo(dtmp) == 0);
					Boolean durante = (dtmp.isAfter(dataEntrada) && dtmp.isBefore(dataSaida));					
					
					if (inicio || durante || fim) {
						HospedagemInfo hospedagemInfo = new HospedagemInfo();
						hospedagemInfo.setHospedagemId(hl.getHospedagem().getId());
						hospedagemInfo.setHospedeLeitoId(hl.getHospedeLeito().getId());
						hospedagemInfo.setHospedeId(hl.getHospede().getId());
						hospedagemInfo.setPessoaId(hl.getHospede().getPessoa().getId()); // Podem ser vários
						
						hospedagemInfo.setInicio(inicio);
						hospedagemInfo.setDurante(durante);
						hospedagemInfo.setFim(fim);

						Dia dia = new Dia();
						dia.setHospedagem(hospedagemInfo);

						// localiza no mapa para atualizar o dia correspondente que antes estava somente com a data mas hospedagem null
						Dia[] dias =  mapaLeitos.get(key);
						// injeta no índice
						dias[celulaIndex] = dia;
					}
					celulaIndex++;
					dtmp = dtmp.plusDays(1);
				}
			}

			MapaHospedagem mapaHospedagem = new MapaHospedagem(dIni, dFim);
			mapaHospedagem.setHospedagens(hospedagensHeaders);
			mapaHospedagem.setHospedes(hospedes);
			for (String key : mapaLeitos.keySet()) {
				CelulaOut celula = new CelulaOut(extractLeitoFromKey(key));
			
				Dia[] dias = mapaLeitos.get(key);
				for (Dia d : dias) {
					celula.getDias().add(d);
				}
				mapaHospedagem.getCelulas().add(celula);
			}
			LocalDate dtmp = dIni;
			while (dtmp.compareTo(dFim) != 1) {
				mapaHospedagem.getDias().add(dtmp);
				dtmp = dtmp.plusDays(1);
			}
			return mapaHospedagem;
			
		} finally {
		}
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
	
	
	// quartoNumero - leitoNumero "000005-000007" quarto 5, leito 7
	private String makeLeitoKey(Integer quartoNumero, Integer leitoNumero) {
		return String.format("%06d-%06d", quartoNumero, leitoNumero); 
	}

	private LeitoOut extractLeitoFromKey(String key) {
		LeitoOut leito = new LeitoOut();
		leito.setQuartoNumero(0);
		leito.setLeitoNumero(0);
		String[] leitoArray = key.split("-");
		if (leitoArray.length == 2) {
			leito.setQuartoNumero(Integer.parseInt(leitoArray[0]));
			leito.setLeitoNumero(Integer.parseInt(leitoArray[1]));
		}
		return leito;
	}

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


