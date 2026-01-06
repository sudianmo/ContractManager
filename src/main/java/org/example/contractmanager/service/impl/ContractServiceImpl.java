package org.example.contractmanager.service.impl;

import org.example.contractmanager.dao.ContractDao;
import org.example.contractmanager.dao.ClientDao;
import org.example.contractmanager.dto.ContractDTO;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.Contract;
import org.example.contractmanager.entity.Client;
import org.example.contractmanager.service.ContractService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractDao contractDao;
    private final ClientDao clientDao;

    public ContractServiceImpl(ContractDao contractDao, ClientDao clientDao) {
        this.contractDao = contractDao;
        this.clientDao = clientDao;
    }

    @Override
    public boolean addContract(ContractDTO contractDTO) {
        Contract contract = convertToEntity(contractDTO);
        int result = contractDao.insert(contract);
        return result > 0;
    }

    @Override
    public boolean deleteContract(Long id) {
        int result = contractDao.deleteById(id);
        return result > 0;
    }

    @Override
    public boolean updateContract(ContractDTO contractDTO) {
        Contract contract = convertToEntity(contractDTO);
        int result = contractDao.update(contract);
        return result > 0;
    }

    @Override
    public ContractDTO getContractById(Long id) {
        Contract contract = contractDao.selectById(id);
        return contract != null ? convertToDTO(contract) : null;
    }

    @Override
    public List<ContractDTO> getAllContracts() {
        return contractDao.selectAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResultDTO<ContractDTO> getContractsByPage(PageQueryDTO pageQuery) {
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        List<Contract> contracts;
        long total;
        
        // 根据筛选条件调用不同的DAO方法
        if (pageQuery.getClientId() != null) {
            contracts = contractDao.selectByClientId(pageQuery.getClientId());
            total = contracts.size();
        } else if (pageQuery.getStatus() != null && !pageQuery.getStatus().isEmpty()) {
            contracts = contractDao.selectByStatus(pageQuery.getStatus());
            total = contracts.size();
        } else if (pageQuery.getKeyword() != null && !pageQuery.getKeyword().isEmpty()) {
            contracts = contractDao.searchByKeyword(pageQuery.getKeyword());
            total = contracts.size();
        } else {
            contracts = contractDao.selectByPage(offset, pageQuery.getPageSize());
            total = contractDao.count();
        }
        
        // 分页截取（对于筛选结果）
        if (pageQuery.getClientId() != null || pageQuery.getStatus() != null || pageQuery.getKeyword() != null) {
            int start = Math.min(offset, contracts.size());
            int end = Math.min(offset + pageQuery.getPageSize(), contracts.size());
            contracts = contracts.subList(start, end);
        }
            
        List<ContractDTO> records = contracts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            
        return new PageResultDTO<>(total, records);
    }

    @Override
    public List<ContractDTO> getContractsByClientId(Long clientId) {
        return contractDao.selectByClientId(clientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDTO> getContractsByStatus(String status) {
        return contractDao.selectByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDTO> searchContracts(String keyword) {
        return contractDao.searchByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ContractDTO convertToDTO(Contract contract) {
        ContractDTO dto = new ContractDTO();
        dto.setId(contract.getId());
        dto.setContractNo(contract.getContractNo());
        dto.setContractName(contract.getContractName());
        dto.setClientId(contract.getClientId());
        
        // 查询客户名称
        if (contract.getClientId() != null) {
            Client client = clientDao.selectById(contract.getClientId());
            if (client != null) {
                dto.setClientName(client.getClientName());
            }
        }
        
        dto.setAmount(contract.getAmount());
        dto.setSignDate(contract.getSignDate());
        dto.setEndDate(contract.getEndDate());
        dto.setStatus(contract.getStatus());
        dto.setRemark(contract.getDescription());
        return dto;
    }

    private Contract convertToEntity(ContractDTO dto) {
        Contract contract = new Contract();
        if (dto.getId() != null) {
            contract.setId(dto.getId());
        }
        contract.setContractNo(dto.getContractNo());
        contract.setContractName(dto.getContractName());
        contract.setClientId(dto.getClientId());
        contract.setAmount(dto.getAmount());
        contract.setSignDate(dto.getSignDate());
        contract.setEndDate(dto.getEndDate());
        contract.setStatus(dto.getStatus());
        contract.setDescription(dto.getRemark());
        return contract;
    }
}
