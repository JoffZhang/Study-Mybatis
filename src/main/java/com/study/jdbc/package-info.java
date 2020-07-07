package com.study.jdbc;

/**
 *
 *JDBC API 允许用户访问任何形式的表格数据，尤其是存储在关系数据库中的数据。
 *JDBC全称Java Database Connectivity
 *JDBC可以通过载入不同的数据库的“驱动程序”而与不同的数据库进行连接。
 *执行流程：
 * 	连接数据源，如：数据库。
 * 	为数据库传递查询和更新指令。
 * 	处理数据库响应并返回的结果。
 *
 *
 * 	JDBC编程中的主要步骤
 * 			1.加载驱动程序，注册数据库驱动类
 *
 * 			Class.forName(DRIVER);
 *
 * 	    	2.通过DriverManager获取数据库连接
 *
 *	    	Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
 *
 * 	    	3.操作数据库
	 * 			通过数据库连接创建Statement
	 * 			Statement statement = connection.createStatement();
 *
	 * 			通过Statement对象执行sql语句，得到ResultSet对象
	 * 			ResultSet resultSet = statement.executeQuery("SELECT id,name,url,alexa,country FROM websites");
 *
	 * 			通过ResultSet读取数据，并将数据转成JavaBean对象
	 * 			while (resultSet.next()) {
	 * 				System.out.println("-------------" + resultSet.getInt("id") + "----------------------");
	 * 				System.out.println(resultSet.getString("name"));
	 * 				System.out.println(resultSet.getString("url"));
	 * 				System.out.println(resultSet.getString("alexa"));
	 * 				System.out.println(resultSet.getString("country"));
	 *           }
 *
	 *           关闭ResultSet、Statement对象以及数据库连接，释放相关资源
	 *           resultSet.close()
	 *           statement.close()
	 *           connection.close()
 *
 * */