package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.controller.MediaUploadController;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @Author: szz
 * @Date: 2018/9/20 上午8:24
 * @Version 1.0
 */

@Service
public class MediaUploadService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MediaUploadController.class);

    @Autowired
    MediaFileRepository mediaFileRepository;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${xc-service-manage-media.upload-location}")
    String uploadPath;

    //视频处理路由
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    public  String routingkey_media_video;

    /**
     * 得到上传文件的路径
     * 目录路径：
     * 1/md5第一个字符
     * 2/md5第二个字符
     * 3/md5本身
     * 文件名称=md5.扩展名
     *
     * @param fileMd5
     * @return
     */

    public String getFilePath(String fileMd5, String fileExt) {
        return uploadPath+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/"+fileMd5+"."+fileExt;
    }

    //得到上传文件的目录路径
    public String getFileFolderPath(String fileMd5) {
        return uploadPath+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
    }

    //得到分块文件的目录
    public String getChunkFileFolderPath(String fileMd5) {
        return uploadPath+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/"+"chunk"+"/";
    }

    //文件上传前的注册
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //检查文件是否存在:1.在物理磁盘上是否存在,2.在数据库是否存在,两者都存在说明此文件已存在
        //1.在物理磁盘上是否存在
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //在磁盘上是否存在
        boolean exists = file.exists();
        //2.判断在数据库是否存在,记录的主键就是md5值
        MediaFile mediaFile = mediaFileRepository.findOne(fileMd5);
        if (mediaFile != null && exists) {
            //此文件已存在
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //得到上传文件的目录,如果不存在就创建
        String fileFolderPath = getFileFolderPath(fileMd5);
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //检查分块文件在磁盘是否存在
    public CheckChunkResult checkchunk(String fileMd5, String chunk, String chunkSize) {
        //得到分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //得到分块文件的路径
        String chunkFilePath=chunkFileFolderPath+chunk;
        File chunkFile = new File(chunkFilePath);
        if (chunkFile.exists()) {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, true);
        } else {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, false);
        }
    }

    //上传分块文件
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, String chunk) {
        //得到分块文件的目录路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolderFile = new File(chunkFileFolderPath);
        //如果块文件不存在则自动创建
        if (!chunkFileFolderFile.exists()) {
            chunkFileFolderFile.mkdirs();
        }

        //得到分块文件的路径
        String chunkFilePath=chunkFileFolderPath+chunk;
        //分块文件
        File chunkFile = new File(chunkFilePath);
        //把上传的文件拷贝到分块目录文件中
        try(FileOutputStream outputStream = new FileOutputStream(chunkFile);
            InputStream inputStream = file.getInputStream()) {
            IOUtils.copy(inputStream, outputStream);
            return ResponseResult.SUCCESS();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("上传chunk分块文件失败:{}"+e.getMessage());
            return ResponseResult.FAIL();
        }
    }


    //合并文件
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //得到分块文件的目录路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolderFile = new File(chunkFileFolderPath);
        //合并的文件路径
        String filePath = getFilePath(fileMd5, fileExt);
        File mergeFile = new File(filePath);

        if (mergeFile.exists()) {
            mergeFile.delete();//如果合并文件已存在,就删掉原来合并一部分的文件
        }

        if (!mergeFile.exists()) {
            try {
                //创建一个新的 空文件
                boolean newFile = mergeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                ExceptionCast.cast(MediaCode.MERGE_FILE_CREATE_FAIL);
            }
        }

        //向合并后的文件写数据
        RandomAccessFile raf_write = null;
        try {
            raf_write = new RandomAccessFile(mergeFile, "rw");//读写权限
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        File[] chunkFiles = chunkFileFolderFile.listFiles();
        //将数据转换成list
        List<File> fileList = Arrays.asList(chunkFiles);
        //需要对chunkFIles分块文件排序,按照升序排
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName())>Integer.parseInt(o2.getName())) {
                    return 1;//升序
                }
                return -1;
            }
        });

        //开始合并
        byte[] b = new byte[1024];
        try {
            for (File chunkFile : fileList) {
                //从块文件中读
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile,"r");
                int len=-1;
                while ((len=raf_read.read(b))!=-1) {
                    //向合并后的文件写
                    raf_write.write(b, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }finally {
            try {
                raf_write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //合并完成后可以将分块文件删除
        delFolder(chunkFileFolderPath);

        //对文件进行校验
        try (InputStream mergeFileInputStream = new FileInputStream(mergeFile)) {
            String newFileMd5 = DigestUtils.md5Hex(mergeFileInputStream);
            if (!fileMd5.equalsIgnoreCase(newFileMd5)) {
                //检验失败
                ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //检验失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        //将文件入库
        MediaFile mediaFile=new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileSize(fileSize);
        mediaFile.setFileName(fileMd5+"."+fileExt);//文件名称
        String filePath_1=fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
        mediaFile.setFilePath(filePath_1);
        mediaFile.setFileType(fileExt);
        mediaFile.setMimeType(mimetype);
        mediaFile.setUploadTime(new Date());
        MediaFile save = mediaFileRepository.save(mediaFile);

        //上传文件成功,向mq发送视频处理的消息
        sendProcessVideo(save.getFileId());

        return new ResponseResult(CommonCode.SUCCESS);
    }


    //向mq发送视频处理消息
    private void sendProcessVideo(String mediaId) {
        //构建消息{"mediaId":"..."}
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("mediaId", mediaId);
        String message=JSON.toJSONString(messageMap);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK,routingkey_media_video,message);
    }

    public static void main(String args[]){
        delFolder("/Users/szz/video/9/c/9c5a9400b333eacc84b1ad461e9dee29/chunk");
        System.out.println("deleted");
    }

    //删除文件夹
    //param folderPath 文件夹完整绝对路径
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
