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