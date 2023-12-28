package com.calvinkeum.inventoryservice.service;

import com.calvinkeum.inventoryservice.dto.InventoryRequest;
import com.calvinkeum.inventoryservice.dto.InventoryResponse;
import com.calvinkeum.inventoryservice.model.Inventory;
import com.calvinkeum.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public void createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();

        inventory.setSku(inventoryRequest.getSku());
        inventory.setQuantity(inventoryRequest.getQuantity());

        inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> sku) {
        return inventoryRepository.findBySkuIn(sku).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .sku(inventory.getSku())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
