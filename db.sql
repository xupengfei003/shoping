/*创建供应商资质表*/
CREATE TABLE `qualification` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account_id` bigint(64) DEFAULT NULL COMMENT '供应商id',
  `qualification_status` int(1) DEFAULT '0' COMMENT '资质状态（0、未发起审核 1、待审核 2、审核通过 3、审核未通过）',
  `reason` varchar(255) DEFAULT NULL COMMENT '审核未通过原因',
  `is_read` int(1) DEFAULT '0' COMMENT '是否已读（0、未读 1、已读）',
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