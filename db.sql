/*增加商品是否失效字段*/
ALTER TABLE supplier_commodity ADD invalid_status int(2) default 1 comment '商品是否失效：0 失效 ，1正常' after status;
/*增加订单详情表-code69字段*/
ALTER TABLE purchase_item ADD code69 VARCHAR(20) COMMENT '商品条码' AFTER goods_id;