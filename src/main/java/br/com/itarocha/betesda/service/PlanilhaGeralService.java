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

		String[] COLUMNs = {"Id", "Nome", "Endereço", "Idade", "Nascimento"};
		try(
				Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
			)
		{
			CreationHelper createHelper = workbook.getCreationHelper();

			Sheet sheet = workbook.createSheet("Planilha Geral");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(COLUMNs[col]);
				cell.setCellStyle(headerCellStyle);
			}

			// CellStyle for Age
			CellStyle ageCellStyle = workbook.createCellStyle();
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("0"));
			
			CellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy"));

			int[] rowIdx = {1};
			dados.getPlanilhaGeral().forEach(h -> {
				Row row = sheet.createRow(rowIdx[0]++);

				row.createCell(0).setCellValue(h.getPessoaId());
				row.createCell(1).setCellValue(h.getPessoaNome());
				row.createCell(2).setCellValue(h.getPessoaEndereco());

				Cell ageCell = row.createCell(3);
				ageCell.setCellValue(h.getPessoaIdade());
				ageCell.setCellStyle(ageCellStyle);
				
				Cell dateCell = row.createCell(4);
				Date date = Date.from(h.getPessoaDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant());
				dateCell.setCellValue(date);
				dateCell.setCellStyle(dateCellStyle);
			});
			
			for (int col = 0; col < COLUMNs.length; col++) {
				sheet.autoSizeColumn(col);
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

