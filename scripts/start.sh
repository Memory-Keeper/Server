#!/bin/bash

sudo yum install -y java-17-amazon-corretto-devel
JARFILE=$(ls /home/ec2-user/app/*.jar)
echo "filename : $JARFILE" >> startsh.log

CURRENT_PID=$(pgrep -f $JARFILE)
echo "currentpid : $CURRENT_PID" >> startsh.log
kill -9 $CURRENT_PID 2>/dev/null
sleep 5

nohup java -jar $JARFILE > /dev/null 2> /dev/null < /dev/null &
