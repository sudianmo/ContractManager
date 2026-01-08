package org.example.contractmanager.dao;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.entity.Project;

import java.util.List;

/**
 * 项目数据访问接口
 */
public interface ProjectDao {
    
    List<Project> selectByPage(PageQueryDTO pageQuery);
    
    int countTotal(PageQueryDTO pageQuery);
    
    Project selectById(Long id);
    
    int insert(Project project);
    
    int update(Project project);
    
    int deleteById(Long id);
    
    List<Project> selectDelayedProjects();
    
    int countByCustomerId(Long customerId);
    
    int softDelete(Long id, Long operatorId);
    List<Project> selectDeleted();
    int restore(Long id);
}
