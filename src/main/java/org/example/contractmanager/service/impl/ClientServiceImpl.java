package org.example.contractmanager.service.impl;

import org.example.contractmanager.repository.ClientRepository;
import org.example.contractmanager.dto.ClientDTO;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.Client;
import org.example.contractmanager.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean addClient(ClientDTO clientDTO) {
        Client client = convertToEntity(clientDTO);
        clientRepository.save(client);
        return true;
    }

    @Override
    public boolean deleteClient(Long id) {
        clientRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateClient(ClientDTO clientDTO) {
        Client client = convertToEntity(clientDTO);
        clientRepository.save(client);
        return true;
    }

    @Override
    public ClientDTO getClientById(Long id) {
        return clientRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResultDTO<ClientDTO> getClientsByPage(PageQueryDTO pageQuery) {
        PageRequest pageRequest = PageRequest.of(
                pageQuery.getPageNum() - 1, 
                pageQuery.getPageSize()
        );
        Page<Client> page = clientRepository.findAll(pageRequest);
        
        List<ClientDTO> records = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResultDTO<>(page.getTotalElements(), records);
    }

    @Override
    public List<ClientDTO> searchClients(String keyword) {
        return clientRepository.findByClientNameContainingOrContactPersonContaining(keyword, keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ClientDTO convertToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setClientName(client.getClientName());
        dto.setContactPerson(client.getContactPerson());
        dto.setPhone(client.getPhone());
        dto.setEmail(client.getEmail());
        dto.setAddress(client.getAddress());
        dto.setCompanyType(client.getCompanyType());
        dto.setCreditLevel(client.getCreditLevel());
        return dto;
    }

    private Client convertToEntity(ClientDTO dto) {
        Client client = new Client();
        if (dto.getId() != null) {
            client.setId(dto.getId());
        }
        client.setClientName(dto.getClientName());
        client.setContactPerson(dto.getContactPerson());
        client.setPhone(dto.getPhone());
        client.setEmail(dto.getEmail());
        client.setAddress(dto.getAddress());
        client.setCompanyType(dto.getCompanyType());
        client.setCreditLevel(dto.getCreditLevel());
        return client;
    }
}
