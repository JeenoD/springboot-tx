package com.jeeno.springboottransaction;

import com.jeeno.springboottransaction.pojo.UserDO;
import com.jeeno.springboottransaction.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author Jeeno
 * @version 1.0.0
 * @date 2021/3/11 22:56
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {

    @Resource
    private IUserService userService;

    @Test
    public void testByJpa() throws InterruptedException {
        // initialize user info
        UserDO userDO = new UserDO();
        userDO.setName("JEENO");
        userDO.setAddress("ZheJiang");
        userDO.setBirth(LocalDateTime.now());

        // insert user info to db
        userDO = userService.insert(userDO);

        // Isolation: Read-committed
        // async thread. it contains three steps: query -> update name -> query
        userService.update(userDO.getId());
        // Isolation: Read-committed
        // async thread. it contains two steps: query -> sleep 2 seconds -> query
        // sleep 5 seconds to make sure the transaction of (userService.update()) committed, and the second queryById should see the updated name of user
        userService.query(userDO.getId());

        Thread.sleep(8000);
    }

    @Test
    public void testByJdbc() throws InterruptedException {
        // initialize user info
        UserDO userDO = new UserDO();
        userDO.setName("JEENO");
        userDO.setAddress("ZheJiang");
        userDO.setBirth(LocalDateTime.now());

        // insert user info to db
        userDO = userService.insert(userDO);

        // Isolation: Read-committed
        // async thread. it contains three steps: query -> update name -> query
        userService.updateByJdbc(userDO.getId());
        // Isolation: Read-committed
        // async thread. it contains two steps: query -> sleep 2 seconds -> query
        // sleep 5 seconds to make sure the transaction of (userService.updateByJdbc()) committed, and the second query should see the updated name of user
        userService.queryByJdbc(userDO.getId());

        Thread.sleep(5000);
    }


}
