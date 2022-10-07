package yygh.hosp.controller;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yygh.common.result.Result;
import yygh.common.utils.MD5;
import yygh.hosp.service.HospitalSetService;

import java.util.List;
import java.util.Random;

/**
 * @create 2022-09-30 10:48
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //1.查询医院所有信息
    @ApiOperation("获取所有医院设置")
    @GetMapping("findAll")
    public Result getAll(){

        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);

    }

    //2.根据id删除医院信息
    @ApiOperation("逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag?Result.ok():Result.fail();
    }

    //3.条件查询分页
    @ApiOperation("条件查询分页")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable Long current,
                                  @PathVariable Long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){

        //分页
        Page<HospitalSet> page = new Page<>(current,limit);
        LambdaQueryWrapper<HospitalSet> lqw = new LambdaQueryWrapper<>();
        //加上条件
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        lqw.like( hosname!= null,HospitalSet::getHosname,hosname)
                        .eq(hoscode !=null,HospitalSet::getHoscode,hoscode);


        //查询返回结果
        Page<HospitalSet> pageSet = hospitalSetService.page(page,lqw);

        return Result.ok(pageSet);
    }
    //4.增加医院数据
    @ApiOperation("增加医院数据")
    @PostMapping("addHospitalSet")
    public Result addHospitalSet(@RequestBody HospitalSet hospitalSet){
        //添加状态,1为可以用，0为不可以用
        hospitalSet.setStatus(1);
        //添加签名密匙
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

        //添加数据
        boolean save = hospitalSetService.save(hospitalSet);

        if (save){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    //5.根据id获取医院数据
    @ApiOperation("根据id获取医院数据")
    @GetMapping("getHospSet/{id}")
    public Result getHospitalSet (@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        if (hospitalSet != null){
            return Result.ok(hospitalSet);
        }else{
            return Result.fail("账号不存在");
        }
    }


    //6.修改医院数据
    @ApiOperation("修改医院数据")
    @PutMapping("update")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean update = hospitalSetService.updateById(hospitalSet);
        if (update){
            return Result.ok();
        }else{
            return Result.fail();
        }

    }
    //7.批量删除医院数据
    @ApiOperation("批量删除医院数据")
    @DeleteMapping("deleteAll")
    public Result deleteAllHospitalSet(@RequestBody List<Long> ids){
        boolean remove = hospitalSetService.removeByIds(ids);
        if (remove){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }
    //8 医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }
    //9 发送签名秘钥
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }



}
