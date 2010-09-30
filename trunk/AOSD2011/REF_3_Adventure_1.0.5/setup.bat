@echo off

%J2EE_HOME%\bin\asant -Dj2ee.home=%J2EE_HOME% -buildfile project/setup/setup.xml  %1 %2 %3 %4
