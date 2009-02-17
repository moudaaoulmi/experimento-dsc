package healthwatcher.view.servlets;

import healthwatcher.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.util.HTMLCode;
import lib.util.Library;


public class ServletWebServer extends HWServlet {

	private String[] keywords = { "##SYSTEM_ROOT##", "##SYSTEM_ACTION##",
			"##SERVLET_SERVER_PATH##", "##CLOSE##" };

	private String[] newWords = { Constants.SYSTEM_ROOT,
			Constants.SYSTEM_ACTION, Constants.SERVLET_SERVER_PATH,
			HTMLCode.closeAdministrator() };

    public void doGet(HttpServletRequest request, HttpServletResponse response)  {

        PrintWriter out = null;

        response.setContentType("text/html");

        String file = request.getParameter("file");
        
        if (file == null) file = "index.html";
        
        try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			out.println(Library.getFileListReplace(keywords, newWords, Constants.FORM_PATH+file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        out.close();
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        PrintWriter out = null;

        response.setContentType("text/html");

        String file = request.getParameter("file");

        try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			out.println(Library.getFileListReplace(keywords, newWords, Constants.FORM_PATH+file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        out.close();
    }
}