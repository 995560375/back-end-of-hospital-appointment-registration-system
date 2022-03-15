package com.atguigu.yygh.cmn.service.impl;

import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;

import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    //    @Autowired

    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dicts = baseMapper.selectList(wrapper);
        //向Dict中设置hasChildren
        for(Dict dict : dicts){
            Long id1 = dict.getId();
            boolean b = this.hasChildren(id1);
            dict.setHasChildren(b);
        }
        return dicts;
    }

    private boolean hasChildren(Long id){
        QueryWrapper<Dict> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(objectQueryWrapper);
        return count > 0;
    }
}
