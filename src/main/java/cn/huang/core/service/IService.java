package cn.huang.core.service;

import cn.huang.core.result.PageResult;
import cn.huang.core.wrapper.delete.LambdaDeleteWrapper;
import cn.huang.core.wrapper.query.LambdaQueryWrapper;

import java.util.List;

/**
 * @author huangl
 * @description IService
 * @date 2023/9/27 17:53:22
 */
public interface IService<T> {

    List<T> queryList(LambdaQueryWrapper<T> lambdaQueryWrapper);

    PageResult<T> queryPage(Long current, Long size, LambdaQueryWrapper<T> lambdaQueryWrapper);

    Long queryCount(LambdaQueryWrapper<T> lambdaQueryWrapper);

    void insert(T entity);

    void insertBatch(List<T> entityList);

    void delete(LambdaDeleteWrapper<T> lambdaDeleteWrapper);

}
