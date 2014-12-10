@echo off
IF "%~1" == "" GOTO SYNTAX

start java -Dselector=%1 -cp ..\target\pricecache-client.jar com.carle.pricecache.client.Subscriber
goto END

:SYNTAX
type syntax.txt
pause

:END