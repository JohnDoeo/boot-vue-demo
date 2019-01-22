package com.johndoeo.bootvueserver.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传下载工具类
 */
public class FileUtil {

    /**
     * 文件上传方法
     *
     * @param file     上传的文件
     * @param saveUrl  保存的路径
     * @param saveName 保存的文件名
     * @return
     */
    public static boolean upload(MultipartFile file, String saveUrl, String saveName) {

        //判断存放图片的目录是否存在，若不存在就创建
        //若存储路径不存在，递归创建文件夹
        File f = new File(saveUrl);
        if (!f.exists()) {
            f.mkdirs();
        }

        String realPath = saveUrl + "/" + saveName;
        File dest = new File(realPath);

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 文件的下载
     * @param saveName 保存在服务器上的文件名
     * @param savaUrl 服务器上的绝对路径
     * @return
     */
    public static boolean download(String saveName,String savaUrl) {
        if (saveName != null) {
            //设置文件路径
            File file = new File(savaUrl+"/"+saveName);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + saveName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     *
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName) {
        return getUUID() + FileUtil.getSuffix(fileOriginName);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
