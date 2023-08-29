package com.hyts.assemble.gencode.vo;

import lombok.Data;


@Data
public class VersionResponseVo {

    private String latestVersion;
    private String content;
    private String releaseUrl;
    private String downloadUrl;
    private Integer upgradeType;

}
