package com.ewolff.monolith.service;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.ItemDTO;

import java.util.Collection;
import java.util.List;

public interface CatalogService {

    double price(long itemId);

    Collection<ItemDTO> findAll();

    ItemDTO getOne(long itemId);

    ItemDTO save(ItemDTO item);

    void delete(long id);

    List<ItemDTO> search(String query);
}
