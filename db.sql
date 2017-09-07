ALTER TABLE `ty_supplier`.`account`   
  ADD COLUMN `contract_name` VARCHAR(255) NULL   COMMENT '合同文件名' AFTER `service_phone`		/* 复制栏位名称 */;