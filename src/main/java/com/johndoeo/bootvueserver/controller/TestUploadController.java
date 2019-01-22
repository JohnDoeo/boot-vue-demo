package com.johndoeo.bootvueserver.controller;

import com.alibaba.fastjson.JSON;
import com.johndoeo.bootvueserver.constant.OperationCode;
import com.johndoeo.bootvueserver.utils.FileUtil;
import com.johndoeo.bootvueserver.utils.OperationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/test")
public class TestUploadController {
    @Value("${file-path.absolutePath}")
    private String absolutePath;

    @RequestMapping("/upload")
    public JSON upload(MultipartFile file){
        final String saveName = FileUtil.getFileName(file.getOriginalFilename());
        if(FileUtil.upload(file,absolutePath,saveName)){
            return OperationUtil.toJSON(OperationCode.ADD,1,"上传成功");
        }else{
            return OperationUtil.toJSON(OperationCode.FAILED,-1,"上传失败");
        }
    }
}
