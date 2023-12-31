# chat_java_wbtkj

# 部署

## 后端服务器
### git
```shell script
yum install git

git config --global user.name "sofice98"
git config --global user.email "773508803@qq.com"

ssh-keygen
```

### MongoDB
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
# db.createUser({user: "admin", pwd: "wbtkjjktbw", roles: [{role: "root", db: "admin"}]})
db.createUser({user: "wbtkj", pwd: "wbtkjjktbw", roles: [{role: "dbOwner", db: "chat-wbtkj"}]})

exit
```

### redis
```shell script
wget http://download.redis.io/releases/redis-6.2.6.tar.gz
tar xzf redis-6.2.6.tar.gz
cd redis-6.2.6
make
make install
cp redis.conf /etc/redis.conf


vim /etc/redis.conf
daemonize yes
requirepass wbtkjjktbw

redis-server /etc/redis.conf
redis-cli ping
systemctl start redis
systemctl enable redis
```

### jdk
```shell script
yum install java-1.8.0-openjdk
```

### PostgreSQL
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

psql -U postgres -d wbtkj_chat
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

### git
```shell script
yum install git

git config --global user.name "sofice98"
git config --global user.email "773508803@qq.com"

ssh-keygen
```

### nginx
```shell script
yum install nginx

vim /etc/nginx/nginx.conf
```

http配置
```
# 修改配置文件
upstream chat-java-wbtkj{
    server 42.192.145.155:10317;
}
server {
    listen       80;
    server_name  chat.wbtkj.top 147.161.32.40;
    root         /usr/share/nginx/html/dist;
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
    server_name chat.wbtkj.top 147.161.32.40;

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

http服务配置

1.签发证书
```shell script
yum install epel-release
yum install certbot
yum install certbot-nginx
certbot certonly --nginx

# 1. 打开定时任务配置
 crontab -e
# 2. 增加定时刷新的配置
30 3 * */2 * /usr/bin/certbot renew --post-hook "service nginx restart" --quiet >> /var/log/cerbot.log
```
2.配置conf
```
vim /etc/nginx/nginx.conf

client_max_body_size 1024m;

server {
       listen       80; #监听端口
       server_name  chat.wbtkj.top 147.161.32.40; #请求域名
       return      301 https://$host$request_uri; #重定向至https访问。
}
upstream chat-java-wbtkj{
    server 43.163.238.17:10317;
}
# WebSocket 配置
map $http_upgrade $connection_upgrade {
    default upgrade;
    '' close;
}
server {
        listen       443 ssl;
        server_name  chat.wbtkj.top 147.161.32.40;

        #error_log logs/xxx_error.log;#错误日志文件
        #access_log logs/xxx_access.log;#访问日志文件

		#ssl证书
        ssl_certificate           /etc/letsencrypt/live/chat.wbtkj.top/fullchain.pem;
        ssl_certificate_key       /etc/letsencrypt/live/chat.wbtkj.top/privkey.pem;
        ssl_session_timeout       5m;
        ssl_protocols             TLSv1 TLSv1.1 TLSv1.2;
        ssl_ciphers               ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:DHE-RSA-AES256-SHA:DHE-RSA-AES128-SHA;
        ssl_session_cache         shared:SSL:50m;
        ssl_prefer_server_ciphers on;

        root /usr/share/nginx/html/dist;
        index index.html;
        
        location ^~/api {
            rewrite /api/(.*)$ /$1 break;
            proxy_pass http://chat-java-wbtkj;
            proxy_http_version 1.1; #代理使用的http协议
            proxy_set_header Host $host; #header添加请求host信息
            proxy_set_header X-Real-IP $remote_addr; # header增加请求来源IP信息
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; # 增加代理记录
        }

        # 拦截websocket请求
        location ^~/websocket {
           rewrite /websocket/(.*)$ /$1 break;
           proxy_pass http://chat-java-wbtkj;
           proxy_http_version 1.1;
           proxy_set_header Upgrade $http_upgrade;
           proxy_set_header Connection $connection_upgrade;
        }
    
        location / {
            try_files $uri $uri/ /index.html;
        }
}

```


### python

```shell script
# chorm https://chromium.cypress.io/linux/stable/109.0.5414.119

# driver http://npm.taobao.org/mirrors/chromedriver/
wget https://registry.npmmirror.com/-/binary/chromedriver/109.0.5414.74/chromedriver_linux64.zip
unzip chromedriver_linux64.zip
mv chromedriver /usr/bin
```