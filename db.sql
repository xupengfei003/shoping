ALTER TABLE `ty_supplier`.`account`   
  ADD COLUMN `contract_name` VARCHAR(255) NULL   COMMENT '合同文件名' AFTER `service_phone`		/* 复制栏位名称 */;

/*key_word表增加sort字段*/
ALTER TABLE key_word ADD sort int(2) COMMENT '排序' AFTER operator;
/*增加商品是否失效字段*/
ALTER TABLE supplier_commodity ADD invalid_status int(2) DEFAULT 1 COMMENT '商品是否失效：0 失效 ，1正常' AFTER status;
/*增加订单详情表-code69字段*/
ALTER TABLE purchase_item ADD code69 VARCHAR(20) COMMENT '商品条码' AFTER goods_id;
/*订单表增加商户名称*/
ALTER TABLE purchase ADD store_name VARCHAR(50) COMMENT '商户名称' AFTER store_id;
/*订单表增加收货人性别*/
ALTER TABLE purchase ADD order_receiver_sex INT(1) COMMENT '收货人性别 男:1，女:0' AFTER order_receiver_name;
/*订单表中修改账户状态字段注释*/
ALTER TABLE purchase MODIFY COLUMN `account_status` varchar(1) DEFAULT '0' COMMENT '账户状态（0:未统计;1:已统计;2:已结算）';
/*增加合同到期短信提醒状态字段*/
ALTER TABLE `ty_supplier`.`account`
  ADD COLUMN `sms_monthago_type` INT(10) NULL  COMMENT '合同到期30天前短信发送标记,0：未发送，1：已发送' AFTER `service_phone`;
/* 运费规则(0:通用规则 1:配送规则) */;
ALTER TABLE `ty_supplier`.`account`
  ADD COLUMN `freight_rules` INT(1) DEFAULT NULL COMMENT '运费规则(0:通用规则 1:配送规则)' AFTER `sms_monthago_type`		/* 运费规则(0:通用规则 1:配送规则) */;
/*重建购物车表*/
DROP TABLE IF EXISTS cart_item;
CREATE TABLE `cart_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL,
  `supplier_id` bigint(20) NOT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  `commodity_id` bigint(20) NOT NULL,
  `commodity_price` decimal(12,2) NOT NULL DEFAULT '0.01' COMMENT '商品价格',
  `commodity_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `commodity_pic` varchar(255) DEFAULT NULL,
  `measure_spec_id` bigint(20) DEFAULT NULL COMMENT '计量规格ID',
  `measure_spec_name` varchar(64) DEFAULT NULL COMMENT '计量规格名称',
  `rule_val` varchar(256) DEFAULT NULL COMMENT '规格值',
  `unit_id` bigint(20) DEFAULT NULL COMMENT '计量单位ID',
  `unit_name` varchar(64) DEFAULT NULL COMMENT '商品单位名称',
  `commodity_properties` varchar(5000) DEFAULT NULL COMMENT '商品属性',
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `count` int(10) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;


/*删除purchase表中拒收图片*/
ALTER TABLE `purchase`
  DROP COLUMN `order_refuse_imgA`,
  DROP COLUMN `order_refuse_imgB`,
  DROP COLUMN `order_refuse_imgC`;

/*订单表增加取消时间*/
ALTER TABLE purchase ADD order_cancel_time datetime COMMENT '取消订单完成时间' AFTER order_cancel_reason;

/*订单表增加发货时间*/
ALTER TABLE purchase ADD deliver_goods_time datetime COMMENT '发货时间' AFTER refund_id;

/*订单表增加拒收类型*/
ALTER TABLE purchase ADD refuse_type VARCHAR(10) COMMENT '拒收类型' AFTER pay_status;

/*订单表增加取消类型*/
ALTER TABLE purchase ADD cancel_type VARCHAR(10) COMMENT '取消类型' AFTER order_refuse_time;

/*订单表增加物流费用*/
ALTER TABLE purchase ADD order_postage DECIMAL(10,2) DEFAULT '0.00' COMMENT '订单邮费 (0:包邮，非零为具体金额)' AFTER deliver_goods_time;

