package com.calvinkeum.inventoryservice.service;

import com.calvinkeum.inventoryservice.dto.InventoryRequest;
import com.calvinkeum.inventoryservice.dto.InventoryResponse;
import com.calvinkeum.inventoryservice.model.Inventory;
import com.calvinkeum.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
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
        log.info("wait started...");
        Thread.sleep(10000);
        log.info("wait ended...");
        return inventoryRepository.findBySkuIn(sku).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .sku(inventory.getSku())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
