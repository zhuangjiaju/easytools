package com.github.zhuangjiaju.easytools.tools.base.wrapper.result;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 分页信息
 *
 * @param <T>
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> {
    /**
     * 数据信息
     */
    private List<T> data;

    /**
     * 分页编码
     */
    @Builder.Default
    private Integer pageNum = 1;

    /**
     * 分页大小
     */
    @Builder.Default
    private Integer pageSize = 10;

    /**
     * 总的大小
     */
    @Builder.Default
    private Long total = 0L;

    /**
     * 是否存在下一页
     */
    private Boolean hasNextPage;

    public Page(List<T> data, Long total, Long pageNum, Long pageSize) {
        this();
        this.data = data;
        this.total = total;
        if (pageNum != null) {
            this.pageNum = Math.toIntExact(pageNum);
        }
        if (pageSize != null) {
            this.pageSize = Math.toIntExact(pageSize);
        }
    }

    public Page(List<T> data, Long total, Integer pageNum, Integer pageSize) {
        this();
        this.data = data;
        this.total = total;
        if (pageNum != null) {
            this.pageNum = pageNum;
        }
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
    }

    public Boolean getHasNextPage() {
        if (hasNextPage == null) {
            hasNextPage = calculateHasNextPage();
        }
        return hasNextPage;
    }

    /**
     * 判断是否还有下一页
     * 根据分页大小来计算 防止total为空
     *
     * @return 是否还有下一页
     */
    public Boolean calculateHasNextPage() {
        // 存在分页大小 根据分页来计算
        if (total > 0) {
            return (long)pageSize * pageNum <= total;
        }
        // 没有数据 肯定没有下一页
        if (data == null || data.isEmpty()) {
            return false;
        }
        // 当前数量小于分页数量
        return data.size() >= pageSize;
    }
}
