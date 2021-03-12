package com.jeeno.springboottransaction.service;

import com.jeeno.springboottransaction.pojo.UserDO;

/**
 * @author Jeeno
 * @version 1.0.0
 * @date 2021/3/11 21:42
 */
public interface IUserService {

    /**
     * query user info by user-id (Jpa)
     * it contains three steps: query -> update name -> query
     * @param id user-id
     */
    void query(Long id);

    /**
     * update user's name by user-id (Jpa)
     * it contains two steps: query -> sleep 2 seconds -> query
     * @param id user-id
     */
    void update(Long id);

    /**
     * query user info by user-id (JdbcTemplate)
     * it contains three steps: query -> update name -> query
     * @param id user-id
     */
    void queryByJdbc(Long id);

    /**
     * update user's name by user-id (JdbcTemplate)
     * it contains two steps: query -> sleep 2 seconds -> query
     * @param id user-id
     */
    void updateByJdbc(Long id);

    /**
     * insert a user info (Jpa)
     * @param userDO user info
     * @return UserDO
     */
    UserDO insert(UserDO userDO);

}
