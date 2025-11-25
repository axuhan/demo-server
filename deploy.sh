#!/bin/bash

# 查找并停止java -jar进程的脚本
stop_java_jars() {
    echo "正在查找java -jar进程..."

    # 查找进程
    pids=$(ps -ef | grep 'java -jar' | grep -v grep | awk '{print $2}')

    if [ -z "$pids" ]; then
        echo "未找到运行的java -jar进程"
        return 0
    fi

    echo "找到以下Java进程:"
    ps -ef | grep 'java -jar' | grep -v grep

    # 发送终止信号
    echo "正在停止进程..."
    for pid in $pids; do
        echo "停止进程 PID: $pid"
        kill -15 $pid
    done

    # 等待进程退出
    sleep 5

    # 检查是否还有进程运行，如果有则强制杀死
    remaining_pids=$(ps -ef | grep 'java -jar' | grep -v grep | awk '{print $2}')
    if [ -n "$remaining_pids" ]; then
        echo "有些进程未正常退出，强制杀死..."
        for pid in $remaining_pids; do
            echo "强制杀死进程 PID: $pid"
            kill -9 $pid
        done
    fi

    echo "所有java -jar进程已停止"
}

move_and_start_new_jar() {
    mkdir /opt/app
    mv ./deployments/bootstrap-0.0.1-SNAPSHOT.jar /opt/app/bootstrap-0.0.1-SNAPSHOT.jar
    cd /opt/app
    nohup java -jar bootstrap-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --server.port=8082 > output.log 2>&1 &
}

# 执行函数
stop_java_jars
move_and_start_new_jar