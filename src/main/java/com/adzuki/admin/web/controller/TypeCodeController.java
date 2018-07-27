package com.adzuki.admin.web.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.adzuki.admin.annotation.LogAspect;
import com.adzuki.admin.common.data.Code;
import com.adzuki.admin.common.data.PageData;
import com.adzuki.admin.common.data.PageParam;
import com.adzuki.admin.common.data.Result;
import com.adzuki.admin.common.utils.CodeTools;
import com.adzuki.admin.entity.TypeCode;
import com.adzuki.admin.manager.TypeCodeManager;
import com.adzuki.admin.web.query.TypeCodeQuery;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/typeCode")
public class TypeCodeController extends BaseController {
    @Autowired
    private TypeCodeManager typeCodeManager;

    @RequestMapping(value = {"list"})
    public String list() {
        return "typeCode/list";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<?> get(Long id) {
        TypeCode typeCode = typeCodeManager.findById(id);
        return Result.createSuccessResult(typeCode);
    }

    @LogAspect(type = LogAspect.LogType.Update_TypeCode, objectNames = TypeCode.class)
    @RequestMapping("/save")
    @ResponseBody
    public Result<?> save(@RequestBody TypeCode tc) {
        String msg = validate(tc);

        if (StringUtils.isBlank(msg)) {
            return Result.createFailResult(msg);
        }

        typeCodeManager.save(tc);
        return Result.createSuccessResult();
    }

    @LogAspect(type = LogAspect.LogType.Delete_TypeCode, argNames = "id")
    @RequestMapping("/del")
    @ResponseBody
    public Result<?> del(Long id) {
        typeCodeManager.deleteById(id);
        return Result.createSuccessResult();
    }

    @RequestMapping("/query")
    @ResponseBody
    public Result<?> query(TypeCodeQuery query, PageParam pageParam) {
    	Condition condition = new Condition(TypeCode.class);
        Criteria criteria = condition.createCriteria();
        if(StringUtils.isNoneBlank(query.getTcode())){
        	criteria.andLike("tcode", query.getTcode() + "%");
        }
        if(StringUtils.isNoneBlank(query.getTname())){
        	criteria.andLike("tname", query.getTname() + "%");
        }
        Result<PageInfo<TypeCode>> result = typeCodeManager.queryPage(condition, pageParam);
        PageData<TypeCode> pageData = new PageData<>(result.getData().getTotal(), result.getData().getList());
        return Result.createSuccessResult(pageData);
    }

    @RequestMapping("/listCode")
    @ResponseBody
    public Result<?> listCode(String type) {
        if (StringUtils.isBlank(type)) {
            return Result.createFailResult("参数无效！");
        }

        List<Code> codes = CodeTools.list(type);
        return Result.createSuccessResult(codes);
    }

    private String validate(TypeCode typeCode) {
        StringBuffer sb = new StringBuffer();

        if (StringUtils.isBlank(typeCode.getTcode())) {
            sb.append("类型代码不能为空！");
        }
        if (StringUtils.isBlank(typeCode.getTname())) {
            sb.append("类型名称不能为空！");
        }

        //，替换成,
        //去掉空格和\r
        //去掉空行
        String content = typeCode.getContent()
                .replaceAll("，", ",")
                .replaceAll("[ \r　]", "")
                .replaceAll("[\n]+", "\n");
        if (content.startsWith("\n")) {
            content = content.substring(1);
        }

        typeCode.setContent(content);

        if (StringUtils.isBlank(typeCode.getContent())) {
            sb.append("代码列表不能为空！");
        } else {
            String[] items = typeCode.getContent().split("\n");
            for (String item : items) {
                String[] codeItem = item.split(",");
                if (codeItem.length != 2 || codeItem.length != 3) {
                    sb.append("代码列表格式不正确！");
                }
            }
        }

        return sb.toString();
    }
}
