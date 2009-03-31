/*
 * Created on 28/09/2005
 */
package com.sun.j2ee.blueprints.uidgen.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.aspectj.lang.SoftException;

import com.sun.j2ee.blueprints.uidgen.counter.ejb.CounterLocal;

/**
 * @author Raquel Maranhao
 */
public aspect UidgenEjbHandler {
	
	/*** UniqueIdGeneratorEJB ***/
	pointcut getCounterHandler() : 
		execution(private CounterLocal UniqueIdGeneratorEJB.getCounter(String));
	
	
	declare soft : FinderException : getCounterHandler();
	
	
	CounterLocal around(UniqueIdGeneratorEJB uid,String name) : 
		getCounterHandler() && args(name) && target(uid) {
		try {
			return proceed(uid,name);
        } catch (FinderException fe) {
            try {
            	return uid.clh.create(name);
            } catch(CreateException ex) {
            	throw new SoftException(ex);
            }
        }
	}
	
	after(String name) throwing(SoftException ce) throws EJBException : 
		getCounterHandler() && args(name) {
		throw new EJBException("Could not create counter " + name + ". Error: " + ce.getMessage());
	}

}
