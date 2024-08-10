#!/bin/bash

# 애플리케이션 실행 경로로 이동
cd /home/ec2-user/app

# 기존에 실행 중인 애플리케이션 프로세스를 종료
sudo pkill -f 'java -jar'

# 새 JAR 파일을 백그라운드에서 실행
nohup java -jar memory-keeper-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &

# 새 프로세스의 PID를 기록
echo $! > application.pid
