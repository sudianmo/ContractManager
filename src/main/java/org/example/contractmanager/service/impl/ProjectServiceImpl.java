package org.example.contractmanager.service.impl;

import org.example.contractmanager.common.BusinessException;
import org.example.contractmanager.dao.ProjectDao;
import org.example.contractmanager.dao.ClientDao;
import org.example.contractmanager.dao.ContractDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.dto.ProjectDTO;
import org.example.contractmanager.entity.Project;
import org.example.contractmanager.entity.Customer;
import org.example.contractmanager.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ContractDao contractDao;

    @Override
    public PageResultDTO<ProjectDTO> getProjectsByPage(PageQueryDTO pageQuery) {
        List<Project> projects = projectDao.selectByPage(pageQuery);
        int total = projectDao.countTotal(pageQuery);

        List<ProjectDTO> records = projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResultDTO<>((long) total, records);
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectDao.selectById(id);
        return project != null ? convertToDTO(project) : null;
    }

    @Override
    public boolean createProject(ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);
        int result = projectDao.insert(project);
        if (result > 0) {
            projectDTO.setId(project.getId());
        }
        return result > 0;
    }

    @Override
    public boolean updateProject(ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);
        return projectDao.update(project) > 0;
    }

    @Override
    public boolean deleteProject(Long id) {
        // 软删除：直接标记删除，关联数据由IsDeleted过滤自动处理
        return projectDao.softDelete(id, 1L) > 0;
    }

    @Override
    public List<ProjectDTO> getDelayedProjects() {
        return projectDao.selectDelayedProjects().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        BeanUtils.copyProperties(project, dto);

        if (project.getCustomerId() != null) {
            Customer customer = clientDao.selectById(project.getCustomerId());
            if (customer != null) {
                dto.setCustomerName(customer.getCustomerName());
            }
        }

        return dto;
    }

    private Project convertToEntity(ProjectDTO dto) {
        Project project = new Project();
        BeanUtils.copyProperties(dto, project);
        return project;
    }
}
