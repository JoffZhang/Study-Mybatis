package com.study.mybatis.start;

import com.study.mybatis.start.bean.Websites;
import com.study.mybatis.start.mapper.WebsitesMapper;
import com.study.mybatis.start.mapper.WebsitesMapper02;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zy
 * @date 2020/6/2 15:38
 */
public class MyBatisDemo01 {

	public static void main(String[] args) throws IOException {
		buildSqlSessionWithXML();
		System.out.println("================================================================");
		//buildSqlSessionNoXml();
	}

	/**
	 * 非xml方式构建SqlSessionFactory
	 */
	private static void buildSqlSessionNoXml() throws IOException {
		DataSource dataSource = MyDataSourceFactory.getMyDataSource("com/study/mybatis/start/jdbc.properties");
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(WebsitesMapper02.class);
		configuration.addMapper(WebsitesMapper.class);
		//如果存在一个同名 XML 配置文件，MyBatis 会自动查找并加载它（在这个例子中，基于类路径和 WebsitesMapper.class 的类名，会加载 WebsitesMapper.xml）
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		/**
		 * SqlSession 提供了在数据库执行 SQL 命令所需的所有方法
		 * 可以通过 SqlSession 实例来直接执行已映射的 SQL 语句
		 */
		try(
				SqlSession sqlSession = sqlSessionFactory.openSession();
		){
			//通过全限定名调用映射语句
			// 操作CRUD，第一个参数：指定statement，规则：命名空间+“.”+statementId
			// 第二个参数：指定传入sql的参数：这里是用户id
			Websites websites = sqlSession.selectOne("com.study.mybatis.start.mapper.WebsitesMapper02.selectWebsites", 1);
			System.out.println(websites);
			/**
			 * 这种方式能够正常工作，对使用旧版本 MyBatis 的用户来说也比较熟悉。
			 * 但现在有了一种更简洁的方式
			 * ——使用和指定语句的参数和返回值相匹配的接口（比如 BlogMapper.class），
			 * 现在你的代码不仅更清晰，更加类型安全，还不用担心可能出错的字符串字面值以及强制类型转换
			 */
			WebsitesMapper mapper = sqlSession.getMapper(WebsitesMapper.class);
			Websites websites1 = mapper.selectWebsites(1);
			System.out.println(websites1);

			System.out.println("=====一下演示一级缓存（同sql同参数）========================");
			WebsitesMapper02 mapper02 = sqlSession.getMapper(WebsitesMapper02.class);

			Websites websites2 = mapper02.selectWebsites(1);
			System.out.println(websites2);

			//可强制清除缓存 	sqlSession.clearCache();
			System.out.println("查询使用缓存");
			Websites websites3 = mapper02.selectWebsites(1);
			System.out.println(websites3);
			System.out.println("重新查询不使用缓存");
			Websites websites4 = mapper02.selectWebsites(2);
			System.out.println(websites4);
		}

	}

	/**
	 * xml方式构建SqlSessionFactory
	 */
	private static void buildSqlSessionWithXML() throws IOException {
		// 指定全局配置文件
		String resource = "com/study/mybatis/start/mybatis-config.xml";
		// 读取配置文件
		InputStream inputStream = Resources.getResourceAsStream(resource);
		// 构建sqlSessionFactory
		SqlSessionFactory sqlSessionFactory  = new SqlSessionFactoryBuilder().build(inputStream);
		try (
				// 获取sqlSession
				SqlSession sqlSession = sqlSessionFactory.openSession();
		){
			WebsitesMapper mapper = sqlSession.getMapper(WebsitesMapper.class);
			Websites websites = mapper.selectWebsites(3);
			System.out.println(websites);
			System.out.println("===================1======================");
			 mapper = sqlSession.getMapper(WebsitesMapper.class);
			 websites = mapper.selectWebsites(3);
			System.out.println(websites);
			System.out.println("==================2=======================");
			mapper = sqlSession.getMapper(WebsitesMapper.class);
			websites = mapper.selectWebsites(1);
			System.out.println(websites);
			System.out.println("==================3=======================");
		}
	}


}

class MyDataSourceFactory{
	/**
	 * POOLED Datasource
	 * @return
	 */
	public static DataSource getMyDataSource(String resource) throws IOException {
		Properties props = Resources.getResourceAsProperties(resource);
		PooledDataSource pooledDataSource = new PooledDataSource();
		pooledDataSource.setDriver(props.getProperty("driver"));
		pooledDataSource.setUrl(props.getProperty("url"));
		pooledDataSource.setUsername(props.getProperty("user"));
		pooledDataSource.setPassword(props.getProperty("password"));
		return pooledDataSource;
	}
}