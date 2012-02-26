@echo off
setlocal ENABLEDELAYEDEXPANSION

SET LIB_HOME=..\
SET PROPERTYPATH=.
SET JARPATH=.

SET CLASSPATH=%JARPATH%\commons-codec.jar;%JARPATH%\commons-httpclient-3.1.jar;%JARPATH%\commons-logging-1.1.jar;%JARPATH%\jackson-all-1.8.5.jar;%JARPATH%\weibo.jar;%JARPATH%\log4j-1.2.16.jar;%JARPATH%\msgpack-0.6.5.jar;%JARPATH%\weibousermanagement.jar;%JARPATH%\javassist.jar;%JARPATH%\lucene-core-3.5.0.jar;%JARPATH%\multilanecommonj.jar;%JARPATH%\lb.jar;.\
echo %CLASS_HOME%

FOR /R %LIB_HOME%\lib %%G IN (*.jar) DO set CLASSPATH=!CLASSPATH!;%%G

set JVM_ARGS=%CLUSTER_PROPS% -Xbootclasspath/p:%JARPATH%/jsr166.jar -XX:+UseParallelGC  -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -Xms512M -Xmx1024M -Djava.library.path=. -classic -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8888,server=y,suspend=n -Ddistribution.component.bootstraplist=com.starcite.marketplace.distribution.Bootstrap

echo ******************************************************************************
echo "java -classpath %CLASSPATH% com.starcite.marketplace.distribution.Bootstrap"
echo ******************************************************************************

java %JVM_ARGS% -classpath %CLASSPATH% com.julu.weibouser.Bootstrap ./runtime.properties
