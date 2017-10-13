/*创建供应商资质表*/
CREATE TABLE `qualification` (
	`id` BIGINT(64) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`account_id` BIGINT(64) NULL DEFAULT NULL COMMENT '供应商id',
	`upload_type` INT(1) NULL DEFAULT NULL COMMENT '资质上传类型（1、小食品零售 2、其他）',
	`reason` VARCHAR(255) NULL DEFAULT NULL COMMENT '审核未通过原因',
	`delete` INT(1) NULL DEFAULT '0' COMMENT '删除状态（0、未删除（默认） 1、删除）',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/*创建供应照商资质片表*/
CREATE TABLE `qualification_image` (
	`id` BIGINT(64) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`qualification_id` BIGINT(64) NULL DEFAULT NULL COMMENT '资质id',
	`qualification_type` INT(1) NULL DEFAULT NULL COMMENT '资质类型（1、质检报告 2、营业执照 3、授权报告 4、食品流通许可证）',
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

/*供应商表增加资质审核状态字段*/
ALTER TABLE `ty_supplier`.`account`
  ADD COLUMN `qualification_status` INT(1) DEFAULT '0'   COMMENT '资质状态（1、待审核 2、审核通过 3、审核未通过）' AFTER `freight_rules`;