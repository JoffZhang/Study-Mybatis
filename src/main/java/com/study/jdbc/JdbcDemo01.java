package com.study.jdbc;

import java.sql.*;

/**
 * JDBC连接数据库步骤
 * 1.加载驱动程序
 * 2.获取数据库连接
 * 3.从操作数据库，CRUD
 * 		执行sql语句方式， 三种：
 * 			1.使用Statement
 * 			2.使用PreparedStatement   可以使用占位符,它是由占位符标识需要输入数据的位置，然后再逐一填入数据。
 * 			3.使用CallableStatement   主要用来调用存储过程
 * 4.结果处理
 *
 *
 *
 */
public class JdbcDemo01 {

	private static final String URL = "jdbc:mysql://192.168.120.143:3306/test1?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&serverTimezone=Asia/Shanghai";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "123456";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";


	public static void main(String[] args) {
		System.out.println("==========================jdbcTest1==========================");
		jdbcTest1();
		System.out.println("==========================jdbcTest2==========================");
		jdbcTest2();
	}

	private static void jdbcTest2() {
		Connection connection = DBUtil.getConnection();
		ResultSet resultSet = null;
		try (
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT id,name,url,alexa,country FROM websites where id=?");
		) {
			preparedStatement.setInt(1, 2);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				System.out.println("-------------" + resultSet.getInt("id") + "----------------------");
				System.out.println(resultSet.getString("name"));
				System.out.println(resultSet.getString("url"));
				System.out.println(resultSet.getString("alexa"));
				System.out.println(resultSet.getString("country"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			System.out.println("关闭资源");
			DBUtil.close(connection,null,resultSet);
		}

	}

	private static void jdbcTest1() {
		try (
				Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
				//3.操作数据库
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT id,name,url,alexa,country FROM websites");
		) {
			//1.加载驱动程序
			Class.forName(DRIVER);
			//2.获取数据库连接

			while (resultSet.next()) {
				System.out.println("-------------" + resultSet.getInt("id") + "----------------------");
				System.out.println(resultSet.getString("name"));
				System.out.println(resultSet.getString("url"));
				System.out.println(resultSet.getString("alexa"));
				System.out.println(resultSet.getString("country"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/**
		 * 使用新语法 try-with-resources 语句，称为 ARM 块(Automatic Resource Management) ，自动资源管理。 try(){}cache(){}
		 * 带有resources的try语句声明一个或多个resources。resources是在程序结束后必须关闭的对象。t
		 * ry-with-resources语句确保在语句末尾关闭每个resources。
		 * 任何实现java.lang.AutoCloseable,包括实现了java.io.Closeable的类，都可以作为resources使用。
		 * finally {
		 * }
		 */
	}
}
