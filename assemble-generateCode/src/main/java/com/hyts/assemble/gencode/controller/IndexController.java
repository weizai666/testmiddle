package com.hyts.assemble.gencode.controller;

import com.fengwenyi.api.result.ResponseTemplate;
import com.hyts.assemble.gencode.config.ErwinProperties;
import com.hyts.assemble.gencode.service.IIndexService;
import com.hyts.assemble.gencode.vo.CodeGeneratorRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
public class IndexController {


    private IIndexService indexService;

    private ErwinProperties erwinProperties;


    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("version", erwinProperties.getApp().getVersion());
        return "index";
    }

    @PostMapping("/code-generator")
    @ResponseBody
    public ResponseTemplate<Void> codeGenerator(@RequestBody @Validated CodeGeneratorRequestVo requestVo) {
        return indexService.codeGenerator(requestVo);
    }

    @Autowired
    public void setIndexService(IIndexService indexService) {
        this.indexService = indexService;
    }

    @Autowired
    public void setErwinProperties(ErwinProperties erwinProperties) {
        this.erwinProperties = erwinProperties;
    }

    @GetMapping("/upgrade")
    @ResponseBody
    private String upgrade() {
        return indexService.upgrade(erwinProperties.getApp().getVersion());
    }
}
