## 自己写的一个读写分离小工具（下面就是介绍写的过程以及使用方法）：
### 开发人员信息：
> 产品设计者： ***Zhu Yahui*** 和 ***xiaomaomi-xj***<br>
> 产品实现者： ***Zhu Yahui*** 和 ***xiaomaomi-xj***<br>
> 产品发布者： ***xiaomaomi-xj***<br>
### 想法1
* 我们增加一个读注解（ZyhDataSourceRead），一个写注解（ZyhDataSourceWrite），可以在类上定义一个读注解，在方法上定义写注解，写注解执行主数据库，读注解执行从数据库
* 写注解要自带事务，这样我们就不用再前去写Transactional注解，这样既不会发生忘写事务的可能，还可以减小事务的粒度，提升效率
* 主数据库只有一个还好，对于从数据库可能是多个，我们要产生一种策略，让这多个数据库来轮询或随机的选择，如果用户没有指定还应该给予默认的方式：随机
### 应得结论1
1. 第一种情况
    * 类上无注解，方法上无注解，为当前数据库，不带事务
    * 类上无注解，方法上写注解，为主数据库，自带事务
    * 类上无注解，方法上读注解，为从数据库，数据源轮询或者随机
2. 第二种情况
    * 类上读注解，方法上无注解，为从数据库，数据源轮询或者随机
    * 类上读注解，方法上读注解，为从数据库，数据源轮询或者随机
    * 类上读注解，方法上写注解，为主数据库，自带事务
3. 第三种情况
    * 类上写注解，方法上无注解，为主数据库，自带事务
    * 类上写注解，方法上写注解，为主数据库，自带事务
    * 类上写注解，方法上读注解，为从数据库，数据源轮询或者随机
### 想法2
* 应该和原先的service层的写法分别开来，应该提供一个独特的注解（ZyhService）来替换原先的service注解
### 实现办法2
* 运用切面编程，只对拥有ZyhService注解的类进行处理，并不会处理service注解的类
### 想法3
* 不能霸王条约，功能应该由用户来控制是否使用。
### 应得结论3
* 设置开关：提供开启读写分离注解（@EnableZyhDynamicDataSource）,不写默认是关闭的
* 关闭的情况下，用户按照springboot配置的数据源还可以有效，以保证不会影响原先的代码
* 开启后，用户按照springboot配置的数据源会失效，但是无需删除原先的配置，代码也不会报错
### 想法4
* 对于不想使用注解或者想在dao层使用事务或者想在service层的方法里面使用事务或者想要进一步减少事务粒度提升效率。
### 实现办法4
* 单独写一个处理读写的类（MyAloneHandlerReadWrite），提供公共静态读写方法，方法参数应该为单方法接口（MyDynamicDataSourceParamInterface），使其可以使用lambda表达式的方式编程
* 对于写方法，应该加上事务，读方法，应该按照之前定义的方式（轮询或者随机）来选择到具体的数据库
### 想法5
* 支持两种常用的商家数据源，一种是springboot默认使用的hikari，另一种就是阿里巴巴的druid
### 实现办法5
* 用户通过配置文件来指定使用hikari还是使用druid，默认的情况下应该是hikari
### 想法6
* 希望用户在写配置文件得时候能够提示，且必要还能提供中文提示
### 解决办法6
* 使用spring-boot-configuration-processor，然后自定义设置additional-spring-configuration-metadata.json文件
### 写这个作品的目的
* 解决只有一个数据库，读写压力大，应该把读写功能分开到不同的数据库中，减轻压力
* 一般情况是读的压力会大，很少会写入，因为界面展示都属于读的范围，所以主要实现，一主多从，主写从读
### 前提环境条件
* 多个数据库必须配置主从同步，才能保证业务的正常，但是代码不应该检查用户是否配置了主从同步
### 配置文件配置方式（指定一种就行了，这里把所有的列了出来）
* zyh-datasource 开头，指定个人作品标识
    * data-source-type 指定商家数据源类型，不写默认hikari，可以指定druid
    * switch-slave-type 指定多个从数据库选择得方式，不写默认为随机，可以指定轮询
    * master （hikari）主数据库
    * slaves （hikari）多个从数据库，应该是一个集合
    * hikari
        * master （hikari）主数据库
        * slaves （hikari）多个从数据库，应该是一个集合
    * druid
        * master （druid）主数据库
        * slaves （druid）多个从数据库，应该是一个集合
