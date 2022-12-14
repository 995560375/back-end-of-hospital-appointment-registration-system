package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;

import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    //    @Autowired
    @Autowired
    private DictMapper dictMapper;

    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
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
    //导出数据字典
    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

        //数据库操作
        List<Dict> dicts = baseMapper.selectList(null);

        //Dict - DictEeVo
        List<DictEeVo> dictEeVos = new ArrayList<>();
        for(Dict dict : dicts){
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            dictEeVos.add(dictEeVo);
        }

        //调用方法
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeVos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //导入数据字典
    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(dictMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //根据value和dictcode查询
    @Override
    public String getDictName(String dictCode, String value) {
        if(StringUtils.isEmpty(dictCode)){
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else {
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("parent_id", parent_id).eq("value", value));
            return finalDict.getName();
        }

    }
    //查询下级节点根据dictCode
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictCode获得id
        Dict dict = this.getDictByDictCode(dictCode);
        //根据id获得子节点
        List<Dict> childData = this.findChildData(dict.getId());
        return childData;
    }

    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }

    private boolean hasChildren(Long id){
        QueryWrapper<Dict> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(objectQueryWrapper);
        return count > 0;
    }
}
