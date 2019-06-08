package br.com.itarocha.betesda.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

public class PlanilhaGeralService {

	public static ByteArrayInputStream toExcel() throws IOException {

		List<Customer> lista = new ArrayList<>();

		lista.add(new Customer(1L, "Raimundo Nonato", "Rua Alfa, 12", 54, LocalDate.of(1970, 05, 20) ));
		lista.add(new Customer(2L, "Joachina Moore", "Av Beta, 324", 26, LocalDate.of(1985, 02, 19)));
		lista.add(new Customer(3L, "Durval de Barros", "Rua das Flores, 856", 35, LocalDate.of(2014, 10, 4)));
		lista.add(new Customer(4L, "Martiniana Oliveira", "Alameda Sem Fim", 28, LocalDate.of(2001, 07, 13)));
		lista.add(new Customer(5L, "Gertrudes Paranhos", "Rua 10, 100", 40, LocalDate.of(2017, 4, 5)));


		String[] COLUMNs = {"Id", "Name", "Address", "Age", "Date"};
		try(
				Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
			)
		{
			CreationHelper createHelper = workbook.getCreationHelper();

			Sheet sheet = workbook.createSheet("Customers");

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
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
			
			CellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy"));

			int rowIdx = 1;
			for (Customer customer : lista) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(customer.getId());
				row.createCell(1).setCellValue(customer.getName());
				row.createCell(2).setCellValue(customer.getAddress());

				Cell ageCell = row.createCell(3);
				ageCell.setCellValue(customer.getAge());
				ageCell.setCellStyle(ageCellStyle);
				
				Cell dateCell = row.createCell(4);
				Date date = Date.from(customer.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
				dateCell.setCellValue(date);
				dateCell.setCellStyle(dateCellStyle);
				
			}

			workbook.write(out);

			try (OutputStream xout = new FileOutputStream("/home/itamar/temp/teste.xls")) {
				workbook.write(xout);
			}

			return new ByteArrayInputStream(out.toByteArray());
		}
	}
}

class Customer {

	private Long id;
	private String name;
	private String address;
	private int age;
	private LocalDate date;

	public Customer() {
	}

	public Customer(Long id, String name, String address, int age, LocalDate date) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.age = age;
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}

