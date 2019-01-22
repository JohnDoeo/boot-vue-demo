package com.johndoeo.bootvueserver.service.impl;

import com.johndoeo.bootvueserver.dao.UserDao;
import com.johndoeo.bootvueserver.module.User;
import com.johndoeo.bootvueserver.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserDao userDao;
    public int deleteByPrimaryKey(Integer id) {
        return userDao.deleteByPrimaryKey(id);
    }

    public int insert(User record) {
        return userDao.insert(record);
    }

    public int insertSelective(User record) {
        return userDao.insertSelective(record);
    }

    public User selectByPrimaryKey(Integer id) {
        return userDao.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(User record) {
        return userDao.updateByPrimaryKeySelective(record);
    }

    public List<User> selectByColumn(User record) {
        return userDao.selectByColumn(record);
    }

    public int batchInsertList(List<User> record) {
        return userDao.batchInsertList(record);
    }

    public int updateByPrimaryKey(User record) {
        return userDao.updateByPrimaryKey(record);
    }
}