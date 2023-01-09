package com.sopp.all.service.impl;

import com.sopp.all.entity.Test;
import com.sopp.all.mapper.TestMapper;
import com.sopp.all.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Sopp
 * @since 2021-06-30
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

}
