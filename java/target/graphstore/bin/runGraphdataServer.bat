@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\conf;"%REPO%"\org\janusgraph\janusgraph-core\0.1.0\janusgraph-core-0.1.0.jar;"%REPO%"\org\apache\tinkerpop\gremlin-core\3.2.3\gremlin-core-3.2.3.jar;"%REPO%"\org\apache\tinkerpop\gremlin-shaded\3.2.3\gremlin-shaded-3.2.3.jar;"%REPO%"\org\yaml\snakeyaml\1.15\snakeyaml-1.15.jar;"%REPO%"\org\javatuples\javatuples\1.2\javatuples-1.2.jar;"%REPO%"\com\jcabi\jcabi-manifests\1.1\jcabi-manifests-1.1.jar;"%REPO%"\com\jcabi\jcabi-log\0.14\jcabi-log-0.14.jar;"%REPO%"\org\slf4j\jcl-over-slf4j\1.7.21\jcl-over-slf4j-1.7.21.jar;"%REPO%"\org\apache\tinkerpop\gremlin-groovy\3.2.3\gremlin-groovy-3.2.3.jar;"%REPO%"\org\apache\ivy\ivy\2.3.0\ivy-2.3.0.jar;"%REPO%"\org\codehaus\groovy\groovy\2.4.7\groovy-2.4.7-indy.jar;"%REPO%"\org\codehaus\groovy\groovy-groovysh\2.4.7\groovy-groovysh-2.4.7-indy.jar;"%REPO%"\org\codehaus\groovy\groovy\2.4.7\groovy-2.4.7.jar;"%REPO%"\jline\jline\2.12\jline-2.12.jar;"%REPO%"\org\codehaus\groovy\groovy-console\2.4.7\groovy-console-2.4.7.jar;"%REPO%"\org\codehaus\groovy\groovy-swing\2.4.7\groovy-swing-2.4.7.jar;"%REPO%"\org\codehaus\groovy\groovy-templates\2.4.7\groovy-templates-2.4.7.jar;"%REPO%"\org\codehaus\groovy\groovy-xml\2.4.7\groovy-xml-2.4.7.jar;"%REPO%"\org\codehaus\groovy\groovy-json\2.4.7\groovy-json-2.4.7-indy.jar;"%REPO%"\org\codehaus\groovy\groovy-jsr223\2.4.7\groovy-jsr223-2.4.7-indy.jar;"%REPO%"\org\apache\commons\commons-lang3\3.3.1\commons-lang3-3.3.1.jar;"%REPO%"\com\github\jeremyh\jBCrypt\jbcrypt-0.4\jBCrypt-jbcrypt-0.4.jar;"%REPO%"\org\apache\tinkerpop\tinkergraph-gremlin\3.2.3\tinkergraph-gremlin-3.2.3.jar;"%REPO%"\org\glassfish\javax.json\1.0\javax.json-1.0.jar;"%REPO%"\com\codahale\metrics\metrics-core\3.0.1\metrics-core-3.0.1.jar;"%REPO%"\com\codahale\metrics\metrics-ganglia\3.0.1\metrics-ganglia-3.0.1.jar;"%REPO%"\info\ganglia\gmetric4j\gmetric4j\1.0.3\gmetric4j-1.0.3.jar;"%REPO%"\com\codahale\metrics\metrics-graphite\3.0.1\metrics-graphite-3.0.1.jar;"%REPO%"\org\reflections\reflections\0.9.9-RC1\reflections-0.9.9-RC1.jar;"%REPO%"\org\javassist\javassist\3.16.1-GA\javassist-3.16.1-GA.jar;"%REPO%"\dom4j\dom4j\1.6.1\dom4j-1.6.1.jar;"%REPO%"\xml-apis\xml-apis\1.0.b2\xml-apis-1.0.b2.jar;"%REPO%"\com\spatial4j\spatial4j\0.4.1\spatial4j-0.4.1.jar;"%REPO%"\commons-collections\commons-collections\3.2.2\commons-collections-3.2.2.jar;"%REPO%"\commons-configuration\commons-configuration\1.10\commons-configuration-1.10.jar;"%REPO%"\commons-io\commons-io\2.3\commons-io-2.3.jar;"%REPO%"\com\google\guava\guava\18.0\guava-18.0.jar;"%REPO%"\com\carrotsearch\hppc\0.7.1\hppc-0.7.1.jar;"%REPO%"\com\github\stephenc\high-scale-lib\high-scale-lib\1.1.4\high-scale-lib-1.1.4.jar;"%REPO%"\com\google\code\findbugs\jsr305\3.0.0\jsr305-3.0.0.jar;"%REPO%"\org\slf4j\slf4j-api\1.7.12\slf4j-api-1.7.12.jar;"%REPO%"\org\janusgraph\janusgraph-hbase\0.1.0\janusgraph-hbase-0.1.0.jar;"%REPO%"\org\slf4j\slf4j-log4j12\1.7.12\slf4j-log4j12-1.7.12.jar;"%REPO%"\log4j\log4j\1.2.16\log4j-1.2.16.jar;"%REPO%"\ch\qos\logback\logback-classic\1.1.2\logback-classic-1.1.2.jar;"%REPO%"\ch\qos\logback\logback-core\1.1.2\logback-core-1.1.2.jar;"%REPO%"\org\apache\hbase\hbase-client\0.98.2-hadoop2\hbase-client-0.98.2-hadoop2.jar;"%REPO%"\org\apache\hbase\hbase-common\0.98.2-hadoop2\hbase-common-0.98.2-hadoop2.jar;"%REPO%"\org\apache\hbase\hbase-protocol\0.98.2-hadoop2\hbase-protocol-0.98.2-hadoop2.jar;"%REPO%"\commons-lang\commons-lang\2.6\commons-lang-2.6.jar;"%REPO%"\commons-logging\commons-logging\1.1.1\commons-logging-1.1.1.jar;"%REPO%"\com\google\protobuf\protobuf-java\2.5.0\protobuf-java-2.5.0.jar;"%REPO%"\io\netty\netty\3.6.6.Final\netty-3.6.6.Final.jar;"%REPO%"\org\apache\zookeeper\zookeeper\3.4.6\zookeeper-3.4.6.jar;"%REPO%"\org\cloudera\htrace\htrace-core\2.04\htrace-core-2.04.jar;"%REPO%"\org\mortbay\jetty\jetty-util\6.1.26\jetty-util-6.1.26.jar;"%REPO%"\org\codehaus\jackson\jackson-mapper-asl\1.8.8\jackson-mapper-asl-1.8.8.jar;"%REPO%"\org\codehaus\jackson\jackson-core-asl\1.8.8\jackson-core-asl-1.8.8.jar;"%REPO%"\org\apache\hadoop\hadoop-common\2.2.0\hadoop-common-2.2.0.jar;"%REPO%"\commons-cli\commons-cli\1.2\commons-cli-1.2.jar;"%REPO%"\org\apache\commons\commons-math\2.1\commons-math-2.1.jar;"%REPO%"\xmlenc\xmlenc\0.52\xmlenc-0.52.jar;"%REPO%"\commons-httpclient\commons-httpclient\3.1\commons-httpclient-3.1.jar;"%REPO%"\commons-net\commons-net\3.1\commons-net-3.1.jar;"%REPO%"\org\mortbay\jetty\jetty\6.1.26\jetty-6.1.26.jar;"%REPO%"\com\sun\jersey\jersey-core\1.9\jersey-core-1.9.jar;"%REPO%"\com\sun\jersey\jersey-json\1.9\jersey-json-1.9.jar;"%REPO%"\org\codehaus\jettison\jettison\1.1\jettison-1.1.jar;"%REPO%"\stax\stax-api\1.0.1\stax-api-1.0.1.jar;"%REPO%"\com\sun\xml\bind\jaxb-impl\2.2.3-1\jaxb-impl-2.2.3-1.jar;"%REPO%"\javax\xml\bind\jaxb-api\2.2.2\jaxb-api-2.2.2.jar;"%REPO%"\javax\activation\activation\1.1\activation-1.1.jar;"%REPO%"\org\codehaus\jackson\jackson-jaxrs\1.8.3\jackson-jaxrs-1.8.3.jar;"%REPO%"\org\codehaus\jackson\jackson-xc\1.8.3\jackson-xc-1.8.3.jar;"%REPO%"\commons-el\commons-el\1.0\commons-el-1.0.jar;"%REPO%"\net\java\dev\jets3t\jets3t\0.6.1\jets3t-0.6.1.jar;"%REPO%"\org\apache\avro\avro\1.7.4\avro-1.7.4.jar;"%REPO%"\com\thoughtworks\paranamer\paranamer\2.3\paranamer-2.3.jar;"%REPO%"\org\xerial\snappy\snappy-java\1.0.4.1\snappy-java-1.0.4.1.jar;"%REPO%"\com\jcraft\jsch\0.1.42\jsch-0.1.42.jar;"%REPO%"\org\apache\commons\commons-compress\1.4.1\commons-compress-1.4.1.jar;"%REPO%"\org\tukaani\xz\1.0\xz-1.0.jar;"%REPO%"\org\apache\hadoop\hadoop-auth\2.2.0\hadoop-auth-2.2.0.jar;"%REPO%"\org\apache\hadoop\hadoop-mapreduce-client-core\2.2.0\hadoop-mapreduce-client-core-2.2.0.jar;"%REPO%"\org\apache\hadoop\hadoop-yarn-common\2.2.0\hadoop-yarn-common-2.2.0.jar;"%REPO%"\org\apache\hadoop\hadoop-yarn-api\2.2.0\hadoop-yarn-api-2.2.0.jar;"%REPO%"\com\google\inject\guice\3.0\guice-3.0.jar;"%REPO%"\javax\inject\javax.inject\1\javax.inject-1.jar;"%REPO%"\aopalliance\aopalliance\1.0\aopalliance-1.0.jar;"%REPO%"\com\sun\jersey\jersey-server\1.9\jersey-server-1.9.jar;"%REPO%"\asm\asm\3.1\asm-3.1.jar;"%REPO%"\com\sun\jersey\contribs\jersey-guice\1.9\jersey-guice-1.9.jar;"%REPO%"\com\google\inject\extensions\guice-servlet\3.0\guice-servlet-3.0.jar;"%REPO%"\org\apache\hadoop\hadoop-annotations\2.2.0\hadoop-annotations-2.2.0.jar;"%REPO%"\com\github\stephenc\findbugs\findbugs-annotations\1.3.9-1\findbugs-annotations-1.3.9-1.jar;"%REPO%"\org\slf4j\slf4j-simple\1.7.21\slf4j-simple-1.7.21.jar;"%REPO%"\io\grpc\grpc-netty\1.6.1\grpc-netty-1.6.1.jar;"%REPO%"\io\grpc\grpc-core\1.6.1\grpc-core-1.6.1.jar;"%REPO%"\io\grpc\grpc-context\1.6.1\grpc-context-1.6.1.jar;"%REPO%"\com\google\errorprone\error_prone_annotations\2.0.19\error_prone_annotations-2.0.19.jar;"%REPO%"\com\google\instrumentation\instrumentation-api\0.4.3\instrumentation-api-0.4.3.jar;"%REPO%"\io\opencensus\opencensus-api\0.5.1\opencensus-api-0.5.1.jar;"%REPO%"\io\netty\netty-codec-http2\4.1.14.Final\netty-codec-http2-4.1.14.Final.jar;"%REPO%"\io\netty\netty-codec-http\4.1.14.Final\netty-codec-http-4.1.14.Final.jar;"%REPO%"\io\netty\netty-codec\4.1.14.Final\netty-codec-4.1.14.Final.jar;"%REPO%"\io\netty\netty-handler\4.1.14.Final\netty-handler-4.1.14.Final.jar;"%REPO%"\io\netty\netty-buffer\4.1.14.Final\netty-buffer-4.1.14.Final.jar;"%REPO%"\io\netty\netty-common\4.1.14.Final\netty-common-4.1.14.Final.jar;"%REPO%"\io\netty\netty-handler-proxy\4.1.14.Final\netty-handler-proxy-4.1.14.Final.jar;"%REPO%"\io\netty\netty-transport\4.1.14.Final\netty-transport-4.1.14.Final.jar;"%REPO%"\io\netty\netty-resolver\4.1.14.Final\netty-resolver-4.1.14.Final.jar;"%REPO%"\io\netty\netty-codec-socks\4.1.14.Final\netty-codec-socks-4.1.14.Final.jar;"%REPO%"\io\grpc\grpc-protobuf\1.6.1\grpc-protobuf-1.6.1.jar;"%REPO%"\com\google\protobuf\protobuf-java-util\3.3.1\protobuf-java-util-3.3.1.jar;"%REPO%"\com\google\code\gson\gson\2.7\gson-2.7.jar;"%REPO%"\com\google\api\grpc\proto-google-common-protos\0.1.9\proto-google-common-protos-0.1.9.jar;"%REPO%"\io\grpc\grpc-protobuf-lite\1.6.1\grpc-protobuf-lite-1.6.1.jar;"%REPO%"\io\grpc\grpc-stub\1.6.1\grpc-stub-1.6.1.jar;"%REPO%"\net\sourceforge\argparse4j\argparse4j\0.8.1\argparse4j-0.8.1.jar;"%REPO%"\com\truward\protobuf\protobuf-jackson\1.0.1\protobuf-jackson-1.0.1.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.4.2\jackson-core-2.4.2.jar;"%REPO%"\commons-codec\commons-codec\1.10\commons-codec-1.10.jar;"%REPO%"\graphstore\java-example\0.1\java-example-0.1.jar

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS%  -classpath %CLASSPATH% -Dapp.name="runGraphdataServer" -Dapp.repo="%REPO%" -Dapp.home="%BASEDIR%" -Dbasedir="%BASEDIR%" graphstore.GraphdataStore %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
