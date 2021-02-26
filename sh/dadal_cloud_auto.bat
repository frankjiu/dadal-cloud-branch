@echo off & color 2F & title BatchRunning & mode con cols=45 lines=35

set src_path=D:\eclipse\workspace\dadal-cloud\
set jar_path=E:\appfiles\
set buildfilespathstr=\target\
set suffixes=1.0-SNAPSHOT.jar

echo ------------
echo %time%
::base jar before others application
set base_jar=dadal-cloud-eureka-register
::set app_list=dadal-cloud-eureka-register dadal-cloud-health-admin  dadal-cloud-store-service  dadal-cloud-ribbon-consumer  dadal-cloud-feign-api  dadal-cloud-hystrix-dashboard  dadal-cloud-turbine-monitor  dadal-cloud-gateway-api   dadal-cloud-config-server  dadal-cloud-config-client  dadal-cloud-rabbitmq-server  dadal-cloud-common  dadal-cloud-log-service  dadal-cloud-user-service 
::set defined_port_list=dadal-cloud-store-service2002 dadal-cloud-store-service2003 dadal-cloud-ribbon-consumer3003
::set app_list=dadal-cloud-eureka-register dadal-cloud-store-service dadal-cloud-config-server dadal-cloud-config-client dadal-cloud-rabbitmq-server 
set app_list=dadal-cloud-eureka-register dadal-cloud-health-admin  dadal-cloud-store-service  dadal-cloud-ribbon-consumer  dadal-cloud-feign-api  dadal-cloud-hystrix-dashboard  dadal-cloud-turbine-monitor  dadal-cloud-gateway-api   dadal-cloud-config-server  dadal-cloud-config-client  dadal-cloud-rabbitmq-server  dadal-cloud-common  dadal-cloud-log-service  dadal-cloud-user-service 
::cluster applications
set defined_port_list=
echo ------------
for %%s in (%app_list%) do (
	echo app:%%s
	if defined base_jar (
		if "%%s" equ "%base_jar%" (
			xcopy /Q /E /Y %src_path%%%s %jar_path%%%s\  && cd /d %jar_path%%%s\ && start mvn install 
			timeout /t 7 >null
		) else (
			xcopy /Q /E /Y %src_path%%%s %jar_path%%%s\  && cd /d %jar_path%%%s\ && start mvn package	
		)
	) else (
		xcopy /Q /E /Y %src_path%%%s %jar_path%%%s\  && cd /d %jar_path%%%s\ && start mvn package
	)
	timeout /t 6 >null
	echo. 
)

echo ------Applications are built over------
timeout /t 3 >null

for %%s in (%app_list%) do (
	copy %jar_path%%%s%buildfilespathstr%%%s-%suffixes% %jar_path%
	timeout /t 5 >null
	if defined defined_port_list (
		for %%w in (%defined_port_list%) do (
			::check if contains the current value
			setlocal enabledelayedexpansion
			echo %%w|findstr "^%%s" >nul
			::echo !errorlevel!
			if !errorlevel! equ 0 (
				::if true, split the port
				set lonpath=%%w
				set shopath=%%s
				set x=!lonpath!
				set port=!x:%%s=!
				::if true, start execute
				echo !port! | findstr "[a-z]" >nul
				if !errorlevel! equ 1 (
					echo Cluster port:!port!
					timeout /t 2 
					::pause
					echo Now starting !shopath!:!port! ...
					start cmd /k  "java -jar !jar_path!!shopath!-!suffixes! --server.port=!port!"
					ping -n 9 127.1>nul  
					echo. 
				)
			) 
		)
	)
	echo Now starting %%s ...
	start cmd /k  "java -jar %jar_path%%%s-%suffixes%"
	ping -n 9 127.1>nul  
	echo. 
)
echo All commands are executed completely
echo The console is going to exit after 90s
timeout /t 90 
pause


