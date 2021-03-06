# MyBatis 学习笔记

[TOC]

## 01-mybatis入门案例

## 02-完成CRUD

> MyBatis  
> https://github.com/mybatis/mybatis-3/releases  下载  
> http://blog.mybatis.org/p/products.html 产品  
> https://sourceforge.net/projects/aopalliance/ aopalliance 下载

### 导入jar包

1. ant-1.9.6.jar
2. ant-launcher-1.9.6.jar
3. asm-5.0.4.jar
4. cglib-3.2.2.jar
5. commons-logging-1.2.jar
6. javassist-3.20.0-GA.jar
7. log4j-1.2.17.jar
8. log4j-api-2.3.jar
9. log4j-core-2.3.jar
10. mybatis-3.4.1.jar
11. mysql-connector-java-5.0.8-bin.jar
12. ognl-3.1.8.jar
13. slf4j-api-1.7.21.jar
14. slf4j-log4j12-1.7.21.jar

### 配置文件

sqlMapConfig.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<!-- default表明当前默认使用哪个源 -->
	<environments default="development">
		<!-- 数据源信息可以配置多个 -->
		<environment id="development">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<property name="driver" value="com.mysql.jdbc.Driver" />
				<property name="url" value="jdbc:mysql://localhost:3306/mybatis_01" />
				<property name="username" value="root" />
				<property name="password" value="tom0018" />
			</dataSource>
		</environment>
	</environments>
	<!-- 注册SQL映射文件 -->
	<mappers>
		<mapper resource="com/mybatis/domain/UserMapper.xml" />
	</mappers>
</configuration>
```
### 建立一个User类
```java
package com.mybatis.domain;

public class User {
	private int id;
	private String name;
	private String address;
	private Integer age;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", address=" + address
				+ ", age=" + age + "]";
	}

	public User(String name, String address, Integer age) {
		super();
		this.name = name;
		this.address = address;
		this.age = age;
	}

	public User(int id, String name, String address, Integer age) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.age = age;
	}

	public User() {
		super();
	}
}
```
### 创建一张表

```sql
CREATE TABLE `l_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `address` varchar(128) NOT NULL,
  `age` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```
### 提供一个SQL映射文件

com.mybatis.domain.UserMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 所有SQL映射文件中命名空间名不能重复 -->
<mapper namespace="com.mybatis.domain.UserMapper">
	<!-- id:当前文件唯一标识 parameterType:当前SQL接收的参数类型 resultType:结果类型(使用全路径) -->
	<select id="selectUserById" parameterType="int"
		resultType="com.mybatis.domain.User">
		select * from l_user where id = #{id}
	</select>

	<delete id="deleteUserById" parameterType="int">
		delete from l_user
		where id = #{id}
	</delete>
	<!-- 如果需要返回自增id需要加入useGeneratedKeys="true" keyProperty="id" keyProperty名字为对象的名字 -->
	<insert id="insertUser" parameterType="com.mybatis.domain.User"
		useGeneratedKeys="true" keyProperty="id">
		insert into l_user (name,
		address,
		age) values(#{name}, #{address}, #{age})
	</insert>

	<!-- 这是静态写法 -->
	<update id="updateUser" parameterType="com.mybatis.domain.User">
		update l_user set
		name=#{name}, address=#{address}, age=#{age} where
		id=#{id}
	</update>

	<select id="selectAllUser" resultType="com.mybatis.domain.User">
		select * from l_user
	</select>
</mapper>
```
### 使用Mybatis框架提供api完成对数据库的操作
```java
package com.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.mybatis.domain.User;

public class MybatisTest {
	private SqlSessionFactory sqlSessionFactory;

	@Before
	public void initSessionFactory() throws IOException {
		String resource = "sqlMapConfig.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void testSelect() {

		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id 锁定一个SQL
		String arg0 = "com.mybatis.domain.UserMapper.selectUserById";
		User user = openSession.selectOne(arg0, 1);
		System.out.println(user);
	}

	@Test
	public void testDelete() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.deleteUserById";
		// 在delete里默认设置为手工提交
		int id = openSession.delete(arg0, 1);
		// 事物提交
		openSession.commit();
		System.out.println("删除" + id + "行");
	}

	@Test
	public void testInsert() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.insertUser";
		// 在delete里默认设置为手工提交
		User user = new User("onefish", "chaoyang", 40);
		int id = openSession.insert(arg0, user);
		// 事物提交
		openSession.commit();
		System.out.println("插入" + id + "行, id:" + user.getId());
	}

	@Test
	public void testUpdate() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.updateUser";
		// 在delete里默认设置为手工提交
		User user = new User(1, "lcm", "chengdu", 43);
		int id = openSession.update(arg0, user);
		// 事物提交
		openSession.commit();
		System.out.println("插入" + id + "行");
	}

	@Test
	public void testSelectAll() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectAllUser";
		
		List<User> selectList = openSession.selectList(arg0);
		for (User user : selectList) {
			System.out.println(user);
		}
	}
}
```

## 03-关于mybatis的几点说明

### 显示mybatis的执行sql语句（log4j）

  通过配置src/log4j.properties文件，将SQL输出到控制台。(需要debug级别)
```ini
### set log levels ###
log4j.rootLogger = debug, stdout, D, E

