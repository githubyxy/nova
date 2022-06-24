y x yyxy
# nova

1：安装docker
yum -y install gcc
yum -y install gcc-c++
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine

yum install -y yum-utils device-mapper-persistent-data lvm2
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
yum -y install docker-ce-18.06.3.ce
vim /etc/docker/daemon.json
、、、
{
  "registry-mirrors": ["https://r5yuqwmu.mirror.aliyuncs.com"],
  "data-root": "/home/data/docker",
  "log-driver": "json-file",
  "log-opts": {"max-size":"50m", "max-file":"1"}
}
、、、

systemctl start docker

docker network create --subnet=172.18.0.0/16 mynetwork

docker pull mysql:5.7

docker run -p 3306:3306 --name mysql_master --net mynetwork --ip 172.18.0.36 -e TZ=Asia/Shanghai -v /home/data/docker/mysql/mysql1/logs:/logs -v /home/data/docker/mysql/mysql1/data:/var/lib/mysql -v /home/data/docker/mysql/mysql1/conf:/etc/mysql -e MYSQL_ROOT_PASSWORD=*** -d --restart=always mysql:5.7

docker run -p 3307:3306 --name mysql_slave1 --net mynetwork --ip 172.18.0.37 -e TZ=Asia/Shanghai -v /home/data/docker/mysql/mysql2/logs:/logs -v /home/data/docker/mysql/mysql2/data:/var/lib/mysql -v /home/data/docker/mysql/mysql2/conf:/etc/mysql -e MYSQL_ROOT_PASSWORD=*** -d --restart=always mysql:5.7

docker run -p 3308:3306 --name mysql_slave2 --net mynetwork --ip 172.18.0.38 -e TZ=Asia/Shanghai -v /home/data/docker/mysql/mysql3/logs:/logs -v /home/data/docker/mysql/mysql3/data:/var/lib/mysql -v /home/data/docker/mysql/mysql3/conf:/etc/mysql -e MYSQL_ROOT_PASSWORD=*** -d --restart=always mysql:5.7


#zookeeper
docker pull zookeeper:3.6

docker run -d --name zookeeper-3.6 -p 2181:2181 --net mynetwork --ip 172.18.0.37 -v /home/data/docker/zookeeper/logs:/datalog -v /home/data/docker/zookeeper/data:/data -v /home/data/docker/zookeeper/conf/zoo.cfg:/conf/zoo.cfg --restart=always -t  zookeeper:3.6

# kafka
docker pull wurstmeister/kafka

docker run -d --name kafka -p 9092:9092 -p 19092:19092 --net mynetwork --ip 172.18.0.38  -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=172.18.0.37:2181 -e KAFKA_INTER_BROKER_LISTENER_NAME=INTERNAL -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT -e KAFKA_ADVERTISED_LISTENERS=INTERNAL://172.18.0.38:19092,EXTERNAL://106.13.38.56:9092 -e KAFKA_LISTENERS=INTERNAL://0.0.0.0:19092,EXTERNAL://0.0.0.0:9092 --restart=always -t wurstmeister/kafka

# redis
docker pull redis

docker run -p 6379:6379 --name redis --net mynetwork --ip 172.18.0.39 -v /home/data/docker/redis/conf/redis.conf:/usr/local/etc/redis/redis.conf -v /home/data/docker/redis/data:/data -d redis /usr/local/etc/redis/redis.conf

# canal
docker pull canal/canal-server:v1.1.4

# 创建账号
CREATE USER canal IDENTIFIED BY '***'; 
# 授予权限
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;
# 刷新并应用
FLUSH PRIVILEGES;

docker run -d --name canal -p 11111:11111 --net mynetwork --ip 172.18.0.41 -e canal.destinations=hermes -e canal.instance.mysql.slaveId=12 -e  canal.auto.scan=false -e canal.instance.master.address=172.18.0.36:3306 -e canal.instance.dbUsername=canal -e canal.instance.dbPassword=*** -e  canal.instance.filter.regex=esen_approval.apt_approval --restart=always canal/canal-server:v1.1.4

#grafana
# 创建持久化目录
cd /root/data/docker
mkdir -p grafana/data
mkdir -p grafana/logs
# 将修改后的配置文件放在指定的目录
cp grafana.ini grafana/
# 修改目录权限，docker方式启动grafana使用的uid和gid为472
chown 472:472 grafana/data -R
# 启动容器
docker run -d -p 3000:3000 --name=grafana -v /root/data/docker/grafana/data:/var/lib/grafana -v /root/data/docker/grafana/logs:/var/log/grafana -v /root/data/docker/grafana/grafana.ini:/etc/grafana/grafana.ini --restart=always grafana/grafana:7.2.1
外网访问: http://ip:3000/login  初始用户名/密码为admin/admin。
配置文件: 📎grafana.ini （需要将该文件中的domain改为服务器的外网ip，否则告警通知链接的host是localhost）

# influxdb
# 创建持久化目录
cd /root/data/docker
mkdir -p influxdb
# 启动容器
docker run -d -p 8086:8086 --name=influxdb -v /root/data/docker/influxdb:/var/lib/influxdb --restart=always influxdb:1.8

# 启动influxdb客户端
docker exec -it influxdb influx
# 创建数据库，数据ttl 2周
CREATE DATABASE "hermes" WITH DURATION 2w REPLICATION 1 NAME "default";
CREATE DATABASE "voiking" WITH DURATION 2w REPLICATION 1 NAME "default";
CREATE DATABASE "sms" WITH DURATION 2w REPLICATION 1 NAME "default";

访问：http://ip:8086 用户名/密码随意




