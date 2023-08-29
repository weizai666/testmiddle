package com.hyts.assemble.gencode.service;

import com.fengwenyi.api.result.ResponseTemplate;
import com.hyts.assemble.gencode.vo.CodeGeneratorRequestVo;


public interface IIndexService {

    /**
     * 生成代码
     * @param requestVo
     * @return
     */
    ResponseTemplate<Void> codeGenerator(CodeGeneratorRequestVo requestVo);

    /**
     * 升级检查
     * */
    String upgrade(String version);

}
