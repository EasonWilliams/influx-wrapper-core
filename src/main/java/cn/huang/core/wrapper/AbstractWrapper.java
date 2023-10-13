package cn.huang.core.wrapper;

/**
 * @author huangl
 * @description AbstractWrapper
 * @date 2023/9/27 14:31:55
 */
public abstract class AbstractWrapper<T> {
    public StringBuffer FLUX = new StringBuffer();

    private final Class<T> entityClass;

    public AbstractWrapper(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

}
