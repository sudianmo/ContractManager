package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.DepartmentPerformance;
import org.example.contractmanager.service.DepartmentPerformanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门业绩统计Controller
 * RESTful API: /api/departments/performance
 * 关键逻辑：提供三个接口 - 按部门查询、按年度查询和分页查询
 */
@RestController
@RequestMapping("/api/departments/performance")
@CrossOrigin(originPatterns = "*")
public class DepartmentPerformanceController {

    private final DepartmentPerformanceService departmentPerformanceService;

    public DepartmentPerformanceController(DepartmentPerformanceService departmentPerformanceService) {
        this.departmentPerformanceService = departmentPerformanceService;
    }

    /**
     * 查询指定部门的业绩统计
     * GET /api/departments/performance/department/{department}
     * @param department 部门名称
     * @return 业绩统计列表
     */
    @GetMapping("/department/{department}")
    public Result<List<DepartmentPerformance>> getPerformanceByDepartment(@PathVariable String department) {
        List<DepartmentPerformance> performanceList = departmentPerformanceService.getPerformanceByDepartment(department);
        return Result.success(performanceList);
    }

    /**
     * 查询指定年度的部门业绩统计
     * GET /api/departments/performance/year/{year}
     * @param year 年度
     * @return 业绩统计列表
     */
    @GetMapping("/year/{year}")
    public Result<List<DepartmentPerformance>> getPerformanceByYear(@PathVariable Integer year) {
        List<DepartmentPerformance> performanceList = departmentPerformanceService.getPerformanceByYear(year);
        return Result.success(performanceList);
    }

    /**
     * 分页查询部门业绩统计
     * GET /api/departments/performance?pageNum=1&pageSize=10
     * @param pageNum 页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResultDTO<DepartmentPerformance>> getPerformanceList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        
        PageResultDTO<DepartmentPerformance> result = departmentPerformanceService.getPerformanceByPage(pageQuery);
        return Result.success(result);
    }
}
