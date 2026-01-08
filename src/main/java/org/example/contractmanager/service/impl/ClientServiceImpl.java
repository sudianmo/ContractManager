package org.example.contractmanager.service.impl;

import org.example.contractmanager.common.BusinessException;
import org.example.contractmanager.dao.ClientDao;
import org.example.contractmanager.dao.ProjectDao;
import org.example.contractmanager.dto.ClientDTO;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.Customer;
import org.example.contractmanager.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;
    private final ProjectDao projectDao;

    public ClientServiceImpl(ClientDao clientDao, ProjectDao projectDao) {
        this.clientDao = clientDao;
        this.projectDao = projectDao;
    }

    @Override
    public boolean addClient(ClientDTO clientDTO) {
        Customer customer = convertToEntity(clientDTO);
        int result = clientDao.insert(customer);
        return result > 0;
    }

    @Override
    public boolean deleteClient(Long id) {
        // 软删除：直接标记删除，关联数据由IsDeleted过滤自动处理
        int result = clientDao.softDelete(id, 1L);
        return result > 0;
    }

    @Override
    public boolean updateClient(ClientDTO clientDTO) {
        Customer customer = convertToEntity(clientDTO);
        int result = clientDao.update(customer);
        return result > 0;
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Customer customer = clientDao.selectById(id);
        return customer != null ? convertToDTO(customer) : null;
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientDao.selectAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResultDTO<ClientDTO> getClientsByPage(PageQueryDTO pageQuery) {
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        List<Customer> customers = clientDao.selectByPage(offset, pageQuery.getPageSize());
        long total = clientDao.count();
        
        List<ClientDTO> records = customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResultDTO<>(total, records);
    }

    @Override
    public List<ClientDTO> searchClients(String keyword) {
        return clientDao.searchByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ClientDTO convertToDTO(Customer customer) {
        ClientDTO dto = new ClientDTO();
        dto.setId(customer.getCustomerId());
        dto.setClientName(customer.getCustomerName());
        dto.setContactPerson(customer.getContactPerson());
        dto.setContactPhone(customer.getPhone());
        dto.setContactEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());
        if (customer.getRegistrationDate() != null) {
            dto.setCreateTime(customer.getRegistrationDate().toString());
        }
        return dto;
    }

    private Customer convertToEntity(ClientDTO dto) {
        Customer customer = new Customer();
        if (dto.getId() != null) {
            customer.setCustomerId(dto.getId());
        }
        customer.setCustomerName(dto.getClientName());
        customer.setContactPerson(dto.getContactPerson());
        customer.setPhone(dto.getContactPhone());
        customer.setEmail(dto.getContactEmail());
        customer.setAddress(dto.getAddress());
        return customer;
    }
}
