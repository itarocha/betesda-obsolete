package br.com.itarocha.betesda.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.model.HospedagemFullVO;
import br.com.itarocha.betesda.model.HospedagemVO;
import br.com.itarocha.betesda.model.HospedeVO;
import br.com.itarocha.betesda.model.hospedagem.MapaRetorno;
import br.com.itarocha.betesda.model.hospedagem.OcupacaoLeito;
import br.com.itarocha.betesda.report.HospedePermanencia;
import br.com.itarocha.betesda.report.PlanilhaGeral;
import br.com.itarocha.betesda.report.RelatorioAtendimentos;
import br.com.itarocha.betesda.service.HospedagemService;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import br.com.itarocha.betesda.util.validation.ResultError;

@RestController
@RequestMapping("/api/app/hospedagem")
public class HospedagemController {

	@Autowired
	private HospedagemService service;
	
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody HospedagemVO model) {
		ItaValidator<HospedagemVO> v = new ItaValidator<HospedagemVO>(model);
		v.validate();
		
		if (model.getHospedes().size() == 0) {
			v.addError("id", "É necessário pelo menos um hóspede");
		} else {
			for (HospedeVO h : model.getHospedes()) {
				if ("T".equals(model.getTipoUtilizacao()) && (h.getAcomodacao() == null)) {
					v.addError("id", String.format("É necessário informar o Leito para o Hóspede [%s]", h.getPessoaNome()));
				}
				/*
				if (!service.pessoaLivre(h.getPessoaId())) {
					v.addError("id", String.format("[%s] está utilizando uma Hospedagem ainda pendente", h.getPessoaNome()));
				}
				*/
				if ("T".equals(model.getTipoUtilizacao()) && (h.getAcomodacao() != null) && 
					(h.getAcomodacao().getLeitoId() != null) && 
					(model.getDataEntrada() != null) && (model.getDataPrevistaSaida() != null) )
				{
					Long leitoId = h.getAcomodacao().getLeitoId();
					LocalDate dataIni = model.getDataEntrada();
					LocalDate dataFim = model.getDataPrevistaSaida();
					Integer leitoNumero = h.getAcomodacao().getLeitoNumero();
					Integer quartoNumero = h.getAcomodacao().getQuartoNumero();
					
					if (!service.leitoLivreNoPeriodo(leitoId, dataIni, dataFim)) {
						v.addError("id", String.format("Quarto %s Leito %s está ocupado no perído", quartoNumero, leitoNumero ));
					}
				}
			}
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			service.create(model);
		    return new ResponseEntity<HospedagemVO>(model, HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value="/mapa", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaRetorno mapaNew(@RequestBody MapaHospedagemRequest model)
	{
		MapaRetorno retorno = service.buildMapaRetorno(model.data);
		return retorno;
	}

	@RequestMapping(value="/planilha_geral", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> planilhaGeral(@RequestBody PeriodoRequest model)
	{
		try {
			RelatorioAtendimentos retorno = service.buildPlanilhaGeral(model.dataIni, model.dataFim);
			return new ResponseEntity<RelatorioAtendimentos>(retorno, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value="/leitos_ocupados", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> leitosOcupados(@RequestBody HospedagemPeriodoRequest model)
	{
		try {
			List<OcupacaoLeito> retorno = service.getLeitosOcupadosNoPeriodo(model.hospedagemId, model.dataIni, model.dataFim);
			
			//List<Long> retorno = service.getLeitosOcupadosNoPeriodo(model.hospedagemId, model.dataIni, model.dataFim);
			return new ResponseEntity<List<OcupacaoLeito>>(retorno, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="/mapa/alterar_hospede", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> alterarHospede(@RequestBody AlteracaoHospedeRequest model)
	{
		try {
			service.alterarTipoHospede(model.hospedeId, model.tipoHospedeId);
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value="/mapa/encerramento", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> encerramento(@RequestBody OperacoesRequest model)
	{
		try {
			service.encerrarHospedagem(model.hospedagemId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/mapa/renovacao", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> renovacao(@RequestBody OperacoesRequest model)
	{
		try {
			service.renovarHospedagem(model.hospedagemId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/remover_hospede", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> removerHospede(@RequestBody RemoverHospedeRequest model)
	{
		try {
			service.removerHospede(model.hospedagemId, model.hospedeId);
			return new ResponseEntity<String>("ok", HttpStatus.OK); 
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/mapa/baixar", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> baixar(@RequestBody BaixaRequest model)
	{
		try {
			service.baixarHospede(model.hospedeId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK); 
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/mapa/transferir", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> transferir(@RequestBody TransferenciaRequest model)
	{
		try {
			service.transferirHospede(model.hospedeId, model.leitoId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK); 
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/mapa/adicionar", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> adicionarHospede(@RequestBody AdicionarHospedeRequest model)
	{
		try {
			service.adicionarHospede(model.hospedagemId, model.pessoaId, model.tipoHospedeId, model.leitoId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK); 
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/mapa/hospedagem_info", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public HospedagemFullVO getHospedagemInfo(@RequestBody HospdeagemInfoRequest model)
	{
		HospedagemFullVO h = service.getHospedagemPorHospedeLeitoId(model.hospedagemId);
		return h;
	}

	@RequestMapping(value="{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.excluirHospedagem(id);
		    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	private static class MapaHospedagemRequest{
		public LocalDate data;
	}

	private static class HospedagemPeriodoRequest{
		public Long hospedagemId;
		public LocalDate dataIni;
		public LocalDate dataFim;
	}
	
	private static class PeriodoRequest{
		public LocalDate dataIni;
		public LocalDate dataFim;
	}

	private static class BaixaRequest{
		public LocalDate data;
		public Long hospedeId;
	}
	
	private static class TransferenciaRequest{
		public LocalDate data;
		public Long hospedeId;
		public Long leitoId;
	}
	
	private static class AlteracaoHospedeRequest {
		public Long hospedeId;
		public Long tipoHospedeId;
	}
	
	private static class AdicionarHospedeRequest{
		public Long hospedagemId;
		public Long pessoaId;
		public Long tipoHospedeId;
		public LocalDate data;
		public Long leitoId;
	}

	private static class RemoverHospedeRequest{
		public Long hospedagemId;
		public Long hospedeId;
	}
	
	private static class OperacoesRequest{
		public LocalDate data;
		public Long hospedagemId;
	}

	private static class HospdeagemInfoRequest{
		public Long hospedagemId;
	}
}
