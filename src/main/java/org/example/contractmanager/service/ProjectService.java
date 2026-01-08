package org.example.contractmanager.service;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    
    PageResultDTO<ProjectDTO> getProjectsByPage(PageQueryDTO pageQuery);
    
    ProjectDTO getProjectById(Long id);
    
    boolean createProject(ProjectDTO projectDTO);
    
    boolean updateProject(ProjectDTO projectDTO);
    
    boolean deleteProject(Long id);
    
    List<ProjectDTO> getDelayedProjects();
}
