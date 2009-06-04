/*
 * Created on 28/09/2005
 */
package com.sun.j2ee.blueprints.uidgen.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import com.sun.j2ee.blueprints.uidgen.counter.ejb.CounterLocal;
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect UidgenEjbHandler {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : FinderException : uniquedIdGeneratorEJB_internalGetCounterHandler();
	declare soft : CreateException : uniquedIdGeneratorEJB_getCounter();
	declare soft : NamingException : uniquedIdGeneratorEJB_ejbCreate();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** UniqueIdGeneratorEJB ***/
	pointcut uniquedIdGeneratorEJB_internalGetCounterHandler() : 
		execution(private CounterLocal UniqueIdGeneratorEJB.internalGetCounter(String,CounterLocal));
	
	pointcut uniquedIdGeneratorEJB_getCounter() : 
		execution(private CounterLocal UniqueIdGeneratorEJB.getCounter(String));
	
	pointcut uniquedIdGeneratorEJB_ejbCreate() : 
		execution(public void UniqueIdGeneratorEJB.ejbCreate());
	
	// ---------------------------
    // Advice's
    // ---------------------------		
	CounterLocal around (String name, CounterLocal counter) throws CreateException : 
		uniquedIdGeneratorEJB_internalGetCounterHandler() &&
		args (name, counter){
		try{
			counter = proceed(name, counter);
		} catch (FinderException fe) {
			UniqueIdGeneratorEJB uid = (UniqueIdGeneratorEJB) thisJoinPoint.getThis();
		    counter = uid.clh.create(name);
		}
		return counter;
	}
	
	CounterLocal around (String name) : 
		uniquedIdGeneratorEJB_getCounter() &&
		args(name){
		try{
			return proceed(name);
		} catch (CreateException ce) {
            throw new EJBException("Could not create counter " + name + ". Error: " + ce.getMessage());
        }
	}

	void around () : uniquedIdGeneratorEJB_ejbCreate(){
		try{
			proceed();
		} catch (NamingException ne) {
	         throw new EJBException("UniqueIdGeneratorEJB Got naming exception! " + ne.getMessage());
	    }
	}
}