/*创建拒收图片表*/
DROP TABLE IF EXISTS `refuse_order_img`;
CREATE TABLE `refuse_order_img` (
  `refuse_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '拒收图片ID',
  `order_id` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '订单ID',
  `url` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '图片url',
  `min_img_url` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '缩略图url',
  `img_size` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '图片尺寸',
  `file_name` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '图片名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`refuse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*创建Banner表*/
CREATE TABLE `banner` (
 `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
 `file_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '图片名称',
 `min_img_url` VARCHAR(255) NULL DEFAULT NULL COMMENT '缩略图链接',
 `url` VARCHAR(255) NULL DEFAULT NULL COMMENT '图片链接',
 `location` CHAR(1) NULL DEFAULT NULL COMMENT '轮播位',
 `on_shelves_time` DATETIME NULL DEFAULT NULL COMMENT '上架时间',
 `off_shelf_time` DATETIME NULL DEFAULT NULL COMMENT '下架时间',
 `status` CHAR(1) NULL DEFAULT NULL COMMENT '状态（0全部、1待发布、2已发布、3已下架、4已删除）',
 `url_type` CHAR(1) NOT NULL COMMENT '0：链接：1：商品id：2：供应商id',
 `url_value` VARCHAR(255) NOT NULL COMMENT 'url值',
 `operator` VARCHAR(50) NULL DEFAULT NULL COMMENT '操作人',
 `create_at` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
 `update_at` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
 PRIMARY KEY (`id`)
)
COMMENT='Banner数据表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/*创建key_words关键字表*/
CREATE TABLE `key_word` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`key_word_type` TINYINT(1) NULL DEFAULT NULL COMMENT '关键字类型，0-商品科属，1-商品名称，2-商品品牌（本期只用商品名称）',
	`key_word_value` VARCHAR(20) NULL DEFAULT NULL COMMENT '关键字名称',
	`operator` VARCHAR(50) NULL DEFAULT NULL COMMENT '操作人（登录的账号名）',
	`create_at` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
	`update_at` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='关键字数据表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/*创建热门商品科属表*/
CREATE TABLE `hot_category` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `min_img` VARCHAR(500) NULL DEFAULT NULL COMMENT '缩略图URL',
  `url` VARCHAR(500) NULL DEFAULT NULL COMMENT '原图URL',
  `category_one_id` BIGINT(20) NULL DEFAULT NULL COMMENT '一级分类id',
  `category_one_name` VARCHAR(20) NULL DEFAULT NULL COMMENT '一级分类名称',
  `category_two_id` BIGINT(20) NULL DEFAULT NULL COMMENT '二级分类id',
  `category_two_name` VARCHAR(20) NULL DEFAULT NULL COMMENT '二级分类名称',
  `category_three_id` BIGINT(20) NULL DEFAULT NULL COMMENT '三级分类id',
  `category_three_name` VARCHAR(20) NULL DEFAULT NULL COMMENT '三级分类名称',
  `sort` INT(1) NULL DEFAULT NULL COMMENT '排序',
  `operator` VARCHAR(50) NULL DEFAULT NULL COMMENT '操作人（登录的账号名）',
  `create_at` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)
  COMMENT='热门分类数据表'
  COLLATE='utf8_general_ci'
  ENGINE=InnoDB
;
/*创建热门商品表*/
CREATE TABLE `hot_commodity` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `min_img` VARCHAR(500) NULL DEFAULT NULL COMMENT '缩略图URL',
  `sc_id` VARCHAR(20) NULL DEFAULT NULL COMMENT '商品ID',
  `code69` VARCHAR(20) NULL DEFAULT NULL COMMENT '商品编码',
  `supplier_id` BIGINT(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `code` VARCHAR(50) NULL DEFAULT NULL COMMENT '商品商家编码',
  `provider_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '供应商名称',
  `city_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '合同注册地（市）',
  `comm_brand_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '商品品牌名称',
  `comm_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '商品名称',
  `comm_unit_name` VARCHAR(64) NULL DEFAULT NULL COMMENT '商品单位名称',
  `comm_measure_name` VARCHAR(64) NULL DEFAULT NULL COMMENT '计量规格名称',
  `rule_val` VARCHAR(256) NULL DEFAULT NULL COMMENT '规格值',
  `inventory` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '库存',
  `sales_volume` INT(11) NULL DEFAULT NULL COMMENT '销量',
  `price` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '市场价',
  `status` INT(2) NULL DEFAULT NULL COMMENT '商品状态',
  `sort` INT(2) NULL DEFAULT NULL COMMENT '排序',
  `operator` VARCHAR(50) NULL DEFAULT NULL COMMENT '操作人（登录的账号名）',
  `create_at` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)
  COMMENT='热门商品数据表'
  COLLATE='utf8_general_ci'
  ENGINE=InnoDB
;

