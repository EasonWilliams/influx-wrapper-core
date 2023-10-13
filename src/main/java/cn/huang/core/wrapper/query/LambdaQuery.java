package cn.huang.core.wrapper.query;

import java.time.Instant;
import java.util.List;

/**
 * @author huangl
 * @description LambdaQuery
 * @date 2023/9/27 17:20:19
 */
public interface LambdaQuery<T> {

    LambdaQuery<T> range(Instant left, Instant right);

    LambdaQuery<T> eq(String key, Object val);

    LambdaQuery<T> ne(String key, Object val);

    LambdaQuery<T> gt(String key, Object val);

    LambdaQuery<T> ge(String key, Object val);

    LambdaQuery<T> lt(String key, Object val);

    LambdaQuery<T> le(String key, Object val);

    LambdaQuery<T> between(String key, Object leftVal, Object rightVal);

    LambdaQuery<T> notBetween(String key, Object leftVal, Object rightVal);

    LambdaQuery<T> like(String key, Object val);

    LambdaQuery<T> pivot();

    LambdaQuery<T> map(String key, String as);

    LambdaQuery<T> group(List<String> keys);

    LambdaQuery<T> window(String time);

    LambdaQuery<T> last();

    LambdaQuery<T> sort(List<String> keys, Boolean desc);

    LambdaQuery<T> count();

    LambdaQuery<T> limit(Long n, Long offset);

}
