package com.atlassw.tools.eclipse.checkstyle.builder;


import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public privileged aspect PackageObjectFactoryHandle
{
    declare soft: ClassNotFoundException: createObjectHandle();
    declare soft: InstantiationException: createObjectHandle();
    declare soft: IllegalAccessException: createObjectHandle();
    
    pointcut createObjectHandle(): 
        execution (* PackageObjectFactory.createObject(..)) ;
    
    Object around(String aClassName)throws CheckstyleException: createObjectHandle() && args(aClassName) {
        Object result = null;
        try{
            result = proceed(aClassName);
        } catch (ClassNotFoundException e) {
            throw new CheckstyleException("Unable to find class for " + aClassName, e); //$NON-NLS-1$
        }catch (InstantiationException e) {
            // /CLOVER:OFF
            throw new CheckstyleException("Unable to instantiate " + aClassName, e); //$NON-NLS-1$
         // /CLOVER:ON
        }catch (IllegalAccessException e) {
            // /CLOVER:OFF
            throw new CheckstyleException("Unable to instantiate " + aClassName, e); //$NON-NLS-1$
         // /CLOVER:ON
        }
        return result;
    }
 
    declare soft: CheckstyleException: createModuleInternalHandle();
    
    pointcut createModuleInternalHandle(): 
        execution (* PackageObjectFactory.createModuleInternal(..)) ;
    
    Object around(String aName)throws CheckstyleException: createModuleInternalHandle() && args(aName) {
        Object result = null;
        try{
            result = proceed(aName);
        }catch(CheckstyleException e){
            throw new CheckstyleException("Unable to instantiate " + aName, e);//$NON-NLS-1$
        }
        return result;
    }
    
    declare soft: CheckstyleException: createModuleHandle();
    
    pointcut createModuleHandle(): 
        execution (* PackageObjectFactory.createModule(..)) ;
    
    Object around(String aName)throws CheckstyleException: createModuleHandle() && args(aName) {
        Object result = null;
        try{
            result = proceed(aName);
        }catch(CheckstyleException e){
            PackageObjectFactory p = (PackageObjectFactory) thisJoinPoint.getThis();
            result = p.createModuleInternal(aName);
        }
        return result;
    }
    
    declare soft: CheckstyleException: doMakeObjectHandle();
    
    pointcut doMakeObjectHandle(): 
        execution (* PackageObjectFactory.doMakeObject(..)) ;
    
    Object around(String aName)throws CheckstyleException: doMakeObjectHandle() && args(aName) {
        Object result = null;
        try{
            result = proceed(aName);
        }catch(CheckstyleException e){
            PackageObjectFactory p = (PackageObjectFactory) thisJoinPoint.getThis();
            result = p.doMakeObjectInternal(aName);
        }
        return result;
    }
    
    declare soft: CheckstyleException: doMakeObjectInternal2Handle();
    
    pointcut doMakeObjectInternal2Handle(): 
        execution (* PackageObjectFactory.doMakeObjectInternal2(..)) ;
    
    Object around(String className): doMakeObjectInternal2Handle() && args(className) {
        Object result = null;
        try{
            result = proceed(className);
        }catch(CheckstyleException e){
           //Do nothing
        }
        return result;
    }
    
}
