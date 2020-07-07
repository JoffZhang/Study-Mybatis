package com.study.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author zy
 * @date 2020/6/2 11:29
 */
public class DBUtil {

	private static String username;//数据库登陆账号
	private static String password;//数据库登陆密码
	private static String url;//数据库 url 及端口
	private static String driver;//驱动类
	//通常情况下把数据密码放置 项目配置文件中 , 相对于放入类中，更安全一些。

	private static Properties properties;
	static{//静态初始化块
		//创建获取配置文件对象。
		properties = new Properties();
		try{
			//创建获取配置文件对象
			InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
			properties.load(in);
			username = properties.getProperty("user");
			password = properties.getProperty("password");
			url = properties.getProperty("url");
			driver = properties.getProperty("driver");

			//加载驱动类
			Class.forName(driver);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 私有化构造器，防止用户重复构建对象，造成资源浪费
	 */
	private DBUtil(){}

	/**
	 * 定义一个Connection类型的变量用来存储获取到的connection实例化对象
	 */
	private static Connection conn;

	//创建数据库连接对象
	public static Connection getConnection(){
		try{
			//conn = DriverManager.getConnection(url,username,password);
			conn = DriverManager.getConnection(url,properties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	//关闭对应的数据流
	public static void close(Connection conn, PreparedStatement preparedStatement, ResultSet resultSet){
		//关闭流原则，先创建后关闭
		if(resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(preparedStatement != null){
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
