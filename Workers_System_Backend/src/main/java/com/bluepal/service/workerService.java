package com.bluepal.service;

import java.io.PrintWriter;
import java.util.List;

import com.bluepal.model.Workers;

import jakarta.servlet.http.HttpServletResponse;

public interface workerService {
	public Workers createWorkers(Workers c);
	public List<Workers> getAllWorkers();
	public Workers getWorkersById(Integer id);
	public void deleteWorkers(Integer id);
	public Workers updateWorkers(Integer id,Workers c);
	public void genrateExcel(HttpServletResponse response) throws Exception;
	public void genratePdf(HttpServletResponse response) throws Exception;
	public void genrateCsv(PrintWriter writer);

}
