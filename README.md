# 缓存框架 (Cache Framework)
## 目的
1. 减少数据库压力
2. 减少WEB应用响应时间
3. 统一缓存处理

## 最新版本

v1.0.0

* 仅支持Memcached内存对象缓存系统
* 仅支持DruidDataSource数据库连接池的SQL解析

## 介绍
该缓存框架的主要功能是拦截指定切面的方法，首次获取数据库元数据并缓存，之后对该方法的调用将返回缓存数据。

核心逻辑为保存所有数据库表的修改时间，保存被缓存方法的获取缓存时间和该方法所用到的表。方法调用并且缓存存在时，该缓存的获取时间小于任一该方法所用表的修改时间，既判断该缓存过期失效。

缓存失效条件：

* 被缓存方法使用的表数据有变动
* 缓存时间过期

## 重要组成部分：
#### CacheManager

框架的入口。使用其实现类统一处理缓存。

框架中存在一个CacheManager接口的配置类实现ConfigCacheManger。

可配置属性为：

* cacheFactory：用于构建缓存客户端实例
* sqlParser：用于SQL解析
* defaultCacheTime：缓存时间（默认1小时）
* filters：过滤器列表
* isProxyCached：是否代理缓存（默认true）。有些场景可能不需要使用缓存，但是必须使用SqlParser解析SQL控制缓存失效。

## 配置

最小配置：

	<aop:config>
		<aop:aspect ref="cacheManager">
			<!-- 切面建议拦截在DAO层，但是会有很多警告在日志中打印。也可以拦截Service层，但是注意更新业务逻辑并不会使缓存失效。 -->
			<aop:around method="doCache" pointcut="execution(* com.qianlixy.framework.cache.business.dao.*.*(..))"/>
		</aop:aspect>
	</aop:config>
	
	<bean id="cacheManager" class="com.qianlixy.framework.cache.ConfigCacheManager" init-method="init">
		<property name="cacheFactory">
			<bean class="com.qianlixy.framework.cache.impl.MemcachedCacheFactory">
				<property name="client" ref="memcachedClient" />
			</bean>
		</property>
		<property name="sqlParser">
			<bean class="com.qianlixy.framework.cache.parse.DruidSqlParser">
				<property name="dataSource" ref="dataSource"></property>
			</bean>
		</property>
	</bean>
	
## 歉意
时间有限，注释不完善，请谅解。！