package com.jeeno.springboottransaction.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Jeeno
 * @version 1.0.0
 * @date 2021/3/11 21:44
 */
@Data
@Entity
@Table(name = "user_info")
public class UserDO {

    /**
     * primary key. auto-increase
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * name
     */
    private String name;

    /**
     *
     */
    private LocalDateTime birth;

    /**
     *
     */
    private String address;

}
