package com.whp.demothymeleaf.util;

import com.whp.demothymeleaf.vo.FastDFSFile;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @Description :
 * @Author :wangcheng
 * @Date :2020/4/30 11:11
 */
@Slf4j
public class FastDFSClient {

    private static TrackerClient trackerClient;
    private static TrackerServer trackerServer;
    private static StorageServer storageServer;

    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
            trackerClient=new TrackerClient();
            trackerServer=trackerClient.getTrackerServer();
            storageServer=trackerClient.getStoreStorage(trackerServer);

        } catch (Exception e) {
            log.error("FastDFS Client init faill!!", e);
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public static String[] upload(FastDFSFile file) {
        log.info("File Name:{},File Length:{}", file.getName(), file.getContent().length);
        String[] uploadResult = null;
        //文件属性信息
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author", file.getAuthor());
        long startTime = System.currentTimeMillis();
        StorageClient storageClient = null;
        try {
            storageClient = new StorageClient(trackerServer,storageServer);
            uploadResult = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (IOException e) {
            log.error("IOException when uploading the file:{}", file.getName(), e);
        } catch (MyException e) {
            log.error("Exception storageClient uploading the file:{}", file.getName(), e);
        }
        log.info("upload time userd:{}ms", System.currentTimeMillis() - startTime);
        //验证上传结果
        if (uploadResult == null && storageClient != null) {
            log.error("upload fail,error code:{}", storageClient.getErrorCode());
        }
        log.info("upload file successfully!!! group_name:{},remoteFileName:{}", uploadResult[0], uploadResult[1]);
        return uploadResult;
    }


    /**
     * 获取文件
     *
     * @param groupName
     * @param remoteFileName
     * @return FileInfo
     * @throws IOException
     * @throws MyException
     */
    public static FileInfo getFile(String groupName, String remoteFileName) throws IOException, MyException {
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        return storageClient.get_file_info(groupName, remoteFileName);
    }

    /**
     * 下载文件
     *
     * @param groupName
     * @param remoteFileName
     * @return
     * @throws IOException
     * @throws MyException
     */
    public static InputStream downFile(String groupName, String remoteFileName) throws IOException, MyException {
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        byte[] bytes = storageClient.download_file(groupName, remoteFileName);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return inputStream;
    }

    /**
     * 删除文件
     *
     * @param groupName
     * @param remoteFileName
     * @throws IOException
     * @throws MyException
     */
    public static void deleteFile(String groupName, String remoteFileName) throws IOException, MyException {
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);;
        int i = storageClient.delete_file(groupName, remoteFileName);
        log.info("delete file success {}", i);
    }


    public static String getTrackerUrl() throws IOException {
        String url = "http://" + trackerServer.getInetSocketAddress().getHostString() + ":" + ClientGlobal.getG_tracker_http_port();
        return url;
    }
}
