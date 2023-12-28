package com.calvinkeum.inventoryservice.repository;

import com.calvinkeum.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findBySkuIn(List<String> sku);
}
