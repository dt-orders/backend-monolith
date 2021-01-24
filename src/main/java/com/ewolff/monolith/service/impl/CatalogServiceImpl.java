package com.ewolff.monolith.service.impl;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.ItemDTO;
import com.ewolff.monolith.persistence.domain.Customer;
import com.ewolff.monolith.persistence.domain.Item;
import com.ewolff.monolith.persistence.repository.ItemRepository;
import com.ewolff.monolith.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.info("Calling CatalogService.findAll()");
        List<ItemDTO> dtoList = new ArrayList<>();
        for (Item item : itemRepo.findAll()) {
            dtoList.add(new ItemDTO(item.getId(), item.getName(), item.getPrice()));
        }
        return dtoList;
    }

    @Override
    public ItemDTO getOne(long itemId) {
        log.info("Calling CatalogService.findOne() for itemId: {}", itemId);
        ItemDTO dto = null;
        Optional<Item> itemOpt = itemRepo.findById(itemId);
        if (itemOpt.isPresent()) {
            dto = new ItemDTO(itemOpt.get().getId(), itemOpt.get().getName(), itemOpt.get().getPrice());
        }
        return dto;
    }

    @Override
    public ItemDTO save(ItemDTO dto) {
        log.info("Calling CatalogService.save() for itemDto: {}", dto);
        Item x = new Item(dto.getName(), dto.getPrice());
        x.setId(dto.getItemId());
        Item i = itemRepo.save(x);
        return new ItemDTO(i.getId(), i.getName(), i.getPrice());
    }

    @Override
    public void delete(long id) {
        log.info("Calling CatalogService.delete() for itemId: {}", id);
        if (itemRepo.findById(id) != null) {
            itemRepo.deleteById(id);
        }
    }

    @Override
    public List<ItemDTO> search(String query) {
        log.info("Calling CatalogService.search() with query: {}", query);

        List<ItemDTO> results = new ArrayList<>();
        List<Item> items = itemRepo.findByNameContaining(query);
        for (Item i : items) {
            results.add(new ItemDTO(i.getId(), i.getName(), i.getPrice()));
        }
        return results;
    }
}
