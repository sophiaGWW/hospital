package yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @create 2022-09-30 10:39
 */
public interface DictService extends IService<Dict> {
    List<Dict> getChlidData(Long id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
}
