# chat_java_wbtkj

# 部署

PostgreSQL
```shell script
git clone --branch v0.4.0 https://github.com/pgvector/pgvector.git
cd pgvector
make
make install # may need sudo

sudo -u postgres psql
ALTER USER postgres WITH PASSWORD 'wbtkjjktbw';
```