/* Copyright 2004 Sun Microsystems, Inc.  All rights reserved.  You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: 
 http://adventurebuilder.dev.java.net/LICENSE.txt
 $Id: ActivityDetailsLocalHome.java,v 1.2 2004/05/26 00:05:59 inder Exp $ */
package com.sun.j2ee.blueprints.activitysupplier.purchaseorder.ejb;

import javax.ejb.*;
import java.util.*;

/**
 * Local Home Interface for the ActivityDetailsBean.
 **/

public interface ActivityDetailsLocalHome  extends EJBLocalHome {

  public ActivityDetailsLocal create(String id, Calendar start,
         Calendar end, int count) throws CreateException;
  public ActivityDetailsLocal findByPrimaryKey(Object k) 
                                                    throws FinderException;
}
