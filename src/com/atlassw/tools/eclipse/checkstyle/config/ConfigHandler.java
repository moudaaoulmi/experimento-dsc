
package com.atlassw.tools.eclipse.checkstyle.config;

import com.atlassw.tools.eclipse.checkstyle.builder.PackageObjectFactory;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class ConfigHandler extends GeneralException
{
     
    public Object createModuleHandler(String aName, PackageObjectFactory class_)
        throws CheckstyleException
    {
        return class_.packageObjectFactory_createModuleHandler(aName);
    }

}
