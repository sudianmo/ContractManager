package org.example.contractmanager.service;

import org.example.contractmanager.dto.ClientDTO;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.Client;

import java.util.List;

/**
 * 客户Service接口
 * 定义客户相关的业务逻辑方法
 */
public interface ClientService {

    /**
     * 新增客户
     * @param clientDTO 客户DTO
     * @return 是否成功
     */
    boolean addClient(ClientDTO clientDTO);

    /**
     * 删除客户
     * @param id 客户ID
     * @return 是否成功
     */
    boolean deleteClient(Long id);

    /**
     * 更新客户
     * @param clientDTO 客户DTO
     * @return 是否成功
     */
    boolean updateClient(ClientDTO clientDTO);

    /**
     * 根据ID查询客户
     * @param id 客户ID
     * @return 客户DTO
     */
    ClientDTO getClientById(Long id);

    /**
     * 查询所有客户
     * @return 客户DTO列表
     */
    List<ClientDTO> getAllClients();

    /**
     * 分页查询客户
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResultDTO<ClientDTO> getClientsByPage(PageQueryDTO pageQuery);

    /**
     * 搜索客户
     * @param keyword 关键字
     * @return 客户DTO列表
     */
    List<ClientDTO> searchClients(String keyword);
}
