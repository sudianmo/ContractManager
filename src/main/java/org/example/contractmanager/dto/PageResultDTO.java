package org.example.contractmanager.dto;

import java.util.List;

/**
 * 分页结果DTO
 * 用于返回分页查询结果给前端
 */
public class PageResultDTO<T> {
    private Long total;  // 总记录数
    private List<T> records;  // 当前页数据

    public PageResultDTO() {
    }

    public PageResultDTO(Long total, List<T> records) {
        this.total = total;
        this.records = records;
    }

    // Getters and Setters
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
