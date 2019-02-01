set port=8097

title Project port:%port%

::hide cmd window
::@echo off
::if "%1" == "h" goto begin
::mshta vbscript:createobject("wscript.shell").run("""%~nx0"" h",0)(window.close)&&exit
:::begin
::REM

::kill process by port
@echo off
for /f "tokens=5" %%i in ('netstat -aon ^| findstr ":%port%"') do (
    set n=%%i
	
)
if defined n ( 
	taskkill /f /pid %n% 
)
java  -jar ../../../../../../target/demo-0.0.1-SNAPSHOT.jar --server.port=%port% --spring.datasource.primary.driver-class-name=oracle.jdbc.OracleDriver --spring.datasource.primary.jdbc-url=jdbc:oracle:thin:@192.168.1.6:1521:cbmdb --spring.datasource.primary.username=cbmdb --spring.datasource.primary.password=cbmdb1qazxsw2   --logging.level.root=info --logging.level.server=info --logging.level.server.config.log=debug --logging.level.server.db.primary=info --logging.level.server.db.secondary=info
pause & exit