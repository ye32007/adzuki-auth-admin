package com.adzuki.admin.manager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.adzuki.admin.common.manager.AbstractManager;
import com.adzuki.admin.entity.TypeCode;
import com.adzuki.admin.exception.CommonException;

@Service
public class TypeCodeManager extends AbstractManager<TypeCode> {
	
	@Transactional
    public int save(TypeCode typeCode) {
        tcodeCheck(typeCode);
        tnameCheck(typeCode);
        if(typeCode.getId() == null){
        	return super.save(typeCode);
        }else{
        	return super.update(typeCode);
        }
        
    }

    private void tcodeCheck(TypeCode typeCode) {
    	Condition condition = new Condition(TypeCode.class);
    	Criteria criteria = condition.createCriteria();
    	criteria.andEqualTo("tcode", typeCode.getTcode());
        if (typeCode.getId() != null) {
            criteria.andNotEqualTo("id", typeCode.getId());
        }
        if (mapper.selectCountByCondition(condition) > 0) {
            throw new CommonException("类型代码[" + typeCode.getTcode() + "]已存在！");
        }
    }

    private void tnameCheck(TypeCode typeCode) {
    	Condition condition = new Condition(TypeCode.class);
    	Criteria criteria = condition.createCriteria();
    	criteria.andEqualTo("tname", typeCode.getTname());
        if (typeCode.getId() != null) {
        	 criteria.andNotEqualTo("id", typeCode.getId());
        }
        if (mapper.selectCountByCondition(condition) > 0) {
            throw new CommonException("类型名称[" + typeCode.getTname() + "]已存在！");
        }
    }
}