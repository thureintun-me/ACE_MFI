package com.mfi.exporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mfi.model.Transaction;

public class ExcelFileExporter {
	
	public static ByteArrayInputStream exportTransactionFile(List<Transaction> transactionList) {
		try(Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Transaction");
			
			Row row = sheet.createRow(0);
			CellStyle headerCellStyle =workbook.createCellStyle();
			headerCellStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
//			creating header cells
			
			Cell cell = row.createCell(0);
			cell.setCellValue("Account Number");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(1);
			cell.setCellValue("COA ID");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(2);
			cell.setCellValue("Date");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(3);
			cell.setCellValue("Amount");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(4);
			cell.setCellValue("Transaction Type");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(5);
			cell.setCellValue("Account Name");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(6);
			cell.setCellValue("Narration");
			cell.setCellStyle(headerCellStyle);
			
//			Create data row for each 
			for(int i=0; i<transactionList.size(); i++) {
				Row datarow = sheet.createRow(i+1); //plus one for exclude the header
				datarow.createCell(0).setCellValue(transactionList.get(i).getAccountNumber());
				datarow.createCell(1).setCellValue(transactionList.get(i).getCoaId());
				datarow.createCell(2).setCellValue(transactionList.get(i).getCreatedDate());
				datarow.createCell(3).setCellValue(transactionList.get(i).getAmount());
				datarow.createCell(4).setCellValue(transactionList.get(i).getTransactionType());
				datarow.createCell(5).setCellValue(transactionList.get(i).getAccountName());
				datarow.createCell(6).setCellValue(transactionList.get(i).getNarration());
			}
			
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			
			return new ByteArrayInputStream(outputStream.toByteArray());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
