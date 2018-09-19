package br.com.itarocha.betesda.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.itarocha.betesda.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.model.Hospedagem;
import br.com.itarocha.betesda.model.HospedagemVO;
import br.com.itarocha.betesda.model.HospedeVO;
import br.com.itarocha.betesda.model.Hospede;
import br.com.itarocha.betesda.model.HospedeLeito;
import br.com.itarocha.betesda.model.Leito;
import br.com.itarocha.betesda.model.Pessoa;
import br.com.itarocha.betesda.model.Quarto;
import br.com.itarocha.betesda.model.TipoHospede;
import br.com.itarocha.betesda.model.TipoUtilizacaoHospedagem;
//import br.com.itarocha.betesda.util.StrUtil;
import br.com.itarocha.betesda.utils.StrUtil;


import br.com.itarocha.betesda.service.H;

@Service
public class HospedagemService {

	@Autowired
	private EntityManager em;
	
	private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");


	/*
	public Quarto create(NovoQuartoVO model) throws Exception{
		Quarto q = new Quarto();
		try {
			DestinacaoHospedagem dest = em.find(DestinacaoHospedagem.class, model.getDestinacaoHospedagem());
			TipoLeito tipoLeito = em.find(TipoLeito.class, model.getTipoLeito());
			SituacaoLeito situacao = em.find(SituacaoLeito.class, model.getSituacao());
			
			q.setNumero(model.getNumero());
			q.setDescricao(model.getDescricao());
			q.setDestinacaoHospedagem(dest);
			q.setAtivo(Logico.S);
			em.persist(q);
			
			for (int i = 1; i <= model.getQuantidadeLeitos(); i++) {
				Leito leito = new Leito();
				leito.setQuarto(q);
				leito.setNumero(i);
				leito.setTipoLeito(tipoLeito);
				leito.setSituacao(situacao);
				em.persist(leito);
			}
		} catch (Exception e) {
			throw e;
		}
		return q;
	}
	*/
	
	public Hospedagem create(HospedagemVO model) throws Exception {
		Hospedagem hospedagem = null;
		try {
			hospedagem = new Hospedagem();
			
			hospedagem.setDataEntrada(model.getDataEntrada());
			hospedagem.setDataPrevistaSaida(model.getDataPrevistaSaida());
			
			DestinacaoHospedagem dest = em.find(DestinacaoHospedagem.class, model.getDestinacaoHospedagemId());
			hospedagem.setDestinacaoHospedagem(dest);
			model.setDestinacaoHospedagemDescricao(dest.getDescricao());
			
			TipoUtilizacaoHospedagem tu = TipoUtilizacaoHospedagem.valueOf(model.getTipoUtilizacao());
			hospedagem.setTipoUtilizacao(tu);
			em.persist(hospedagem);
			model.setId(hospedagem.getId()); 
			
			for (HospedeVO hvo: model.getHospedes()) {
				Hospede h = new Hospede();
				h.setHospedagem(hospedagem);
				Pessoa p = em.find(Pessoa.class, hvo.getPessoaId());
				// se p == null throw
				h.setPessoa(p);
				hvo.setPessoaId(p.getId());
				hvo.setPessoaNome(p.getNome());
				hvo.setPessoaDataNascimento(p.getDataNascimento());
				
				TipoHospede th = em.find(TipoHospede.class, hvo.getTipoHospedeId());
				h.setTipoHospede(th);
				hvo.setTipoHospedeDescricao(th.getDescricao());
				em.persist(h);
				hvo.setId(h.getId());
				
				hospedagem.getHospedes().add(h);
				HospedeLeito hl = new HospedeLeito();
				hl.setHospede(h);
				hl.setDataEntrada(hospedagem.getDataEntrada());
				hl.setDataSaida(hospedagem.getDataPrevistaSaida());
				Quarto quarto = em.find(Quarto.class, hvo.getAcomodacao().getQuartoId());
				hvo.getAcomodacao().setQuartoNumero(quarto.getNumero());
				hl.setQuarto(quarto);
				Leito leito = em.find(Leito.class, hvo.getAcomodacao().getLeitoId());
				hl.setLeito(leito);
				hvo.getAcomodacao().setLeitoNumero(leito.getNumero());
				em.persist(hl);
				hvo.getAcomodacao().setId(hl.getId());
				
				h.getLeitos().add(hl);
			}
		} catch (Exception e) {
			throw e;
		}
		return hospedagem;
	}
	
