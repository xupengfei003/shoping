package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import so.sao.shop.supplier.domain.FreightRules;

@Mapper
public interface FreightRulesDao {
    void insert(FreightRules freightRules);
}