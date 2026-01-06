package org.example.contractmanager.service.impl;

import org.example.contractmanager.repository.ContractRepository;
import org.example.contractmanager.dto.ContractDTO;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.Contract;
import org.example.contractmanager.service.ContractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public boolean addContract(ContractDTO contractDTO) {
        Contract contract = convertToEntity(contractDTO);
        contractRepository.save(contract);
        return true;
    }

    @Override
    public boolean deleteContract(Long id) {
        contractRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateContract(ContractDTO contractDTO) {
        Contract contract = convertToEntity(contractDTO);
        contractRepository.save(contract);
        return true;
    }

    @Override
    public ContractDTO getContractById(Long id) {
        return contractRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<ContractDTO> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResultDTO<ContractDTO> getContractsByPage(PageQueryDTO pageQuery) {
        PageRequest pageRequest = PageRequest.of(
                pageQuery.getPageNum() - 1,
                pageQuery.getPageSize()
        );
        Page<Contract> page = contractRepository.findAll(pageRequest);
        
        List<ContractDTO> records = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResultDTO<>(page.getTotalElements(), records);
    }

    @Override
    public List<ContractDTO> getContractsByClientId(Long clientId) {
        return contractRepository.findByClientId(clientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDTO> getContractsByStatus(String status) {
        return contractRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDTO> searchContracts(String keyword) {
        return contractRepository.findByContractNoContainingOrContractNameContaining(keyword, keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ContractDTO convertToDTO(Contract contract) {
        ContractDTO dto = new ContractDTO();
        dto.setId(contract.getId());
        dto.setContractNo(contract.getContractNo());
        dto.setContractName(contract.getContractName());
        dto.setClientId(contract.getClientId());
        dto.setAmount(contract.getAmount());
        dto.setSignDate(contract.getSignDate());
        dto.setStartDate(contract.getStartDate());
        dto.setEndDate(contract.getEndDate());
        dto.setStatus(contract.getStatus());
        dto.setDescription(contract.getDescription());
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
        contract.setStartDate(dto.getStartDate());
        contract.setEndDate(dto.getEndDate());
        contract.setStatus(dto.getStatus());
        contract.setDescription(dto.getDescription());
        return contract;
    }
}
