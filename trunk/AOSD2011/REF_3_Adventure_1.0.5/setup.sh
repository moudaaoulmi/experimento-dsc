#! /bin/sh
# $Id: setup.sh,v 1.7 2004/08/06 00:12:05 gmurray71 Exp $
$J2EE_HOME/bin/asant -Dj2ee.home=$J2EE_HOME -buildfile project/setup/setup.xml "$@"

