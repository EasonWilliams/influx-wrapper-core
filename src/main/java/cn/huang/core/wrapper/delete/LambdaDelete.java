package cn.huang.core.wrapper.delete;

import java.time.OffsetDateTime;

/**
 * @author huangl
 * @description LambdaDelete
 * @date 2023/10/12 11:37:17
 */
public interface LambdaDelete<T> {
    LambdaDelete<T> range(OffsetDateTime start, OffsetDateTime end);
}
