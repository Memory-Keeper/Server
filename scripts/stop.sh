#!/bin/bash

# 애플리케이션 프로세스를 종료
if [ -f /home/ec2-user/app/application.pid ]; then
  PID=$(cat /home/ec2-user/app/application.pid)
  echo "Stopping application with PID: $PID"
  kill $PID
  sleep 5
  if ps -p $PID > /dev/null
  then
    echo "Application did not stop, killing..."
    kill -9 $PID
  fi
  rm /home/ec2-user/app/application.pid
else
  echo "No application running."
fi
