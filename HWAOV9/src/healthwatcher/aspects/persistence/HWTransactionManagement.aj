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
 * Created on Sep 17, 2006 by Thiago Tonelli Bartolomei
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
package healthwatcher.aspects.persistence;

import healthwatcher.business.HealthWatcherFacade;
import lib.exceptions.TransactionException;
import lib.persistence.IPersistenceMechanism;

/**
 * This aspect makes all methods in the facade transactionals and softens
 * the transaction exceptions in the persistence mechanism
 */
public aspect HWTransactionManagement {

    pointcut transactionalMethods(): execution(* HealthWatcherFacade.*(..)) && ! execution(static * *.*(..));
    
    declare soft: TransactionException : 
        call(void IPersistenceMechanism.beginTransaction())    || 
        call(void IPersistenceMechanism.rollbackTransaction()) || 
        call(void IPersistenceMechanism.commitTransaction());
    
    after() returning: transactionalMethods()  {
        getPm().commitTransaction();
    }
    
    after() throwing: transactionalMethods()  {
        getPm().rollbackTransaction();
    }
    
    before(): transactionalMethods() {
        getPm().beginTransaction();
    }
    
    public IPersistenceMechanism getPm() {
		return HWPersistence.aspectOf().getPm();
	}
}
