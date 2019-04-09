package com.ylchen.service;

import com.ylchen.model.User;

import java.util.List;

public interface UserService {
    /**
     * 批量新增用户
     * @param user
     * @return
     */
    boolean insertBatch(List<User> user);


    /**
     * 查询用于
     * @param user
     * @return
     */
    List<User> findByUser(User user);
}
