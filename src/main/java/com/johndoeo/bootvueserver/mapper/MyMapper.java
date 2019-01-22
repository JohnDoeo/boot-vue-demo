package com.johndoeo.bootvueserver.mapper;


import com.johndoeo.bootvueserver.mapper.provider.MyProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Administrator
 * @param <T> 不能为空
 *            Created by XMH on 2016/5/31.
 */
public interface MyMapper<T> extends Mapper<T> {

    /**
     * 根据实体中的属性值进行查询，查询条件默认使用等号,Begin 大于 end 小于
     *
     * @param record
     * @return
     */
    @SelectProvider(type = MyProvider.class, method = "dynamicSQL")
    List<T> selectByColumn(T record);


    /**
     * 批量插入，支持数据库自增字段，支持回写
     *
     * @param recordList
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = MyProvider.class, method = "dynamicSQL")
    int batchInsertList(List<T> recordList);


}
