package com.hyts.assemble.emailSender.core.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 邮箱发送记录表
 * </p>
 *
 * @author libo
 * @since 2022-08-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("email_send_record")
public class EmailSendRecordEntity extends Model<EmailSendRecordEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 邮箱发送数据配置
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 发送信息内容
     */
    @TableField("content")
    private String content;

    /**
     * 0-实时发送,1-定时发送
     */
    @TableField("type")
    private Integer type;

    /**
     * 接收者邮箱，采用逗号分割。
     */
    @TableField("rece_users")
    private String receUsers;

    /**
     * 抄送人邮箱，采用逗号分割。
     */
    @TableField("copy_users")
    private String copyUsers;

    /**
     * 发送状态
     */
    @TableField("send_status")
    private Integer sendStatus;

    /**
     * cron表达式
     */
    @TableField("cron_expression")
    private String cronExpression;

    /**
     * 计划下一次发送时间
     */
    @TableField("next_plan_datetime")
    private LocalDateTime nextPlanDatetime;

    /**
     * 上一次发送时间
     */
    @TableField("last_send_datetime")
    private LocalDateTime lastSendDatetime;

    /**
     * 0 未完成、1 已完成
     */
    @TableField("execute_status")
    private Integer executeStatus;

    /**
     * 执行发送阈值
     */
    @TableField("execute_threshold")
    private Integer executeThreshold;

    /**
     * 执行发送次数
     */
    @TableField("execute_send_count")
    private Integer executeSendCount;

    /**
     * 创建时间
     */
    @TableField("create_datetime")
    private LocalDateTime createDatetime;

    /**
     * 0-是否html
     */
    @TableField("is_html")
    private Integer isHtml;

    @TableField("attachments")
    private String attachments;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
