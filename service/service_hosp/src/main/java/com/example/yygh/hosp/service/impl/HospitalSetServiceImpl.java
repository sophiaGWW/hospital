package com.example.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.yygh.hosp.mapper.HospitalSetMapper;
import com.example.yygh.hosp.service.HospitalSetService;
import com.example.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @create 2022-09-30 10:40
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    @Override
    public String getSignByHoscode(String hoscode) {
        LambdaQueryWrapper<HospitalSet> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HospitalSet::getHoscode,hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(lqw);
        return hospitalSet.getSignKey();
    }
}
