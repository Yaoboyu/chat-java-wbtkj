# chat_java_wbtkj

# 部署

git
```shell script
git config --global user.name "sofice98"
git config --global user.email "773508803@qq.com"
 
```

MongoDB
```shell script
sysctl -w net.ipv4.tcp_keepalive_time=120
```

PostgreSQL
```shell script
yum install postgresql15-server postgresql15-contrib

vi /var/lib/pgsql/15/data/pg_hba.conf
# 修改
# IPv4 local connections:
host    all      all       0.0.0.0/0      scram-sha-256

vi /var/lib/pgsql/15/data/postgresql.conf
# 修改
# - Connection Settings -
listen_addresses = '*'   # what IP address(es) to listen on;

/usr/pgsql-15/bin/postgresql-15-setup initdb
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

# 优化
## 后端
1. UserRole表放入缓存
2. 刷热度用消息队列
3. 历史记录加缓存