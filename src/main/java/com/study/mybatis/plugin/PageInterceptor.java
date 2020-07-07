package com.study.mybatis.plugin;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @author zy
 * @date 2020/7/7 11:32
 *在 mybatis 中， 可以拦截的方法包括
 * Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
 * ParameterHandler (getParameterObject, setParameters)
 * ResultSetHandler (handleResultSets, handleOutputParameters)
 * StatementHandler (prepare, parameterize, batch, update, query)
 *
 *
 * PageInterceptor拦截
 * Executor{
 *   <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException;
 * }
 * 方法
 */
@Intercepts({
		@Signature(type = Executor.class,method = "query",args = {
				MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class
		})
})
public class PageInterceptor implements Interceptor {


	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("public Object intercept(Invocation invocation) 执行拦截逻辑的方法");
		//从Invocation对象中获取被拦截的方法信息，这里就是
		//public abstract java.util.List org.apache.ibatis.executor.Executor.query(
		// 	org.apache.ibatis.mapping.MappedStatement,java.lang.Object,
		// 	org.apache.ibatis.session.RowBounds,
		// 	org.apache.ibatis.session.ResultHandler
		// 	) throws java.sql.SQLException
		Object[] args = invocation.getArgs();
		//获取MappedStatement对象
		MappedStatement ms = (MappedStatement) args[0];
		//获取用户传入的实参
		Object parameter = args[1];
		//获取RowBounds对象
		RowBounds rowBounds = (RowBounds) args[2];
		//获取分页参数
		System.out.println(Thread.currentThread().getId());
		PageUtil.Page pageParam = PageUtil.getPageParam();
		if(pageParam != null){
			//获取rowBounds.getOffset    getLimit
			int offset = rowBounds.getOffset();
			int limit = rowBounds.getLimit();
			//构造新的sql语句  select  ,......   limit 0,1
			//获取BoundSql对象，其中记录了包含“？”占位符的sql语句
			BoundSql boundSql = ms.getBoundSql(parameter);
			String pageSql = getPageSql(boundSql.getSql(),pageParam.getOffset(),pageParam.getLimit());
			//根据当前的sql语句创建新的MappedStatement对象，并更新到Invocation对象记录的参数列表中
			BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			MappedStatement mappedStatement = createMappedStatement(ms,newBoundSql);
			args[0] = mappedStatement;
			//当前连接的Excutor.query()方法中的RowBounds参数不再控制查找结果集的范围，所以要进行重置
			args[2] = new RowBounds(RowBounds.NO_ROW_OFFSET,RowBounds.NO_ROW_LIMIT);
		}
		//通过Invoation.proceed()方法调用被拦截的Executor.query()方法
		Object proceed = invocation.proceed();
		PageUtil.removePageParam();
		return proceed;
	}

	private MappedStatement createMappedStatement(MappedStatement ms, BoundSql newBoundSql) {
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(),new StaticSqlSource(ms.getConfiguration(),newBoundSql.getSql(),newBoundSql.getParameterMappings()), ms.getSqlCommandType());
		builder.keyColumn(delimitedArrayToString(ms.getKeyColumns()));
		builder.keyGenerator(ms.getKeyGenerator());
		builder.keyProperty(delimitedArrayToString(ms.getKeyProperties()));
		builder.lang(ms.getLang());
		builder.resource(ms.getResource());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.resultOrdered(ms.isResultOrdered());
		builder.resultSets(delimitedArrayToString(ms.getResultSets()));
		builder.resultSetType(ms.getResultSetType());
		builder.timeout(ms.getTimeout());
		builder.statementType(ms.getStatementType());
		builder.useCache(ms.isUseCache());
		builder.cache(ms.getCache());
		builder.databaseId(ms.getDatabaseId());
		builder.fetchSize(ms.getFetchSize());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		return builder.build();
	}

	private String getPageSql(String boundSql, int offset, int limit) {
		//获取boundSql
		StringBuilder stringBuffer = new StringBuilder(boundSql.trim());
		//对sql语句进行格式化，在映射配置文件中编写SQL是，或是经过动态SQL解析之后，SQL语句的格式会比较凌乱，这里可以对SQL语句进行格式化
		//通过Dialect策略，检测当前使用的数据库产品是否支持分页功能
		stringBuffer.append(" limit ");
		if(offset>0){
			stringBuffer.append(offset).append(",").append(limit);
		}else{
			stringBuffer.append(limit);
		}
		return stringBuffer.toString();
	}


	@Override
	public Object plugin(Object target) {
		System.out.println("public Object plugin(Object target) 界定是否触发intercept()方法");
		return Plugin.wrap(target,this);
	}

	@Override
	public void setProperties(Properties properties) {
		System.out.println("public void setProperties(Properties properties) 根据配置初始化Interceptor对象");
	}

	private String delimitedArrayToString(String[] array) {
		if (array == null || array.length == 0) {
			return "";
		}
		return String.join(",",array);
	}
}
