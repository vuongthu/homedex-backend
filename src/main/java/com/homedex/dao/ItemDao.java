package com.homedex.dao;

import com.homedex.dao.entities.ItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemDao extends CrudRepository<ItemEntity, UUID> {

    List<ItemEntity> findAllByCategoryEntityIdOrderByNameAsc(UUID categoryId);

    List<ItemEntity> findAllByCategoryEntity_HouseholdEntity_IdAndLikedOrderByNameAsc(UUID householdId, Boolean liked);

    List<ItemEntity> findAllByCategoryEntity_HouseholdEntity_IdAndPurchaseOrderByNameAsc(UUID householdId, Boolean purchase);
}
