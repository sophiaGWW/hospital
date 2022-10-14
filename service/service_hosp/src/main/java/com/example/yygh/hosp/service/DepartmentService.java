package com.example.yygh.hosp.service;


import com.example.yygh.model.hosp.Department;
import com.example.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @create 2022-10-08 17:29
 */
public interface DepartmentService{
    void save(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo vo);
    //删除科室接口
    void remove(String hoscode, String depcode);
}
