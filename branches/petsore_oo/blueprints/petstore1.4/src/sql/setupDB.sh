#
# Copyright 2004 Sun Microsystems, Inc. All rights reserved.
# SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
#

sqlplus -s system/none2tuff@vortex << ENDSQL
  whenever oserror exit 1
  whenever sqlerror exit 1 
  @createUser.sql
ENDSQL
sqlplus -s petstore/petstore@vortex << ENDSQL
  whenever oserror exit 1
  whenever sqlerror continue 
  @petstoredb.sql
ENDSQL
sqlplus -s opc/opc@vortex << ENDSQL
  whenever oserror exit 1
  whenever sqlerror continue 
  @opcdb.sql
ENDSQL
sqlplus -s supplier/supplier@vortex << ENDSQL
  whenever oserror exit 1
  whenever sqlerror continue 
  @supplierdb.sql
ENDSQL
