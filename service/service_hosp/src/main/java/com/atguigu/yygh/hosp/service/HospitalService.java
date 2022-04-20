package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;


public interface HospitalService {
    //上传医院接口
    void save(Map<String, Object> paramMap);
    //根据医院变好进行查询
    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object> showHospDetail(String id);
    //获取医院名称
    String getHospName(String hoscode);
}
