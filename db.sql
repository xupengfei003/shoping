-- 更改订单状态注释说明
ALTER TABLE `purchase` MODIFY COLUMN order_status INT(1) COMMENT '订单状态:1.待付款2.待发货3.已发货4.已完成5.已拒收退款审核6.已退款7.已支付退款审核8.待付款已取消19.确认送达';
-- 更改订单金额备注说明
ALTER TABLE `purchase` MODIFY COLUMN order_price DECIMAL(10,2) COMMENT '商品金额小计';
-- 更改订单成本金额备注说明
ALTER TABLE `purchase` MODIFY COLUMN order_settleme_price DECIMAL(10,2) COMMENT '订单成本金额';
-- 订单表新增折扣优惠、合计总价、实付金额、退款金额、上一个订单状态字段
ALTER TABLE purchase
	ADD discount DECIMAL(10,2) DEFAULT 0  NULL COMMENT '折扣优惠' AFTER `order_postage`,
	ADD order_total_price DECIMAL(10,2) COMMENT '合计总价' AFTER `discount`,
	ADD pay_amount DECIMAL(10,2) COMMENT '实付金额' AFTER `order_total_price`,
	ADD drawback_price DECIMAL(10,2) DEFAULT 0  NULL COMMENT '退款金额' AFTER `pay_amount`,
	ADD prefix_order_status INT(1) DEFAULT 0  NULL COMMENT '上一个订单状态（只有5和7有该状态）' AFTER `drawback_price`;




/*创建产品销量表*/
CREATE TABLE `comm_sales` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`sc_id` BIGINT(20) NULL DEFAULT NULL COMMENT '供应商商品表对应商品ID',
	`actual_sales` INT(10) NOT NULL DEFAULT '0' COMMENT '实际销量',
	`virtual_sales` INT(10) NOT NULL DEFAULT '0' COMMENT '虚拟销量（100-150之间的随机数）',
	`create_at` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
	`update_at` DATETIME NULL DEFAULT NULL COMMENT '修改时间',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `sc_id` (`sc_id`)
)
	COMMENT='商品销量数据表'
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
;

/*初始化产品销量表，将所有商品表sc中未删除添加到商品销量表，并初始化实际和虚拟销量*/
insert into comm_sales (sc_id) select sc.id from supplier_commodity sc where sc.deleted=0;
update comm_sales cs set cs.actual_sales=0,cs.virtual_sales=round(rand()*(150-100)+100),create_at=now(),update_at=now();

/*创建发票内容表*/
CREATE TABLE invoice_content (
   id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
   invoice_content_name varchar(20) DEFAULT NULL COMMENT '发票内容名称',
   operator varchar(50) DEFAULT NULL COMMENT '操作人（登录的账号名）',
   sort int(2) DEFAULT NULL COMMENT '排序',
   create_at datetime DEFAULT NULL COMMENT '创建时间',
   update_at datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='发票内容配置表'
;

/*创建--供应商发票设置表*/
CREATE TABLE `invoice_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `status` int(1) DEFAULT '0' COMMENT '开启状态，0-关闭，1-开启',
  `invoice` int(1) DEFAULT '0' COMMENT '增值税普通发票，0-未选择，1-已选择',
  `special_invoice` int(1) DEFAULT '0' COMMENT '增值税专用发票，0-未选择，1-已选择',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='供应商发票设置表'
;

/*添加供应商发票设置表--供应商ID字段唯一索引*/
ALTER TABLE `invoice_setting` ADD UNIQUE(`supplier_id`);

/*更改key_word表关键字类型备注*/
ALTER TABLE `key_word` MODIFY COLUMN key_word_type INT(1) COMMENT '关键字类型，0-供应商名称，1-商品名称，2-商品品牌';

/*商品分类表增加状态status字段*/
ALTER TABLE `comm_category`
  ADD COLUMN `status` INT(1) DEFAULT 0  NULL  COMMENT '科属状态0隐藏，1展示' AFTER `updated_at`;

/*创建用户优惠券数据表*/
CREATE TABLE `account_coupon` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户优惠券id',
  `coupon_id` BIGINT(20) NULL DEFAULT NULL COMMENT '优惠券id',
  `account_id` BIGINT(20) NULL DEFAULT NULL COMMENT '用户id（现阶段为门店id）',
  `status` INT(11) NULL DEFAULT NULL COMMENT '优惠券状态0（可使用），1（已使用），2（未生效），3（已废弃）,4(已过期)',
  `use_time` DATETIME NULL DEFAULT NULL COMMENT '使用时间',
  `get_time` DATETIME NULL DEFAULT NULL COMMENT '领取时间',
  `create_at` DATETIME NULL DEFAULT NULL COMMENT '记录创建时间',
  `update_at` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)
  COLLATE='utf8_general_ci'
  ENGINE=InnoDB
;

/*创建优惠券数据表*/
CREATE TABLE `coupon` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券id',
  `name` VARCHAR(50) NULL DEFAULT NULL COMMENT '优惠券名称' COLLATE 'utf8_bin',
  `status` INT(11) NULL DEFAULT NULL COMMENT '优惠券状态0（已生效），1（未生效），2（已过期），3（已废弃）',
  `discount_way` INT(11) NULL DEFAULT NULL COMMENT '折扣方式（1：折扣，2满减）',
  `category_id` BIGINT(20) NULL DEFAULT NULL COMMENT '优惠券适用类型，0为全品类',
  `coupon_value` DECIMAL(10,2) NULL DEFAULT NULL COMMENT '优惠券金额',
  `usable_value` DECIMAL(10,2) NULL DEFAULT NULL COMMENT '可用条件（金额）',
  `create_num` INT(11) NULL DEFAULT NULL COMMENT '创建数量',
  `send_num` INT(11) NULL DEFAULT NULL COMMENT '已发放数量（即已领取数量）',
  `use_num` INT(11) NULL DEFAULT NULL COMMENT '已使用数量',
  `send_start_time` DATETIME NULL DEFAULT NULL COMMENT '发放开始时间',
  `send_end_time` DATETIME NULL DEFAULT NULL COMMENT '发放结束时间',
  `use_start_time` DATETIME NULL DEFAULT NULL COMMENT '使用开始时间',
  `use_end_time` DATETIME NULL DEFAULT NULL COMMENT '使用结束时间',
  `create_at` DATETIME NULL DEFAULT NULL COMMENT '记录创建时间',
  `update_at` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)
  COLLATE='utf8_general_ci'
  ENGINE=InnoDB
;



