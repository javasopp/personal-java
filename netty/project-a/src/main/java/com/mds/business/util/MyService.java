package com.mds.business.util;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mds.business.common.message.Message;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MyService
 * @Description: 抽象出来定义全局service的方法。
 * @Author: Sopp
 * @Date: 2019/4/19 14:32
 **/
public interface MyService<T> {
    /**
     * 功能描述: 插入一条记录（选择字段，策略插入）
     *
     * @param entity: 插入的实体类
     * @return Message
     * @auther: Sopp
     * @date: 2019/4/19 14:32
     */
    Message save(T entity);


    /**
     * 功能描述: 插入（批量），该方法不适合 Oracle
     *
     * @param entityList: 插入的集合
     * @return: Message
     * @auther: Sopp
     * @date: 2019/4/19 14:32
     */
    Message saveBatch(Collection<T> entityList);


    /**
     * 功能描述: 插入（批量）
     *
     * @param entityList: 实体对象集合
     * @param batchSize:  插入批次数量
     * @return: Message
     * @auther: Sopp
     * @date: 2019/4/19 14:33
     */
    Message saveBatch(Collection<T> entityList, int batchSize);


    /**
     * 功能描述: 批量修改插入
     *
     * @param entityList: 实体对象集合
     * @param batchSize:  每次的数量
     * @return: Message
     * @auther: Sopp
     * @date: 2019/4/19 14:34
     */
    Message saveOrUpdateBatch(Collection<T> entityList, int batchSize);


    /**
     * 功能描述: 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:52
     */
//    Message removeByIds(Collection<? extends Serializable> idList) throws OperFailException;


    /**
     * 功能描述: 根据 ID 选择修改
     *
     * @param entity 实体对象
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:54
     */
    Message updateById(T entity);


    /**
     * 功能描述: 根据 whereEntity 条件，更新记录
     *
     * @param entity        实体对象
     * @param updateWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:55
     */
    Message update(T entity, Wrapper<T> updateWrapper);


    /**
     * 功能描述:  根据ID 批量更新
     *
     * @param entityList 实体对象集合
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:56
     */
    Message updateBatchById(Collection<T> entityList);


    /**
     * 功能描述: 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:56
     */
    Message updateBatchById(Collection<T> entityList, int batchSize);


    /**
     * 功能描述: TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:56
     */
    Message saveOrUpdate(T entity);


    /**
     * 功能描述: 根据 ID 查询
     *
     * @param id 主键ID
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:57
     */
    Message getById(Serializable id);


    /**
     * 功能描述: 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:57
     */
    List<T> listByIds(Collection<? extends Serializable> idList);


    /**
     * 功能描述: 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:57
     */
    List<T> listByMap(Map<String, Object> columnMap);


    /**
     * 功能描述: 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:58
     */
    Message getOne(Wrapper<T> queryWrapper);


    /**
     * 功能描述: 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:58
     */
    Message getMap(Wrapper<T> queryWrapper);


    /**
     * 功能描述: 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:59
     */
//    Message getObj(Wrapper<T> queryWrapper);


    /**
     * 功能描述: 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:59
     */
    Message count(Wrapper<T> queryWrapper);


    /**
     * 功能描述: 查询列表
     *
     * @param queryWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 14:59
     */
    List<T> list(Wrapper<T> queryWrapper);


    /**
     * 功能描述: 分页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 15:00
     */
    List<T> page(IPage<T> page, Wrapper<T> queryWrapper);


    /**
     * 功能描述: 查询列表
     *
     * @param queryWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 15:00
     */
    Message listMaps(Wrapper<T> queryWrapper);


    /**
     * 功能描述: 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 15:00
     */
    Message listObjs(Wrapper<T> queryWrapper);


    /**
     * 功能描述: 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类
     * @return Message
     * @auther Sopp
     * @date: 2019/4/19 15:00
     */
//    Message pageMaps(IPage<T> page, Wrapper<T> queryWrapper);
}
