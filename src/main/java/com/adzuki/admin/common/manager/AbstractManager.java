package com.adzuki.admin.common.manager;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import com.adzuki.admin.common.data.PageParam;
import com.adzuki.admin.common.data.Result;
import com.adzuki.admin.common.mapper.CoreMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public abstract class AbstractManager<T>  {
	
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required=false)
    protected CoreMapper<T> mapper;

    private Class<T> modelClass;    // 当前泛型真实类型的Class

    @SuppressWarnings("unchecked")
	public AbstractManager() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public int save(T model) {
        return mapper.insertSelective(model);
    }

    public int save(List<T> models) {
        return mapper.insertList(models);
    }

    public int deleteById(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void deleteByIds(String ids) {
        Stream.of(ids.split(",")).map(Long::valueOf).forEach(this::deleteById);
    }
    
    public int deleteLogicById(Long id) throws Exception {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField("id");
            field.setAccessible(true);
            field.set(model, id);//id
            field = modelClass.getDeclaredField("logicState");
            field.setAccessible(true);
            field.set(model, false);//logicState
            return mapper.updateByPrimaryKeySelective(model);
        } catch (ReflectiveOperationException e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    public int update(T model) {
    	try {
	        Field field = modelClass.getDeclaredField("updateTime");
	        field.setAccessible(true);
	        field.set(model, new Date());
    	} catch(Exception e) {}
        return mapper.updateByPrimaryKeySelective(model);
    }
    
    public int updateSelective(T model) {
    	try {
	        Field field = modelClass.getDeclaredField("updateTime");
	        field.setAccessible(true);
	        field.set(model, new Date());
    	} catch(Exception e) {}
        return mapper.updateByPrimaryKeySelective(model);
    }

    public T findById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public T findBy(String fieldName, Object value) throws Exception {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }
    
    public List<T> findByExample(Example example) {
        return mapper.selectByExample(example);
    }

    public List<T> findAll() {
        return mapper.selectAll();
    }
    
    public Result<PageInfo<T>> queryPage(Condition condition, PageParam pageParam){
		try {
            PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
            List<T> result = findByCondition(condition);
            PageInfo<T> pageInfo = new PageInfo<>(result);
            return Result.createSuccessResult(pageInfo);
        }catch (Exception e) {
            logger.error("queryPage查找异常", e);
            return Result.createFailResult("系统异常");
        }
	}
    
    public Result<PageInfo<T>> queryPage(Example example, PageParam pageParam){
		try {
            PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
            List<T> result = findByExample(example);
            PageInfo<T> pageInfo = new PageInfo<>(result);
            return Result.createSuccessResult(pageInfo);
        }catch (Exception e) {
            logger.error("queryPage查找异常", e);
            return Result.createFailResult("系统异常");
        }
	}

}
