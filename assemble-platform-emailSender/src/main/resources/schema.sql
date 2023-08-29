create table email_send_config
(
    id        bigint auto_increment
        primary key,
    namespace varchar(64)   default ''     not null comment '命名空间',
    protocol  varchar(16)   default ''     not null comment '协议:smtpimap',
    hostname  varchar(32)   default ''     not null comment '服务主机地址',
    port      int           default 25     not null comment '端口',
    username  varchar(32)   default ''     not null comment '用户',
    password  varchar(64)   default ''     not null comment '密码',
    template  varchar(2048) default ''     not null comment '模板信息',
    encoding  varchar(32)   default 'utf8' not null comment '消费发送utf8',
    enable    tinyint       default 0      not null comment '0-开启 1-关闭',
    scheduler varchar(16)   default ''     not null comment '任务调度'
)
    comment '邮箱发送配置';

create table email_send_record
(
    id                 bigint auto_increment comment '邮箱发送数据配置'
        primary key,
    title              varchar(128)  default ''                not null comment '标题',
    content            text                                    not null comment '发送信息内容',
    type               tinyint       default 0                 not null comment '0-实时发送,1-定时发送',
    rece_users         varchar(512)  default ''                not null comment '接收者邮箱，采用逗号分割。',
    copy_users         varchar(512)  default ''                not null comment '抄送人邮箱，采用逗号分割。',
    send_status        tinyint       default 0                 not null comment '发送状态',
    cron_expression    varchar(16)   default ''                not null comment 'cron表达式',
    next_plan_datetime datetime                                not null comment '计划下一次发送时间',
    last_send_datetime datetime      default CURRENT_TIMESTAMP not null comment '上一次发送时间',
    execute_status     tinyint       default 0                 not null comment '0 未完成、1 已完成',
    execute_threshold  int           default 1                 not null comment '执行发送阈值',
    execute_send_count int           default 0                 not null comment '执行发送次数',
    create_datetime    datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    is_html            tinyint       default 0                 not null comment '0-是否html',
    attachments        varchar(1024) default ''                not null
)
    comment '邮箱发送记录表';

