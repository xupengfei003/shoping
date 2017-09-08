<<<<<<< HEAD
ALTER TABLE `ty_supplier`.`account`   
  ADD COLUMN `contract_name` VARCHAR(255) NULL   COMMENT '��ͬ�ļ���' AFTER `service_phone`		/* ������λ���� */;
=======
ALTER TABLE `ty_supplier`.`account`   
  ADD COLUMN `contract_name` VARCHAR(255) NULL   COMMENT '合同文件名' AFTER `service_phone`		/* 复制栏位名称 */;

/*增加商品是否失效字段*/
ALTER TABLE supplier_commodity ADD invalid_status int(2) default 1 comment '商品是否失效：0 失效 ，1正常' after status;
/*增加订单详情表-code69字段*/
ALTER TABLE purchase_item ADD code69 VARCHAR(20) COMMENT '商品条码' AFTER goods_id;
/*订单表增加商户名称*/
ALTER TABLE purchase ADD store_name VARCHAR(50) COMMENT '商户名称' AFTER store_id;
/*订单表增加收货人性别*/
ALTER TABLE purchase ADD order_receiver_sex INT(1) COMMENT '收货人性别 男:1，女:0' AFTER order_receiver_name;
>>>>>>> c919d16c903684fc174778a6d70db71cbcb00983
