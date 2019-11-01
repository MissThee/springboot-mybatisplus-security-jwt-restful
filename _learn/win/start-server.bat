set port=8098

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
	if %%i neq 0 (
		set n=%%i
	)
)
if defined n (
	taskkill /f /pid %n% 
)
java ^
-Djava.rmi.server.hostname=localhost ^
-Dcom.sun.management.jmxremote ^
-Dcom.sun.management.jmxremote.port=1099 ^
-Dcom.sun.management.jmxremote.authenticate=false ^
-Dcom.sun.management.jmxremote.ssl=false ^
-jar ^
../../../../../../../../../target/demo-0.0.1-SNAPSHOT.jar ^
--server.port=%port% ^
--spring.datasource.primary.driver-class-name=com.mysql.jdbc.Driver ^
--spring.datasource.primary.jdbc-url=jdbc:mysql://localhost:3306/mybatis_test_db?useUnicode=true^&useJDBCCompliantTimezoneShift=true^&useLegacyDatetimeCode=false^&serverTimezone=Asia/Shanghai^&nullNamePatternMatchesAll=true^&useSSL=false^&characterEncoding=utf-8 ^
--spring.datasource.primary.username=user ^
--spring.datasource.primary.password=1234 ^
--spring.profiles.active=prod

pause & exit