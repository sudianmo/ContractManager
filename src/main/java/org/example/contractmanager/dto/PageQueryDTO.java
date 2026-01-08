package org.example.contractmanager.dto;

/**
 * 分页查询参数DTO
 * 用于接收前端的分页查询请求
 */
public class PageQueryDTO {
    private Integer pageNum = 1;  // 页码，默认的1页
    private Integer pageSize = 10;  // 每页大小，默认10条
    private String keyword;  // 搜索关键字（可选）
    private String status;  // 合同状态（可选）
    private Long clientId;  // 客户ID（可选）
    private Long contractId;  // 合同ID（可选）
    private String sortField;  // 排序字段（可选）
    private String sortOrder;  // 排序方式：ASC/DESC（可选）

    public PageQueryDTO() {
    }

    // Getters and Setters
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
}
