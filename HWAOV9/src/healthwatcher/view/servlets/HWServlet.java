/**
 * Copyright (c) 2006 Macacos.org. All Rights Reserved
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation; either version 
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 * the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, 
 * MA  02110-1301  USA
 * 
 * You can find the license also here: http://www.gnu.org/copyleft/lesser.html
 * 
 * 
 * Created on Sep 16, 2006 by Thiago Tonelli Bartolomei
 * -------------------------------------------------
 *           \                                                                           
 *            \                                                                          
 *               __                                                                      
 *          w  c(..)o                                                                    
 *           \__(o)                                                                      
 *               /\                                                                      
 *            w_/(_)-~                                                                   
 *                /|                                                                     
 *               | \                                                                     
 *               m  m   
 */
package healthwatcher.view.servlets;


import java.io.IOException;

import healthwatcher.business.HealthWatcherFacade;
import healthwatcher.view.IFacade;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * TODO - describe this file
 * 
 * @author Thiago Tonelli Bartolomei <thiago.bartolomei@gmail.com>
 */
public class HWServlet extends HttpServlet {

	protected IFacade facade = null;

    public void init(ServletConfig config) throws ServletException {
    	facade = HealthWatcherFacade.getInstance();
    }
    
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