### 输出到控制台 ##
log4j.appender.stdout = org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target = System.out
log4j.appender.stdout.layout = org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern =  %d{ABSOLUTE} %5p %c{1}:%L - %m%n

### 输出到日志文件 ###
log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = logs/log.log
log4j.appender.D.Append = true
## 输出DEBUG级别以上的日志
log4j.appender.D.Threshold = DEBUG 	
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n

### 保存异常信息到单独文件 ###
log4j.appender.E = org.apache.log4j.DailyRollingFileAppender
log4j.appender.E.File = logs/error.log 	## 异常日志文件名
log4j.appender.E.Append = true
## 只输出ERROR级别以上的日志!!!
log4j.appender.E.Threshold = ERROR 	
log4j.appender.E.layout = org.apache.log4j.PatternLayout
log4j.appender.E.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
```
### resultMap标签
  当数据表中的字段和对就在的实体类中的属性不完全对应时，可以使用resultMap标签进行字段和属性的对应(不具体指哪个表，只是字段)
```xml
	<!-- ressultMap标签的使用 id随意但要唯一 -->
	<resultMap type="com.mybatis.domain.User" id="userMap">
		<!-- 描述属性和字段的对应关系 为演示，不用都写 -->
		<id column="name" property="id" />
		<result column="name" property="userName" />
		<result column="address" property="address" />
		<result column="age" property="age" />
	</resultMap>
	<select id="selectUserById2" parameterType="int" resultMap="userMap">
		select * from
		l_user where id = #{id}
	</select>
```
### parameterType和resultType，hashmap类型使用

####返回值

UserMapper.xml

```xml
	<!-- 根据id查询User,返回值为Map类型 -->
	<select id="selectUserById4Map" parameterType="int" resultType="map">
		select * from l_user where id = #{id}
	</select>
```
MybatisTest.java
```java
	/**
	 * 根据id查user,返回值为map类型
	 */
	@Test
	public void testSelectMap() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectUserById4Map";
		Map<String, Object> selectOne = openSession.selectOne(arg0, 5);
		System.out.println(selectOne);
	}
```
#### 输入参数
UserMapper.xml
```xml
	<!-- 保存一个user，传入参数类型为map -->
	<insert id="insertUser4Map" parameterType="map">
		insert into l_user (name, address, age) values(#{name}, #{address}, #{age})
	</insert>
```
MybatisTest.java
```java
	/**
	 * 插入，使用map类型
	 */
	@Test
	public void testInsertMap() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.insertUser4Map";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("name", "map");
		map.put("address", "beijing");
		map.put("age", 1);
		
		openSession.insert(arg0, map);
		openSession.commit();
		System.out.print(map);
	}
