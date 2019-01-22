package com.johndoeo.bootvueserver.service;

import com.johndoeo.bootvueserver.module.User;

import java.util.List;

public interface IUserService {
    public int deleteByPrimaryKey(Integer id);

    public int insert(User record);

    public int insertSelective(User record);

    public User selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(User record);

    public int updateByPrimaryKey(User record);

    public List<User> selectByColumn(User record);

    public int batchInsertList(List<User> record);
}