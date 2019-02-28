@echo off
cd /d %~dp0
set filename=%~n0

setlocal EnableDelayedExpansion
for %%n in (*.exe) do (
ren "%%n" "!filename!.exe"
)
for %%n in (*.xml) do (
ren "%%n" "!filename!.xml"
)


:menu
echo [i].install
echo [u].uninstall
echo [1].start
echo [2].stop
echo [3].restart
:select
set /p yn=PLEASE INPUT :
if /i "%yn%"=="i" goto server-install
if /i "%yn%"=="u" goto server-uninstall
if /i "%yn%"=="1" goto server-start
if /i "%yn%"=="2" goto server-stop
if /i "%yn%"=="3" goto server-restart
if /i "%yn%"=="c" exit
echo Wrong value,input again.&ping 0 -n "2">nul&cls&goto menu
:server-install
%filename%.exe install
echo TASK FINISHED
goto select
:server-uninstall
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