```
### SQL 字段引用

UserMapper.xml
```xml
	<sql id="userTabAll">
		id, name, address, age
	</sql>
	<select id="selectAllfield" resultType="com.mybatis.domain.User">
		select
		<include refid="userTabAll" />
		from l_user
	</select>
```
MybatisTest.java
```java
	/**
	 * 测试字段引用(抽取字段名)
	 */
	@Test
	public void testSelectFeild() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectAllfield";
		List<User> selectList = openSession.selectList(arg0);
		for (User user : selectList) {
			System.out.println(user);
		}
	}
```
## 04-关于mybatis的几点说明2

### 利用sqlMapConfig.xml定义别名

sqlMapConfig.xml
> 在sqlMapConfig.xml中定义别名时，一定要在environments之上

```xml
	<!-- 定义Bean别名 位置需在environments之上 -->
	<typeAliases>
		<typeAlias type="com.mybatis.domain.User" alias="User" />
	</typeAliases>
```
UserMapper.xml
```xml
	<select id="selectAllfield" resultType="User">
		select
		<include refid="userTabAll" />
		from l_user
	</select>
```
### 模糊查询

> 对于模糊查询，由于使用get方法(getName)，所以需要实用实体类。(string无 string.getName方法)  
> \# 与$的区分：#只是暂位符，而$是真证变量替代，所以要指定具体类型，以便映射  

UserMapper.xml
```xml
	<!-- 模糊查询 parameterType 如果使用string会报错，需使用对象User或map对象-->
	<!-- #与$的区分：#只是暂位符，而$是真证变量替代，所以要指定具体类型，以便映射 -->
	<select id="selectUserByNameLike" parameterType="User" resultType="User">
		select <include refid="userTabAll"/>
		from l_user where name like '%${name}%'
	</select>
```
MybatisTest.java
```java
	/**
	 * 根据name进行模糊查询
	 */
	@Test
	public void testSelectLike() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectUserByNameLike";
		
		User user2 = new User();
		user2.setName("l");
		List<User> selectList = openSession.selectList(arg0, user2);
		for (User user : selectList) {
			System.out.println(user);
		}
	}
```
## 05-关联查询
### 创建 Order 和 Customer实体类
  Order.java
```java
package com.mybatis.domain;
/**
 * 订单
 * @author onefish
 *
 */
public class Order {
	private int id;
	private String orderNumber;
	private Double priceDouble;
	
	private Customer customer;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Double getPriceDouble() {
		return priceDouble;
	}

	public void setPriceDouble(Double priceDouble) {
		this.priceDouble = priceDouble;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}	
}
```
Customer.java
```java
package com.mybatis.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 客户实体
 * 
 * @author onefish
 * 
 */
public class Customer {
	private int id;
	private String name;
	private String address;
	private Integer age;

	private Set<Order> orders = new HashSet<Order>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
}
```
### 创建 Customer 和 Order 表

  Customer
```sql
CREATE TABLE `l_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nameName` varchar(32) DEFAULT NULL,
  `address` varchar(128) NOT NULL,
  `age` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
Order
```sql
CREATE TABLE `l_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderNumber` varchar(20) DEFAULT NULL,
  `price` double,
  `customerId` int, 
  foreign key(customerId) references l_customer(id),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
### 创建表XML

> 可以将所有配置都写在一个文件里。为了区分方便，写在多个文件

sqlMapConfig.xml
> 定义别名及映射文件

```xml
	<!-- 定义Bean别名 位置需在environments之上 -->
	<typeAliases>
		<typeAlias type="com.mybatis.domain.User" alias="User" />
		<typeAlias type="com.mybatis.domain.Order" alias="Order" />
		<typeAlias type="com.mybatis.domain.Customer" alias="Customer" />
	</typeAliases>
	
	<!-- 注册SQL映射文件 -->
	<mappers>
		<mapper resource="com/mybatis/domain/UserMapper.xml" />
		<mapper resource="com/mybatis/domain/CustomerMapper.xml" />
		<mapper resource="com/mybatis/domain/OrderMapper.xml" />
	</mappers>
