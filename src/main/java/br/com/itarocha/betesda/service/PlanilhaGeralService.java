package br.com.itarocha.betesda.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.itarocha.betesda.report.ChaveValor;
import br.com.itarocha.betesda.report.RelatorioAtendimentos;

public class PlanilhaGeralService {

	public static ByteArrayInputStream toExcel(RelatorioAtendimentos dados) throws IOException {

		String[] colsPlanilhaGeral = {	"Atendimento", "Pessoa", "Nome", "Endereço", "Nascimento", "Idade", "Faixa Etária", 
										"Tipo", "Encaminhador", "CPF", "RG", "Telefone", "Endereço", "Cidade de Origem", "UF Origem",
										"Tipo de Hospedagem", "Dt. Ingresso", "Dt. Desligamento", "Dias"};
		
		
		String[] colsPessoas = {"Id", "Nome", "Faixa Etária", "Cidade Origem", "UF", "Encaminhador", "Tipo Utilização", "Tipo de Hóspede"};

		String[] colsCidades = { "Cidade", "UF", "Quantidade"};

		String[] colsEncaminhadores = { "Nome", "Quantidade"};

		String[] colsAtividades = { "Descrição", "Quantidade"};
		
		try(
				Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
			)
		{
			CreationHelper createHelper = workbook.getCreationHelper();

			// Estilos
			CellStyle ageCellStyle = workbook.createCellStyle();
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("0"));
			
			CellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy"));

			CellStyle diaMesCellStyle = workbook.createCellStyle();
			diaMesCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/mm"));
			
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.GREEN.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
			headerCellStyle.setFont(headerFont);
			
			Font fontNegrito = workbook.createFont();
			fontNegrito.setBold(true);
			fontNegrito.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerNegritoStyle = workbook.createCellStyle();
			headerNegritoStyle.setFont(fontNegrito);
			//headerNegritoStyle.setFillBackgroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
			
			
			// Planilha Geral
			Sheet sheetPlanilhaGeral = workbook.createSheet("Planilha Geral");

			Row headerRow = sheetPlanilhaGeral.createRow(0);

			// Header
			for (int col = 0; col < colsPlanilhaGeral.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(colsPlanilhaGeral[col]);
				cell.setCellStyle(headerCellStyle);
			}


			int[] rowIdx = {1};
			dados.getPlanilhaGeral().forEach(h -> {
				Row row = sheetPlanilhaGeral.createRow(rowIdx[0]++);

				row.createCell(0).setCellValue(h.getHospedagemId());
				row.createCell(1).setCellValue(h.getPessoaId());
				row.createCell(2).setCellValue(h.getPessoaNome());
				row.createCell(3).setCellValue(h.getPessoaEndereco());

				
				Cell dateCell = row.createCell(4);
				Date date = Date.from(h.getPessoaDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant());
				dateCell.setCellValue(date);
				dateCell.setCellStyle(dateCellStyle);

				Cell ageCell = row.createCell(5);
				ageCell.setCellValue(h.getPessoaIdade());
				ageCell.setCellStyle(ageCellStyle);
				
				row.createCell(6).setCellValue(h.getPessoaFaixaEtaria());
				row.createCell(7).setCellValue(h.getTipoUtilizacaoDescricao());
				row.createCell(8).setCellValue(h.getEncaminhadorNome());
				row.createCell(9).setCellValue(h.getPessoaCPF());
				row.createCell(10).setCellValue(h.getPessoaRG());
				row.createCell(11).setCellValue(h.getPessoaTelefone());

				row.createCell(12).setCellValue(h.getPessoaEndereco());
				row.createCell(13).setCellValue(h.getPessoaCidadeOrigem());
				row.createCell(14).setCellValue(h.getPessoaCidadeOrigemUF());
				row.createCell(15).setCellValue(h.getTipoHospede());
				
				Date dataEntrada = Date.from(h.getDataEntrada().atStartOfDay(ZoneId.systemDefault()).toInstant());
				row.createCell(16).setCellValue(dataEntrada);
				row.getCell(16).setCellStyle(diaMesCellStyle);
				
				Date dataSaida = Date.from(h.getDataSaida().atStartOfDay(ZoneId.systemDefault()).toInstant());
				row.createCell(17).setCellValue(dataSaida);
				row.getCell(17).setCellStyle(diaMesCellStyle);
				
				row.createCell(18).setCellValue(h.getDiasPermanencia());
				row.getCell(18).setCellStyle(ageCellStyle);
				
			});
			
			for (int col = 0; col < colsPlanilhaGeral.length; col++) {
				sheetPlanilhaGeral.autoSizeColumn(col);
			}

			///// PESSOAS
			Sheet sheetPessoas = workbook.createSheet("Pessoas Atendidas");

			Row rowHeaderPessoas = sheetPessoas.createRow(0);

			// Header
			for (int col = 0; col < colsPessoas.length; col++) {
				Cell cell = rowHeaderPessoas.createCell(col);
				cell.setCellValue(colsPessoas[col]);
				cell.setCellStyle(headerCellStyle);
			}


			int[] rowPessoa = {1};
			dados.getListaPessoas().forEach(h -> {
				Row row = sheetPessoas.createRow(rowPessoa[0]++);

				//String[] colsPessoas = {"Id", "Nome", "Faixa Etária", "Cidade Origem", "UF", "Encaminhador", "Tipo Utilização", "Tipo de Hóspede"};
				
				row.createCell(0).setCellValue(h.getId());
				row.createCell(1).setCellValue(h.getNome());
				row.createCell(2).setCellValue(h.getFaixaEtaria());
				row.createCell(3).setCellValue(h.getCidadeOrigem());
				row.createCell(4).setCellValue(h.getCidadeOrigemUf());
				row.createCell(5).setCellValue(h.getEncaminhadorNome());
				row.createCell(6).setCellValue(h.getTipoUtilizacaoDescricao());
				row.createCell(7).setCellValue(h.getTipoHospede());
			});
			
			for (int col = 0; col < colsPessoas.length; col++) {
				sheetPessoas.autoSizeColumn(col);
			}
			//////
			
			
			
			// Ranking de Cidades
			Sheet sheetCidades = workbook.createSheet("Ranking de Cidades");

			Row hrCidades = sheetCidades.createRow(0);

			// Header
			for (int col = 0; col < colsCidades.length; col++) {
				Cell cell = hrCidades.createCell(col);
				cell.setCellValue(colsCidades[col]);
				cell.setCellStyle(headerCellStyle);
			}

			int[] rowIdxCidades = {1};
			dados.getRankingCidades().forEach(h -> {
				Row row = sheetCidades.createRow(rowIdxCidades[0]++);

				row.createCell(0).setCellValue(h.getCidade() );
				row.createCell(1).setCellValue(h.getUf());
				row.createCell(2).setCellValue(h.getQuantidade());
				row.getCell(2).setCellStyle(ageCellStyle);
				
			});
			
			for (int col = 0; col < colsCidades.length; col++) {
				sheetCidades.autoSizeColumn(col);
			}
			
			// Ranking de Encaminhadores
			Sheet sheetEncaminhadores = workbook.createSheet("Ranking de Encaminhadores");

			Row hrEncaminhadores = sheetEncaminhadores.createRow(0);

			// Header
			for (int col = 0; col < colsEncaminhadores.length; col++) {
				Cell cell = hrEncaminhadores.createCell(col);
				cell.setCellValue(colsCidades[col]);
				cell.setCellStyle(headerCellStyle);
			}

			int[] rowIdxEncaminhadores = {1};
			dados.getRankingEncaminhamentos().forEach(h -> {
				Row row = sheetEncaminhadores.createRow(rowIdxEncaminhadores[0]++);

				row.createCell(0).setCellValue(h.getNome() );
				row.createCell(1).setCellValue(h.getQuantidade());
				row.getCell(1).setCellStyle(ageCellStyle);
				
			});
			
			for (int col = 0; col < colsEncaminhadores.length; col++) {
				sheetEncaminhadores.autoSizeColumn(col);
			}
			
			
			// Relatório de Atividades/Hospedagens
			Sheet sheetAtividades = workbook.createSheet("Atividades e Hospedagens");
			
			Row hrAtividades = sheetAtividades.createRow(0);

			// Header
			for (int col = 0; col < colsAtividades.length; col++) {
				Cell cell = hrAtividades.createCell(col);
				cell.setCellValue(colsAtividades[col]);
				cell.setCellStyle(headerNegritoStyle);
			}
			
			int[] rowAtividades = {1};
			
			dados.getAtividadesHospedagem().forEach(a -> {
				
				String titulo = a.getTitulo();
				
				List<ChaveValor> lista = a.getLista();
				
				// Cabeçalho
				Row rSubTit = sheetAtividades.createRow(rowAtividades[0]++);
				Cell cell = rSubTit.createCell(0);  
				cell.setCellStyle(headerNegritoStyle);
				cell.getCellStyle().setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());
				cell.setCellValue(titulo);
				//Setar para negrito
				
				lista.forEach(item -> {
					Row row = sheetAtividades.createRow(rowAtividades[0]++);
					row.createCell(0).setCellValue(item.getNome() );
					row.createCell(1).setCellValue(item.getQuantidade());
				});
				Row rEnd = sheetAtividades.createRow(rowAtividades[0]++);
				
			});
			for (int col = 0; col < colsAtividades.length; col++) {
				sheetAtividades.autoSizeColumn(col);
			}

			
			
			/*
			dados.getMapAtividadesHospedagem().keySet().forEach(titulo -> {
				
				List<ChaveValor> lista = dados.getMapAtividadesHospedagem().get(titulo);
				
				// Cabeçalho
				Row rSubTit = sheetAtividades.createRow(rowAtividades[0]++);
				Cell cell = rSubTit.createCell(0);  
				cell.setCellStyle(headerNegritoStyle);
				cell.getCellStyle().setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
				cell.setCellValue(titulo);
				//Setar para negrito
				
				lista.forEach(item -> {
					Row row = sheetAtividades.createRow(rowAtividades[0]++);
					row.createCell(0).setCellValue(item.getNome() );
					row.createCell(1).setCellValue(item.getQuantidade());
				});
				Row rEnd = sheetAtividades.createRow(rowAtividades[0]++);
				
			});
			*/
			
			// Finalmente...
			workbook.write(out);

			/*
			try (OutputStream xout = new FileOutputStream("/home/itamar/temp/teste_new.xls")) {
				workbook.write(xout);
			}
			*/
			
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
}

