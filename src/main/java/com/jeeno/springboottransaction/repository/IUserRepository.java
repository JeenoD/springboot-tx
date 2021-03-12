package com.jeeno.springboottransaction.repository;

import com.jeeno.springboottransaction.pojo.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * @author Jeeno
 * @version 1.0.0
 * @date 2021/3/11 22:50
 */
@Repository
public interface IUserRepository extends JpaRepository<UserDO, Long> {

    /**
     * update user'name by user-id
     * @param id user-id
     * @return Integer
     */
    @Modifying(clearAutomatically = true)
    @Transactional(label = "updateNameById", isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Query(value = "UPDATE UserDO u SET u.name = '修改后' WHERE u.id = ?1")
    Integer updateNameById(Long id);

    /**
     * query user info by user-id
     * @param id user-id
     * @return Optional
     */
    @Query(value = "SELECT u FROM UserDO u WHERE u.id = ?1")
    Optional<UserDO> findByUserId(Long id);

}
