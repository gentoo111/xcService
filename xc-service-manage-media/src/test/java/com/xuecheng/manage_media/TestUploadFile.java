package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: szz
 * @Date: 2018/9/20 上午9:33
 * @Version 1.0
 */

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class TestUploadFile {


    @Test
    public void testSplitFile()throws  Exception{
        //源文件
        File sourceFile = new File("/Users/szz/video/Lucene.avi");
        //分块文件所在目录及路径
        String chunkFilePath = "/Users/szz/video/chunks/";
        File chunkFileFolder = new File(chunkFilePath);

        if (!chunkFileFolder.exists()) {
            chunkFileFolder.mkdirs();
        }

        //读取源文件对象
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");

        //每个块的大小
        long chunkSize=1*1024*1024;
        //得到块数
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);

        byte[] b = new byte[1024];
        for (int i = 0; i < chunkNum; i++) {
            //快文件
            File chunkFile=new File(chunkFilePath + i);//块文件的名称为序号
            //块文件的写对象
            RandomAccessFile raf_write = new RandomAccessFile(chunkFile, "rw");//读写权限
            int len=-1;
            while ((len=raf_read.read(b))!=-1) {
                //向快文件中写
                raf_write.write(b,0,len);
                //当已经读取了块文件的大小后,就不再读了
                if (chunkFile.length()>=chunkSize) {
                    break;
                }
            }
            raf_write.close();
        }
        raf_read.close();

    }

    //文件合并
    @Test
    public void testMergeFile()throws Exception{
        //分块文件所在路径
        String chunkFilePath = "/Users/szz/video/chunks/";
        File chunkFIleFolder = new File(chunkFilePath);

        //创建合并后的文件
        File mergeFile=new File("/Users/szz/video/Lucene2.avi");
        //创建空的合并文件
        boolean newFile = mergeFile.createNewFile();
        //向合并的文件中写
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");

        //获取所有块文件
        File[] chunkFiles = chunkFIleFolder.listFiles();
        //转为list并按名称排序
        List<File> fileList = Arrays.asList(chunkFiles);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {

                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;//升序
                }
                return -1;
            }
        });
        byte[] b = new byte[1024];
        for (File chunkFile : fileList) {
            //从块文件中读
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
            int len=-1;
            while ((len=raf_read.read(b))!=-1) {
                //向合并后的文件写
                raf_write.write(b,0,len);
            }
            raf_read.close();
        }
        raf_write.close();
    }
}
