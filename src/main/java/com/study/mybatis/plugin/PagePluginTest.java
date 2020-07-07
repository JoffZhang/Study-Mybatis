package com.study.mybatis.plugin;

import com.study.mybatis.start.bean.Websites;
import com.study.mybatis.start.mapper.WebsitesMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zy
 * @date 2020/7/7 15:12
 */
public class PagePluginTest {

	public static void main(String[] args) throws IOException {
		// 指定全局配置文件
		String resource = "com/study/mybatis/plugin/mybatis-config.xml";
		// 读取配置文件
		InputStream inputStream = Resources.getResourceAsStream(resource);
		// 构建sqlSessionFactory
		SqlSessionFactory sqlSessionFactory  = new SqlSessionFactoryBuilder().build(inputStream);
		try (
				// 获取sqlSession
				SqlSession sqlSession = sqlSessionFactory.openSession();
		) {
			PageUtil.page(0,1);
			System.out.println(Thread.currentThread().getId());
			WebsitesMapper mapper = sqlSession.getMapper(WebsitesMapper.class);
			Websites websites = mapper.selectWebsites(3);
			System.out.println(websites);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}
