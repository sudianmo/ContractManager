package org.example.contractmanager.dao.impl;

import org.example.contractmanager.common.BusinessException;
import org.example.contractmanager.dao.ProductDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Product> selectByPage(PageQueryDTO pageQuery) {
        StringBuilder sql = new StringBuilder(
                "SELECT ProductID as id, ProductName as productName, Specification as specification, UnitPrice as unitPrice, StockQuantity as stockQuantity, Category as category FROM Products WHERE IsDeleted = 0");
        List<Object> params = new ArrayList<>();

        if (pageQuery.getKeyword() != null && !pageQuery.getKeyword().isEmpty()) {
            sql.append(" AND (ProductName LIKE ? OR Category LIKE ?)");
            String keyword = "%" + pageQuery.getKeyword() + "%";
            params.add(keyword);
            params.add(keyword);
        }

        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        sql.append(" ORDER BY ProductID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(pageQuery.getPageSize());

        return jdbcTemplate.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(Product.class));
    }

    @Override
    public int countTotal(PageQueryDTO pageQuery) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Products WHERE IsDeleted = 0");
        List<Object> params = new ArrayList<>();

        if (pageQuery.getKeyword() != null && !pageQuery.getKeyword().isEmpty()) {
            sql.append(" AND (ProductName LIKE ? OR Category LIKE ?)");
            String keyword = "%" + pageQuery.getKeyword() + "%";
            params.add(keyword);
            params.add(keyword);
        }

        return jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
    }

    @Override
    public Product selectById(Long id) {
        String sql = "SELECT ProductID as id, ProductName as productName, Specification as specification, UnitPrice as unitPrice, StockQuantity as stockQuantity, Category as category FROM Products WHERE ProductID = ? AND IsDeleted = 0";
        List<Product> products = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), id);
        return products.isEmpty() ? null : products.get(0);
    }

    @Override
    public int insert(Product product) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_AddProduct")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ProductName", Types.NVARCHAR),
                        new SqlParameter("Specification", Types.NVARCHAR),
                        new SqlParameter("UnitPrice", Types.DECIMAL),
                        new SqlParameter("StockQuantity", Types.INTEGER),
                        new SqlParameter("Category", Types.NVARCHAR),
                        new SqlOutParameter("NewProductID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ProductName", product.getProductName())
                .addValue("Specification", product.getSpecification())
                .addValue("UnitPrice", product.getUnitPrice())
                .addValue("StockQuantity", product.getStockQuantity())
                .addValue("Category", product.getCategory());

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "创建产品失败");
        }

        Object newIdObj = out.get("NewProductID");
        if (newIdObj instanceof Number) {
            product.setId(((Number) newIdObj).longValue());
        }

        return 1;
    }

    @Override
    public int update(Product product) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_UpdateProduct")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ProductID", Types.BIGINT),
                        new SqlParameter("ProductName", Types.NVARCHAR),
                        new SqlParameter("Specification", Types.NVARCHAR),
                        new SqlParameter("UnitPrice", Types.DECIMAL),
                        new SqlParameter("StockQuantity", Types.INTEGER),
                        new SqlParameter("Category", Types.NVARCHAR),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ProductID", product.getId())
                .addValue("ProductName", product.getProductName())
                .addValue("Specification", product.getSpecification())
                .addValue("UnitPrice", product.getUnitPrice())
                .addValue("StockQuantity", product.getStockQuantity())
                .addValue("Category", product.getCategory());

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "更新产品失败");
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
                .withProcedureName("SP_SoftDeleteProduct")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ProductID", Types.BIGINT),
                        new SqlParameter("OperatorID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ProductID", id)
                .addValue("OperatorID", operatorId);

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "删除产品失败");
        }
        return 1;
    }

    @Override
    public List<Product> selectLowStock(Integer threshold) {
        String sql = "SELECT TOP 5 ProductID as id, ProductName as productName, Specification as specification, UnitPrice as unitPrice, StockQuantity as stockQuantity, Category as category FROM Products WHERE StockQuantity <= ? AND IsDeleted = 0 ORDER BY StockQuantity ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), threshold);
    }

    @Override
    public int countContractProductsByProductId(Long productId) {
        String sql = "SELECT COUNT(*) FROM ContractProducts WHERE ProductID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
        return count != null ? count : 0;
    }

    @Override
    public List<Product> selectDeleted() {
        String sql = "SELECT ProductID as id, ProductName as productName, Specification as specification, UnitPrice as unitPrice, StockQuantity as stockQuantity, Category as category FROM Products WHERE IsDeleted = 1 ORDER BY DeletedAt DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }

    @Override
    public int restore(Long id) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_RestoreProduct")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ProductID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ProductID", id);

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "恢复产品失败");
        }
        return 1;
    }
}
