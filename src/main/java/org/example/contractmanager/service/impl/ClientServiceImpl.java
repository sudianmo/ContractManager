package org.example.contractmanager.service.impl;

import org.example.contractmanager.dao.ClientDao;
import org.example.contractmanager.dto.ClientDTO;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.Client;
import org.example.contractmanager.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public boolean addClient(ClientDTO clientDTO) {
        Client client = convertToEntity(clientDTO);
        int result = clientDao.insert(client);
        return result > 0;
    }

    @Override
    public boolean deleteClient(Long id) {
        int result = clientDao.deleteById(id);
        return result > 0;
    }

    @Override
    public boolean updateClient(ClientDTO clientDTO) {
        Client client = convertToEntity(clientDTO);
        int result = clientDao.update(client);
        return result > 0;
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientDao.selectById(id);
        return client != null ? convertToDTO(client) : null;
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
        List<Client> clients = clientDao.selectByPage(offset, pageQuery.getPageSize());
        long total = clientDao.count();
        
        List<ClientDTO> records = clients.stream()
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

    private ClientDTO convertToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setClientName(client.getClientName());
        dto.setContactPerson(client.getContactPerson());
        dto.setContactPhone(client.getPhone());
        dto.setContactEmail(client.getEmail());
        dto.setAddress(client.getAddress());
        if (client.getRegistrationDate() != null) {
            dto.setCreateTime(client.getRegistrationDate().toString());
        }
        return dto;
    }

    private Client convertToEntity(ClientDTO dto) {
        Client client = new Client();
        if (dto.getId() != null) {
            client.setId(dto.getId());
        }
        client.setClientName(dto.getClientName());
        client.setContactPerson(dto.getContactPerson());
        client.setPhone(dto.getContactPhone());
        client.setEmail(dto.getContactEmail());
        client.setAddress(dto.getAddress());
        return client;
    }
}
