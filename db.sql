<<<<<<< HEAD
ALTER TABLE `ty_supplier`.`account`   
  ADD COLUMN `contract_name` VARCHAR(255) NULL   COMMENT '合同文件名' AFTER `service_phone`		/* 复制栏位名称 */;
=======
ALTER TABLE `ty_supplier`.`account`   
  ADD COLUMN `contract_name` VARCHAR(255) NULL   COMMENT '鍚堝悓鏂囦欢鍚�' AFTER `service_phone`		/* 澶嶅埗鏍忎綅鍚嶇О */;

/*澧炲姞鍟嗗搧鏄惁澶辨晥瀛楁*/
ALTER TABLE supplier_commodity ADD invalid_status int(2) default 1 comment '鍟嗗搧鏄惁澶辨晥锛�0 澶辨晥 锛�1姝ｅ父' after status;
/*澧炲姞璁㈠崟璇︽儏琛�-code69瀛楁*/
ALTER TABLE purchase_item ADD code69 VARCHAR(20) COMMENT '鍟嗗搧鏉＄爜' AFTER goods_id;
/*璁㈠崟琛ㄥ鍔犲晢鎴峰悕绉�*/
ALTER TABLE purchase ADD store_name VARCHAR(50) COMMENT '鍟嗘埛鍚嶇О' AFTER store_id;
/*璁㈠崟琛ㄥ鍔犳敹璐т汉鎬у埆*/
ALTER TABLE purchase ADD order_receiver_sex INT(1) COMMENT '鏀惰揣浜烘�у埆 鐢�:1锛屽コ:0' AFTER order_receiver_name;
>>>>>>> c919d16c903684fc174778a6d70db71cbcb00983
