package com.example.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.example.yygh.model.hosp.Department;
import com.example.yygh.vo.hosp.DepartmentQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.example.yygh.hosp.repository.DepartmentRepository;
import com.example.yygh.hosp.service.DepartmentService;

import java.util.Date;
import java.util.Map;

/**
 * @create 2022-10-08 17:30
 */
@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;




    @Override
    public void save(Map<String, Object> paramMap) {
        //把参数map集合转换成department对象
        String mapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(mapString, Department.class);
        //upadteorsave
        //根据department的hoscode和depcode获取Repository中的数据，若为空
        Department departmentExist = departmentRepository.findByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        department.setUpdateTime(new Date());
        department.setIsDeleted(0);
        if(departmentExist != null){
            //更新
        }else {
            //添加
            department.setCreateTime(new Date());
        }
        departmentRepository.save(department);

    }

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param vo
     * @return
     */
    @Override
    public Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo vo) {

        //0为第一页
        Pageable pageable = PageRequest.of(page-1, limit);

        Department department = new Department();
        BeanUtils.copyProperties(vo, department);
        department.setIsDeleted(0);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Department> example = Example.of(department, matcher);
       Page<Department> pages = departmentRepository.findAll(example, pageable);
        return pages;

    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.findByHoscodeAndDepcode(hoscode, depcode);
        if (department != null){
            departmentRepository.deleteById(department.getId());
        }
    }


}

