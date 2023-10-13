package cn.huang.core.result;

import java.util.List;

/**
 * @author huangl
 * @description Page Result
 * @date 2023/9/28 14:49:43
 */
public class PageResult<T> {
    private Long total;
    private long current;
    private long size;
    private List<T> records;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
