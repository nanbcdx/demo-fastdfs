package com.whp.demothymeleaf.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/30 10:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FastDFSFile {
    private String name;
    private byte[] content;
    private String ext;
    private String md5;
    private String author;

}