/*费用规则*/
CREATE TABLE `freight_rules` (
  `id` int(11) NOT NULL COMMENT '运费规则ID',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `rules_type` int(2) DEFAULT NULL COMMENT '规则类型:0-通用规则,1-配送地区物流费用规则',
  `address_province` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '省',
  `address_city` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '市',
  `address_district` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '区',
  `whether_shipping` int(2) DEFAULT NULL COMMENT '是否包邮:0-不包,1-包邮',
  `send_amount` decimal(10,2) DEFAULT NULL COMMENT '起送金额',
  `default_piece` int(11) DEFAULT NULL COMMENT '默认计件',
  `excess_piece` int(11) DEFAULT NULL COMMENT '超量计件',
  `default_amount` decimal(10,2) DEFAULT NULL COMMENT '运费基础金额',
  `excess_amount` decimal(10,2) DEFAULT NULL COMMENT '运费增加金额',
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT NULL COMMENT '更改时间',
  `distribution_scope_id` int(11) DEFAULT NULL COMMENT '配送范围主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*配送范围*/
CREATE TABLE `distribution_scope` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配送范围ID',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `address_province` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '省',
  `address_city` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '市',
  `address_district` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '区',
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '配送范围备注',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT NULL COMMENT '更改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


/*结算明细列表增加字段*/
ALTER TABLE order_money_record ADD postage_total_amount decimal(10,2) DEFAULT '0.00' COMMENT '运费总金额';
ALTER TABLE order_money_record ADD order_total_amount decimal(10,2) DEFAULT '0.00' COMMENT '订单总金额';

/*商品表增加字段*/
ALTER TABLE supplier_commodity ADD min_order_quantity int(10) NULL  COMMENT '最小起订量';

/*供应商与商品关系审核表*/
CREATE TABLE `supplier_commodity_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sc_id` bigint(20) DEFAULT NULL COMMENT '供应商商品表id',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `updated_at` datetime DEFAULT NULL COMMENT '审核时间',
  `created_at` datetime DEFAULT NULL COMMENT '提交审核时间',
  `status` int(8) DEFAULT '0' COMMENT '审核状态',
  `audit_result` int(1) DEFAULT NULL COMMENT '审核结果：0代表未通过，1代表通过，2代表待审核',
  `audit_flag` int(1) DEFAULT NULL COMMENT '1代表供应商当前审核记录',
  `audit_by` bigint(20) DEFAULT NULL COMMENT '审核人',
  `audit_opinion` varchar(200) DEFAULT NULL COMMENT '审核意见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COMMENT='供应商与商品关系审核表';

/*增加商品编辑待审核临时数据表*/
CREATE TABLE `supplier_commodity_tmp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sca_id` bigint(20) DEFAULT NULL COMMENT '供应商商品审批表id',
  `sku` varchar(20) DEFAULT NULL COMMENT 'SKU',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `code69` varchar(20) DEFAULT NULL COMMENT '商品条码',
  `tag_id` bigint(20) DEFAULT NULL COMMENT '标签ID',
  `code` varchar(50) DEFAULT NULL COMMENT '商家编码',
  `measure_spec_id` bigint(20) DEFAULT NULL COMMENT '计量规格ID',
  `rule_val` varchar(256) DEFAULT NULL COMMENT '规格值',
  `inventory` decimal(11,2) DEFAULT NULL COMMENT '库存',
  `unit_id` bigint(20) DEFAULT NULL COMMENT '计量单位ID',
  `price` decimal(10,2) DEFAULT NULL COMMENT '市场价',
  `unit_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `min_img` varchar(500) DEFAULT NULL COMMENT '缩略图URL',
  `created_at` datetime DEFAULT NULL COMMENT '创建人',
  `updated_at` datetime DEFAULT NULL COMMENT '创建时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) DEFAULT '0' COMMENT '删除标记',
  `status` int(8) DEFAULT '0' COMMENT '商品状态',
  `invalid_status` int(2) DEFAULT '1' COMMENT '商品是否失效：0--失效 ，1--正常',
  `remark` text COMMENT '商品描述',
  `min_order_quantity` int(10) DEFAULT NULL COMMENT '最小起订量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='供应商与商品关系待审核数据表';

/*增加商品图片表*/
CREATE TABLE `comm_imge_tmp` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sca_id` bigint(50) DEFAULT NULL COMMENT '供应商商品审核表ID',
  `name` varchar(500) DEFAULT NULL COMMENT '图片名称',
  `url` varchar(500) DEFAULT NULL COMMENT '图片存储路径',
  `type` varchar(50) DEFAULT NULL COMMENT '图片格式',
  `size` varchar(50) DEFAULT NULL COMMENT '图片尺寸',
  `created_at` datetime DEFAULT NULL COMMENT '上传时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `thumbnail_url` varchar(500) DEFAULT NULL COMMENT '缩略图url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='商品图片表';


