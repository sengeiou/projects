package com.normal.core.mybatis;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Intercepts( {
       @Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class}) })
public class PageInterceptor implements Interceptor {

    /**
     * Method to be executed after interception
     */
    public Object intercept(Invocation invocation) throws Throwable {
       //For StatementHandler, there are only two implementation classes, one is RoutingStatementHandler, and the other is BaseStatementHandler, an abstract class.
       //BaseStatementHandler has three subclasses: SimpleStatementHandler, PreparedStatementHandler and Callable StatementHandler.
       //SimpleStatement Handler is used to process Statement, Prepared Statement Handler is used to process Prepared Statement, and Callable Statement Handler is used to process Prepared Statement.
       //Processing Callable Statement. Mybatis is a Running Statement Handler for Sql statement processing, and has one in the Routing Statement Handler.
       //For the delegate attribute of StatementHandler type, RoutingStatementHandler establishes the corresponding BaseStatementHandler, namely SimpleStatementHandler, according to the different Statements.
       //Prepared Statement Handler or Callable Statement Handler. In Routing Statement Handler, the implementation of all Statement Handler interface methods is the method corresponding to the delegate invoked.
       //We have marked the prepare method on the Page Interceptor class with @Signature that intercepts only the StatementHandler interface, because Mybatis only when RoutingStatementHandler is established
       //The package is wrapped through the plugin method of the Interceptor, so the target object we intercepted here must be the RoutingStatementHandler object.
       RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
       //Get the delegate attribute of the current RoutingStatementHandler object by reflection
       StatementHandler delegate = (StatementHandler)ReflectUtil.getFieldValue(handler, "delegate");
       //Get the boundSql of the current StatementHandler, either calling handler.getBoundSql() or calling delegate.getBoundSql() directly. The result is the same, as I said before.
       //All StatementHandler interface methods implemented by RoutingStatementHandler are called delegate corresponding methods.
       BoundSql boundSql = delegate.getBoundSql();
       //Get the parameter object currently bound to Sql, which is the parameter object we pass in when we call the corresponding Mapper mapping statement
       Object obj = boundSql.getParameterObject();
       //Here, we simply pass in a Page object and assume that it needs paging.
       if (obj instanceof Page<?>) {
           Page<?> page = (Page<?>) obj;
           //Getting the mappedStatement attribute of the delegate parent BaseStatementHandler by reflection
           MappedStatement mappedStatement = (MappedStatement)ReflectUtil.getFieldValue(delegate, "mappedStatement");
           //The intercepted prepare method parameter is a Connection object
           Connection connection = (Connection)invocation.getArgs()[0];
           //Gets the Sql statement to be executed, that is, the Sql statement we wrote directly in the Mapper mapping statement
           String sql = boundSql.getSql();
           //Set the total number of records for the current page parameter object
           this.setTotalRecord(page,
                  mappedStatement, connection);
           //Get Paging Sql Statements
           String pageSql = this.getMysqlPageSql(page, new StringBuffer(sql));
           //Use reflection to set SQL attributes corresponding to current BoundSql to build paging Sql statements for us
           ReflectUtil.setFieldValue(boundSql, "sql", pageSql);
       }
       return invocation.proceed();
    }
 
 
    /**
     * Method of encapsulating original object corresponding to interceptor
     */
    public Object plugin(Object target) {
       return Plugin.wrap(target, this);
    }

    /**
     * Setting the properties set when registering interceptors
     */
    public void setProperties(Properties properties) {
    }
   

    /**
     * Getting Paging Query Statements of Mysql Database
     * @param page Paging object
     * @param sqlBuffer StringBuffer object containing the original sql statement
     * @return Mysql Database Paging Statement
     */
    private String getMysqlPageSql(Page<?> page, StringBuffer sqlBuffer) {
       //To calculate the location of the first record, the location of the record in Mysql starts at 0.
       int offset = (page.getPageNo() - 1) * page.getPageSize();
       sqlBuffer.append(" limit ").append(offset).append(",").append(page.getPageSize());
       return sqlBuffer.toString();
    }
   
