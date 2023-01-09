package com.mds.business.util;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.mds.business.common.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MyServiceImpl
 * @Description: 封装service实现类代码。
 * @Author: Sopp
 * @Date: 2019/4/19 9:34
 **/
@SuppressWarnings("unchecked")
@Slf4j
public class MyServiceImpl<M extends BaseMapper<T>, T> implements MyService<T> {


    /**
     * 注入对应mapper
     */
    @Autowired
    protected M baseMapper;


    /**
     * 判断数据库操作是否成功
     * 注意！！ 该方法为 Integer 判断，不可传入 int 基本类型
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected static boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    /**
     * 批量操作 SqlSession
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }

    /**
     * 获取SqlStatement
     *
     * @param sqlMethod
     * @return
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message save(T entity) {
        if (MyServiceImpl.retBool(baseMapper.insert(entity)) == true) {
            return Message.success();
        } else {
            return null;
        }
    }

    /**
     * 插入（批量），该方法不适合 Oracle
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message saveBatch(Collection<T> entityList) {
        return saveBatch(entityList, 30);
    }

    /**
     * 批量插入
     *
     * @param entityList
     * @param batchSize
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message saveBatch(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            log.error("插入集合不能为空，请重试。");
        } else {
            try (SqlSession batchSqlSession = sqlSessionBatch()) {
                int i = 0;
                String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
                for (T anEntityList : entityList) {
                    batchSqlSession.insert(sqlStatement, anEntityList);
                    if (i >= 1 && i % batchSize == 0) {
                        batchSqlSession.flushStatements();
                    }
                    i++;
                }
                batchSqlSession.flushStatements();
            } catch (Throwable e) {
                throw ExceptionUtils.mpe("报错了，不能执行批量插入的方法，原因是：", e);
            }
        }
        return Message.success();
    }


    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     * @param batchSize  每次的数量
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            log.error("不能传递空的集合，请重试");
            throw new IllegalArgumentException();
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            for (T anEntityList : entityList) {
                saveOrUpdate(anEntityList);
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            log.error("不能执行saveOrUpdateBatch方法，原因是：", e);
            throw ExceptionUtils.mpe("Error: Cannot execute saveOrUpdateBatch Method. Cause", e);
        }
        return Message.success();
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message updateById(T entity) {
        if (MyServiceImpl.retBool(baseMapper.updateById(entity)) == true) {
            return Message.success();
        } else {
            return null;
        }
    }

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity        实体对象
     * @param updateWrapper 实体对象封装操作类
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message update(T entity, Wrapper<T> updateWrapper) {
        if (MyServiceImpl.retBool(baseMapper.update(entity, updateWrapper)) == true) {
            return Message.success();
        } else {
            return null;
        }
    }

    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message updateBatchById(Collection<T> entityList) {
        return updateBatchById(entityList, 30);
    }

    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message updateBatchById(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            log.error("不能传递空的集合，请重试");
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            String sqlStatement = sqlStatement(SqlMethod.UPDATE_BY_ID);
            for (T anEntityList : entityList) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            log.error("不能执行updateBatchById方法，原因是：", e);
            throw ExceptionUtils.mpe("Error: Cannot execute updateBatchById Method. Cause", e);
        }
        return Message.success();
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message saveOrUpdate(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return save(entity);
                } else {
                    /*
                     * 更新成功直接返回，失败执行插入逻辑
                     */
                    if (updateById(entity).getCode() == 0) {
                        return updateById(entity);
                    } else {
                        return save(entity);
                    }
                }
            } else {
                log.error("错误：不能执行。因为找不到对应实体类的@TableId注解。");
                throw ExceptionUtils.mpe("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return null;
    }

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    @Override
    public Message getById(Serializable id) {
        T t = baseMapper.selectById(id);
        if (t == null) {
            return Message.noData();
        } else {
            return Message.success(t);
        }
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     */
    @Override
    public List<T> listByIds(Collection<? extends Serializable> idList) {
        if (idList.isEmpty()) {
            return null;
        }
        List<T> list = baseMapper.selectBatchIds(idList);
        return returnListCheck(list);
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    @Override
    public List<T> listByMap(Map<String, Object> columnMap) {
        if (columnMap.isEmpty()) {
            return null;
        }
        List<T> list = baseMapper.selectByMap(columnMap);
        return returnListCheck(list);
    }

    /**
     * 校验list是否为空，统一封装对应代码。
     *
     * @return
     */
    private List<T> returnListCheck(List<T> list) {
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类
     */
    @Override
    public Message getOne(Wrapper<T> queryWrapper) {
        List<T> list = baseMapper.selectList(queryWrapper);
        if (list.size() > 1) {
            log.error("你个莽卡，怎么有这么多结果信息。");
            return Message.error("查询错误，存在多个结果。");
        } else {
            return null != list.get(0) ? Message.success(list.get(0)) : Message.fail();
        }
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类
     */
    @Override
    public Message getMap(Wrapper<T> queryWrapper) {
        List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);
        if (list.isEmpty()) {
            return Message.noData();
        } else {
            return Message.success(list);
        }
    }


    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类
     */
    @Override
    public Message count(Wrapper<T> queryWrapper) {
        int count = baseMapper.selectCount(queryWrapper);
        return Message.success(count);
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类
     */
    @Override
    public List<T> list(Wrapper<T> queryWrapper) {
        List<T> list = baseMapper.selectList(queryWrapper);
        return returnListCheck(list);
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类
     */
    @Override
    public List<T> page(IPage<T> page, Wrapper<T> queryWrapper) {
        IPage<T> selectPage = baseMapper.selectPage(page, queryWrapper);
        List<T> list = selectPage.getRecords();
        return returnListCheck(list);
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类
     */
    @Override
    public Message listMaps(Wrapper<T> queryWrapper) {
        List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);
        if (list.isEmpty()) {
            return Message.noData();
        } else {
            return Message.success(list);
        }
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类
     */
    @Override
    public Message listObjs(Wrapper<T> queryWrapper) {
        List<Object> list = baseMapper.selectObjs(queryWrapper);
        if (list.isEmpty()) {
            return Message.noData();
        } else {
            return Message.success(list);
        }
    }

//    /**
//     * 翻页查询
//     *
//     * @param page         翻页对象
//     * @param queryWrapper 实体对象封装操作类
//     */
//    @Override
//    public Message pageMaps(IPage<T> page, Wrapper<T> queryWrapper) {
//        IPage<Map<String, Object>> iPage = baseMapper.selectMapsPage(page, queryWrapper);
//        List<Map<String, Object>> list = iPage.getRecords();
//        if (list.isEmpty()) {
//            return Message.noData();
//        } else {
//            return Message.success(list);
//        }
//    }
}
