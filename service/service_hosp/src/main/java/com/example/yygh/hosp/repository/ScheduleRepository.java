package com.example.yygh.hosp.repository;

import com.example.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @create 2022-10-13 17:11
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {

    Schedule findByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);
}
