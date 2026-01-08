package org.example.contractmanager.service;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.DepartmentPerformance;

import java.util.List;

/**
 * 部门业绩统计Service接口
 */
public interface DepartmentPerformanceService {

    /**
     * 查询指定部门的业绩统计
     * @param department 部门名称
     * @return 业绩统计列表
     */
    List<DepartmentPerformance> getPerformanceByDepartment(String department);

    /**
     * 查询指定年度的部门业绩统计
     * @param year 年度
     * @return 业绩统计列表
     */
    List<DepartmentPerformance> getPerformanceByYear(Integer year);

    /**
     * 分页查询部门业绩统计
     * @param pageQuery 分页参数
     * @return 分页结果
     */
    PageResultDTO<DepartmentPerformance> getPerformanceByPage(PageQueryDTO pageQuery);
}
