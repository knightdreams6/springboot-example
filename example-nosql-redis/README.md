## Redis

> 官方文档地址: https://redis.io/docs/
>
> Spring Data 集成文档：https://docs.spring.io/spring-data/redis/reference/



#### 概述

> Redis 是一种高级键值存储。它与 memcached 类似，但数据集不是易失性的，值可以是字符串，与 memcached 完全相同，但也可以是列表、集合和有序集合。所有这些数据类型都可以通过原子操作进行操作，以推送/弹出元素、添加/删除元素、执行服务器端并集、交集、集合之间的差异等。Redis 支持不同类型的排序能力。



#### docker 环境安装 

> dockerHub地址：https://hub.docker.com/_/redis

```shell
docker pull redis:7.2.3
```

```shell
docker run --name redis -d -p 6379:6379 -v /docker/redis/data:/data redis:7.2.3 redis-server --save 60 1 --loglevel warning
```

