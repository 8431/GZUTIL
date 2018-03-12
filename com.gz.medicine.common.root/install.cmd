echo off
set JAVA_HOME=C:\work\java\jdk1.6.0_24
set M2_HOME=C:\work\Java\apache-maven-3.0.3
set ANT_HOME=C:\work\Java\apache-ant-1.8.2

set PATHP=%PATH%
set PATH=%PATH%;%M2_HOME%\bin;%ANT_HOME%\bin

set configInstance=%1

if '%1'=='' set configInstance=NODE1

set skipTest=%2

if '%2'=='' set skipTest=true

echo on

echo skipTest = %skipTest%

echo configInstance = %configInstance%

mvn clean install -Dmaven.test.skip=%skipTest% -Dassembly.skipAssembly=true


set PATH=%PATHP%