```
OrderMapper.xml
> 注意字段重名，及字段位置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 所有SQL映射文件中命名空间名不能重复 -->
<mapper namespace="com.mybatis.domain.OrderMapper">

	<!-- 根据id查询订单，同时关联查询客户信息 -->
	<resultMap type="Order" id="orderMap">
		<id property="id" column="id" />
		<result property="orderNumber" column="orderNumber" />
		<result property="price" column="price" />
		<!-- 用来描述多对一关系 -->
		<association property="customer" javaType="Customer">
			<!-- 此处id与order表中的id重复，会取order表中的id值，需在sql中用别名 -->
			<id property="id" column="cid" />
			<result property="name" column="userName" />
			<result property="address" column="address" />
			<result property="age" column="age" />
		</association>
	</resultMap>
	<select id="selectOrderById" parameterType="int" resultMap="orderMap">
		<!-- 解决重名问题 表字段有位置问题：如果是c.*, o.* o.id 的值为c.id -->
		select o.*, c.*, c.id as cid from l_customer as c, l_order as o where
		o.`customerId` = c.id and o.`id` =#{id}
	</select>
</mapper>
```
MybatisTest.java
```java
	/**
	 * 关联查询 Order
	 */
	@Test
	public void testSelectOrderById() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.OrderMapper.selectOrderById";
		
		Order order = openSession.selectOne(arg0, 2);
		System.out.println(order);
	}
```
## 07-关联查询2
### 集合
CustomerMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 所有SQL映射文件中命名空间名不能重复 -->
<mapper namespace="com.mybatis.domain.CustomerMapper">
	<resultMap type="Customer" id="customerMap">
		<id property="id" column="id" />
		<result property="name" column="userName" />
		<result property="address" column="address" />
		<result property="age" column="age" />
		<!-- 使用collection来描述集合(一对多) ofType指定集合的类型 -->
		<collection property="orders" ofType="Order" column="customerId">
			<id property="id" column="oid" />
			<result property="orderNumber" column="orderNumber" />
			<result property="price" column="price" />
		</collection>
	</resultMap>
 

<!-- 
	# 也可以简写成
  	<mapper namespace="com.mybatis.domain.CustomerMapper">
   	<resultMap type="Customer" id="customerMap">
   		<collection property="orders" ofType="Order" column="customerId" extend="customerMap">
			<id property="id" column="oid" />
			<result property="orderNumber" column="orderNumber" />
			<result property="price" column="price" />
		</collection>
	</resultMap>
-->
	<select id="selectCustomerById" parameterType="int" resultMap="customerMap">
		select c.*, o.*, o.id as oid from l_customer c , l_order o where
		c.id=o.customerId
		and c.id =#{id}
	</select>
</mapper>
```
MybatisTest.java
```java
	/**
	 * 关联集合查询 Customer
	 */
	@Test
	public void testSelectCustomerById() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.CustomerMapper.selectCustomerById";

		Customer customer = openSession.selectOne(arg0, 1);
		System.out.println(customer);
	}
```

## 08-动态SQL
### 动态标签if语句
UserMapper.xml
```xml
	<!-- 动态查询 -->
	<select id="selectUserByCondition" parameterType="User"
	resultMap="userMap">
	select * from l_user where 1=1
		<!-- 此处id为属性的id(User.id) -->
		<if test="id != 0">
			and id = #{id}
		</if>
		<if test="name != null">
			and name = #{name}
		</if>
	</select>
```
MybastisTest.java
```java
	/**
	 * 动态SQL 查询
	 */
	@Test
	public void testSelectUserCondition() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectUserByCondition";

		User user = new User();
		user.setName("onefish");
		
		List<User> user2 = openSession.selectList(arg0, user);
		System.out.println("MybatisTest.testSelectUserCondition()");
		for (User user3 : user2) {
			System.out.println(user3);
		}
	}
