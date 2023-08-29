/**
 * Copyright [2020] [LiBo/Alex of copyright liboware@gmail.com ]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyts.assemble.minio.service;

import com.hyts.assemble.common.model.http.ResultResponse;
import com.jvm123.minio.service.MinioFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @project-name:dubbo-shopping
 * @package-name:com.dubbo.shopping.oss.minio.service
 * @author:LiBo/Alex
 * @create-date:2022-04-17 17:44
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@Service
public class MinioHttpOssService {


    @Autowired
    MinioFileService fileStoreService;

    /**
     * bucket
     * @param bucketName
     * @return
     */
    public ResultResponse create(@RequestParam("bucketName") String bucketName){
        return fileStoreService.createBucket(bucketName)? ResultResponse.success(): ResultResponse.failure("创建oss bucket失败！");
    }


    /**
     * 存储文件
     * @param file
     * @param bucketName
     * @return
     */
    public ResultResponse upload(@RequestParam("file") MultipartFile file, @RequestParam("bucketName") String bucketName){
        try {
            fileStoreService.save(bucketName,file.getInputStream(),file.getOriginalFilename());
        } catch (IOException e) {
            log.error("upload the file is error",e);
            return ResultResponse.failure("upload the file is error");
        }
        return ResultResponse.success();
    }


    /**
     * 删除文件
     * @param bucketName
     * @param bucketName
     * @return
     */
    public ResultResponse delete(@RequestParam("bucketName") String bucketName, @RequestParam("fileName") String fileName){
        return fileStoreService.delete(bucketName,fileName)? ResultResponse.success(): ResultResponse.failure("删除oss bucket文件失败！");
    }



    /**
     * 下载文件
     * @param bucketName
     * @param bucketName
     * @return
     */
    public void download(HttpServletResponse httpServletResponse, @RequestParam("bucketName") String bucketName, @RequestParam("fileName") String fileName){
        try (InputStream inputStream = fileStoreService.getStream(bucketName, fileName)){
            httpServletResponse.addHeader("Content-Disposition","attachment;filename="+fileName);
            ServletOutputStream os = httpServletResponse.getOutputStream();
            fileStoreService.writeTo(bucketName, fileName, os);
        } catch (IOException e) {
            log.error("download file is failure!",e);
        }
    }

}
