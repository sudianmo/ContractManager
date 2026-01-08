package org.example.contractmanager.service.impl;

import org.example.contractmanager.dao.ProjectTaskProgressDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ProjectTaskProgress;
import org.example.contractmanager.service.ProjectTaskProgressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目任务进度Service实现类
 * 业务逻辑层：调用DAO查询视图数据
 */
@Service
public class ProjectTaskProgressServiceImpl implements ProjectTaskProgressService {

    private final ProjectTaskProgressDao projectTaskProgressDao;

    public ProjectTaskProgressServiceImpl(ProjectTaskProgressDao projectTaskProgressDao) {
        this.projectTaskProgressDao = projectTaskProgressDao;
    }

    @Override
    public List<ProjectTaskProgress> getTaskProgressByProjectId(Long projectId) {
        // 查询指定项目的所有任务进度
        return projectTaskProgressDao.selectByProjectId(projectId);
    }

    @Override
    public PageResultDTO<ProjectTaskProgress> getTaskProgressByPage(PageQueryDTO pageQuery) {
        // 计算偏移量：(页码-1) * 每页大小
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        List<ProjectTaskProgress> records = projectTaskProgressDao.selectByPage(offset, pageQuery.getPageSize());
        long total = projectTaskProgressDao.count();
        return new PageResultDTO<>(total, records);
    }
}
