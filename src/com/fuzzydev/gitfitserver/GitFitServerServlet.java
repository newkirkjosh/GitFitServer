package com.fuzzydev.gitfitserver;

import java.io.IOException;
import javax.servlet.http.*;

import org.omg.CORBA.portable.InputStream;

@SuppressWarnings("serial")
public class GitFitServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		InputStream in = req.getInputStream();
		in.read_string();
	}
	

}
