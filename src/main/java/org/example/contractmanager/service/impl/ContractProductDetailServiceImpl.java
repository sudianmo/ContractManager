package org.example.contractmanager.service.impl;

import org.example.contractmanager.dao.ContractProductDetailDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ContractProductDetail;
import org.example.contractmanager.service.ContractProductDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 合同产品明细Service实现类
 * 业务逻辑层：调用DAO查询视图数据
 */
@Service
public class ContractProductDetailServiceImpl implements ContractProductDetailService {

    private final ContractProductDetailDao contractProductDetailDao;

    public ContractProductDetailServiceImpl(ContractProductDetailDao contractProductDetailDao) {
        this.contractProductDetailDao = contractProductDetailDao;
    }

    @Override
    public List<ContractProductDetail> getProductDetailsByContractId(Long contractId) {
        // 查询指定合同的所有产品明细
        return contractProductDetailDao.selectByContractId(contractId);
    }

    @Override
    public PageResultDTO<ContractProductDetail> getProductDetailsByPage(PageQueryDTO pageQuery) {
        // 计算偏移量：(页码-1) * 每页大小
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        List<ContractProductDetail> records = contractProductDetailDao.selectByPage(offset, pageQuery.getPageSize());
        long total = contractProductDetailDao.count();
        return new PageResultDTO<>(total, records);
    }
}
