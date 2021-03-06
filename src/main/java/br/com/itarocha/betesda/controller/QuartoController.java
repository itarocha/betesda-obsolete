package br.com.itarocha.betesda.controller;

import java.util.ArrayList;
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

import br.com.itarocha.betesda.model.EditLeitoVO;
import br.com.itarocha.betesda.model.EditQuartoVO;
import br.com.itarocha.betesda.model.Leito;
import br.com.itarocha.betesda.model.NovoQuartoVO;
import br.com.itarocha.betesda.model.Quarto;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.service.DestinacaoHospedagemService;
import br.com.itarocha.betesda.service.EntidadeService;
import br.com.itarocha.betesda.service.QuartoService;
import br.com.itarocha.betesda.service.SituacaoLeitoService;
import br.com.itarocha.betesda.service.TipoHospedeService;
import br.com.itarocha.betesda.service.TipoLeitoService;
import br.com.itarocha.betesda.service.TipoServicoService;
import br.com.itarocha.betesda.util.validation.ItaValidator;

@RestController
@RequestMapping("/api/app/quarto")
public class QuartoController {

	@Autowired
	private QuartoService service;

	@Autowired
	private TipoLeitoService tls;

	@Autowired
	private DestinacaoHospedagemService dhs;
	
	@Autowired
	private SituacaoLeitoService sls;
	
	@Autowired
	private TipoHospedeService ths;
	
	@Autowired
	private TipoServicoService tss;
	
	@Autowired
	private EntidadeService etds;
	
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<Quarto> lista = service.findAll();
		return new ResponseEntity<List<Quarto>>(lista, HttpStatus.OK);
	}

	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			Quarto model = service.find(id);
			if (model != null) {
				return new ResponseEntity<>(model, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Quarto não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/leito/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getLeitoById(@PathVariable("id") Long id) {
		try {
			Leito model = service.findLeito(id);
			if (model != null) {
				EditLeitoVO leito = new EditLeitoVO();
				leito.setId(model.getId());
				leito.setNumero(model.getNumero());
				leito.setQuartoId(model.getQuarto().getId());
				leito.setQuartoNumero(model.getQuarto().getNumero());
				leito.setTipoLeito(model.getTipoLeito().getId());
				leito.setSituacao(model.getSituacao().getId());
				
				return new ResponseEntity<>(leito, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Leito não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping("/por_destinacao_hospedagem/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarByDestinacaoHospedagem(@PathVariable("id") Long id) {
		List<Quarto> lista = service.findAllByDestinacaoHospedagem(id);
		return new ResponseEntity<List<Quarto>>(lista, HttpStatus.OK);
	}

	@RequestMapping("/{id}/leitos")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarLeitosByQuarto(@PathVariable("id") Long id) {
		List<Leito> lista = service.findLeitosByQuarto(id);
		return new ResponseEntity<List<Leito>>(lista, HttpStatus.OK);
	}

	@RequestMapping("/leitos_disponiveis")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarLeitosDisponiveis() {
		List<Leito> lista = service.findLeitosDisponiveis();
		return new ResponseEntity<List<Leito>>(lista, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody NovoQuartoVO model) throws Exception {
		ItaValidator<NovoQuartoVO> v = new ItaValidator<NovoQuartoVO>(model);
		v.validate();
		if (service.existeOutroQuartoComEsseNumero(model.getNumero())) {
			v.addError("numero", "Existe outro Quarto com esse número");
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
	
		// TODO tratar exceção
		Quarto saved = null;
		saved = service.create(model);
	    //return Response.status(200).entity(saved).build();
	    return new ResponseEntity<Quarto>(saved, HttpStatus.OK);
	}
	
	@RequestMapping(value="/alterar", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravarAlteracao(@RequestBody EditQuartoVO model) {
		ItaValidator<EditQuartoVO> v = new ItaValidator<EditQuartoVO>(model);
		v.validate();
		try {
			if (model.getId() != null) {
				if (service.existeOutroQuartoComEsseNumero(model.getId(), model.getNumero())) {
					v.addError("numero", "Existe outro Quarto com esse número");
				}
			}
			
			if (!v.hasErrors() ) {
				return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
			}
		
			Quarto saved = null;
			saved = service.update(model);
		    return new ResponseEntity<Quarto>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="/leito", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravarLeito(@RequestBody EditLeitoVO model) {
		ItaValidator<EditLeitoVO> v = new ItaValidator<EditLeitoVO>(model);
		v.validate();
		
		try {
			if (model.getId() == null) {
				if (service.existeOutroLeitoComEsseNumero(model.getQuartoId(), model.getNumero())) {
					v.addError("numero", "Existe outro Leito com esse número");
				}
			} else {
				if (service.existeOutroLeitoComEsseNumero(model.getId(), model.getQuartoId(), model.getNumero())) {
					v.addError("numero", "Existe outro Leito com esse número");
				}
			}
			
			if (!v.hasErrors() ) {
				return new ResponseEntity<>(v.getErrors(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
			Leito saved = null;
			saved = service.saveLeito(model);
			return new ResponseEntity<Leito>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
		}
	 }

	@RequestMapping(value="/leito/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluirLeito(@PathVariable("id") Long id) {
		try {
			service.removeLeito(id);
			return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@RequestMapping("/listas")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listas() {
		AutoWired retorno = new AutoWired();
		retorno.listaTipoLeito = tls.listSelect();
		
		retorno.listaDestinacaoHospedagem = dhs.listSelect();
		
		retorno.listaSituacaoLeito = sls.listSelect();
		
		retorno.listaTipoHospede = ths.listSelect();
		
		retorno.listaTipoServico = tss.listSelect();

		retorno.listaEntidade = etds.listSelect();
		
		return new ResponseEntity<AutoWired>(retorno, HttpStatus.OK);
	}
	
	static class AutoWired {
		public List<SelectValueVO> listaTipoLeito = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaDestinacaoHospedagem = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaSituacaoLeito = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaTipoHospede = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaTipoServico = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaEntidade = new ArrayList<SelectValueVO>();
	} 
	
}
