package org.example.contractmanager.service.impl;

import org.example.contractmanager.dao.ContractFullInfoDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ContractFullInfo;
import org.example.contractmanager.service.ContractFullInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 合同全量信息Service实现类
 * 业务逻辑层：调用DAO查询视图数据
 */
@Service
public class ContractFullInfoServiceImpl implements ContractFullInfoService {

    private final ContractFullInfoDao contractFullInfoDao;

    public ContractFullInfoServiceImpl(ContractFullInfoDao contractFullInfoDao) {
        this.contractFullInfoDao = contractFullInfoDao;
    }

    @Override
    public ContractFullInfo getContractFullInfoById(Long contractId) {
        // 直接返回DAO查询结果，无需额外业务逻辑
        return contractFullInfoDao.selectById(contractId);
    }

    @Override
    public PageResultDTO<ContractFullInfo> getContractFullInfoByPage(PageQueryDTO pageQuery) {
        // 计算偏移量：(页码-1) * 每页大小
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        List<ContractFullInfo> records = contractFullInfoDao.selectByPage(offset, pageQuery.getPageSize());
        long total = contractFullInfoDao.count();
        return new PageResultDTO<>(total, records);
    }
}
