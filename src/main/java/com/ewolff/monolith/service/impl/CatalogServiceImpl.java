package com.ewolff.monolith.service.impl;

import com.ewolff.monolith.dto.ItemDTO;
import com.ewolff.monolith.persistence.domain.Item;
import com.ewolff.monolith.persistence.repository.ItemRepository;
import com.ewolff.monolith.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService {

    private final ItemRepository itemRepo;

    private final ModelMapper mapper;

    public CatalogServiceImpl(ItemRepository itemRepo, ModelMapper mapper) {
        this.itemRepo = itemRepo;
        this.mapper = mapper;
    }

    @Override
    public double price(Long itemId) {
        return getOne(itemId).getPrice();
    }

    @Override
    public Collection<ItemDTO> findAll() {
        log.info("Calling CatalogService.findAll()");
        Iterable<Item> items = itemRepo.findAll();
        List<ItemDTO> dtoList = StreamSupport.stream(items.spliterator(), false)
                .map(item -> mapper.map(item, ItemDTO.class))
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public ItemDTO getOne(Long itemId) {
        log.info("Calling CatalogService.findOne() for itemId: {}", itemId);
        ItemDTO dto = null;
        Optional<Item> itemOpt = itemRepo.findById(itemId);
        if (itemOpt.isPresent()) {
            dto = mapper.map(itemOpt.get(), ItemDTO.class);
        }
        return dto;
    }

    @Override
    public ItemDTO save(ItemDTO dto) {
        log.info("Calling CatalogService.save() for itemDto: {}", dto);
        Item x = mapper.map(dto, Item.class);
        x.setId(dto.getItemId());
        log.info("CatalogService.save() Item: {}", x);
        Item i = itemRepo.save(x);
        return new ItemDTO(i.getId(), i.getName(), i.getPrice());
    }

    @Override
    public void delete(Long id) {
        log.info("Calling CatalogService.delete() for itemId: {}", id);
        if (itemRepo.findById(id).isPresent()) {
            itemRepo.deleteById(id);
        }
    }

    @Override
    public List<ItemDTO> search(String query) {
        log.info("Calling CatalogService.search() with query: {}", query);
        List<Item> items = itemRepo.findByNameContaining(query);
        return items
                .stream()
                .map(item -> mapper.map(item, ItemDTO.class))
                .collect(Collectors.toList());
    }
}
