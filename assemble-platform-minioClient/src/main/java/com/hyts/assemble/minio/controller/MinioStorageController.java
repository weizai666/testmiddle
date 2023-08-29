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
package com.hyts.assemble.minio.controller;

import com.hyts.assemble.common.model.http.ResultResponse;
import com.hyts.assemble.minio.service.MinioHttpOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.admin.web
 * @author:LiBo/Alex
 * @create-date:2021-12-05 3:24 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/api/oss")
@Api(value="MINO-OSS服务",tags = {"MINO服务组件"},description = "主要作为OSS服务组件的-MINIO服务")
public class MinioStorageController {


    @Autowired
    MinioHttpOssService fileStoreService;


    /**
     * bucket
     * @param bucketName
     * @return
     */
    @GetMapping("/create")
    @ApiOperation(value="创建Bucket服务",notes = "创建Bucket服务")
    public ResultResponse create(@RequestParam("bucketName") String bucketName){
        try {
            return fileStoreService.create(bucketName);
        }catch (Exception e){
            log.error("create bucket is failure!",e);
            return ResultResponse.failure("create bucket is failure!");
        }
    }


    /**
     * 存储文件
     * @param file
     * @param bucketName
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value="上传文件",notes = "上传文件到指定Bucket服务")
    @ApiImplicitParam(name = "file", value = "上传的文件", dataType = "java.io.File", required = true)
    public ResultResponse upload(@RequestParam("file") MultipartFile file, @RequestParam("bucketName") String bucketName){
        try {
            return fileStoreService.upload(file,bucketName);
        } catch (Exception e) {
            log.error("upload the file is error",e);
            return ResultResponse.failure("upload the file is error");
        }
    }

    /**
     * 删除文件
     * @param bucketName
     * @param bucketName
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation(value="删除文件",notes = "删除文件指定Bucket服务")
    public ResultResponse delete(@RequestParam("bucketName") String bucketName, @RequestParam("fileName") String fileName){
        try {
            return fileStoreService.delete(bucketName,fileName);
        }catch (Exception e){
            log.error("delete bucket is failure!",e);
            return ResultResponse.failure("delete bucketis failure!");
        }
    }


    /**
     * 下载文件
     * @param bucketName
     * @param bucketName
     * @return
     */
    @GetMapping("/download")
    @ApiOperation(value="下载文件",notes = "下载文件指定Bucket服务")
    public void download(HttpServletResponse httpServletResponse,@RequestParam("bucketName") String bucketName,@RequestParam("fileName") String fileName){
        try {
            fileStoreService.download(httpServletResponse,bucketName,fileName);
        } catch (Exception e) {
            log.error("download file is failure!",e);
        }
    }

}
