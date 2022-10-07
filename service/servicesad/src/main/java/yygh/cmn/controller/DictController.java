package yygh.cmn.controller;

import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yygh.cmn.service.DictService;
import yygh.common.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @create 2022-10-04 19:07
 */
@Api
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "导入")
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }



    @ApiOperation(value="导出")
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {
        dictService.exportData(response);

    }


    //获取某id下所有子集
    @ApiOperation("获取某id下所有子集")
    @GetMapping("getChlidData/{id}")
    public Result getChlidData(@PathVariable Long id){
       List<Dict> list = dictService.getChlidData(id);
       return Result.ok(list);

    }
}
