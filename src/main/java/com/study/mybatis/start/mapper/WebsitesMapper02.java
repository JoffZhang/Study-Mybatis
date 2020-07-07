package com.study.mybatis.start.mapper;

import com.study.mybatis.start.bean.Websites;
import org.apache.ibatis.annotations.Select;

/**
 * @author zy
 * @date 2020/6/3 15:46
 */
public interface WebsitesMapper02 {
	@Select("select id,name,url,alexa,country from websites where id = #{id}")
	Websites selectWebsites(Integer id);
}
