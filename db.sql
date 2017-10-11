/*创建供应商资质表*/
CREATE TABLE `qualification` (
	`id` BIGINT(64) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`account_id` BIGINT(64) NULL DEFAULT NULL COMMENT '供应商id',
	`qualification_type` INT(1) NULL DEFAULT NULL COMMENT '资质类型（1、质检报告 2、营业执照 3、授权报告 4、食品流通许可证）',
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

/*创建供应商资质照片表*/
CREATE TABLE `qualification_image` (
	`id` BIGINT(64) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`qualification_id` BIGINT(64) NULL DEFAULT NULL COMMENT '资质id',
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
