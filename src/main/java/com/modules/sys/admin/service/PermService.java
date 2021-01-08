package com.modules.sys.admin.service;

import com.modules.sys.admin.model.dto.PermGetDto;
import com.modules.sys.admin.model.entity.Perm;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
public interface PermService {

    Perm findById(Long id) throws Exception;

    List<Perm> findAll() throws Exception;

    List<Perm> findPage(PermGetDto dto) throws Exception;

    int count(PermGetDto dto) throws Exception;

    boolean save(Perm perm) throws Exception;

    boolean delete(Long id) throws Exception;

}
