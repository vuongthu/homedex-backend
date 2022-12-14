package com.homedex.households;

import com.homedex.categories.ItemsService;
import com.homedex.categories.models.Item;
import com.homedex.dao.HouseholdDao;
import com.homedex.dao.ItemDao;
import com.homedex.dao.UserDao;
import com.homedex.dao.entities.HouseholdEntity;
import com.homedex.dao.entities.UserEntity;
import com.homedex.households.models.Household;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class HouseholdsService {
    private final HouseholdDao householdDao;
    private final UserDao userDao;
    private final ItemDao itemDao;

    public HouseholdsService(HouseholdDao householdDao, UserDao userDao, ItemDao itemDao) {
        this.householdDao = householdDao;
        this.userDao = userDao;
        this.itemDao = itemDao;
    }

    public Household createHousehold(String name, String image, UUID userId) {
        UserEntity userEntity = userDao.findById(userId).orElseThrow();
        Set<UserEntity> userEntities = new HashSet<>();
        userEntities.add(userEntity);
        HouseholdEntity householdEntity = householdDao.save(HouseholdEntity.builder().name(name).image(image).userEntities(userEntities).build());
        return mapToHousehold(householdEntity);
    }

    public List<Household> getHouseholds() {
        return StreamSupport.stream(householdDao.findAll().spliterator(), true)
                .map(this::mapToHousehold).toList();
    }

    public Household getHouseholdById(UUID householdId) {
        HouseholdEntity entity = householdDao.findById(householdId).orElseThrow();
        return mapToHousehold(entity);
    }

    private Household mapToHousehold(HouseholdEntity entity) {
        return new Household(entity.getId(), entity.getName(), entity.getImage());
    }

    public void deleteHousehold(UUID householdId) {
        householdDao.deleteHouseholdEntityById(householdId);
    }

    public void updateHousehold(String name, String image, UUID householdId) {
        HouseholdEntity entity = householdDao.findById(householdId).orElseThrow();
        if (name != null) {
            entity.setName(name);
        }

        if (image != null) {
            entity.setImage(image);
        }
        householdDao.save(entity);
    }

    public List<Household> getHouseholdsForUser(UUID userId) {
        return householdDao.findHouseholdEntitiesByUserEntitiesId(userId).stream()
                .map(this::mapToHousehold).toList();
    }

    public List<Item> getLikedItemsByHousehold(UUID householdId) {
        return itemDao.findAllByCategoryEntity_HouseholdEntity_IdAndLikedOrderByNameAsc(householdId, true).stream()
                .map(ItemsService::mapToItem)
                .toList();
    }

    public List<Item> getItemsToPurchaseByHousehold(UUID householdId) {
        return itemDao.findAllByCategoryEntity_HouseholdEntity_IdAndPurchaseOrderByNameAsc(householdId, true).stream()
                .map(ItemsService::mapToItem)
                .toList();
    }
}
