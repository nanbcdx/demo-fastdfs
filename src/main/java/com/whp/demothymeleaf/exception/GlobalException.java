package com.whp.demothymeleaf.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/29 11:42
 */
@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(MultipartException.class)
    public String handleException(MultipartException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message","上传文件超过指定大小");
        return "redirect:/status";
    }
}
