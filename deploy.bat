rem set MAVEN_OPTS=-Dfile.encoding=UTF-8  assembly:assembly
call mvn -U -Denv=dev -DfailIfNoTests=false -P dev clean install package

echo ------------------------------


pause