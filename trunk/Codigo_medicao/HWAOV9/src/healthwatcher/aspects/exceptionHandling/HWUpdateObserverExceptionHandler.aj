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
 * Created on Sep 20, 2006 by Thiago Tonelli Bartolomei
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
package healthwatcher.aspects.exceptionHandling;

import healthwatcher.aspects.patterns.UpdateStateObserver;
import healthwatcher.view.command.*;

import java.io.IOException;
import java.io.PrintWriter;

import lib.util.HTMLCode;

import org.aspectj.lang.SoftException;

/**
 * This aspect deals with exceptions raised inside the observer pattern implementation.
 * 
 * Note that this aspect needs a lot of knowleadge about how the observer pattern affects
 * the application and how the application should deal with its exceptions.
 * 
 * @author Thiago Tonelli Bartolomei <thiago.bartolomei@gmail.com>
 */
public privileged aspect HWUpdateObserverExceptionHandler {

	// Makes soft all exceptions raised in the observer aspect
	declare soft : Exception : within(UpdateStateObserver);

	// Makes soft all IO exceptions raised in this aspect
	declare soft : IOException : within(HWUpdateObserverExceptionHandler+);
	
	void around(CommandServlet command) : 
		execution(void Update*Data.executeCommand(..)) && this(command){
		
		try {
			
			proceed(command);
			
        } catch (SoftException e) {
    		PrintWriter out = command.response.getWriter();
    		out.println(HTMLCode.errorPage(e.getWrappedThrowable().getMessage()));
    		out.close();
		}
	}
}
