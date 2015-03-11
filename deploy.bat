rem set MAVEN_OPTS=-Dfile.encoding=UTF-8
call mvn -U -Denv=dev -DfailIfNoTests=false -P dev clean install package assembly:assembly

echo ------------------------------


pause