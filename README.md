# chat_java_wbtkj

# 部署

MongoDB
```shell script
sudo sysctl -w net.ipv4.tcp_keepalive_time=120
```

PostgreSQL
```shell script
sudo yum install postgresql15-server postgresql15-contrib

sudo vi /var/lib/pgsql/15/data/pg_hba.conf
# 修改
# IPv4 local connections:
host    all      all       0.0.0.0/0      scram-sha-256

sudo vi /var/lib/pgsql/15/data/postgresql.conf
# 修改
# - Connection Settings -
listen_addresses = '*'   # what IP address(es) to listen on;

sudo /usr/pgsql-15/bin/postgresql-15-setup initdb
sudo systemctl start postgresql-15
sudo systemctl enable postgresql-15

sudo yum install pgvector_15

sudo -u postgres psql

ALTER USER postgres WITH PASSWORD 'wbtkjjktbw';
CREATE EXTENSION vector;
```


# 优化
## 后端
1. UserRole表放入缓存
2. 刷热度用消息队列