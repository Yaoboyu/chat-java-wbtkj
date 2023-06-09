# run.sh
# 切换到jar包目录下
cd /root/chat-java-wbtkj
# 杀死之前的项目进程
PID=$(ps -ef | grep chat-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
    echo Application is already stopped
else
    echo kill $PID
    kill $PID
fi
# 停5秒
sleep 2s
# 运行项目
nohup java -jar chat-0.0.1-SNAPSHOT.jar > /dev/null --spring.profiles.active=test 2>&1 &
#nohup java -jar chat-0.0.1-SNAPSHOT.jar > /root/chat-java-wbtkj/log.log --spring.profiles.active=test 2>&1 &