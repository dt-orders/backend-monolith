package com.ewolff.monolith.service.impl;

import com.ewolff.monolith.dto.ItemDTO;
import com.ewolff.monolith.persistence.domain.Item;
import com.ewolff.monolith.persistence.repository.ItemRepository;
import com.ewolff.monolith.service.CatalogService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CatalogServiceImpl implements CatalogService {

    private ItemRepository itemRepo;

    public CatalogServiceImpl(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @Override
    public double price(long itemId) {
        return getOne(itemId).getPrice();
    }

    @Override
    public Collection<ItemDTO> findAll() {
        List<ItemDTO> dtoList = new ArrayList<>();
        for (Item item : itemRepo.findAll()) {
            dtoList.add(new ItemDTO(item.getId(), item.getName(), item.getPrice()));
        }
        return dtoList;
    }

    @Override
    public ItemDTO getOne(long itemId) {
        ItemDTO dto = null;
        Optional<Item> itemOpt = itemRepo.findById(itemId);
        if (itemOpt.isPresent()) {
            dto = new ItemDTO(itemOpt.get().getId(), itemOpt.get().getName(), itemOpt.get().getPrice());
        }
        return dto;
    }
}
