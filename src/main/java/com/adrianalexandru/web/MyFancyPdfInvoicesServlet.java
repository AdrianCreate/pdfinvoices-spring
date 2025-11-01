package com.adrianalexandru.web;

import com.adrianalexandru.context.MyFancyPdfInvoicesApplicationConfiguration;
import com.adrianalexandru.model.Invoice;
import com.adrianalexandru.service.InvoiceService;
import com.adrianalexandru.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.List;

public class MyFancyPdfInvoicesServlet extends HttpServlet {
	
	private UserService userService;
	private ObjectMapper objectMapper;
	private InvoiceService invoiceService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyFancyPdfInvoicesApplicationConfiguration.class);
		
		ctx.registerShutdownHook();
		
		this.userService = ctx.getBean(UserService.class);
		this.objectMapper = ctx.getBean(ObjectMapper.class);
		this.invoiceService = ctx.getBean(InvoiceService.class);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getRequestURI().equalsIgnoreCase("/")) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(
					"<html>\n" +
							"<body>\n" +
							"<h1>Hello World</h1>\n" +
							"<p>This is my very first, embedded Tomcat, HTML Page!</p>\n" +
							"</body>\n" +
							"</html>");
		} else if (request.getRequestURI().equalsIgnoreCase("/invoices")) {
			response.setContentType("application/json; charset=UTF-8");
			List<Invoice> invoices = invoiceService.findAll();
			response.getWriter().print(objectMapper.writeValueAsString(invoices));
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getRequestURI().equalsIgnoreCase("/invoices")) {
			
			String userId = request.getParameter("user_id");
			Integer amount = Integer.parseInt(request.getParameter("amount"));
			
			Invoice invoice = invoiceService.create(userId, amount);
			
			response.setContentType("application/json; charset=UTF-8");
			String json = objectMapper.writeValueAsString(invoice);
			response.getWriter().print(json);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}