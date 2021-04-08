#!/bin/sh
RESOURCE_DIR=/wls/iwc/hnzsh
RESOURCE_NAME=hnzsh-0.0.1-SNAPSHOT.jar

tpid=`ps -ef|grep $RESOURCE_DIR/$RESOURCE_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
echo 'Stop Process...'
kill -15 $tpid
fi
sleep 5
tpid=`ps -ef|grep $RESOURCE_DIR/$RESOURCE_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
echo 'Kill Process!'
kill -9 $tpid
else
echo 'Stop Success!'
fi

tpid=`ps -ef|grep $RESOURCE_DIR/$RESOURCE_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'App is running.'
else
    echo 'App is NOT running.'
    rm -f tpid
    if [ ! -n "$1" ] ;then
        nohup java -Dfile.encoding=utf-8 -jar $RESOURCE_DIR/$RESOURCE_NAME --spring.config.location=application-pro.yml --spring.profiles.active=pro >$RESOURCE_DIR/logs/server.log 2>&1 &
    else
        nohup java -Dfile.encoding=utf-8 -jar $RESOURCE_DIR/$RESOURCE_NAME --spring.config.location=application-pro.yml --spring.profiles.active=pro --dateTime=$1 >$RESOURCE_DIR/logs/server.log 2>&1 &
    fi
    echo $! > tpid
    echo Start Success!
    echo "retCode:0"
    echo "retMes:successtrue"
    sleep 5
    tail -f -n 0 $RESOURCE_DIR/logs/server.log|while read line
    do
       echo $line
       is_startup=`echo $line|grep "Started DemoApplication"|wc -l`
       if [ $is_startup -eq 1 ];
           then kill -9 `ps axu|grep "tail -f -n 0 $RESOURCE_DIR/logs/server.log"|grep -v "grep"|awk '{printf $2}'`
        fi
    done
fi
