CREATE TABLE `excel_entity` (
  `id` int(11) DEFAULT NULL COMMENT '自增主键',
  `table_name` varchar(100) DEFAULT NULL COMMENT '表名字',
  `table_sql` varchar(255) DEFAULT NULL COMMENT '表sql'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表实体描述对象'

CREATE TABLE `entity_sftp_sql` (
  `table_name` varchar(100) NOT NULL COMMENT '表名称',
  `sftp_host` varchar(20) DEFAULT NULL COMMENT 'sftp文件服务器地址',
  `sftp_port` varchar(6) DEFAULT NULL COMMENT 'sftp文件服务器端口',
  `sftp_username` varchar(50) DEFAULT NULL COMMENT 'sftp文件服务器用户名',
  `sftp_pwd` varchar(50) DEFAULT NULL COMMENT 'sftp文件服务器密码',
  `sftp_dir` varchar(100) DEFAULT NULL COMMENT 'sftp文件服务器上传目录',
  `sftp_sql` text COMMENT '数据筛选sql',
  PRIMARY KEY (`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据信息关联表'
