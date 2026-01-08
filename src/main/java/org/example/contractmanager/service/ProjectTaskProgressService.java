package org.example.contractmanager.service;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ProjectTaskProgress;

import java.util.List;

/**
 * 项目任务进度Service接口
 */
public interface ProjectTaskProgressService {

    /**
     * 根据项目ID查询任务进度
     * @param projectId 项目ID
     * @return 任务进度列表
     */
    List<ProjectTaskProgress> getTaskProgressByProjectId(Long projectId);

    /**
     * 分页查询项目任务进度
     * @param pageQuery 分页参数
     * @return 分页结果
     */
    PageResultDTO<ProjectTaskProgress> getTaskProgressByPage(PageQueryDTO pageQuery);
}
