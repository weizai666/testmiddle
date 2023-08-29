package com.hyts.assemble.emailSender.core.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 邮箱发送配置
 * </p>
 *
 * @author libo
 * @since 2022-08-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("email_send_config")
public class EmailSendConfigEntity extends Model<EmailSendConfigEntity> {


    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 命名空间
     */
    @TableField("namespace")
    private String namespace;

    /**
     * 协议:smtpimap
     */
    @TableField("protocol")
    private String protocol;

    /**
     * 服务主机地址
     */
    @TableField("hostname")
    private String hostname;

    /**
     * 端口
     */
    @TableField("port")
    private Integer port;

    /**
     * 用户
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 模板信息
     */
    @TableField("template")
    private String template;

    /**
     * 消费发送utf8
     */
    @TableField("encoding")
    private String encoding;

    /**
     * 0-开启 1-关闭
     */
    @TableField("enable")
    private Integer enable;

    /**
     * cron表达式
     */
    @TableField("scheduler")
    private String cronExpression;



    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
