## Neo4j

> 官方文档地址: https://neo4j.com/docs/



docker安装文档：https://neo4j.com/docs/operations-manual/current/docker/

```shell
docker run 
    --name neo4j -d 
    --restart always 
    --publish=7474:7474 --publish=7687:7687
    --env NEO4J_AUTH=neo4j/knight21@ 
    --volume=/home/docker/neo4j/data:/data 
neo4j:5.15.0-community
```


#### 什么是？

图形数据库是一种专门用于存储和管理图形数据的数据库系统。与传统的关系型数据库或文档数据库不同，图形数据库以图形结构的方式存储数据，其中节点表示实体，边表示节点之间的关系。图形数据库非常适合表示和处理复杂的关系型数据。

Neo4j是一种高性能的图数据库管理系统。它以图结构的方式存储和处理数据，其中的节点表示实体（如人、地点、产品等），边表示这些实体之间的关系。Neo4j是一个基于ACID（原子性、一致性、隔离性和持久性）的事务性数据库，被广泛用于构建复杂的关系型数据模型。

Neo4j提供了一种名为Cypher的查询语言，用于执行图形数据库中的各种操作。Cypher是一种直观和强大的查询语言，使用户能够轻松地检索和操作图数据库中的数据。它支持基于模式的查询，例如查找特定节点之间的路径、查找具有特定属性的节点等。

图数据库可以使用一些基本概念来存储任何类型的数据：

- 节点 - 代表域的实体。
- 标签 - 通过将节点分组来塑造域。
- 关系 - 连接两个节点。
- 属性 - 为节点和关系添加品质的命名值。



##### 节点

*Neo4j 将数据存储在图中作为节点*

最简单的图只有一个节点，其中包含一些称为属性的命名值。例如，让我们画一个社交图：

1. 画一个圆作为节点。
2. 添加埃米尔这个名字。
3. 请注意，他来自瑞典。

![one_node](https://knight-img.oss-cn-beijing.aliyuncs.com/544e831f14d411eebe3628d0eaa3d234.png)

*关键信息：*

- *节点通常表示可以用零个或多个标签进行分类的实体或离散对象。*
- *数据作为节点的属性存储。*
- *属性是简单的键值对。*



##### 标签

*关联一组节点*

可以通过对每个成员应用标签来将节点分组在一起。在此社交图中，您标记代表人员的每个节点。

1. 将标签“Person”添加到您为 Emil 创建的节点。
2. 将“Person”节点设置为红色。

![labeled_node](https://knight-img.oss-cn-beijing.aliyuncs.com/56da951c14d411ee9fbb28d0eaa3d234.png)

关键信息：

- 一个节点可以有零个或多个标签。
- 标签用于对节点进行分类。



##### 更多节点

*Neo4j 是无模式的*

与任何数据库一样，在 Neo4j 中存储数据就像添加更多节点一样简单。节点可以具有共同属性和独特属性的组合。添加更多节点和属性：

1. 埃米尔·克鲁特得分为 99。
2. 来自瑞典的约翰正在学习冲浪。
3. 伊恩，来自英国，是一位作家。
4. 来自比利时的里克（Rik）有一只名叫奥瓦尔（Orval）的猫。
5. 艾莉森，来自美国，冲浪者。

![more_nodes](https://knight-img.oss-cn-beijing.aliyuncs.com/5997bd9814d411ee812a28d0eaa3d234.png)

关键信息：

- 相似的节点可以具有不同的属性。
- 属性可以保存不同的数据类型，例如“数字”、“字符串”或“布尔值”。
- 属性也可以是包含字符串、数字或布尔值的同类列表（数组）。
- Neo4j 可以存储数十亿个节点。



##### 人际关系

*连接节点*

Neo4j 的真正力量在于连接的数据。要关联任意两个节点，请添加描述记录如何关联的关系。

在我们的社交图中，您可以简单地说谁认识（关系类型认识）谁：

1. 埃米尔认识约翰和伊恩。
2. 约翰认识伊恩和里克。
3. 里克和伊恩认识艾莉森。

![relationships](https://knight-img.oss-cn-beijing.aliyuncs.com/5bd2312e14d411eea46528d0eaa3d234.png)

关键信息：

- 关系总是有方向的。
- 关系总是有一种类型。
- 关系形成数据模式、图形结构。



##### 关系属性

*存储两个节点共享的信息*

在属性图中，关系还可以包含描述关系的属性。更仔细地观察埃米尔的关系，请注意：

- 埃米尔 (Emil) 自 2001 年起就认识约翰 (Johan)。
- 埃米尔 (Emil) 对伊恩 (Ian) 的评分为 5 分（满分 5 分）。
- 其他人都可以具有类似的关系属性。

![rel-props](https://knight-img.oss-cn-beijing.aliyuncs.com/5f82dfdb14d411eeacb928d0eaa3d234.png)





#### 如何部署？

docker 镜像地址仓库:  https://hub.docker.com/_/neo4j

docker安装文档：https://neo4j.com/docs/operations-manual/current/docker/

```shell
docker pull neo4j:5.15.0-community
```

```shell
docker run \
    --name neo4j -d \
    --publish=7474:7474 --publish=7687:7687 \
    --volume=/home/docker/neo4j/data:/data \
    --env=NEO4J_AUTH=none \
    neo4j:5.15.0-community  
```



#### 示例描述

> 以部门为例，要创建一个如下的图：

![graph](https://knight-img.oss-cn-beijing.aliyuncs.com/2fb0f3d614d411eeafcc28d0eaa3d234.png)

