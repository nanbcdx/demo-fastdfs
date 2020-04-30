package com.whp.demothymeleaf.service;

import com.whp.demothymeleaf.util.FastDFSClient;
import com.whp.demothymeleaf.vo.FastDFSFile;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/30 11:56
 */
@Service
@Slf4j
public class FileService {

    public String uploadFile(MultipartFile file) throws IOException {
        String[] fileAbsolutePath={};
        String fileName=file.getOriginalFilename();
        String ext=fileName.substring(fileName.lastIndexOf(".")+1);
        byte[] fileBuff=null;
        InputStream inputStream = file.getInputStream();
        if(inputStream!=null){
            int len=inputStream.available();
            fileBuff=new byte[len];
            inputStream.read(fileBuff);
        }
        inputStream.close();
        FastDFSFile fastFile=new FastDFSFile();
        fastFile.setName(fileName);
        fastFile.setContent(fileBuff);
        fastFile.setExt(ext);
        fileAbsolutePath= FastDFSClient.upload(fastFile);
        if(fileAbsolutePath==null){
            throw new RuntimeException("upload fail");
        }
        String path=FastDFSClient.getTrackerUrl()+fileAbsolutePath[0]+"/"+fileAbsolutePath[1];
        return path;
    }

    public void deletFile(String filePath) throws IOException, MyException {
        String groupName = filePath.substring(0, filePath.indexOf("/"));
        String remoteFileName=filePath.substring(filePath.indexOf("/")+1);
        FastDFSClient.deleteFile(groupName,remoteFileName);
    }
}
