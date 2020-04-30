package com.whp.demothymeleaf.controller;

import com.whp.demothymeleaf.entity.User;
import com.whp.demothymeleaf.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/28 19:45
 */
@Controller
@RequestMapping("/")
@Slf4j
public class IndexController {

    @Autowired
    private FileService fileService;

    @GetMapping("index")
    public String index(Model model){
        model.addAttribute("message","欢迎来到首页面");
        model.addAttribute("username","王成");
        model.addAttribute("flag","yes");
        model.addAttribute("users",getUserList());
        return "index";
    }

    private List<User> getUserList(){
        List<User> list=new ArrayList<>();
        list.add(new User("www",10,"111"));
        list.add(new User("ggg",10,"222"));
        list.add(new User("rrr",10,"333"));
        return list;
    }

    @GetMapping("delete")
    public String deleteFile(String filePath,RedirectAttributes redirectAttributes){
        try {
            fileService.deletFile(filePath);
            redirectAttributes.addFlashAttribute("message","删除成功");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/status";
    }

    @PostMapping("upload")
    public String uploadFile(MultipartFile file, RedirectAttributes redirectAttributes){
        String uploadFolder="e:/upload/";
        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message","上传内容为空");
            return "status";
        }

        try{
            String path=fileService.uploadFile(file);
            redirectAttributes.addFlashAttribute("message","upload success:"+file.getOriginalFilename());
            redirectAttributes.addFlashAttribute("path","file path url:"+path);
        }catch (Exception e){
            log.error("file upload fail:",e);
        }
//        String datePath = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
//        String filePath=uploadFolder+datePath;
//        File uploadFile=new File(filePath);
//        if(!uploadFile.exists()){
//            uploadFile.mkdirs();
//        }
//
//        Path path= Paths.get(filePath+file.getOriginalFilename());
//        try {
//            Files.write(path,file.getBytes());
//            redirectAttributes.addFlashAttribute("message",file.getOriginalFilename());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return "redirect:/status";
    }

    @PostMapping("uploadMore")
    public String uploadFileMore(@RequestParam("file") MultipartFile[] files , Model model){
        String uploadFolder="e:/upload/";
        if(files.length==0){
            model.addAttribute("message","上传内容为空");
            return "status";
        }
        String datePath = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        String filePath=uploadFolder+datePath;
        File uploadFile=new File(filePath);
        if(!uploadFile.exists()){
            uploadFile.mkdirs();
        }
        for(MultipartFile file:files){
            Path path=Paths.get(filePath+file.getOriginalFilename());
            try {
                Files.write(path,file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("message","上传成功!");

        return "status";
    }


    @GetMapping("status")
    public String status(){
        return "status";
    }
}
