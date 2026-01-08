package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ProductDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Product> selectByPage(PageQueryDTO pageQuery) {
        StringBuilder sql = new StringBuilder("SELECT ProductID as id, ProductName as productName, Specification as specification, UnitPrice as unitPrice, StockQuantity as stockQuantity, Category as category FROM Products WHERE IsDeleted = 0");
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
        String sql = "INSERT INTO Products (ProductName, Specification, UnitPrice, StockQuantity, Category) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getSpecification());
            ps.setBigDecimal(3, product.getUnitPrice());
            ps.setInt(4, product.getStockQuantity());
            ps.setString(5, product.getCategory());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            product.setId(key.longValue());
        }
        return 1;
    }

    @Override
    public int update(Product product) {
        String sql = "UPDATE Products SET ProductName = ?, Specification = ?, UnitPrice = ?, StockQuantity = ?, Category = ? WHERE ProductID = ?";
        return jdbcTemplate.update(sql,
                product.getProductName(),
                product.getSpecification(),
                product.getUnitPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getId());
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM Products WHERE ProductID = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    @Override
    public int softDelete(Long id, Long operatorId) {
        String sql = "UPDATE Products SET IsDeleted = 1, DeletedBy = ?, DeletedAt = GETDATE() WHERE ProductID = ?";
        return jdbcTemplate.update(sql, operatorId, id);
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
        String sql = "UPDATE Products SET IsDeleted = 0, DeletedBy = NULL, DeletedAt = NULL WHERE ProductID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
