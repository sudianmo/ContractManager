package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.PaymentDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentDaoImpl implements PaymentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Payment> selectByPage(PageQueryDTO pageQuery) {
        StringBuilder sql = new StringBuilder(
                "SELECT PaymentID as id, ContractID as contractId, PaymentAmount as paymentAmount, PaymentDate as paymentDate, PaymentMethod as paymentMethod, PaymentStatus as paymentStatus, Remarks as remarks FROM Payments WHERE IsDeleted = 0");
        List<Object> params = new ArrayList<>();

        if (pageQuery.getContractId() != null) {
            sql.append(" AND ContractID = ?");
            params.add(pageQuery.getContractId());
        }

        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        sql.append(" ORDER BY PaymentID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(pageQuery.getPageSize());

        return jdbcTemplate.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(Payment.class));
    }

    @Override
    public int countTotal(PageQueryDTO pageQuery) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Payments WHERE IsDeleted = 0");
        List<Object> params = new ArrayList<>();

        if (pageQuery.getContractId() != null) {
            sql.append(" AND ContractID = ?");
            params.add(pageQuery.getContractId());
        }

        return jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
    }

    @Override
    public Payment selectById(Long id) {
        String sql = "SELECT PaymentID as id, ContractID as contractId, PaymentAmount as paymentAmount, PaymentDate as paymentDate, PaymentMethod as paymentMethod, PaymentStatus as paymentStatus, Remarks as remarks FROM Payments WHERE PaymentID = ? AND IsDeleted = 0";
        List<Payment> payments = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), id);
        return payments.isEmpty() ? null : payments.get(0);
    }

    @Override
    public int insert(Payment payment) {
        String sql = "INSERT INTO Payments (ContractID, PaymentAmount, PaymentDate, PaymentMethod, PaymentStatus, Remarks) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, payment.getContractId());
            ps.setBigDecimal(2, payment.getPaymentAmount());
            ps.setDate(3, payment.getPaymentDate() != null ? Date.valueOf(payment.getPaymentDate()) : null);
            ps.setString(4, payment.getPaymentMethod());
            ps.setString(5, payment.getPaymentStatus());
            ps.setString(6, payment.getRemarks());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            payment.setId(key.longValue());
        }
        return 1;
    }

    @Override
    public int update(Payment payment) {
        String sql = "UPDATE Payments SET ContractID = ?, PaymentAmount = ?, PaymentDate = ?, PaymentMethod = ?, PaymentStatus = ?, Remarks = ? WHERE PaymentID = ?";
        return jdbcTemplate.update(sql,
                payment.getContractId(),
                payment.getPaymentAmount(),
                payment.getPaymentDate(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getRemarks(),
                payment.getId());
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM Payments WHERE PaymentID = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Payment> selectByContractId(Long contractId) {
        String sql = "SELECT PaymentID as id, ContractID as contractId, PaymentAmount as paymentAmount, PaymentDate as paymentDate, PaymentMethod as paymentMethod, PaymentStatus as paymentStatus, Remarks as remarks FROM Payments WHERE ContractID = ? AND IsDeleted = 0 ORDER BY PaymentDate DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), contractId);
    }

    @Override
    public int countByContractId(Long contractId) {
        String sql = "SELECT COUNT(*) FROM Payments WHERE ContractID = ? AND IsDeleted = 0";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, contractId);
        return count != null ? count : 0;
    }

    @Override
    public BigDecimal sumTotalPayments() {
        String sql = "SELECT ISNULL(SUM(PaymentAmount), 0) FROM Payments WHERE PaymentStatus = 'Completed' AND IsDeleted = 0";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class);
    }

    @Override
    public int softDelete(Long id, Long operatorId) {
        String sql = "UPDATE Payments SET IsDeleted = 1, DeletedBy = ?, DeletedAt = GETDATE() WHERE PaymentID = ?";
        return jdbcTemplate.update(sql, operatorId, id);
    }

    @Override
    public List<Payment> selectDeleted() {
        String sql = "SELECT PaymentID as id, ContractID as contractId, PaymentAmount as paymentAmount, PaymentDate as paymentDate, PaymentMethod as paymentMethod, PaymentStatus as paymentStatus, Remarks as remarks FROM Payments WHERE IsDeleted = 1 ORDER BY DeletedAt DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class));
    }

    @Override
    public int restore(Long id) {
        String sql = "UPDATE Payments SET IsDeleted = 0, DeletedBy = NULL, DeletedAt = NULL WHERE PaymentID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
