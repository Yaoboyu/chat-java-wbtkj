# chat_java_wbtkj

# 部署

## 后端服务器
git
```shell script
yum install git

git config --global user.name "sofice98"
git config --global user.email "773508803@qq.com"

ssh-keygen
```

MongoDB
```shell script
# 下载地址：https://www.mongodb.com/try/download/community
wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-6.0.6.tgz
tar -zxvf mongodb-linux-x86_64-rhel70-6.0.6.tgz
mv mongodb-linux-x86_64-rhel70-6.0.6 /usr/local/mongodb
cd /usr/local/mongodb
mkdir -p /usr/local/mongodb/log  # 日志目录
mkdir -p /usr/local/mongodb/data  # 数据库目录
mkdir -p /usr/local/mongodb/conf  # 配置目录
touch /usr/local/mongodb/log/mongodb.log  # 创建日志文件
chmod 755 /usr/local/mongodb/log
chmod 755 /usr/local/mongodb/data

vim /usr/local/mongodb/conf/mongodb.conf
```
```
systemLog:
  destination: file
  path: /usr/local/mongodb/log/mongod.log # log path
  logAppend: true
storage:
  dbPath: /usr/local/mongodb/data # data directory
  engine: wiredTiger #存储引擎
  journal: #是否启用journal日志
    enabled: true
net:
  bindIp: 0.0.0.0
  port: 27017 # port
processManagement:
  fork: true
security:
  authorization: enabled
```


```shell script
export PATH=/usr/local/mongodb/bin:$PATH
# 启动
mongod --config /usr/local/mongodb/conf/mongodb.conf
# 关闭
mongod --config /usr/local/mongodb/conf/mongodb.conf --shutdown
sysctl -w net.ipv4.tcp_keepalive_time=120
```
mongo shell
```
vim /etc/yum.repos.d/mongodb-org-4.4.repo
[mongodb-org-4.4]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/4.4/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-4.4.asc

yum install mongodb-org-shell
mongo
use admin
db.createUser({user: "wbtkj", pwd: "wbtkjjktbw", roles: [{role: "root", db: "chat-wbtkj"}]})
exit
```

redis
```shell script
yum install redis

vim /etc/redis.conf
daemonize yes
requirepass wbtkjjktbw

systemctl start redis
systemctl enable redis
```

jdk
```shell script
yum install java-1.8.0-openjdk
```

PostgreSQL
```shell script
yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
yum install postgresql15-server postgresql15-contrib
/usr/pgsql-15/bin/postgresql-15-setup initdb

vim /var/lib/pgsql/15/data/pg_hba.conf
# 修改
# IPv4 local connections:
host    all      all       0.0.0.0/0      scram-sha-256

vim /var/lib/pgsql/15/data/postgresql.conf
# 修改
# - Connection Settings -
listen_addresses = '*'   # what IP address(es) to listen on;


systemctl start postgresql-15
systemctl enable postgresql-15

yum install pgvector_15

sudo -u postgres psql

ALTER USER postgres WITH PASSWORD 'wbtkjjktbw';
CREATE EXTENSION vector;
```

python
```shell script
yum install zlib-devel bzip2-devel openssl-devel ncurses-devel sqlite-devel readline-devel tk-devel gcc make
yum install libffi-devel -y

#  建立新目录
mkdir /usr/local/python3 

#  以3.6.2为例，需要下载高版本请访问：https://www.python.org/ftp/python
wget --no-check-certificate https://www.python.org/ftp/python/3.6.6/Python-3.6.6.tgz 
#  解压安装包
tar xzvf Python-3.6.6.tgz
cd Python-3.6.6
#  编译安装
./configure --prefix=/usr/local/python3 --with-ssl
make
make install

# 创建软链接
ln -s /usr/local/python3/bin/python3 /usr/bin/python3

#添加到PATH环境变量里
export PATH=$PATH:/usr/local/python3/bin

python3 -V

# pip3
wget --no-check-certificate https://github.com/pypa/pip/archive/9.0.1.tar.gz
tar -xzvf 9.0.1.tar.gz
cd pip-9.0.1
python3 setup.py install
sudo ln -s /usr/local/python3/bin/pip /usr/bin/pip3
pip3 install --upgrade pip
pip3 -V
```


## 前端服务器

git
```shell script
yum install git

git config --global user.name "sofice98"
git config --global user.email "773508803@qq.com"

ssh-keygen
```

nginx
``` shell script
yum install nginx

vim /etc/nginx/nginx.conf
```

```
# 修改配置文件
upstream chat-java-wbtkj{
    server 42.192.145.155:10317;
}
server {
    listen       80;
    listen       [::]:80;
    server_name  _;
    root         /usr/share/nginx/html/;
    index index.html;

    location ^~/api {
        rewrite ^/api/(.*)$ /$1 break;
        proxy_pass http://chat-java-wbtkj;
    }

    location / {
        try_files $uri $uri/ /index.html;
    }
}
# WebSocket 配置
map $http_upgrade $connection_upgrade {
    default upgrade;
    '' close;
}

server {
    listen 8888;
    server_name _;

    location / {
      proxy_pass http://chat-java-wbtkj;
      proxy_http_version 1.1;
      proxy_set_header Upgrade $http_upgrade;
      proxy_set_header Connection $connection_upgrade;
    }
}
```

```shell script
# 启动 Nginx：
systemctl start nginx
# 重启 Nginx：
systemctl restart nginx
# 设置 Nginx 开机自启动：
systemctl enable nginx
```

# TODO
## 后端
1. UserRole表放入缓存
2. 刷热度用消息队列
3. 历史记录加缓存
4. 手机号注册