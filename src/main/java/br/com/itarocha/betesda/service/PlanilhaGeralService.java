package br.com.itarocha.betesda.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.itarocha.betesda.report.RelatorioAtendimentos;

public class PlanilhaGeralService {

	public static ByteArrayInputStream toExcel(RelatorioAtendimentos dados) throws IOException {

		String[] colsPlanilhaGeral = {	"Atendimento", "Pessoa", "Nome", "Endereço", "Nascimento", "Idade", "Faixa Etária", 
										"Tipo", "Encaminhador", "CPF", "RG", "Telefone", "Endereço", "Cidade de Origem", "UF Origem",
										"Tipo de Hospedagem", "Dt. Ingresso", "Dt. Desligamento", "Dias"};

		String[] colsCidades = { "Cidade", "UF", "Quantidade"};

		String[] colsEncaminhadores = { "Nome", "Quantidade"};
		
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
			headerCellStyle.setFont(headerFont);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
			
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

