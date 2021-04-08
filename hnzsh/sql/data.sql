CREATE TABLE `tw_evn_hn_sinopec_chk_trd_d` (
  `date_cd` VARCHAR(32) DEFAULT NULL COMMENT '日期',
  `city` VARCHAR(32) DEFAULT NULL COMMENT '地市',
  `start_dt` VARCHAR(32) DEFAULT NULL COMMENT '起始日期',
  `end_dt` VARCHAR(32) DEFAULT NULL COMMENT '截止日期',
  `merc_id` VARCHAR(32) DEFAULT NULL COMMENT '商户编号',
  `trd_tm` VARCHAR(32) DEFAULT NULL COMMENT '交易时间',
  `trd_acc_no` VARCHAR(64) DEFAULT NULL COMMENT '交易账号',
  `merc_nm` VARCHAR(256) DEFAULT NULL COMMENT '商户名称',
  `acrcv_amt` VARCHAR(32) DEFAULT NULL COMMENT '实收金额',
  `p_day_id` VARCHAR(32) DEFAULT NULL COMMENT '分区'
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4

CREATE OR REPLACE VIEW V_TM_MSP_WTMKTSETT_D AS
SELECT
B.PROV_NM,--省份
STAT_DT ,   --统计日期
PAYMENT_DT , --付款日期
PAYMENT_AMT  ,  --应付金额
ADJUST_PAYMENT_AMT,--应付调整金额
REAL_PAYMENT_AMT   --实际付款金额
FROM REPORT.TM_MSP_WTMKTSETT_D A
LEFT JOIN (SELECT DISTINCT DATE_CD,PROV_CD,PROV_NM FROM CFG.DIM_STD_REGION) B
ON A.DATE_CD=B.DATE_CD AND A.CAP_ID=B.PROV_CD
WHERE A.CAP_ID NOT IN ('11','10','19')
;

create table TM_MSP_WTHHBSETT_D
(
  stat_dt     VARCHAR2(8),
  payment_dt  VARCHAR2(8),
  wld_cd      VARCHAR2(4),
  wld_name    VARCHAR2(10),
  payment_amt NUMBER(15,2),
  adjust_amt  NUMBER(15,2),
  settle_amt  NUMBER(15,2)
)


CREATE TABLE REPORT.dim_dept_nolist (
  cm_go VARCHAR2(6) DEFAULT NULL ,
  prov_nm VARCHAR2(10) DEFAULT NULL
);
comment on column dim_dept_nolist.cm_go is '往来段';
comment on column dim_dept_nolist.prov_nm is '省份名称';


insert into REPORT.dim_dept_nolist values('3410','安徽');
insert into REPORT.dim_dept_nolist values('2710','北京');
insert into REPORT.dim_dept_nolist values('2410','福建');
insert into REPORT.dim_dept_nolist values('4810','甘肃');
insert into REPORT.dim_dept_nolist values('2110','广东');
insert into REPORT.dim_dept_nolist values('3310','广西');
insert into REPORT.dim_dept_nolist values('4510','贵州');
insert into REPORT.dim_dept_nolist values('2610','海南');
insert into REPORT.dim_dept_nolist values('3010','河北');
insert into REPORT.dim_dept_nolist values('2510','河南');
insert into REPORT.dim_dept_nolist values('4410','黑龙江');
insert into REPORT.dim_dept_nolist values('3510','湖北');
insert into REPORT.dim_dept_nolist values('3610','湖南');
insert into REPORT.dim_dept_nolist values('4310','吉林');
insert into REPORT.dim_dept_nolist values('2310','江苏');
insert into REPORT.dim_dept_nolist values('3710','江西');
insert into REPORT.dim_dept_nolist values('3710','辽宁');
insert into REPORT.dim_dept_nolist values('4210','内蒙古');
insert into REPORT.dim_dept_nolist values('5010','宁夏');
insert into REPORT.dim_dept_nolist values('4910','青海');
insert into REPORT.dim_dept_nolist values('3210','山东');
insert into REPORT.dim_dept_nolist values('4110','山西');
insert into REPORT.dim_dept_nolist values('4010','陕西');
insert into REPORT.dim_dept_nolist values('2810','上海');
insert into REPORT.dim_dept_nolist values('3810','四川');
insert into REPORT.dim_dept_nolist values('2910','天津');
insert into REPORT.dim_dept_nolist values('4710','西藏');
insert into REPORT.dim_dept_nolist values('5110','新疆');
insert into REPORT.dim_dept_nolist values('4610','云南');
insert into REPORT.dim_dept_nolist values('2210','浙江');
insert into REPORT.dim_dept_nolist values('3910','重庆');
 commit on;