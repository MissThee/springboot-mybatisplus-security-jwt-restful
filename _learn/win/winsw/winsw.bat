@echo off

cd /d %~dp0

set batFileName=%~n0

set filename=
for  %%i in ( *.jar ) do (
    set filename=%%~ni
    goto findJar
)
echo %filename%.jar NOT FOUND.
goto menu
:findJar
echo %filename%.jar EXIST. RENAME ".exe" and ".xml".
for %%n in (*.exe) do (
    if %%n NEQ %filename%.exe (ren %%n %filename%.exe)
)
for %%n in (*.xml) do (
    if %%n NEQ %filename%.xml (ren %%n %filename%.xml)
)

:menu
echo Project Name : %filename%
echo ----------------------
echo [I].Install    Service
echo [U].Uninstall  Service
echo [1].Start      Service
echo [2].Stop       Service
echo [3].Restart    Service
echo [C].Exit
echo ----------------------
:select
set yn=
set /p yn=PLEASE INPUT :
if /i "%yn%"=="i" goto server-install
if /i "%yn%"=="u" goto server-uninstall
if /i "%yn%"=="1" goto server-start
if /i "%yn%"=="2" goto server-stop
if /i "%yn%"=="3" goto server-restart
if /i "%yn%"=="c" exit
echo Wrong Value!!&ping 0 -n "2">nul&cls&goto menu
:server-install
%filename%.exe install
%filename%.exe start
echo TASK FINISHED
goto select
:server-uninstall
%filename%.exe stop
%filename%.exe uninstall
echo TASK FINISHED
goto select
:server-start
%filename%.exe start
echo TASK FINISHED
goto select
:server-stop
%filename%.exe stop
echo TASK FINISHED
goto select
:server-restart
%filename%.exe restart
echo TASK FINISHED
goto select