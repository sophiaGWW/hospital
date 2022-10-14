package com.example.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.yygh.cmn.client.DictFeignClient;
import com.example.yygh.model.hosp.Hospital;
import com.example.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.example.yygh.hosp.repository.HospitalRepository;
import com.example.yygh.hosp.service.HospitalService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @create 2022-10-07 16:47
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> paramMap) {
        //把参数map集合转换成hospital对象
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);
        //判断是否存在数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);

        if(hospitalExist != null ) {
            //修改
            hospital.setStatus(hospital.getStatus());
            hospital.setCreateTime(hospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
//0：未上线 1：已上线
            //添加
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }


    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        //利用mongodb来执行sql语句

        return hospitalRepository.getHospitalByHoscode(hoscode);

    }

    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {

        Pageable pageable = PageRequest.of(page-1, limit);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);

        Example<Hospital> hospitalExample = Example.of(hospital, matcher);
        //创建实例
        Page<Hospital> pages = hospitalRepository.findAll(hospitalExample, pageable);
        
        //获取医院list集合，遍历，进行医院封装
        pages.getContent().stream().forEach(item -> {
            this.setHospital(item);
        });
        
        return pages;
    }

    private Hospital setHospital(Hospital item) {
        String hostype = dictFeignClient.getName("Hostype", item.getHostype());
        String provinceName = dictFeignClient.getName( item.getProvinceCode());
        String cityName = dictFeignClient.getName( item.getCityCode());
        String districtName = dictFeignClient.getName( item.getDistrictCode());

        item.getParam().put("hostype",hostype);
        item.getParam().put("fullAddress",provinceName+cityName+districtName);

        return item;

    }
}
