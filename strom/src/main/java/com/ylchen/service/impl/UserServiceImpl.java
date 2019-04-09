package com.ylchen.service.impl;

import com.ylchen.dao.UserDao;
import com.ylchen.model.User;
import com.ylchen.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "UserServiceImpl")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Override
    public boolean insertBatch(List<User> user) {
        boolean flag=false;
        try {
            flag=userDao.insertBatch(user);
            logger.info("批量新增"+user.size()+"条数据成功!");
        } catch (Exception e) {
            logger.error("批量新增失败!数据:{},原因是:",e);
        }
        return flag;
    }

    @Override
    public List<User> findByUser(User user) {
        return userDao.findByUser(user);
    }
}
