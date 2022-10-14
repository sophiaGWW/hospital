package com.example.yygh.hosp.service;

import com.example.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @create 2022-09-30 10:39
 */
public interface HospitalSetService extends IService<HospitalSet> {
    String getSignByHoscode(String hoscode);
}
