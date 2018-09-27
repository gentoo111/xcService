package com.xuecheng.filesystem.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Paths;

/**
 * @Author: szz
 * @Date: 2018/9/9 下午5:23
 * @Version 1.0
 * 教程http://dushen.iteye.com/blog/2404919
 * 默认不指定key的情况下，以文件内容的hash值作为文件名  String key = null;
 */
public class QiNiuUtils {
    /**
     * 查看自己的Access Key和Secret Key，方法如下：进入七牛控制管理台->个人中心->秘钥管理，即可查看AK(Access Key)和SK(Secret Key)，记录下这两个值。
     */

    private static String accessKey = "1sTm-vDOe_2xj6fyN6jed99-Ecssk6Lp4LLUmMtx";
    private static String secretKey = "yJoX5lvdKcFk3k0u6VfiHM1p61S5BHUCc0RkBoub";
    private static String bucket = "solo";
    /**
     * 获取上传凭证
     */
    public static String getUploadCredential() {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
        return upToken;
    }


    /**
     * 文件上传
     * @param zone
     *    华东    Zone.zone0()
     *    华北    Zone.zone1()
     *    华南    Zone.zone2()
     *    北美    Zone.zoneNa0()
     * @param upToken 上传凭证
     * @param localFilePath 需要上传的文件本地路径
     * @return
     */
    public static DefaultPutRet fileUpload(Zone zone, String upToken, String localFilePath) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return putRet;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                // ignore
            }
        }
        return null;
    }

    public static Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }

    /**
     * 公有空间返回文件URL
     * @param fileName
     * @param domainOfBucket
     *  说一下参数：fileName：没错，就是上传文件时，返回的DefaultPutRet对象中的key属性，也就是七牛控制管理台中看到的文件名。
     *           domainOfBucket：你的存储空间对应的domain，可以在控制台中查看，就是下边这个。
     *
     * p12bgfv4l.bkt.clouddn.com
     * @return
     */
    public static String publicFile(String fileName,String domainOfBucket) {
        String encodedFileName=null;
        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        //System.out.println(finalUrl);
        return finalUrl;
    }

    /**
     * 私有空间返回文件URL
     * @param auth
     * @param fileName
     * @param domainOfBucket
     * @param expireInSeconds
     * @return
     */
    public static String privateFile(Auth auth,String fileName,String domainOfBucket,long expireInSeconds) {
        String encodedFileName=null;
        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(finalUrl);
        return finalUrl;
    }


    public static void main(String[] args) {
        String publicFile = uploadAndGetHttpPath("/Users/szz/IdeaProjects/xcEduService/d:/1.jpg");
        System.out.println(publicFile);
        //publicFile("Fi7kMW654AP-Nb4mh_baD_szSBhO","p12bgfv4l.bkt.clouddn.com");

        //charArrayUpload(Zone.zone0(),CredentialsManager.getUploadCredential());
        //streamUpload(Zone.zone0(),CredentialsManager.getUploadCredential());
       // breakPointUpload(Zone.zone0(),getUploadCredential(),"D:\\qiniu\\test.jpg");
    }

    public static String uploadAndGetHttpPath(String path) {
        DefaultPutRet defaultPutRet = fileUpload(Zone.zone0(), getUploadCredential(), path);
        return publicFile(defaultPutRet.hash, "p12bgfv4l.bkt.clouddn.com");
    }

    /**
     * 字符组上传
     * @param zone
     * @param upToken
     * @return
     */
    public static DefaultPutRet charArrayUpload(Zone zone,String upToken) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");

            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return putRet;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    // ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            // ignore
        }
        return null;
    }

    /**
     * 数据流上传
     * @param zone
     * @param upToken
     * @return
     */
    public static DefaultPutRet streamUpload(Zone zone,String upToken) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // ...生成上传凭证，然后准备上传
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            byte[] uploadBytes = "test streamUpload \n hello qiniu cloud".getBytes("utf-8");
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
            try {
                Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return putRet;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    // ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            // ignore
        }
        return null;
    }

    /**
     * 断点续传
     * @param zone
     * @param upToken
     * @param localFilePath
     * @return
     */
    public static DefaultPutRet breakPointUpload(Zone zone,String upToken,String localFilePath) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        // ...其他参数参考类注释
        // 如果是Windows情况下，格式是 D:\\qiniu\\test.png
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), bucket).toString();
        try {
            // 设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            try {
                Response response = uploadManager.put(localFilePath, key, upToken);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return putRet;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    // ignore
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // MyPutRet myPutRet=response.jsonToObject(MyPutRet.class);
        return null;
    }


}