	public void getHospedagens() {
		try {
			LocalDate dIni = LocalDate.parse("12/08/2018", fmt);
			LocalDate dFim = LocalDate.parse("18/08/2018", fmt);
			
			StringBuilder sbH = StrUtil.loadFile("/sql/h.sql");
			StringBuilder sbHospedagens = StrUtil.loadFile("/sql/hospedagens_por_periodo.sql");
			StringBuilder sbHospedes = StrUtil.loadFile("/sql/hospedes_por_periodo.sql");
			StringBuilder sbLeitos = StrUtil.loadFile("/sql/hospede_leito_por_periodo.sql");
			
			//System.out.println(sbHospedagens.toString());
			
			TypedQuery<H> qH = em.createQuery("SELECT NEW br.com.itarocha.betesda.service.H(hl.id, hl.hospede) FROM HospedeLeito hl", H.class);
			//qH.setParameter("DATA_INI", dIni);
			//qH.setParameter("DATA_FIM", dFim);
			List<H> hresult = qH.getResultList();
			
			for (H h: hresult) {
				System.out.println(h.hospede.getPessoa().getNome() + " - " + h.hospede.getHospedagem().getDataEntrada() + " até " + h.hospede.getHospedagem().getDataPrevistaSaida());
			}

			/*
			// Hospedagens
			Query qHospedagens = em.createNativeQuery(sbHospedagens.toString(), HospedagemResult.class);
			qHospedagens.setParameter("DATA_INI", dIni);
			qHospedagens.setParameter("DATA_FIM", dFim);
			List<HospedagemResult> xresult = qHospedagens.getResultList();
			
			// Poderia ser um Map<> de Hospedagem
			List<HospedagemResult> lstHospedagens = new ArrayList<HospedagemResult>();
			List<Object[]> result = qHospedagens.getResultList();
			
			for (Object[] o : result) {
				lstHospedagens.add(new HospedagemResult( (BigInteger)o[0], 
														 (BigInteger)o[1],
														 (String)o[2], 
														 ((java.sql.Date)o[3]).toLocalDate(),
														 ((java.sql.Date)o[4]).toLocalDate(),
														 null,//((java.sql.Date)o[5]).toLocalDate(),
														 ((java.sql.Date)o[6]).toLocalDate()
														));
			}
			*/
			
/*
			// HóspedeLeitos
			Query qLeitos = em.createNativeQuery(sbLeitos.toString());
			qLeitos.setParameter("DATA_INI", dIni);
			qLeitos.setParameter("DATA_FIM", dFim);
			// Poderia ser um Map<> de Hospedagem
			List<LeitoResult> lstLeitos = new ArrayList<LeitoResult>();
			result = qLeitos.getResultList();
			for (Object[] o : result) {
				lstLeitos.add(new LeitoResult( 
						(BigInteger) o[0],
						(BigInteger) o[1], 
						(BigInteger) o[2],
						((java.sql.Date)o[3]).toLocalDate(),
						((java.sql.Date)o[4]).toLocalDate(),
						(BigInteger) o[5],
						(Integer) o[6],
						(BigInteger) o[7],
						(Integer) o[8]));
			}
			
			// Hóspede
			Query qHospedes = em.createNativeQuery(sbHospedes.toString());
			qHospedes.setParameter("DATA_INI", dIni);
			qHospedes.setParameter("DATA_FIM", dFim);
			// Poderia ser um Map<> de Hospedagem
			List<HospedeResult> lstHospedes = new ArrayList<HospedeResult>();
			result = qHospedes.getResultList();
			for (Object[] o : result) {
				lstHospedes.add(new HospedeResult((BigInteger) o[0], (BigInteger) o[1], (String) o[2], (BigInteger) o[3], (String) o[4]));
			}
		
			
			// Para ficar mais fácil de buscar
			Map<BigInteger, HospedagemResult> mh;
			// Mapear Leitos
			// Mapear Hóspedes
			
			System.out.println("Hospedagens");
			for (HospedagemResult h : lstHospedagens) {
				System.out.println(String.format("HId: %s %s %s %s %s %s", h.hospedagemId, h.destinacaoHospedagemId, 
						h.tipoUtilizacao, fmt.format(h.dataEntrada), fmt.format(h.dataPrevistaSaida), fmt.format(h.dataSaida)));
			}

			System.out.println("Leitos");
			for (LeitoResult h : lstLeitos) {
				System.out.println(String.format("HId: %s HóspedeId: %s PessoaId: %s %s %s %s %s Leito: %s-%s", 
						h.hospedagemId, h.hospedeId,  h.pessoaId, fmt.format(h.dataEntrada), fmt.format(h.dataSaida), 
						h.quartoId, h.leitoId, h.quartoNumero, h.leitoNumero));
			}

			System.out.println("Hóspedes");
			for (HospedeResult h : lstHospedes) {
				System.out.println(String.format("HId: %s PessoaId: %s %s %s %s",
							h.hospedagemId, h.pessoaId, h.nome, h.tipoHospedeId, h.descricao));
			}
			*/
		} finally {
		}
		
	}
	
/*

	public PacoteEnvio<EventoFila> getEventosAhProcessar() throws SQLException, Exception {
		PacoteEnvio<EventoFila> pacoteEnvio = null;
		Collection<EventoFila> lista = new ArrayList<EventoFila>();
		Integer codEmpresa = 0;
		Integer tipoAmbiente = 0;
		StringBuffer strQuery = new StringBuffer();

		try {
			strQuery.append(SimpleSQL.loadSQLFromResource(this.getClass(), "/sql/ConsultaEventos.sql"));
			SimpleSQL.removeSQLComment(DIALECT == DataSourceWrapper.ORACLE_DIALECT ? "ORACLE" : "MSSQL", strQuery);

			entityManager.clear();
			Query query = null;

			List<Object> listCodEmp = getListCodEmp();

			for (Object codEmp : listCodEmp) {
				query = entityManager.createNativeQuery(strQuery.toString(), EventoFilaEsocial.class);

				for (int ambiente : ambientes) {
					String viewTpAmb = ambiente == 1 ? "P" : "T";
					log.info(String.format("Buscando eventos para ENVIO para empresa [%s] no ambiente [%s/%s]...", codEmp.toString(), ambiente, viewTpAmb));
					query.setParameter("CODEMP", codEmp.toString());
					query.setParameter("TPAMB", ambiente);
					// /////////query.setParameter("VIEWTPAMB", viewTpAmb);
					lista = (Collection<EventoFila>) query.getResultList();
					if (!lista.isEmpty()) {
						codEmpresa = Integer.parseInt(codEmp.toString());
						tipoAmbiente = ambiente;
						break;
					} else {
						log.info(String.format("Nenhum evento no ambiente [%s]", ambiente));
					}
				}
				if (!lista.isEmpty()) {
					break;
				} else {
					log.info(String.format("Nenhum evento para empresa [%s]", codEmp.toString()));
				}
			}
			
			
			boolean tem3000 = tem3000(lista);
			
			pacoteEnvio = new PacoteEnvio<EventoFila>(codEmpresa, tipoAmbiente, lista);
			 
			if(tem3000) {
				pacoteEnvio.setGrupoESocial("2");
			} else if(!lista.isEmpty()){
				query = entityManager.createNativeQuery("SELECT GRUPOESOCIAL FROM TFPEVT WHERE TIPOEVENTO = :tipoEvento").setParameter("tipoEvento", lista.iterator().next().getTipoEvento());
				pacoteEnvio.setGrupoESocial(query.getSingleResult().toString());
			}
		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.info(" ERRO DE COLUNA \n" + errors.toString());

			log.info(String.format("Erro ao buscar lotes a processar", e.getMessage()));
			throw new IllegalStateException("Erro ao buscar lotes a processar", e);
		}
		return pacoteEnvio;
	}
 
 
 */ 	
 	
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