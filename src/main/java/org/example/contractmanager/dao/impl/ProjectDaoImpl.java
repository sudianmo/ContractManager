package org.example.contractmanager.dao.impl;

import org.example.contractmanager.common.BusinessException;
import org.example.contractmanager.dao.ProjectDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Project> selectByPage(PageQueryDTO pageQuery) {
        StringBuilder sql = new StringBuilder(
                "SELECT ProjectID as id, ProjectName as projectName, CustomerID as customerId, StartDate as startDate, EndDate as endDate, Budget as budget, Status as status, Description as description FROM Projects WHERE IsDeleted = 0");
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
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_AddProject")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ProjectName", Types.NVARCHAR),
                        new SqlParameter("CustomerID", Types.BIGINT),
                        new SqlParameter("StartDate", Types.DATE),
                        new SqlParameter("EndDate", Types.DATE),
                        new SqlParameter("Budget", Types.DECIMAL),
                        new SqlParameter("Status", Types.VARCHAR),
                        new SqlParameter("Description", Types.NVARCHAR),
                        new SqlOutParameter("NewProjectID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ProjectName", project.getProjectName())
                .addValue("CustomerID", project.getCustomerId())
                .addValue("StartDate", project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null)
                .addValue("EndDate", project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null)
                .addValue("Budget", project.getBudget())
                .addValue("Status", project.getStatus())
                .addValue("Description", project.getDescription());

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");

        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "创建项目失败");
        }

        Object newIdObj = out.get("NewProjectID");
        if (newIdObj instanceof Number) {
            project.setId(((Number) newIdObj).longValue());
        }

        return 1;
    }

    @Override
    public int update(Project project) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_UpdateProject")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ProjectID", Types.BIGINT),
                        new SqlParameter("ProjectName", Types.NVARCHAR),
                        new SqlParameter("CustomerID", Types.BIGINT),
                        new SqlParameter("StartDate", Types.DATE),
                        new SqlParameter("EndDate", Types.DATE),
                        new SqlParameter("Budget", Types.DECIMAL),
                        new SqlParameter("Status", Types.VARCHAR),
                        new SqlParameter("Description", Types.NVARCHAR),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ProjectID", project.getId())
                .addValue("ProjectName", project.getProjectName())
                .addValue("CustomerID", project.getCustomerId())
                .addValue("StartDate", project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null)
                .addValue("EndDate", project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null)
                .addValue("Budget", project.getBudget())
                .addValue("Status", project.getStatus())
                .addValue("Description", project.getDescription());

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "更新项目失败");
        }
        return 1;
    }

    @Override
    public int deleteById(Long id) {
        return softDelete(id, 1L);
    }

    @Override
    public int softDelete(Long id, Long operatorId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_SoftDeleteProject")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ProjectID", Types.BIGINT),
                        new SqlParameter("OperatorID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ProjectID", id)
                .addValue("OperatorID", operatorId);

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "删除项目失败");
        }
        return 1;
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
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_RestoreProject")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ProjectID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ProjectID", id);

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "恢复项目失败");
        }
        return 1;
    }
}
