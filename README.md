# chat_java_wbtkj

# 部署

MongoDB
```shell script
sudo sysctl -w net.ipv4.tcp_keepalive_time=120
```

PostgreSQL
```shell script
git clone --branch v0.4.0 https://github.com/pgvector/pgvector.git
cd pgvector
make
make install # may need sudo

sudo -u postgres psql
ALTER USER postgres WITH PASSWORD 'wbtkjjktbw';
```


# 优化
## 后端
1. UserRole表放入缓存
2. 刷热度用消息队列