package com.fuzzydev.gitfitserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class GitFitServerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(GitFitServerServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("THIS SHIT KRAY");	
		String name = req.getParameter("firstName");
		log.log(Level.INFO, name);
	}
}
