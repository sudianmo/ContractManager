package org.example.contractmanager.dao;

import org.example.contractmanager.entity.DepartmentPerformance;
import java.util.List;

/**
 * 部门业绩统计DAO接口
 * 查询DepartmentPerformanceView视图
 */
public interface DepartmentPerformanceDao {

    /**
     * 查询指定部门的业绩统计
     * @param department 部门名称
     * @return 业绩统计列表（按年度）
     */
    List<DepartmentPerformance> selectByDepartment(String department);

    /**
     * 查询指定年度的部门业绩统计
     * @param year 年度
     * @return 业绩统计列表
     */
    List<DepartmentPerformance> selectByYear(Integer year);

    /**
     * 查询所有部门业绩统计
     * @return 业绩统计列表
     */
    List<DepartmentPerformance> selectAll();

    /**
     * 分页查询部门业绩统计
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 业绩统计列表
     */
    List<DepartmentPerformance> selectByPage(int offset, int pageSize);

    /**
     * 统计总数
     * @return 总数
     */
    long count();
}
