package com.johndoeo.bootvueserver.mapper.provider;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.FieldHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;

import java.util.List;
import java.util.Set;

/**
 * Created by XMH on 2016/5/31.
 */
public class MyProvider extends BaseSelectProvider {
    public MyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 默认等于  大于 Begin  小于 End
     *
     * @param ms
     * @return
     */
    public String selectByColumn(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        List<EntityField>  entityFieldList = FieldHelper.getProperties(entityClass);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));


        //增加where 条件
        sql.append("<where>");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            sql.append(SqlHelper.getIfNotNull(column, " AND " + column.getColumnEqualsHolder(), false));
            sql.append(getColumnBegin(column,entityFieldList));
            sql.append(getColumnEnd( column,entityFieldList));
        }
        sql.append("</where>");
        sql.append(getOrderBy(entityClass,entityFieldList));
        return sql.toString();
    }


    /**
     * 批量插入
     *
     * @param ms
     */
    public String batchInsertList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }

    /**
     * 插入，主键id，自增
     *
     * @param ms
     */
    public String insertUseGeneratedKeys(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(SqlHelper.insertValuesColumns(entityClass, true, false, false));
        return sql.toString();
    }


    private String getOrderBy(Class<?> entityClass,List<EntityField>  entityFieldList){

        StringBuilder sql = new StringBuilder();
       /* if(entityIsContainProperty("orderBy",entityFieldList)){
            sql.append("<if test=\"");
            sql.append(getOrderByTest(entityClass));
            sql.append("\">");
            sql.append("order by  ${orderBy}");
            sql.append("</if>");
        }else { */
            sql.append(SqlHelper.orderByDefault(entityClass));
        /*}*/
        return sql.toString();
    }

    /*private String getOrderByTest(Class<?> entityClass){
        Set<EntityColumn>  columnList = EntityHelper.getColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append("orderBy").append(" != null");
        sql.append(" and (");
        if(columnList!=null){
            StringBuilder test = new StringBuilder();
            for (EntityColumn c : columnList) {
                test.append("orderBy").append(" = ").append("'").append(c.getColumn()).append(" asc").append("'");
                test.append(" or ");
                test.append("orderBy").append(" = ").append("'").append(c.getColumn()).append("'");
                test.append(" or ");
                test.append("orderBy").append(" = ").append("'").append(c.getColumn()).append(" desc").append("'");
                test.append(" or ");
            }
            String testStr = test.toString();
            testStr =testStr.substring(0,testStr.length()-3);
            sql.append(testStr);
        }
        sql.append(")");
        return sql.toString();
    }*/

    private String getColumnBegin( EntityColumn column,List<EntityField>  entityFieldList) {
        String property = column.getProperty() + "Begin";
        if(entityIsContainProperty(property,entityFieldList)){
            StringBuilder sql = new StringBuilder();
            sql.append("<if test=\"");
            sql.append(property).append(" != null");
            sql.append("\">");
            sql.append(" and ").append(column.getColumn() + " &gt;=" + " #{" + property + "}");
            sql.append("</if>");
            return sql.toString();
        }
      return "";

    }

    private String getColumnEnd( EntityColumn column,List<EntityField>  entityFieldList) {
        String property = column.getProperty() + "End";
        if(entityIsContainProperty(property,entityFieldList)) {
            StringBuilder sql = new StringBuilder();
            sql.append("<if test=\"");
            sql.append(property).append(" != null");
            sql.append("\">");
            sql.append(" and ").append(column.getColumn() + " &lt;" + " #{" + property + "}");
            sql.append("</if>");
            return sql.toString();
        }
        return "";
    }

    /**
     * 实体类是否包含 此property属性
     * @param property
     * @return
     */
    private boolean entityIsContainProperty(String property,List<EntityField>  entityFieldList){
        if(entityFieldList!=null){
            for(EntityField entityField:entityFieldList){
                if(entityField.getName().equals(property)){
                    return true;
                }
            }
        }
        return false;
    }

}