```
### 动态标签where语句
> 会自动处理where语法  

UserMapper.xml
```xml
	<!-- 动态查询 where -->
	<select id="selectUserByCondition2" parameterType="User"
		resultMap="userMap">
		select * from l_user
		<where>
			<!-- 此处id为属性的id(User.id) -->
			<if test="id !=null and id != 0">
				and id = #{id}
			</if>
			<if test="name != null">
				and name = #{name}
			</if>
		</where>
	</select>
```
MybastisTest.java
```java
	/**
	 * 动态SQL 查询
	 */
	@Test
	public void testSelectUserCondition() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectUserByCondition2";

		User user = new User();
		user.setName("onefish");
		
		List<User> user2 = openSession.selectList(arg0, user);
		System.out.println("MybatisTest.testSelectUserCondition()");
		for (User user3 : user2) {
			System.out.println(user3);
		}
	}
```
### 动态标签set语句
> set 要求条件至少要有一个成立  

UserMapper.xml
```xml
	<!-- 动态查询 set -->
	<update id="updateUserByConition" parameterType="User">
		update l_user
		<set>
			<!-- 此处id为属性的id(User.id) -->
			<if test="id !=null and id != 0">
				id = #{id},
			</if>
			<if test="name != null">
				name = #{name},
			</if>
			<if test="age != 0">
				age = #{age},
			</if>
		</set>
		where id = #{id}
	</update>
```
MybastisTest.java
```java
	/**
	 * 动态SQL update set语法
	 */
	@Test
	public void testUpdateUserByConition() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.updateUserByConition";

		User user = new User();
		user.setId(6);
		user.setAge(35);
		user.setName("lcm");
		int id = openSession.update(arg0, user);
		openSession.commit();
		System.out.println(id);
	}
```
## 09-批量操作

### 批量插入  foreach标签
UserMapper.xml
```xml
	<!-- 批量插入数据 -->
	<insert id="insertManyUser" parameterType="map">
		insert into l_user(name, address, age) values
		<foreach collection="users" item="user" separator=",">
			(#{user.name}, #{user.address}, #{user.age})
		</foreach>
	</insert>
```
MybastisTest.java
```java
	/**
	 * 批量插入
	 */
	@Test
	public void testInsertManyUser() {
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < 3; i++) {
			User user = new User();
			user.setName("test" + i);
			user.setAddress("beijing" + i);
			user.setAge(40 + i);
			System.out.println(user);
			users.add(user);
		}
		// 需要使用Map进行包装。
		Map<String, List<User>> map = new HashMap<String, List<User>>();
		map.put("users", users);
		
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.insertManyUser";
		openSession.insert(arg0, map);
		openSession.commit();
		openSession.close();
		System.out.println(users);
	}
```
### foreach 标签 separator、open、close
> separator：表示分隔符  
> open：表示在foreach 开始时(不是循环过程中)添加的字符  
> close：表示在foreach 结束时(不是循环过程中)添加的字符  

```xml
	<!-- 批量查找 -->
	<select id="selectManyUser" parameterType="map">
		select * from l_user where id in
		<foreach collection="ids" item="id" separator="," open="(" close=")">
			#{id}
		</foreach>
	</select>
