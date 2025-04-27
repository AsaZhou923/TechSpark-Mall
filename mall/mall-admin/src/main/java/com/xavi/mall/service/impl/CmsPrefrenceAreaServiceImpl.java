package com.xavi.mall.service.impl;

import com.xavi.mall.mapper.CmsPrefrenceAreaMapper;
import com.xavi.mall.model.CmsPrefrenceArea;
import com.xavi.mall.model.CmsPrefrenceAreaExample;
import com.xavi.mall.service.CmsPrefrenceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品优选管理Service实现类
 * Created by xavier
 */
@Service
public class CmsPrefrenceAreaServiceImpl implements CmsPrefrenceAreaService {
    @Autowired
    private CmsPrefrenceAreaMapper prefrenceAreaMapper;

    @Override
    public List<CmsPrefrenceArea> listAll() {
        return prefrenceAreaMapper.selectByExample(new CmsPrefrenceAreaExample());
    }
}
