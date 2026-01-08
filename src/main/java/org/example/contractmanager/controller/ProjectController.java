package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.dto.ProjectDTO;
import org.example.contractmanager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(originPatterns = "*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public Result<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        boolean success = projectService.createProject(projectDTO);
        return success ? Result.success("创建成功", projectDTO) : Result.error("创建失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteProject(@PathVariable Long id) {
        boolean success = projectService.deleteProject(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @PutMapping("/{id}")
    public Result<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        projectDTO.setId(id);
        boolean success = projectService.updateProject(projectDTO);
        return success ? Result.success("更新成功", projectDTO) : Result.error("更新失败");
    }

    @GetMapping("/{id}")
    public Result<ProjectDTO> getProject(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return Result.success(project);
    }

    @GetMapping
    public Result<PageResultDTO<ProjectDTO>> getProjects(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        pageQuery.setKeyword(keyword);
        pageQuery.setStatus(status);
        
        PageResultDTO<ProjectDTO> pageResult = projectService.getProjectsByPage(pageQuery);
        return Result.success(pageResult);
    }

    @GetMapping("/delayed")
    public Result<List<ProjectDTO>> getDelayedProjects() {
        List<ProjectDTO> projects = projectService.getDelayedProjects();
        return Result.success(projects);
    }
}
