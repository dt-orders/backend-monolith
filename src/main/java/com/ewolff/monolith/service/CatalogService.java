package com.ewolff.monolith.service;

import com.ewolff.monolith.dto.ItemDTO;

import java.util.Collection;

public interface CatalogService {

    double price(long itemId);

    Collection<ItemDTO> findAll();

    ItemDTO getOne(long itemId);
}
