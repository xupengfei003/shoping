package com.sao.so.supplier.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface DictItemMapper {

    List<String> selectBank();
    List<String> selectHangYe();
}