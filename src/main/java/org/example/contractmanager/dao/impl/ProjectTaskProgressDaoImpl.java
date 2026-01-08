package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ProjectTaskProgressDao;
import org.example.contractmanager.entity.ProjectTaskProgress;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * 项目任务进度DAO实现类
 * 使用JdbcTemplate查询视图ProjectTaskProgressView
 */
@Repository
public class ProjectTaskProgressDaoImpl implements ProjectTaskProgressDao {

    private final JdbcTemplate jdbcTemplate;

    public ProjectTaskProgressDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper - 映射视图查询结果到实体对象
     * 关键逻辑：将项目任务进度数据映射为ProjectTaskProgress对象
     * 注意：视图已简化，仅查询核心字段
     */
    private final RowMapper<ProjectTaskProgress> rowMapper = (rs, rowNum) -> {
        ProjectTaskProgress progress = new ProjectTaskProgress();
        
        // 任务信息（匹配ProjectTaskProgressView视图）
        long taskId = rs.getLong("TaskID");
        if (!rs.wasNull()) {
            progress.setTaskId(taskId);
        }
        
        long projectId = rs.getLong("ProjectID");
        if (!rs.wasNull()) {
            progress.setProjectId(projectId);
        }
        
        progress.setProjectName(rs.getString("ProjectName"));
        progress.setTaskName(rs.getString("TaskName"));
        progress.setTaskAssignee(rs.getString("TaskAssignee"));
        progress.setTaskStatus(rs.getString("TaskStatus"));
        
        int completionRate = rs.getInt("CompletionRate");
        if (!rs.wasNull()) {
            progress.setCompletionRate(completionRate);
        }
        
        return progress;
    };

    @Override
    public List<ProjectTaskProgress> selectByProjectId(Long projectId) {
        // 根据项目ID查询该项目的所有任务进度
        String sql = "SELECT * FROM ProjectTaskProgressView WHERE ProjectID = ? ORDER BY TaskID";
        return jdbcTemplate.query(sql, rowMapper, projectId);
    }

    @Override
    public List<ProjectTaskProgress> selectAll() {
        String sql = "SELECT * FROM ProjectTaskProgressView ORDER BY ProjectID DESC, TaskID";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<ProjectTaskProgress> selectByPage(int offset, int pageSize) {
        // SQL Server分页语法：按项目ID降序
        String sql = "SELECT * FROM ProjectTaskProgressView ORDER BY ProjectID DESC, TaskID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, rowMapper, offset, pageSize);
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM ProjectTaskProgressView";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}
