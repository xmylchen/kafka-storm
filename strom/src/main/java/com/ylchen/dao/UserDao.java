package com.ylchen.dao;

import com.ylchen.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    /**
     * 批量新增据插入数据
     *
     * @param entityList
     * @return
     * @throws Exception
     * @throws
     */
    boolean insertBatch(List<User> entityList) throws Exception;


    /**
     *
     * @param user
     * @return
     * @throws Exception
     */
    List<User> findByUser(User user);
}