### 配置文件例子
```yml
zyh-datasource:
  data-source-type: druid
  switch-slave-type: polling
#  master:
#    jdbc-url: jdbc:mysql://localhost:3306/test1?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
#    username: root
#    password: 123456
#  slaves:
#    - jdbc-url: jdbc:mysql://localhost:3306/test2?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
#      username: root
#      password: 123456
#    - jdbc-url: jdbc:mysql://localhost:3306/test3?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
#      username: root
#      password: 123456
#    - jdbc-url: jdbc:mysql://localhost:3306/test4?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
#      username: root
#      password: 123456
  druid:
    master:
      url: jdbc:mysql://localhost:3306/test1?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
      username: root
      password: 123456
    slaves:
      - url: jdbc:mysql://localhost:3306/test2?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
        username: root
        password: 123456
        # 设置了默认的驱动就是com.mysql.cj.jdbc.Driver可以选择省略
        driver-class-name: com.mysql.cj.jdbc.Driver
      - url: jdbc:mysql://localhost:3306/test3?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
        username: root
        password: 123456
      - url: jdbc:mysql://localhost:3306/test4?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
        username: root
        password: 123456
        driver-class-name: com.mysql.cj.jdbc.Driver
#  hikari:
#    master:
#      jdbc-url: jdbc:mysql://localhost:3306/test1?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
#      username: root
#      password: 123456
#      driver-class-name: com.mysql.cj.jdbc.Driver
#    slaves:
#      - jdbc-url: jdbc:mysql://localhost:3306/test2?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
#        username: root
#        password: 123456
#      - jdbc-url: jdbc:mysql://localhost:3306/test3?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
#        username: root
#        password: 123456
#      - jdbc-url: jdbc:mysql://localhost:3306/test4?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
#        username: root
#        password: 123456
```
### 使用方法
#### 配置文件（就在上面）
#### 引入依赖（二选一就行了，内容功能一致,只是放在了不同的平台）
#### 基于github的：**[maven依赖中央厂库地址](https://mvnrepository.com/artifact/io.github.xiaomaomi-xj/zyh-dynamic-datasource/1.0.3 "跳转到厂库地址")**
```xml
<dependency>
    <groupId>io.github.xiaomaomi-xj</groupId>
    <artifactId>zyh-dynamic-datasource</artifactId>
    <version>1.0.3</version>
</dependency>
```
#### 基于gitee的：**[maven依赖中央厂库地址](https://mvnrepository.com/artifact/io.gitee.xiaomaomi-xj/zyh-dynamic-datasource/1.0.3 "跳转到厂库地址")**
##### ***[gitee的项目位置](https://gitee.com/xiaomaomi-xj/zyh-dynamic-datasource "跳转到gitee的项目所在位置")***
```xml
<dependency>
    <groupId>io.gitee.xiaomaomi-xj</groupId>
    <artifactId>zyh-dynamic-datasource</artifactId>
    <version>1.0.3</version>
</dependency>
```
#### 启动类
```java
//在启动类上开启
@SpringBootApplication
@EnableZyhDynamicDataSource
public class Demo01Application {
    public static void main(String[] args) {
        SpringApplication.run(Demo01Application.class, args);
    }
}
```
#### service层使用
```java
@ZyhService
@ZyhDataSourceRead
public class ToggleTestServiceImpl implements ToggleTestService {

    private final ToggleTestDao toggleTestDao;

    public ToggleTestServiceImpl(ToggleTestDao toggleTestDao) {
        this.toggleTestDao = toggleTestDao;
    }

    @Override
    public DataMap getContext() {
        //从数据库
        return toggleTestDao.getContext();
    }

    @ZyhDataSourceWrite(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void removeContext(String id) {
        //主数据库
        toggleTestDao.removeContext(id);
        //事务正常回滚
        int i=1/0;
    }
}
```
#### 如果想更加细致的控制事务粒度
##### 在service层方法里面使用方法：
```java
@Override
public void delContextMethod(String id) {
    //读（从库）
    System.out.println(MyAloneHandlerReadWrite.read(toggleTestDao::getContext, DataMap.class));
    //写,（主库）
    MyAloneHandlerReadWrite.write(()->toggleTestDao.removeContext(id),Integer.class);
}
```
##### 或者在dao层使用方法：
```java
//主数据库
return MyAloneHandlerReadWrite.write(()->{
            int update = jdbcTemplate.update("delete  from  user  where id = ?", 2);
            //事务正常回滚
            int i=1/0;
            return update;
        },Integer.class);
```
### 使用demo的例子：
> 可以看这个: 
> [动态数据源测试项目](https://github.com/xiaomaomi-xj/dynamic-datasource-test-demo "进入动态数据源测试项目")