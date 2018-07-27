package com.adzuki.admin.common.mapper;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface CoreMapper<T> extends
		Mapper<T>,
		ConditionMapper<T>,
		IdsMapper<T>, 
		InsertListMapper<T>,
		MySqlMapper<T>{

}