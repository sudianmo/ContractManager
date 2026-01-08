package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ProjectTaskProgress;
import org.example.contractmanager.service.ProjectTaskProgressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目任务进度Controller
 * RESTful API: /api/projects/task-progress
 * 关键逻辑：提供两个接口 - 按项目查询和分页查询
 */
@RestController
@RequestMapping("/api/projects/task-progress")
@CrossOrigin(originPatterns = "*")
public class ProjectTaskProgressController {

    private final ProjectTaskProgressService projectTaskProgressService;

    public ProjectTaskProgressController(ProjectTaskProgressService projectTaskProgressService) {
        this.projectTaskProgressService = projectTaskProgressService;
    }

    /**
     * 根据项目ID查询任务进度
     * GET /api/projects/task-progress/project/{projectId}
     * @param projectId 项目ID
     * @return 任务进度列表
     */
    @GetMapping("/project/{projectId}")
    public Result<List<ProjectTaskProgress>> getTaskProgressByProject(@PathVariable Long projectId) {
        List<ProjectTaskProgress> progressList = projectTaskProgressService.getTaskProgressByProjectId(projectId);
        return Result.success(progressList);
    }

    /**
     * 分页查询项目任务进度
     * GET /api/projects/task-progress?pageNum=1&pageSize=10
     * @param pageNum 页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResultDTO<ProjectTaskProgress>> getTaskProgressList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        
        PageResultDTO<ProjectTaskProgress> result = projectTaskProgressService.getTaskProgressByPage(pageQuery);
        return Result.success(result);
    }
}