```

## 10-spring整合mybatis1

### 库文件

> mybatis-3.4.1.jar 需要 mybatis-spring-1.3.0.jar 库。这个必须对应  
>

### 配置文件

jdbc.properties

```properties
driverClass=com.mysql.jdbc.Driver
jdbcUrl=jdbc:mysql://localhost:3306/mybatis_01
userName=root
password=tom0018
maxPoolSize=50
minPoolSize=20
initPoolSize=20
```

### sping配置文件

beans.xml  


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="
     http://www.springframework.org/schema/beans
     http://www.springframework.org/schema/beans/spring-beans.xsd
     http://www.springframework.org/schema/context
     http://www.springframework.org/schema/context/spring-context.xsd
     http://www.springframework.org/schema/tx
     http://www.springframework.org/schema/tx/spring-tx.xsd
     http://www.springframework.org/schema/aop
     http://www.springframework.org/schema/aop/spring-aop.xsd">
    
    <!-- 读取配置文件 -->
    <context:property-placeholder location="classpath:jdbc.properties"/> 
    <!-- 配置数据源 使用c3p0-->
	<!-- <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource"></bean> -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="driverClass" value="${driverClass}"/>
		<property name="jdbcUrl" value="${jdbcUrl}"/>
		<property name="user" value="${userName}"/>
		<property name="password" value="${password}"/>
		<property name="maxPoolSize" value="${maxPoolSize}"/>
		<property name="minPoolSize" value="${minPoolSize}"/>
		<property name="initialPoolSize" value="${initPoolSize}"></property>	
	</bean>
	
	<!-- 配置Mybatis工厂类 -->
	<bean id="sessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean" >
		<!-- 通过setter方法，注入数据源 -->
		<property name="dataSource" ref="dataSource"/>
		<!-- 注入mybatis核心配置文件 -->
		<property name="configLocation" value="classpath:sqlMapConfig.xml"/>
	</bean>
	
	<!-- 配置事物管理器 -->
	<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource"/>
	</bean>
	
	<!-- 事物通知 -->
	<tx:advice id="mybatis_advice" transaction-manager="txManager">
		<tx:attributes>
			<tx:method name="save*" propagation="REQUIRED" isolation="DEFAULT"/>
			<tx:method name="delete*" propagation="REQUIRED"/>
			<tx:method name="update*" propagation="REQUIRED"/>
			<tx:method name="find*" read-only="true"/>
		</tx:attributes>
	</tx:advice>
	
	<!-- aop切面 -->
	<aop:config>
		<aop:pointcut expression="execution(* com.mybastis.service.*.*(..))" id="mybatis_pointCut"/>
		<aop:advisor advice-ref="mybatis_advice" pointcut-ref="mybatis_pointCut"/>
	</aop:config>
</beans>
```

### mybatis配置文件
sqlMapConfig.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<!-- 定义Bean别名 位置需在environments之上 -->
	<typeAliases>
		<typeAlias type="com.mybatis.domain.User" alias="User" />
		<typeAlias type="com.mybatis.domain.Order" alias="Order" />
		<typeAlias type="com.mybatis.domain.Customer" alias="Customer" />
	</typeAliases>

	<!-- 注册SQL映射文件 -->
	<mappers>
		<mapper resource="com/mybatis/domain/UserMapper.xml" />
		<mapper resource="com/mybatis/domain/CustomerMapper.xml" />
		<mapper resource="com/mybatis/domain/OrderMapper.xml" />
	</mappers>
</configuration>
```
###  log4j配置文件
log4j.properties
```properties
 ### set log levels ###
log4j.rootLogger = debug, stdout, D, E

### 输出到控制台 ##
log4j.appender.stdout = org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target = System.out
log4j.appender.stdout.layout = org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern =  %d{ABSOLUTE} %5p %c{1}:%L - %m%n

### 输出到日志文件 ###
log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = logs/log.log
log4j.appender.D.Append = true
## 输出DEBUG级别以上的日志
log4j.appender.D.Threshold = DEBUG \t
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n

### 保存异常信息到单独文件 ###
log4j.appender.E = org.apache.log4j.DailyRollingFileAppender
## 异常日志文件名
log4j.appender.E.File = logs/error.log \t
log4j.appender.E.Append = true
## 只输出ERROR级别以上的日志!!!
log4j.appender.E.Threshold = ERROR \t
log4j.appender.E.layout = org.apache.log4j.PatternLayout
log4j.appender.E.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
```

## 11-spring整合mybatis2
### 创建Dao
建立接口文件
IUserDao.java
```java
package com.mybatis.dao;

