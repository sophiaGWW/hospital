package com.example.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.example.yygh.model.cmn.Dict;
import com.example.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.example.yygh.cmn.config.DictListener;
import com.example.yygh.cmn.mapper.DictMapper;
import com.example.yygh.cmn.service.DictService;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @create 2022-10-04 19:06
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> getChlidData(Long id) {
        LambdaQueryWrapper<Dict> lqw = new LambdaQueryWrapper<>();
        lqw.eq(id != 0, Dict::getParentId, id);
        List<Dict> list = baseMapper.selectList(lqw);
        //向list集合每个dict对象中设置hasChildren
        for (Dict dict : list) {
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        }

        return list;
    }

    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
          // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
            for(Dict dict : dictList) {
                DictEeVo dictVo = new DictEeVo();
                BeanUtils.copyProperties(dict, dictVo, DictEeVo.class);
                dictVoList.add(dictVo);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //根据value和dicthode查询信息
    @Override
    public String getDictName(String dictCode, String value) {
        LambdaQueryWrapper<Dict> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.isEmpty(dictCode)){
            //查省市县情况
            lqw.eq(Dict::getValue,value);
            Dict dict = baseMapper.selectOne(lqw);
            return dict.getName();
        }
        //通过dict获取总分类对象
        lqw.eq(Dict::getDictCode,dictCode);
        Dict dictParent = baseMapper.selectOne(lqw);
        //获取所需对象的parentId
        Long parent_id = dictParent.getId();
        LambdaQueryWrapper<Dict> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(Dict::getParentId,parent_id).eq(Dict::getValue,value);
        //查出所需对象
        Dict dict = baseMapper.selectOne(lqw1);

        return dict.getName();
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id) {
        LambdaQueryWrapper<Dict> lqw = new LambdaQueryWrapper<>();
        lqw.eq(id != 0, Dict::getParentId, id);
        Integer count = baseMapper.selectCount(lqw);
        // 0>0    1>0
        return count > 0;

    }
}
