package com.bluepal.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bluepal.model.Workers;
import com.bluepal.service.workerService;

import jakarta.servlet.http.HttpServletResponse;
@CrossOrigin(origins = "http://localhost:3000")

@Controller
@RequestMapping("/api/v1/")
public class workerController {
	@Autowired
	private workerService service;
	
	@PostMapping("/save")
	public ResponseEntity<Workers> createWorkers(@RequestBody Workers c)
	{
		return new ResponseEntity<Workers>(service.createWorkers(c),HttpStatus.CREATED );
		
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Workers>> getAllWorkers()
	{
	return new ResponseEntity<List<Workers>>(service.getAllWorkers(),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Workers> getWorkerById(@PathVariable Integer id)
	{
		return new ResponseEntity<Workers>(service.getWorkersById(id),HttpStatus.OK);
		
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<String> deleteWorkers(@PathVariable Integer id)
	
	{
		service.deleteWorkers(id);
		return new ResponseEntity<String>("Delete-Sucessfully",HttpStatus.OK);
	}
	
	@PostMapping("/update/{id}")
	public ResponseEntity<Workers> updateWorkers(@PathVariable Integer id,@RequestBody Workers c)
	{
		return new ResponseEntity<Workers>(service.createWorkers(c),HttpStatus.OK);
		
	}
	@GetMapping("/excel")
	public void downloadExcelSheet(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		String headKey="Content-Disposition";
		String headValue="attachment;filename=worker.xls";
		response.addHeader(headKey, headValue);
		service.genrateExcel(response);
	}
	
	@GetMapping("/pdf")
	public void downloadPdf(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");
		String headKey="Content-Disposition";
		String headValue="attachment;filename=worker.pdf";
		response.addHeader(headKey, headValue);
		service.genratePdf(response);
	}
	@GetMapping("/csv")
    public void getAllWorkersInCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"worker.csv\"");
        try (PrintWriter writer = servletResponse.getWriter()) {
        	service.genrateCsv(writer);
        } catch (IOException e) {
            System.out.printf("Error while writing CSV: %s", e.getMessage());
        }
    }
}
