package com.bluepal.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluepal.model.Workers;
import com.bluepal.repository.workerRepo;
import com.bluepal.utils.EmailUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class workerServiceImpl implements workerService {

	@Autowired
	private workerRepo repo;
	@Autowired
	private EmailUtils emailutils;
	@Override
	public Workers createWorkers(Workers c) {
		return repo.save(c);
		
	}

	@Override
	public List<Workers> getAllWorkers() {
		return repo.findAll();
	}

	@Override
	public Workers getWorkersById(Integer id) {
		return repo.findById(id).get() ;
	}
	
	@Override
	public void deleteWorkers(Integer id) {
		Workers citizen = repo.findById(id).get();
		if(citizen!=null) {
			repo.delete(citizen);
		}
	}

	@Override
	public Workers updateWorkers(Integer id, Workers c) {
		Workers oldCitizen=repo.findById(id).get();
		if(oldCitizen!= null)
		{
			c.setId(id);
			return repo.save(c);
		}
		return null;
	}

	@Override
	public void genrateExcel(HttpServletResponse response) throws Exception {
		List<Workers> list = repo.findAll();
		HSSFWorkbook workbook=new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow Headerrow = sheet.createRow(0);
		Headerrow.createCell(0).setCellValue("FirstName");
		Headerrow.createCell(1).setCellValue("LastName");
		Headerrow.createCell(2).setCellValue("Email");
		Headerrow.createCell(3).setCellValue("Address");
		Headerrow.createCell(4).setCellValue("Salary");
		
		int RowIndex=1;
		for(Workers record : list){
			HSSFRow DataRow = sheet.createRow(RowIndex);
			
			DataRow.createCell(0).setCellValue(record.getFirstName());
			DataRow.createCell(1).setCellValue(record.getLastName());
			DataRow.createCell(2).setCellValue(record.getEmail());
			DataRow.createCell(3).setCellValue(record.getAddress());
			DataRow.createCell(4).setCellValue(record.getSalary());
			

			RowIndex++;
			
		}
		File f =new File("Worker.xls");
		FileOutputStream fos=new FileOutputStream(f);
		workbook.write(fos);
		
		emailutils.sendEmail(f);
		
		ServletOutputStream outputStream= response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		fos.close();

		
	}

	@Override
	public void genratePdf(HttpServletResponse response) throws Exception {
		Document d =new Document(PageSize.A4);
		
		ServletOutputStream outputStream=response.getOutputStream();
		PdfWriter.getInstance(d, outputStream);
		d.open();
		
		Document d1= new Document(PageSize.A4);
		File f =new File("Worker.pdf");
		FileOutputStream fos =new FileOutputStream(f);
		PdfWriter.getInstance(d1, fos);
		d1.open();
		
		Font fontTitle =FontFactory.getFont(FontFactory.TIMES_BOLDITALIC);
		fontTitle.setSize(20);
		
		
		Paragraph p=new Paragraph("Workers Info ", fontTitle);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		
		d.add(p);
		d1.add(p);
		
		PdfPTable table= new PdfPTable(5);
		table.setWidthPercentage(100);
		table.setWidths(new int[] {3,3,3,3,3});
		
		PdfPCell cell=new PdfPCell();
		cell.setBackgroundColor(CMYKColor.GRAY);
		cell.setPadding(5);
		Font font =FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(CMYKColor.WHITE);
		
		cell.setPhrase(new Phrase("FirstName",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("LastName",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Address",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Salary",font));
		table.addCell(cell);
		
		

		List<Workers> records=repo.findAll();
		
		for(Workers record : records) {
			table.addCell(record.getFirstName());
			table.addCell(record.getLastName());
			table.addCell(record.getEmail());
			table.addCell(record.getAddress());
			table.addCell(record.getSalary());

		}
		d.add(table);
		d1.add(table);
		d.close();
		outputStream.close(); 
		
		d1.close();
		fos.close();
		
		
		emailutils.sendEmail(f);
	}
	

	
	public void genrateCsv(PrintWriter writer) {

        List<Workers> c = repo.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("ID", "First Name", "Last Name","Email","Department");
            for (Workers c1 : c) {
                csvPrinter.printRecord(c1.getFirstName(), c1.getLastName(), c1.getEmail(), c1.getAddress(), c1.getSalary());
            }
        } catch (IOException e) {
          System.out.printf("Error While writing CSV ", e.getMessage());
        }
    }

	
}
