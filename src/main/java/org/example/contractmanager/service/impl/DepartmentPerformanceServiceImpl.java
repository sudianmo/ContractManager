package org.example.contractmanager.service.impl;

import org.example.contractmanager.dao.DepartmentPerformanceDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.DepartmentPerformance;
import org.example.contractmanager.service.DepartmentPerformanceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门业绩统计Service实现类
 * 业务逻辑层：调用DAO查询视图数据
 */
@Service
public class DepartmentPerformanceServiceImpl implements DepartmentPerformanceService {

    private final DepartmentPerformanceDao departmentPerformanceDao;

    public DepartmentPerformanceServiceImpl(DepartmentPerformanceDao departmentPerformanceDao) {
        this.departmentPerformanceDao = departmentPerformanceDao;
    }

    @Override
    public List<DepartmentPerformance> getPerformanceByDepartment(String department) {
        // 查询指定部门的业绩统计
        return departmentPerformanceDao.selectByDepartment(department);
    }

    @Override
    public List<DepartmentPerformance> getPerformanceByYear(Integer year) {
        // 查询指定年度的部门业绩统计
        return departmentPerformanceDao.selectByYear(year);
    }

    @Override
    public PageResultDTO<DepartmentPerformance> getPerformanceByPage(PageQueryDTO pageQuery) {
        // 计算偏移量：(页码-1) * 每页大小
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        List<DepartmentPerformance> records = departmentPerformanceDao.selectByPage(offset, pageQuery.getPageSize());
        long total = departmentPerformanceDao.count();
        return new PageResultDTO<>(total, records);
    }
}
