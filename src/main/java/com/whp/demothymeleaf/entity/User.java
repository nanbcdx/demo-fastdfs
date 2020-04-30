package com.whp.demothymeleaf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/28 22:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private int age;
    private  String pass;
}
