@echo off

SETLOCAL

if "%JAVA_HOME%" == "" goto error

set BASE=%~dp0..
if "%BASE%"=="" set BASE=.

for %%i in ("%BASE%\lib\*.jar") do call :cpSuma %%i

%JAVA_HOME%\bin\java.exe %JAVA_OPTS% -classpath "%LOCALCLASSPATH%" org.tigris.aopmetrics.AopMetricsCLI %1 %2 %3 %4 %5 %6 %7 %8 %9

goto end

:cpSuma
 set LOCALCLASSPATH=%LOCALCLASSPATH%;%1
 GOTO end


:error

echo ERROR: JAVA_HOME not found in your environment.
echo Please, set the JAVA_HOME variable in your environment to match the
echo location of the Java Virtual Machine you want to use.

goto end

:end

ENDLOCAL