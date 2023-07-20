## Canal

> 仓库地址: https://github.com/alibaba/canal



#### 什么是？

**Canal**是一个基于MySQL二进制日志的高性能数据同步系统，

**Canal Server**能够解析MySQL binlog并订阅数据更改，而**Canal Client**可以实现将更改广播到任何地方。



#### 快速开始

* 先开启Binlog写入功能，配置binlog-format为ROW模式，my.cnf中配置如下

```
[mysqld]
log-bin=mysql-bin # 开启 binlog
binlog-format=ROW # 选择 ROW 模式
server_id=1 # 配置 MySQL replaction 需要定义，不要和 canal 的 slaveId 重复
```

* 授权canal链接 MySQL账号具有作为MySQL从属的权限，已有账户可直接授予

```sql
CREATE USER canal IDENTIFIED BY 'canal';  
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;
FLUSH PRIVILEGES;
```



#### 镜像地址

> https://hub.docker.com/r/canal/canal-server

启动docker的脚本

>  https://github.com/alibaba/canal/blob/master/docker/run.sh

参数描述地址

> https://github.com/alibaba/canal/wiki/AdminGuide

```
sh run.sh -e canal.instance.master.address=192.168.50.44:3306 \
  -e canal.instance.dbUsername=canal \
  -e canal.instance.dbPassword=canal \
  -e canal.instance.connectionCharset=UTF-8 \
  -e canal.instance.tsdb.enable=true \
  -e canal.instance.gtidon=false \
  -e canal.instance.filter.regex=dolphin_tcm\..* \
  -e canal.serverMode=rabbitMQ  \
  -e canal.mq.dynamicTopic=true \
  -e canal.mq.flatMessage=false \
  -e canal.mq.topic=canal-routing-key \
  -e rabbitmq.host=192.168.50.44 \
  -e rabbitmq.virtual.host=/ \
  -e rabbitmq.username=imaboss \
  -e rabbitmq.password=imaboss \
  -e rabbitmq.deliveryMode=1 \
  -e rabbitmq.exchange=canal-exchange
```

