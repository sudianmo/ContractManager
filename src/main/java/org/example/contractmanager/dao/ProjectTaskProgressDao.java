package org.example.contractmanager.dao;

import org.example.contractmanager.entity.ProjectTaskProgress;
import java.util.List;

/**
 * 项目任务进度DAO接口
 * 查询ProjectTaskProgressView视图
 */
public interface ProjectTaskProgressDao {

    /**
     * 根据项目ID查询任务进度
     * @param projectId 项目ID
     * @return 任务进度列表
     */
    List<ProjectTaskProgress> selectByProjectId(Long projectId);

    /**
     * 查询所有项目任务进度
     * @return 任务进度列表
     */
    List<ProjectTaskProgress> selectAll();

    /**
     * 分页查询项目任务进度
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 任务进度列表
     */
    List<ProjectTaskProgress> selectByPage(int offset, int pageSize);

    /**
     * 统计总数
     * @return 总数
     */
    long count();
}
