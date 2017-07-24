/*
Navicat MySQL Data Transfer

Source Server         : 10.100.50.27
Source Server Version : 50719
Source Host           : 10.100.50.27:3306
Source Database       : ty_supplier

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-07-24 19:38:58
*/

SET FOREIGN_KEY_CHECKS=0;


-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL,
  `supplier_id` bigint(20) NOT NULL,
  `commodity_id` bigint(20) NOT NULL,
  `commodity_price` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '商品价格',
  `commodity_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `commodity_properties` varchar(5000) DEFAULT NULL COMMENT '商品属性',
  `commodity_pic` varchar(255) DEFAULT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `count` int(10) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for commodity
-- ----------------------------
DROP TABLE IF EXISTS `commodity`;
CREATE TABLE `commodity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '商品名称',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED COMMENT='商品表';

-- ----------------------------
-- Table structure for comm_brand
-- ----------------------------
DROP TABLE IF EXISTS `comm_brand`;
CREATE TABLE `comm_brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(200) DEFAULT NULL COMMENT '品牌名称',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='商品品牌';

-- ----------------------------
-- Table structure for comm_category
-- ----------------------------
DROP TABLE IF EXISTS `comm_category`;
CREATE TABLE `comm_category` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` int(50) DEFAULT NULL COMMENT 'PID',
  `name` varchar(50) NOT NULL COMMENT '类型名称',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `level` int(50) DEFAULT NULL COMMENT '类型级别',
  `sort` int(50) DEFAULT NULL COMMENT '顺序',
  `deleted` int(1) DEFAULT NULL COMMENT '删除标记 0:未删除 1:删除',
  `created_at` bigint(50) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(50) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8 COMMENT='商品类型表';

-- ----------------------------
-- Table structure for comm_category_ship
-- ----------------------------
DROP TABLE IF EXISTS `comm_category_ship`;
CREATE TABLE `comm_category_ship` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `comm_id` bigint(50) DEFAULT NULL COMMENT '商品Id',
  `comm_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `category_id` bigint(50) DEFAULT NULL COMMENT '类型Id',
  `category_name` varchar(50) DEFAULT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='商品和类型关系表';

-- ----------------------------
-- Table structure for comm_imge
-- ----------------------------
DROP TABLE IF EXISTS `comm_imge`;
CREATE TABLE `comm_imge` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sc_id` bigint(50) DEFAULT NULL COMMENT '供应商商品ID',
  `name` varchar(50) DEFAULT NULL COMMENT '图片名称',
  `url` varchar(200) DEFAULT NULL COMMENT '图片存储路径',
  `type` varchar(50) DEFAULT NULL COMMENT '图片格式',
  `size` varchar(50) DEFAULT NULL COMMENT '图片尺寸',
  `created_at` bigint(50) DEFAULT NULL COMMENT '上传时间',
  `updated_at` bigint(50) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='商品图片表';

-- ----------------------------
-- Table structure for order_money_record
-- ----------------------------
DROP TABLE IF EXISTS `order_money_record`;
CREATE TABLE `order_money_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现申请id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '申请人',
  `bank_name` varchar(60) DEFAULT NULL COMMENT '开户行',
  `bank_name_branch` varchar(60) DEFAULT NULL COMMENT '开户支行',
  `bank_account` varchar(20) DEFAULT NULL COMMENT '银行卡号',
  `total_money` decimal(10,2) DEFAULT NULL COMMENT '提现金额',
  `state` varchar(1) NOT NULL DEFAULT '0' COMMENT '状态 0 提现申请中 1 已通过 2 已完成',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `serial_number` varchar(40) DEFAULT NULL COMMENT '银行流水号',
  `order_id` varchar(4000) DEFAULT NULL COMMENT '订单编号',
  PRIMARY KEY (`record_id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for purchase