import com.mybatis.domain.User;

public interface IUserDao {
	public int saveUser(User user);
	public User findUserById(int id);
}
```
建立接口实现文件
> 在与spring 整合时，spring为mybatis提了SqlSessionDaoSupport父类  
> 继承后，可以通过 this.getSqlSession(); 直接得到mybatis sqlSession。  

UserDaoImpl.java
```java
package com.mybatis.dao;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.mybatis.domain.User;

public class UserDaoImpl extends SqlSessionDaoSupport implements IUserDao{

	@Override
	/**
	 * 保存User数据，并返回主键id
	 */
	public int saveUser(User user) {
		// TODO Auto-generated method stub
		// 从父类中得到SqlSession
		SqlSession openSession = this.getSqlSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.insertUser";
		// 在delete里默认设置为手工提交
		int id = openSession.insert(arg0, user);
		// 事物提交
		openSession.commit();
		System.out.println("插入" + id + "行, id:" + user.getId());
		return user.getId();
	}

	@Override
	public User findUserById(int id) {
		// TODO Auto-generated method stub
		// 从父类中得到SqlSession
		SqlSession openSession = this.getSqlSession();
		String arg0 = "com.mybatis.domain.UserMapper.selectUserById";
		User user1 = openSession.selectOne(arg0, id);
		// 在spring下。没有必要再关闭。会由spring进行关闭操作
		//openSession.close();
		return user1;
	}
}
```
### 创建Service
建立接口文件
IUserService.java
```java
package com.mybatis.service;

import com.mybatis.domain.User;

public interface IUserService {
	public int saveUser(User user);
	public User findUserById(int id);
}
```
建立接口实现文件
UserServiceImpl.java
```java
package com.mybatis.service;

import com.mybatis.dao.IUserDao;
import com.mybatis.domain.User;

public class UserServiceImpl implements IUserService {
	private IUserDao userDao;
	
	// 此方法不是必须
	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public int saveUser(User user) {
		// TODO Auto-generated method stub
		return userDao.saveUser(user);
	}

	@Override
	public User findUserById(int id) {
		// TODO Auto-generated method stub
		return userDao.findUserById(id);
	}

}
```

添加注入到配置
beans.xml
```xml
	<!-- 由于没有使用注解，需注入bean -->
	<bean id="userDao" class="com.mybatis.dao.UserDaoImpl">
		<!-- 注入父类(SqlSessionDaoSupport)属性 -->
		<property name="sqlSessionFactory" ref="sessionFactory"/>
	</bean>
	<!-- 注入service -->
	<bean id="userSevice" class="com.mybatis.service.UserServiceImpl">
		<property name="userDao" ref="userDao"/>
	</bean>
```
## 12-注解开发mybatis

> 不建议使用引种方式。失去使用mybatis真正的含义，失去可变动性。

### 建立接口文件

IUserAnnotationDao.java
```java
package com.mybatis.dao;

import org.apache.ibatis.annotations.Select;

import com.mybatis.domain.User;

public interface IUserAnnotationDao {
	@Select(value="select * from l_user where id=#{id}")
	public User findUserByIdA(int id);
}
```

### 接口配置
sqlMapConfig.xml
> 在mapper里增加相关接口  

```xml
	<!-- 注册SQL映射文件 -->
	<mappers>
		<mapper class="com.mybatis.dao.IUserAnnotationDao"/>
	</mappers>
```

### 测试
```java
	/**
	 * Annotation 实现方法。
	 * @throws IOException
	 */
	@Test
	public void testSelectUserByIdAn() throws IOException{
		String resource = "sqlMapConfig.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		SqlSession openSession = sqlSessionFactory.openSession();
		// 调用getMapper
		IUserAnnotationDao annotationDao = openSession.getMapper(IUserAnnotationDao.class);
		User user = annotationDao.findUserByIdA(1);
		System.out.println(user);
	}
