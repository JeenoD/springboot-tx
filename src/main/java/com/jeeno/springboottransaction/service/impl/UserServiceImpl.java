package com.jeeno.springboottransaction.service.impl;

import com.jeeno.springboottransaction.pojo.UserDO;
import com.jeeno.springboottransaction.repository.IUserRepository;
import com.jeeno.springboottransaction.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Map;
import java.util.Optional;


/**
 * @author Jeeno
 * @version 1.0.0
 * @date 2021/3/11 21:42
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserRepository userRepository;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private EntityManager entityManager;

    @Async
    @Override
    @Transactional(label = "query", rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void query(Long id) {
        // first query
        Optional<UserDO> optional = userRepository.findByUserId(id);
        log.info("my-query-1 name:{}", optional.get().getName());
        try {
            // thread sleep for 2 seconds, make sure another transaction({@link com.jeeno.springboottransaction.service.impl.UserServiceImpl.update}) done and committed.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * TODO in order to realize the READ-COMMITTED isolation, uncommenting this row
         * have to explicitly clear entityManger's cache, make sure local-param (optional) is the latest data in db. why?
         * mysql server received the second query sql anyway. you can see the attached image in project
         */
        // entityManager.clear();

        //  second query
        optional = userRepository.findByUserId(id);
        log.info("my-query-2 name:{}", optional.get().getName());
    }

    @Async
    @Override
    @Transactional(label = "update", rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void update(Long id) {
        Optional<UserDO> optional = userRepository.findByUserId(id);
        log.info("my-update-1 name:{}", optional.get().getName());
        userRepository.updateNameById(id);
        optional = userRepository.findByUserId(id);
        log.info("my-update-2 name:{}", optional.get().getName());
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void queryByJdbc(Long id) {
        String sql = "select * from user_info where id=" + id;
        Map userDO = jdbcTemplate.queryForMap(sql);
        log.info("my-query-1 name:{}", userDO.get("name"));
        try {
            // thread sleep for 2 seconds, make sure another transaction({@link com.jeeno.springboottransaction.service.impl.UserServiceImpl.updateByJdbc}) done and committed.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userDO = jdbcTemplate.queryForMap(sql);
        log.info("my-query-2 name:{}", userDO.get("name"));
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void updateByJdbc(Long id) {
        String sql = "select * from user_info where id=" + id;
        Map userDO = jdbcTemplate.queryForMap(sql);
        log.info("my-update-1 name:{}", userDO.get("name"));

        jdbcTemplate.execute("UPDATE user_info u SET u.name = '修改后' WHERE u.id = "+id);
        userDO = jdbcTemplate.queryForMap(sql);
        log.info("my-update-2 name:{}", userDO.get("name"));
    }

    @Override
    public UserDO insert(UserDO userDO) {
        return userRepository.saveAndFlush(userDO);
    }

}
