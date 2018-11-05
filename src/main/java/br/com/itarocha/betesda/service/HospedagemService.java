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
import br.com.itarocha.betesda.model.hospedagem.HospedagemOut;
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
			
			//LocalDate dIni = LocalDate.parse("12/08/2018", fmt);
			//LocalDate dIni = LocalDate.parse("21/10/2018", fmt);

			// Monta lista de leitos 
			StringBuilder sbLeitos = StrUtil.loadFile("/sql/leitos.sql");
			TypedQuery<LeitoVO> qLeitos = em.createQuery(sbLeitos.toString(), LeitoVO.class);
			List<LeitoVO> leitos = qLeitos.getResultList();
			
			// Gerando mapa vazio no formato chave (quartoNumero-leitoNumero) e array de Dia
			Map<String, Dia[]> mapa = gerarMapa(dIni, 7, leitos);

			// Monta lista de HospedeLeitoVO
			StringBuilder sbHospedeLeitos = StrUtil.loadFile("/sql/hospede_leito.sql");
			TypedQuery<HospedeLeitoVO> qHospedeLeito = em.createQuery(sbHospedeLeitos.toString(), HospedeLeitoVO.class)
					.setParameter("DATA_INI", dIni )
					.setParameter("DATA_FIM", dFim );
			List<HospedeLeitoVO> hospedeLeitos = qHospedeLeito.getResultList();
			
			LocalDate hoje = LocalDate.now();

			//TODO: Completar com o tipo específico de hospedagem
			List<Object> listaHospedagens = new ArrayList<Object>();
			
			// Popula os leitos ocupados no mapa
			for(HospedeLeitoVO hl : hospedeLeitos) {
				LocalDate dataEntrada = hl.getHospedeLeito().getDataEntrada(); 
				LocalDate dataSaida = hl.getHospedeLeito().getDataSaida(); 
				int celulaIndex = 0;
				LocalDate dtmp = dIni;
				// loop da primeira data até a última
				
				String key = makeLeitoKey(hl.getHospedeLeito().getQuarto().getNumero(), hl.getHospedeLeito().getLeito().getNumero());
				System.out.print(" ["+key+"] ");

				// Encapsular todo mundo e montar uma lista de hospedagens
				hl.getHospedagem().getId();
				hl.getHospedagem().getDataEntrada();
				hl.getHospedagem().getDataEfetivaSaida();
				hl.getHospedagem().getDataPrevistaSaida();
				String status = "emAberto";
				if (hl.getHospedagem().getDataEfetivaSaida() != null) {
					status = "encerrada";
				} else if (hl.getHospedagem().getDataPrevistaSaida().isAfter(hoje) ) {
					status = "vencida";
				} else {
					status = "emAberto";
				}
				
				//TODO: adicionar listaHospedagens
				
				System.out.println(dataEntrada + " até " + dataSaida);
				while (dtmp.compareTo(dFim) != 1) {
					System.out.print(dtmp + " - ");

					Boolean inicio 	= (dataEntrada.compareTo(dtmp) == 0); 
					Boolean fim 	= (dataSaida.compareTo(dtmp) == 0);
					Boolean durante = (dtmp.isAfter(dataEntrada) && dtmp.isBefore(dataSaida));					
					
					if (inicio || durante || fim) {
						
						HospedagemOut hospedagem = new HospedagemOut();
						hospedagem.setHospedagemId(hl.getHospedagem().getId());
						hospedagem.setHospedeId(hl.getHospede().getId());
						hospedagem.setPessoaId(hl.getHospede().getPessoa().getId()); // Podem ser vários
						
						hospedagem.setInicio(inicio);
						hospedagem.setDurante(durante);
						hospedagem.setFim(fim);

						Dia dia = new Dia();
						//dia.setData(dtmp);
						dia.setHospedagem(hospedagem);

						// localiza no mapa para atualizar o dia correspondente que antes estava somente com a data mas hospedagem null
						Dia[] dias =  mapa.get(key);
						// injeta no índice
						dias[celulaIndex] = dia;
					}
					celulaIndex++;
					dtmp = dtmp.plusDays(1);
				}
				//System.out.println("");

			}

			//Montagem do MapaHospedagem
			MapaHospedagem mh = new MapaHospedagem();
			mh.setDataIni(dIni);
			mh.setDataFim(dFim);
			for (String key : mapa.keySet()) {
				LeitoOut leito = extractLeitoFromKey(key);
				
				CelulaOut celula = new CelulaOut();
				celula.setLeito(leito);
				
				Dia[] dias = mapa.get(key);
				for (Dia d : dias) {
					Dia dia = new Dia();
					//dia.setData(d.getData());
					celula.getDias().add(d);
				}
				//System.out.println();
				mh.getCelulas().add(celula);
			}
			LocalDate dtmp = dIni;
			while (dtmp.compareTo(dFim) != 1) {
				mh.getDias().add(dtmp);
				dtmp = dtmp.plusDays(1);
			}
			return mh;
			
		} finally {
		}
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

	// Gera um mapa vazio com todos os leitos e datas
	private Map<String, Dia[]> gerarMapa(LocalDate dIni, Integer numeroDias, List<LeitoVO> leitos){
		
		/*
		Customer james = customers.stream()
		  .filter(customer -> "James".equals(customer.getName()))
		  .findAny()
		  .orElse(null);
		*/
		
		Map<String, Dia[]> mapa = new HashMap<>();
		LocalDate dFim = dIni.plusDays(numeroDias-1);
		
		for(LeitoVO leito : leitos) {
			Dia[] cels = new Dia[numeroDias];
			//Dia cel = new Dia();
			//cels[0] = cel;
			String key = makeLeitoKey(leito.getQuartoNumero(), leito.getNumero()); 

			LocalDate dtmp = dIni;
			int index = 0;
			while (dtmp.compareTo(dFim) != 1) {
				Dia cel = new Dia();
				//cel.setData(dtmp);
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


