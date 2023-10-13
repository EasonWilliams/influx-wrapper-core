package cn.huang.core;

import cn.huang.core.wrapper.delete.LambdaDeleteWrapper;
import cn.huang.core.wrapper.query.LambdaQueryWrapper;

/**
 * @author huangl
 * @description Wrappers
 * @date 2023/9/27 15:11:49
 */
public final class Wrappers {

    public static <T> LambdaQueryWrapper<T> lambdaQuery(Class<T> entityClass) {
        return new LambdaQueryWrapper<>(entityClass);
    }

    public static <T> LambdaDeleteWrapper<T> lambdaDelete(Class<T> entityClass) {
        return new LambdaDeleteWrapper<>(entityClass);
    }

}
