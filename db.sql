/*创建供应商资质表*/
CREATE TABLE `qualification` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account_id` bigint(64) DEFAULT NULL COMMENT '供应商id',
  `qualification_status` int(1) DEFAULT '0' COMMENT '资质状态（0、未发起审核 1、待审核 2、审核通过 3、审核未通过）',
  `reason` varchar(255) DEFAULT NULL COMMENT '审核未通过原因',
  `delete` int(1) DEFAULT '0' COMMENT '删除状态（0、未删除（默认） 1、删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8
;

/*创建供应照商资质片表*/
CREATE TABLE `qualification_image` (
	`id` BIGINT(64) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`qualification_id` BIGINT(64) NULL DEFAULT NULL COMMENT '资质id',
	`qualification_type` INT(1) NULL DEFAULT NULL COMMENT '资质类型（1、开户银行许可证 2、营业执照3、授权报告 4、质检报告 5、食品流通许可证）',
	`cloud_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '云端名称',
	`min_cloud_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '云端缩略图名称',
	`file_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '图片名称',
	`url` VARCHAR(255) NULL DEFAULT NULL COMMENT '云端大图地址',
	`min_img_url` VARCHAR(255) NULL DEFAULT NULL COMMENT '云端缩略图地址',
	`delete` INT(1) NULL DEFAULT '0' COMMENT '删除状态（0、未删除（默认） 1、删除）',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/*创建商品箱规单位表*/
CREATE TABLE `comm_carton` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '箱规单位名称',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID,管理员supplierId为0',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品箱规单位'
;

/*创建自配送时确认收货订单表*/
CREATE TABLE `received_purchase` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` varchar(30) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*
增加供应商是否首次登陆
 */
ALTER TABLE `account`
  ADD COLUMN `is_read` INT(1) DEFAULT 0  NULL   COMMENT '是否已读（0、未读 1、已读）' AFTER `contract_name`;

/*
商品表新增字段
 */
ALTER TABLE `supplier_commodity`
  ADD COLUMN `carton_id` BIGINT(20)  NULL  COMMENT '箱规单位id' AFTER `min_order_quantity`,
  ADD COLUMN `carton_val` INT(10)  NULL  COMMENT '箱规数值' AFTER `carton_id`,
  ADD COLUMN `inventory_minimum` INT(10) DEFAULT 0  NULL  COMMENT '库存下限值' AFTER `carton_val`,
  ADD COLUMN `inventory_status` INT(1) DEFAULT 0  NULL  COMMENT '库存状态，0-正常，1-预警' AFTER `inventory_minimum`,
  ADD COLUMN `measure_spec_val` INT(10)  NULL  COMMENT '计量规格数值' AFTER `inventory_status`,
  ADD COLUMN `production_date` DATETIME  NULL  COMMENT '生产日期' AFTER `measure_spec_val`,
  ADD COLUMN `guarantee_period` INT(10)  NULL  COMMENT '有效期' AFTER `production_date`,
  ADD COLUMN `guarantee_period_unit` VARCHAR (10)  NULL  COMMENT '有效期单位' AFTER `guarantee_period`,
  MODIFY COLUMN inventory BIGINT(20);

/*
商品tmp表新增字段
 */
ALTER TABLE `supplier_commodity_tmp`
  ADD COLUMN `carton_id` BIGINT(20)  NULL  COMMENT '箱规单位id' AFTER `min_order_quantity`,
  ADD COLUMN `carton_val` INT(10)  NULL  COMMENT '箱规数值' AFTER `carton_id`,
  ADD COLUMN `inventory_minimum` INT(10) DEFAULT 0  NULL  COMMENT '库存下限值' AFTER `carton_val`,
  ADD COLUMN `inventory_status` INT(1) DEFAULT 0  NULL  COMMENT '库存状态，0-正常，1-预警' AFTER `inventory_minimum`,
  ADD COLUMN `measure_spec_val` INT(10)  NULL  COMMENT '计量规格数值' AFTER `inventory_status`,
  ADD COLUMN `production_date` DATETIME  NULL  COMMENT '生产日期' AFTER `measure_spec_val`,
  ADD COLUMN `guarantee_period` INT(10)  NULL  COMMENT '有效期' AFTER `production_date`,
  ADD COLUMN `guarantee_period_unit` VARCHAR (10)  NULL  COMMENT '有效期单位' AFTER `guarantee_period`,
  MODIFY COLUMN inventory BIGINT(20);

/*
省市区无三级菜单的市增加三级菜单
 */
INSERT INTO `sys_region` VALUES ('441901', '0', '523000', '市辖区', '441900', '113.7634339907565500', '23.0430238153682400', '2', '0');
INSERT INTO `sys_region` VALUES ('442001', '0', '528400', '市辖区', '442000', '113.4220600207980600', '22.5451775145130000', '2', '0');
INSERT INTO `sys_region` VALUES ('442102', '0', '', '市辖区', '442101', '116.6324890191240200', '20.7317264938579980', '2', '0');
INSERT INTO `sys_region` VALUES ('460201', '0', '572000', '市辖区', '460200', '109.5227712813588400', '18.2577759148974860', '2', '0');
INSERT INTO `sys_region` VALUES ('620201', '0', '735100', '市辖区', '620200', '98.2816345852569810', '39.8023973267335260', '2', '0');
INSERT INTO `sys_region` VALUES ('820101', '0', '', '市辖区', '820100', '113.5664323351728800', '22.1950041592184670', '2', '0');
INSERT INTO `sys_region` VALUES ('820201', '0', '', '市辖区', '820200', '113.5575191018249200', '22.2041179884433360', '2', '0');

/*
商品数据待上架修改为已下架
 */
update supplier_commodity set status = 3 where status = 0;
update supplier_commodity_tmp set status = 3 where status = 0;

/*
为公共商品库添加唯一索引
 */
ALTER TABLE `commodity` ADD UNIQUE INDEX `code69` (`code69`);