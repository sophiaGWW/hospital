package com.example.yygh.hosp.repository;

import com.example.yygh.model.hosp.Department;
import com.example.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @create 2022-10-08 17:28
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    //MongoRepository根据此接口智能sql语句
    Department findByHoscodeAndDepcode(String hoscode,String depcode);
}