    /**
     * Getting Paging Query Statements of Oracle Database
     * @param page Paging object
     * @param sqlBuffer StringBuffer object containing the original sql statement
     * @return Oracle Paging Query Statement in Database
     */
    private String getOraclePageSql(Page<?> page, StringBuffer sqlBuffer) {
       //To calculate the location of the first record, Oracle paging is done through rownum, which starts with 1.
       int offset = (page.getPageNo() - 1) * page.getPageSize() + 1;
       sqlBuffer.insert(0, "select u.*, rownum r from (").append(") u where rownum < ").append(offset + page.getPageSize());
       sqlBuffer.insert(0, "select * from (").append(") where r >= ").append(offset);
       //The above Sql statement is mosaic like this:
       //select * from (select u.*, rownum r from (select * from t_user) u where rownum < 31) where r >= 16
       return sqlBuffer.toString();
    }
   
    /**
     * Set the total number of records for the current parameter object page
     *
     * @param page Mapper Parametric Objects Corresponding to Mapping Statements
     * @param mappedStatement Mapper Mapping statement
     * @param connection Current database connection
     */
    private void setTotalRecord(Page<?> page,
           MappedStatement mappedStatement, Connection connection) {
       //Get the corresponding BoundSql, which is actually the same object as the BoundSql we got using StatementHandler.
       //The boundSql in delegate is also obtained through the mappedStatement.getBoundSql(paramObj) method.
       BoundSql boundSql = mappedStatement.getBoundSql(page);
       //Get the Sql statement we wrote ourselves in Mapper mapping statement
       String sql = boundSql.getSql();
       //Get the corresponding SQL statement to calculate the total number of records by querying Sql statement
       String countSql = this.getCountSql(sql);
       //Obtaining the corresponding parameter mapping through BoundSql
       List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
       //Configuration, Sql statement countSql of query record number, parameterMappings of parameter mapping relation and page of parameter object are used to establish BoundSql object corresponding to query record number.
       BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, page);
       //Establish a ParameterHandler object for setting parameters through mappedStatement, parameter object page and BoundSql object countBoundSql
       ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);
       //A PreparedStatement object corresponding to countSql is created by connection.
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       try {
           pstmt = connection.prepareStatement(countSql);
           parameterHandler.setParameters(pstmt);
           rs = pstmt.executeQuery();
           if (rs.next()) {
              int totalRecord = rs.getInt(1);
              //Set the total number of records for the current parameter page object
              page.setTotalRecord(totalRecord);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           try {
              if (rs != null)
                  rs.close();
               if (pstmt != null)
                  pstmt.close();
           } catch (SQLException e) {
              e.printStackTrace();
           }
       }
    }
   
    /**
     * Sql statement to obtain the corresponding total number of query records according to the original Sql statement
     * @param sql
     * @return
     */
    private String getCountSql(String sql) {
       
       return "select count(1) from (" + sql + ") a";
    }
   
    /**
     * A Tool Class for Operating with Reflection
     *
     */
    private static class ReflectUtil {
       /**
        * Getting the specified attributes of the specified object by reflection
        * @param obj Target object
        * @param fieldName Target attribute
        * @return Value of target attribute
        */
       public static Object getFieldValue(Object obj, String fieldName) {
           Object result = null;
           Field field = ReflectUtil.getField(obj, fieldName);
           if (field != null) {
              field.setAccessible(true);
              try {
                  result = field.get(obj);
              } catch (IllegalArgumentException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } catch (IllegalAccessException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
           }
           return result;
       }
      
       /**
        * Getting the specified attributes in the specified object by reflection
        * @param obj Target object
        * @param fieldName Target attribute
        * @return Target field
        */
       private static Field getField(Object obj, String fieldName) {
           Field field = null;
          for (Class<?> clazz=obj.getClass(); clazz != Object.class; clazz=clazz.getSuperclass()) {
              try {
                  field = clazz.getDeclaredField(fieldName);
                  break;
              } catch (NoSuchFieldException e) {
                  //There is no need to do any processing here. The subclass returns null without the parent class that the field may correspond to.
              }
           }
           return field;
       }
 
       /**
        * Use reflection to set the specified property of the specified object to the specified value
        * @param obj Target object
        * @param fieldName Target attribute
         * @param fieldValue target value
        */
       public static void setFieldValue(Object obj, String fieldName,
              String fieldValue) {
           Field field = ReflectUtil.getField(obj, fieldName);
           if (field != null) {
              try {
                  field.setAccessible(true);
                  field.set(obj, fieldValue);
              } catch (IllegalArgumentException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } catch (IllegalAccessException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
           }
        }
    }
 
}