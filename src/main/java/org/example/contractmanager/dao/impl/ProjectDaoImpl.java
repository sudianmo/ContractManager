package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ProjectDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Project> selectByPage(PageQueryDTO pageQuery) {
        StringBuilder sql = new StringBuilder("SELECT ProjectID as id, ProjectName as projectName, CustomerID as customerId, StartDate as startDate, EndDate as endDate, Budget as budget, Status as status, Description as description FROM Projects WHERE IsDeleted = 0");
        List<Object> params = new ArrayList<>();

        if (pageQuery.getKeyword() != null && !pageQuery.getKeyword().isEmpty()) {
            sql.append(" AND ProjectName LIKE ?");
            params.add("%" + pageQuery.getKeyword() + "%");
        }

        if (pageQuery.getStatus() != null && !pageQuery.getStatus().isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(pageQuery.getStatus());
        }

        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        sql.append(" ORDER BY ProjectID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(pageQuery.getPageSize());

        return jdbcTemplate.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(Project.class));
    }

    @Override
    public int countTotal(PageQueryDTO pageQuery) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Projects WHERE IsDeleted = 0");
        List<Object> params = new ArrayList<>();

        if (pageQuery.getKeyword() != null && !pageQuery.getKeyword().isEmpty()) {
            sql.append(" AND ProjectName LIKE ?");
            params.add("%" + pageQuery.getKeyword() + "%");
        }

        if (pageQuery.getStatus() != null && !pageQuery.getStatus().isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(pageQuery.getStatus());
        }

        return jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
    }

    @Override
    public Project selectById(Long id) {
        String sql = "SELECT ProjectID as id, ProjectName as projectName, CustomerID as customerId, StartDate as startDate, EndDate as endDate, Budget as budget, Status as status, Description as description FROM Projects WHERE ProjectID = ? AND IsDeleted = 0";
        List<Project> projects = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Project.class), id);
        return projects.isEmpty() ? null : projects.get(0);
    }

    @Override
    public int insert(Project project) {
        String sql = "INSERT INTO Projects (ProjectName, CustomerID, StartDate, EndDate, Budget, Status, Description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getProjectName());
            ps.setLong(2, project.getCustomerId());
            ps.setDate(3, project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null);
            ps.setDate(4, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            ps.setBigDecimal(5, project.getBudget());
            ps.setString(6, project.getStatus());
            ps.setString(7, project.getDescription());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            project.setId(key.longValue());
        }
        return 1;
    }

    @Override
    public int update(Project project) {
        String sql = "UPDATE Projects SET ProjectName = ?, CustomerID = ?, StartDate = ?, EndDate = ?, Budget = ?, Status = ?, Description = ? WHERE ProjectID = ?";
        return jdbcTemplate.update(sql,
                project.getProjectName(),
                project.getCustomerId(),
                project.getStartDate(),
                project.getEndDate(),
                project.getBudget(),
                project.getStatus(),
                project.getDescription(),
                project.getId());
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM Projects WHERE ProjectID = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    @Override
    public int softDelete(Long id, Long operatorId) {
        String sql = "UPDATE Projects SET IsDeleted = 1, DeletedBy = ?, DeletedAt = GETDATE() WHERE ProjectID = ?";
        return jdbcTemplate.update(sql, operatorId, id);
    }

    @Override
    public List<Project> selectDelayedProjects() {
        String sql = "SELECT TOP 5 ProjectID as id, ProjectName as projectName, CustomerID as customerId, StartDate as startDate, EndDate as endDate, Budget as budget, Status as status, Description as description FROM Projects WHERE EndDate < GETDATE() AND Status != 'Completed' AND IsDeleted = 0 ORDER BY EndDate ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Project.class));
    }
    
    @Override
    public int countByCustomerId(Long customerId) {
        String sql = "SELECT COUNT(*) FROM Projects WHERE CustomerID = ? AND IsDeleted = 0";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId);
        return count != null ? count : 0;
    }
    
    @Override
    public List<Project> selectDeleted() {
        String sql = "SELECT ProjectID as id, ProjectName as projectName, CustomerID as customerId, StartDate as startDate, EndDate as endDate, Budget as budget, Status as status, Description as description FROM Projects WHERE IsDeleted = 1 ORDER BY DeletedAt DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Project.class));
    }
    
    @Override
    public int restore(Long id) {
        String sql = "UPDATE Projects SET IsDeleted = 0, DeletedBy = NULL, DeletedAt = NULL WHERE ProjectID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
