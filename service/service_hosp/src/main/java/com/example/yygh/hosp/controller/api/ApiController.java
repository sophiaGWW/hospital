package com.example.yygh.hosp.controller.api;




import com.example.yygh.model.hosp.Department;
import com.example.yygh.model.hosp.Hospital;
import com.example.yygh.model.hosp.Schedule;
import com.example.yygh.vo.hosp.DepartmentQueryVo;
import com.example.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.yygh.common.exception.YyghException;
import com.example.yygh.common.helper.HttpRequestHelper;
import com.example.yygh.common.result.Result;
import com.example.yygh.common.result.ResultCodeEnum;
import com.example.yygh.common.utils.MD5;
import com.example.yygh.hosp.service.DepartmentService;
import com.example.yygh.hosp.service.HospitalService;
import com.example.yygh.hosp.service.HospitalSetService;
import com.example.yygh.hosp.service.ScheduleService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @create 2022-10-07 16:49
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    //删除排班接口
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        //必填
        String hosScheduleId = (String)paramMap.get("hosScheduleId");
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
//签名校验
//        if(!HttpRequestHelper.isSignEquals(paramMap, hospitalSetService.getSignKey(hoscode))) {
//            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
//        }

        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();

    }

        //上传排班接口
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
       Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
       //TODO 签名校验
        scheduleService.save(paramMap);
        return Result.ok();
    }
    //分页查询排班接口
    @PostMapping("schedule/list")
    public Result schedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        //非必填
        String depcode = (String)paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));

        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
//        if(!HttpRequestHelper.isSignEquals(paramMap, hospitalSetService.getSignKey(hoscode))) {
//            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
//        }

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.selectPage(page , limit, scheduleQueryVo);
        return Result.ok(pageModel);

    }






        //删除科室接口
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //把value为string[]转换为object
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //获取hoscode,depcode
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        //签名校验

        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }

    //查询医院接口
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        //把value为string[]转换为object
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //获取sign，hoscode
        String sign = (String) paramMap.get("sign");
        String hoscode = (String) paramMap.get("hoscode");
        //根据hoscode获取hospitalset中的sign
        String signKey = hospitalSetService.getSignByHoscode(hoscode);
        String signKeyMD5 = MD5.encrypt(signKey);
        //判断签名是否一样
        if (!sign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    //上传医院接口
    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        //把value为string[]转换为object
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //1.获取sign，hoscode
        String sign = (String) paramMap.get("sign");
        String hoscode = (String) paramMap.get("hoscode");
        //根据hoscode获取hospitalset中的sign
        String signKey = hospitalSetService.getSignByHoscode(hoscode);
        String signKeyMD5 = MD5.encrypt(signKey);
        //判断签名是否一样
        if (!sign.equals(signKeyMD5)) {

            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //传输过程中logoData“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String) paramMap.get("logoData");
        if (!StringUtils.isEmpty(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }
        hospitalService.save(paramMap);
        return Result.ok();
    }

    //上传科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        //把value为string[]转换为object
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //1.获取sign，hoscode
        String sign = (String) paramMap.get("sign");
        String hoscode = (String) paramMap.get("hoscode");
        //根据hoscode获取hospitalset中的sign
        String signKey = hospitalSetService.getSignByHoscode(hoscode);
        String signKeyMD5 = MD5.encrypt(signKey);
        //判断签名是否一样
        if (!sign.equals(signKeyMD5)) {

            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(paramMap);
        return Result.ok();
    }
    //查询科室接口
    @ApiOperation(value = "获取分页列表")
    @PostMapping("department/list")
    public Result department(HttpServletRequest request) {
        //把value为string[]转换为object
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) paramMap.get("hoscode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1:Integer.parseInt((String) paramMap.get("page"));
        //TODO 签名校验
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1:Integer.parseInt((String) paramMap.get("limit"));
        DepartmentQueryVo vo = new DepartmentQueryVo();
        vo.setHoscode(hoscode);

       Page<Department> departmentPage= departmentService.findPageDepartment(page,limit,vo);

       return Result.ok(departmentPage);

    }
    }





