#Author : Qiuqiang
#Desc : restart tomcat every Saturday morning
#Time : 2019-02-28
tomcat_path="/home/frank/tomcat/apache-tomcat-8.5.34"

#!/bin/sh
#kill tomcat pid: 使用killall 需要安装命令-- yum install psmisc
#在每周六早三点零一分定时执行关闭 1 3 * * 6
#执行crontab -e 后写入 1 3 * * 6 /home/frank/auto/restart_tomcat.sh >> /home/frank/auto/restart_tomcat.log
#编辑tomcat的bin目录下的setclasspath.sh添加JAVA_HOME和JRE_HOME环境变量
#export JAVA_HOME=/home/frank/jdk/jdk1.8.0_181
#export JRE_HOME=/home/frank/jdk/jdk1.8.0_181/jre

echo ""
echo "-----------------------Begin--------------------------"
#pidlist=`ps -ef|grep tomcat|grep -v "grep"|awk '{print $2}'`
pidlist=`ps -ef|grep /home/frank/jdk/jdk1.8.0_181*|grep -v "grep"|awk '{print $2}'`
#判断变量数组是否为空
if [ ! $pidlist ]; then
	lengths=0
	echo "'pidlist' IS NULL, the system isn't running"
else
	lengths=${#pidlist[@]}
	echo "运行项目的进程号数组pidlist:"$pidlist"包含"$lengths"个元素"
fi 

if [ $lengths -gt 0 ]; then
	echo "Ready To Shutdown..."
	#停止进程(killall  java)
	#killall  java
	#排除重启进程本身或采用非关键字起名策略
	ps -ef | grep tomcat | grep -v grep | grep -v restart_tomcat | awk '{print $2}' | xargs kill -9
	echo "tomcat service stopped successed !"
	#清理缓存
	rm -rf $tomcat_path/work/*
	rm -rf $tomcat_path/conf/Catalina/
	echo "cache deleted !"
fi

#开启项目
date +"%Y-%m-%d %H:%M:%S Now Starting Tomcat ..."
sh $tomcat_path/bin/startup.sh
sleep 6

#查询启动失败后循环重启五次
for((i=1;i<=6;i++)); do  
	#若启动不成功,尝试第1次启动
	if [ $(ps -ef|grep -c /home/frank/jdk/jdk1.8.0_181/bin/java) -ne 1 ]; then
		if [ i eq 6 ]; then
			echo "!!! Failed !!!"
			break
		else
			date +"%Y-%m-%d %H:%M:%S The "${i}"Th Restarting Tomcat ..."
			sh $tomcat_path/bin/startup.sh
			#等待启动
			sleep 30
		fi
	else
		#若启动成功,提示后跳出循环
		echo "Congratulation, tomcat service started successed !!!"
		break
	fi
done

echo "-----------------------End--------------------------"
echo ""