```

### 14-一级缓存
> 一级缓存不用配置，系统默认
> 缓存的是对象的引用  
> 缓存的生命周期为session的生命周期  

### 15-二级缓存
sqlMapConfig.xml
> 二级缓存需要设置  
> 用于设置二级缓存，总开关。  
> 一级缓存，缓存的是对象的引用。二级缓存，缓存的是数据(散装数据)  

```xml
	<!-- 设置二级缓存，需放到头部 -->
	<settings>
		<setting name ="cacheEnabled" value="true"/>
	</settings>
```
UserMapper.xml
> 二级缓存，需要针对每个表进行设置，通过<cache/> 标签打开。  
> 如果某条语句不需要cache，可以在其标签设置  useCache="false" 属性  
> 查询数据，会同时放入1级和2级缓存。  
> 二级缓存的生命周期是sessionFactory的生命周期。  

```xml
	<!-- 开启User表的二级缓存，针对每个表,默认使用mybatis自带2级缓存 -->
	<cache/>
	
	<!-- id:当前文件唯一标识 parameterType:当前SQL接收的参数类型 resultType:结果类型(使用全路径)
		如果此条数据不需要缓存。 加useCache="false"
	-->
	<select id="selectUserByIdNoCache" parameterType="int"
		resultType="com.mybatis.domain.User" useCache="false">
		select * from l_user where id = #{id}
	</select>
```
User.java
> 使用2级缓存，必须在对象上开启实例化接口。  

```java
public class User implements java.io.Serializable{
}
```

## 16-ehcache缓存
### 导入包文件
> ehcache-core-2.6.8.jar  
> mybatis-ehcache-1.0.3.jar  

### ehcache配置
> 配置文件可以从ehcache-core-2.6.8.jar包中取得（ehcache-failsafe.xml名字改为ehcache.xml） 
> ehcache的版本要和mybatis的版本匹配   

ehcache.xml
```xml
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
	<!-- 放在系统temp目录 -->
    <diskStore path="java.io.tmpdir"/>
	<!-- 也可以自己指定 -->
	<!-- <diskStore path="/opt/tmp"/> -->
    <defaultCache
		<!-- 在内存中缓存多少对象，多余则放在磁盘 -->
		maxElementsInMemory="10000"
		<!-- 缓存对象是否永久有效，一但设置了，timeout将不起作用 -->
		eternal="false"
		<!-- 仅当element不是永久有效时使用， 可选属性，默认值是0，可闲置时间无穷大 -->
		<!-- 设置element在失效前的允许闲置时间。 -->
		timeToIdleSeconds="120"
		<!-- 仅当element不是永久有效时使用， 可选属性，默认值是0，可闲置时间无穷大 -->
		<!-- 设置Element 在失效前允许存活时间，最大时间介于创建时间和失效时间之间。 -->
		timeToLiveSeconds="120"
		<!-- 磁盘中最大缓存对象数。若是0表示无穷大 -->
		maxElementsOnDisk="10000000"
		<!-- overflowToDisk 配置此属性，当mwdhb中Element数量达到maxElementsInMemory时
		Echcahe将会Element写到磁盘中 -->
		<!-- dispSpoolBufferSizeMB 这个参数设置DiskStore的缓存区大小，默认是30MB。
		每个cache都应该有自己的一个缓冲区。 --> 
		<!-- diskPersistent 是否在重启服务时，清除磁盘上的缓存数据，true不清除 -->
		<!-- 磁盘失效线程运行时间间隔 -->
		diskExpiryThreadIntervalSeconds="120"
		<!-- 当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清内存。
		默认策略是LRU（最近最少使用）、FIFO、LFU -->
		memoryStoreEvictionPolicy="LRU">
        <persistence strategy="localTempSwap"/>
    </defaultCache>
</ehcache>
```
UserMapper.xml
```xml
<!-- 开启User表的二级缓存，针对每个表,默认使用mybatis自带2级缓存 -->
	<!-- <cache/> -->
	<!-- 也可以指定第三方如：ehcache -->
	<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```
