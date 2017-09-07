/*增加商品是否失效字段*/
ALTER TABLE supplier_commodity ADD invalid_status int(2) default 1 comment '商品是否失效：0 失效 ，1正常' after status;