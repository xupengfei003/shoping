ALTER TABLE `ty_supplier`.`account`   
  ADD COLUMN `contract_name` VARCHAR(255) NULL   COMMENT '合同文件名' AFTER `service_phone`		/* 复制栏位名称 */;

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