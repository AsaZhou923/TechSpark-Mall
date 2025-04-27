package com.xavi.mall.demo.service;

import com.xavi.mall.demo.dto.PmsBrandDto;
import com.xavi.mall.model.PmsBrand;

import java.util.List;

/**
 * DemoService接口
 * Created by xavier
 */
public interface DemoService {
    List<PmsBrand> listAllBrand();

    int createBrand(PmsBrandDto pmsBrandDto);

    int updateBrand(Long id, PmsBrandDto pmsBrandDto);

    int deleteBrand(Long id);

    List<PmsBrand> listBrand(int pageNum, int pageSize);

    PmsBrand getBrand(Long id);
}
