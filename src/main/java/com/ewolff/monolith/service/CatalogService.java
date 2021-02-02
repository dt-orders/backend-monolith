package com.ewolff.monolith.service;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.ItemDTO;

import java.util.Collection;
import java.util.List;

public interface CatalogService {

    double price(Long itemId);

    Collection<ItemDTO> findAll();

    ItemDTO getOne(Long itemId);

    ItemDTO save(ItemDTO item);

    void delete(Long id);

    List<ItemDTO> search(String query);
}
