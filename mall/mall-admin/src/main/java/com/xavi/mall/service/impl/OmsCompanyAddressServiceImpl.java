package com.xavi.mall.service.impl;

import com.xavi.mall.mapper.OmsCompanyAddressMapper;
import com.xavi.mall.model.OmsCompanyAddress;
import com.xavi.mall.model.OmsCompanyAddressExample;
import com.xavi.mall.service.OmsCompanyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址管理Service实现类
 * Created by xavier
 */
@Service
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {
    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;
    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