-- ----------------------------
DROP TABLE IF EXISTS `purchase`;
CREATE TABLE `purchase` (
  `order_id` varchar(16) NOT NULL COMMENT '订单编号',
  `store_id` bigint(32) DEFAULT NULL COMMENT '商户ID',
  `user_id` bigint(32) NOT NULL COMMENT '买家ID',
  `order_price` decimal(10,2) unsigned zerofill DEFAULT NULL COMMENT '订单实付金额',
  `order_receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `order_receiver_mobile` varchar(11) DEFAULT NULL COMMENT '收货人电话',
  `order_address` varchar(150) DEFAULT NULL COMMENT '收货人地址',
  `order_ship_method` int(1) DEFAULT NULL COMMENT '配送方式',
  `order_payment_num` varchar(32) DEFAULT '' COMMENT '支付流水号',
  `order_create_time` bigint(20) DEFAULT NULL COMMENT '下单时间',
  `order_payment_method` int(1) DEFAULT NULL COMMENT '支付方式',
  `order_payment_time` bigint(20) DEFAULT NULL COMMENT '订单支付时间',
  `order_refund_reason` varchar(150) DEFAULT NULL COMMENT '买家申请退货理由',
  `order_refund_time` bigint(20) DEFAULT NULL COMMENT '卖家申请退货时间',
  `order_refuse_reason` varchar(150) DEFAULT NULL COMMENT '卖家拒绝理由',
  `order_refuse_time` bigint(20) DEFAULT NULL COMMENT '卖家拒绝时间',
  `order_cancel_reason` int(1) DEFAULT NULL COMMENT '买家取消订单原因',
  `order_shipment_number` varchar(20) DEFAULT NULL COMMENT '物流单号',
  `order_invoice` int(1) DEFAULT NULL COMMENT '发票状态:0有1没有',
  `order_status` int(1) DEFAULT NULL COMMENT '订单状态 1待付款2代发货3已发货4已收货5已拒收6已退款7已完成',
  `account_status` varchar(1) DEFAULT '0' COMMENT '账户状态（1：已统计 0：未统计 2：已提现）',
  `drawback_time` bigint(20) DEFAULT NULL COMMENT '退款时间',
  `distributor_name` varchar(50) DEFAULT NULL COMMENT '配送人姓名',
  `distributor_mobile` varchar(11) DEFAULT NULL COMMENT '配送人电话',
  `logistics_company` varchar(100) DEFAULT NULL COMMENT '物流公司',
  PRIMARY KEY (`order_id`),
  KEY `storeId_index` (`store_id`),
  KEY `userId_index` (`user_id`),
  KEY `create_time_index` (`order_create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for purchase_item
-- ----------------------------
DROP TABLE IF EXISTS `purchase_item`;
CREATE TABLE `purchase_item` (
  `details_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '详情编号',
  `goods_attribute` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '商品属性',
  `goods_id` bigint(40) DEFAULT NULL COMMENT '商品编号',
  `goods_number` int(11) DEFAULT NULL COMMENT '商品数量',
  `goods_unit_price` decimal(10,2) DEFAULT NULL COMMENT '商品单价',
  `goods_tatol_price` decimal(10,2) DEFAULT NULL COMMENT '商品总价',
  `goods_favourable` decimal(10,2) DEFAULT NULL COMMENT '商品优惠',
  `goods_image` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片',
  `goods_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  `order_id` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '订单ID',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌id',
  `brand_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '品牌名称',
  PRIMARY KEY (`details_id`),
  KEY `orderId_index` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '供应商名称',
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='供应商';

-- ----------------------------
-- Table structure for supplier_commodity
-- ----------------------------
DROP TABLE IF EXISTS `supplier_commodity`;
CREATE TABLE `supplier_commodity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sku` varchar(20) DEFAULT NULL COMMENT 'SKU',
  `category_one_id` bigint(20) DEFAULT NULL COMMENT '类型ID',
  `category_two_id` bigint(20) DEFAULT NULL COMMENT '类型ID',
  `category_three_id` bigint(20) DEFAULT NULL COMMENT '类型ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `commodity_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `brand` varchar(20) DEFAULT NULL COMMENT '品牌',
  `name` varchar(200) DEFAULT '' COMMENT '商品名称',
  `code69` varchar(20) DEFAULT NULL COMMENT '商品编码',
  `code` varchar(50) DEFAULT NULL COMMENT '商家编码',
  `rule_name` varchar(50) DEFAULT NULL COMMENT '规格名称',
  `rule_val` varchar(50) DEFAULT NULL COMMENT '规格值',
  `inventory` decimal(11,2) DEFAULT NULL COMMENT '库存',
  `unit` varchar(50) DEFAULT NULL COMMENT '计量单位',
  `price` decimal(10,0) DEFAULT NULL COMMENT '市场价',
  `unit_price` decimal(10,0) DEFAULT NULL COMMENT '售价',
  `min_img` varchar(500) DEFAULT NULL COMMENT '缩略图URL',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建人',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) DEFAULT '0' COMMENT '删除标记',
  `status` int(8) DEFAULT '0' COMMENT '商品状态',
  `description` text COMMENT '商品介绍',
  `remark` text COMMENT '商品描述',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=131 DEFAULT CHARSET=utf8 COMMENT='供应商与商品关系表';

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `pid` int(20) NOT NULL,
  `url` varchar(100) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `account_id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `provider_name` varchar(20) DEFAULT NULL COMMENT '供应商名称',
  `responsible` varchar(64) DEFAULT NULL COMMENT '供应商法人代表',
  `responsible_phone` varchar(20) DEFAULT NULL COMMENT '供应商法人电话',
  `license` varchar(64) DEFAULT NULL COMMENT '营业执照',
  `license_time_create` bigint(20) DEFAULT NULL COMMENT '供应商营业执照开始日期',
  `license_time_end` bigint(20) DEFAULT NULL COMMENT '供应商营业执照截至日期',
  `business_type` varchar(64) DEFAULT NULL COMMENT '行业类型',
  `regist_address` varchar(255) DEFAULT '' COMMENT '注册地址',
  `register_address_detail` varchar(255) DEFAULT NULL COMMENT '注册详细地址（包括街道门牌号）',
  `discount` varchar(64) DEFAULT NULL COMMENT '折扣信息',
  `bank_name` varchar(64) DEFAULT NULL COMMENT '开户银行',
  `bank_num` varchar(128) DEFAULT NULL COMMENT '开户行账号',
  `bank_user_name` varchar(64) DEFAULT NULL COMMENT '开户人姓名',
  `contract_create_date` bigint(20) DEFAULT NULL COMMENT '合同创建日期',
  `contract_end_date` bigint(20) DEFAULT NULL COMMENT '合同截至日期',
  `remittance_type` varchar(32) DEFAULT NULL COMMENT '汇款结算方式 1:自然月 2:固定时间',
  `remittanced` varchar(255) DEFAULT NULL COMMENT 'remittance_type=1时表示每月哪天,remittance_type=2时表示天数',
  `account_status` int(10) DEFAULT '2' COMMENT '账户状态(0 删除，1启用，2停用)',
  `create_date` bigint(20) DEFAULT NULL COMMENT '创建日期',
  `update_date` bigint(20) DEFAULT NULL COMMENT '修改日期',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `balance` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '余额',
  `income` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '历史总收入',
  `contract_responsible` varchar(255) DEFAULT NULL COMMENT '法人代表(合同)',
  `contract_responsible_phone` varchar(255) DEFAULT NULL COMMENT '法人电话号码(合同)',
  `contract_license` varchar(255) DEFAULT NULL COMMENT '营业执照(合同)',
  `contract_license_create` bigint(20) DEFAULT NULL COMMENT '营业执照(合同)开始日期',
  `contract_license_end` bigint(20) DEFAULT NULL COMMENT '合同营业执照截至日期',
  `contract_register_address` varchar(255) DEFAULT NULL COMMENT '省市区(合同)',
  `contract_register_address_detail` varchar(255) DEFAULT NULL COMMENT '注册详细地址(合同)',
  `upload_mode` varchar(10) DEFAULT NULL COMMENT '上传方式 ，1单次，2批量',
  `sms_code` varchar(255) DEFAULT NULL COMMENT '短信验证码(一期暂时)',
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_delivery_address
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_address`;
CREATE TABLE `t_delivery_address` (
  `addr_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收货地址id',
  `consignee` varchar(30) DEFAULT NULL COMMENT '收货人',
  `consignee_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `addr_province` varchar(30) DEFAULT NULL COMMENT '省',
  `addr_city` varchar(30) DEFAULT NULL COMMENT '市',
  `addr_area` varchar(30) DEFAULT NULL COMMENT '区',
  `addr_street` varchar(50) DEFAULT NULL COMMENT '街道',
  `addr_details` varchar(100) DEFAULT NULL COMMENT '详细地址',
  `addr_default` tinyint(1) unsigned DEFAULT '0' COMMENT '是否设为默认地址(0：非默认地址；1：默认地址)',
  `del_state` tinyint(1) unsigned DEFAULT '0' COMMENT '删除状态(0：未删除；1：已删除)',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`addr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL COMMENT '字典类型',
  `name` varchar(255) DEFAULT NULL COMMENT '字典名',
  `create_time` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_item`;
CREATE TABLE `t_dict_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dict_type` varchar(50) DEFAULT NULL COMMENT '字典类型',
  `code` varchar(255) DEFAULT NULL COMMENT '字典项名',
  `name` varchar(50) DEFAULT NULL COMMENT '字典项code',
  `create_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `per_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `per_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '权限名称',
  `per_url` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '可访问地址',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `per_desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '权限描述',
  PRIMARY KEY (`per_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '角色名称',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `role_desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `per_id` bigint(20) NOT NULL COMMENT '权限id',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_sys_region
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_region`;
CREATE TABLE `t_sys_region` (
  `sr_id` int(11) NOT NULL COMMENT '主键',
  `type` int(1) DEFAULT '0' COMMENT '类型 0省 1直辖市',
  `code` varchar(100) NOT NULL COMMENT '编码',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父级ID',
  `lng` double(19,16) DEFAULT '0.0000000000000000' COMMENT '经度',
  `lat` double(19,16) DEFAULT '0.0000000000000000' COMMENT '纬度',
  `level` int(1) NOT NULL DEFAULT '0' COMMENT '层级 0省、直辖市、港澳台 1市 2县/区 3镇 4村',
  `sort_no` int(11) NOT NULL DEFAULT '0' COMMENT '排序序号',
  PRIMARY KEY (`sr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='行政区字典表';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '登录名称',
  `login_password` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '登录密码',
  `last_password_reset_date` bigint(20) DEFAULT NULL COMMENT '最后一次修改密码时间',
  `user_email` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户邮箱',
  `active_time` bigint(20) DEFAULT NULL COMMENT '激活时间',
  `login_time` bigint(20) DEFAULT NULL COMMENT '登录时间',
  `last_login_time` bigint(20) DEFAULT NULL COMMENT '上次登录时间',
  `user_status` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户状态(0 删除，1启用，2停用)',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `sms_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '短信验证码(一期暂时)',
  `remark` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_user_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_user_permission`;
CREATE TABLE `t_user_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `per_id` bigint(20) NOT NULL COMMENT '权限id',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `t_user_roles`;
CREATE TABLE `t_user_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
