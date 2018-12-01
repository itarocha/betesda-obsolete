package br.com.itarocha.betesda.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.itarocha.betesda.model.Hospedagem;
import br.com.itarocha.betesda.model.HospedagemFullVO;
import br.com.itarocha.betesda.model.HospedagemVO;
import br.com.itarocha.betesda.model.HospedeVO;
import br.com.itarocha.betesda.model.hospedagem.MapaHospedagem;
import br.com.itarocha.betesda.service.HospedagemService;
import br.com.itarocha.betesda.util.validation.ItaValidator;

@CrossOrigin
@RestController
@RequestMapping("/api/hospedagem")
public class HospedagemController {

	@Autowired
	private HospedagemService service;
	
	@RequestMapping(method = RequestMethod.POST)
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
				
				if (!service.pessoaLivre(h.getPessoaId())) {
					v.addError("id", String.format("[%s] está utilizando uma Hospedagem ainda pendente", h.getPessoaNome()));
				}

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
			return new ResponseEntity<>(v.getErrors(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			service.create(model);
		    return new ResponseEntity<HospedagemVO>(model, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/mapa", method = RequestMethod.POST)
	public MapaHospedagem mapa(@RequestBody MapaHospedagemRequest model)
	{
		MapaHospedagem listagem = new MapaHospedagem();
		try {
			listagem  = service.getHospedagens(model.data);
		} catch(Exception e) {
			throw e;
		} 
		
		return listagem;
	}

	@RequestMapping(value="/mapa/encerramento", method = RequestMethod.POST)
	public ResponseEntity<?> encerramento(@RequestBody OperacoesRequest model)
	{
		//System.out.println("Recebido: "+model.hospedagemId + " e "+ model.data);
		try {
			service.encerrarHospedagem(model.hospedagemId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<String>("erro", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value="/mapa/hospedagem_info", method = RequestMethod.POST)
	public HospedagemFullVO getHospedagemInfo(@RequestBody HospdeagemInfoRequest model)
	{
		//System.out.println("Recebido: "+model.hospedeLeitoId);
		HospedagemFullVO h = service.getHospedagemPorHospedeLeitoId(model.hospedeLeitoId);
		return h;
		//return new Hospedagem();
	}

	@RequestMapping(value="/mapa/testes", method = RequestMethod.POST)
	public String testes(@RequestBody OperacoesRequest model)
	{
		System.out.println("Recebido: "+model.hospedagemId + " e "+ model.data);
		service.encerrarHospedagem(model.hospedagemId, model.data);
		
		return "Ok";
	}

	@RequestMapping(value="/xis")
	public String xis()
	{
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		/* 2018-08-14 ~ 2018-08-22 leito_id = 62  */
		LocalDate dIni = LocalDate.parse("14/08/2018", fmt);
		LocalDate dFim = LocalDate.parse("22/08/2018", fmt);
		Long leitoId = 62L; // deve retornar false porque contém 2 utilizações (no teste)

		String retorno = service.leitoLivreNoPeriodo(leitoId, dIni, dFim) ? "true" : "false";
		
		Long pessoaId = 6L;
		service.pessoaLivre(pessoaId);
		return retorno;
	}

	private static class MapaHospedagemRequest{
		public LocalDate data;
	}

	private static class OperacoesRequest{
		public LocalDate data;
		public Long hospedagemId;
	}

	private static class HospdeagemInfoRequest{
		public Long hospedeLeitoId;
	}
}
