package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;

import java.util.Map;

//上传医院接口
public interface HospitalService {
    void save(Map<String, Object> paramMap);
    //根据医院变好进行查询
    Hospital getByHoscode(String hoscode);
}
