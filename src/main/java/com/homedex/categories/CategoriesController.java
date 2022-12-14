package com.homedex.categories;

import com.homedex.categories.models.Category;
import com.homedex.categories.models.CategoryRequest;
import com.homedex.categories.models.Item;
import com.homedex.categories.models.ItemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

    private static final String HOUSEHOLD_ID_PARAM = "household-id";
    private static final String CATEGORY_ID_PARAM = "category-id";
    private static final String ITEM_ID_PARAM = "item-id";
    private final CategoriesService categoriesService;
    private final ItemsService itemsService;

    public CategoriesController(CategoriesService categoriesService, ItemsService itemsService) {
        this.categoriesService = categoriesService;
        this.itemsService = itemsService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody CategoryRequest request,
            @RequestParam(HOUSEHOLD_ID_PARAM) UUID householdId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriesService.createCategory(request.name(), householdId));
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(@RequestParam(HOUSEHOLD_ID_PARAM) UUID householdId) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriesService.getCategories(householdId));
    }

    @GetMapping("{category-id}")
    public ResponseEntity<Category> getCategory(@PathVariable(CATEGORY_ID_PARAM) UUID categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriesService.getCategory(categoryId));
    }

    @DeleteMapping("{category-id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(CATEGORY_ID_PARAM) UUID categoryId) {
        categoriesService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{category-id}")
    public ResponseEntity<Category> updateCategoryName(@RequestBody CategoryRequest request, @PathVariable(CATEGORY_ID_PARAM) UUID categoryId) {
        categoriesService.updateCategoryName(request.name(), categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(categoriesService.getCategory(categoryId));
    }

    // Items

    @PostMapping("{category-id}/items")
    public ResponseEntity<Item> createItem(@RequestBody ItemRequest request, @PathVariable(CATEGORY_ID_PARAM) UUID categoryId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemsService.createItem(request.name(), request.measurement(), request.brand(), request.addInfo(), request.expiration(), request.unit(), categoryId));
    }

    @GetMapping("{category-id}/items")
    public ResponseEntity<List<Item>> getItems(@PathVariable(CATEGORY_ID_PARAM) UUID categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(itemsService.getItems(categoryId));
    }

    @PutMapping("{category-id}/items/{item-id}")
    public ResponseEntity<Item> updateItem(
            @RequestBody ItemRequest request,
            @PathVariable(CATEGORY_ID_PARAM) UUID categoryId,
            @PathVariable(ITEM_ID_PARAM) UUID itemId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(itemsService.updateItem(request.name(), request.measurement(), request.brand(), request.addInfo(), request.expiration(), request.unit(), itemId));
    }

    @DeleteMapping("{category-id}/items/{item-id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable(CATEGORY_ID_PARAM) UUID categoryId,
            @PathVariable(ITEM_ID_PARAM) UUID itemId
    ) {
        itemsService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{category-id}/items/{item-id}/like")
    public ResponseEntity<Void> likeItem(
            @PathVariable(CATEGORY_ID_PARAM) UUID categoryId,
            @PathVariable(ITEM_ID_PARAM) UUID itemId
    ) {
        itemsService.toggleLikeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{category-id}/items/{item-id}/purchase")
    public ResponseEntity<Void> purchaseItem(
            @PathVariable(CATEGORY_ID_PARAM) UUID categoryId,
            @PathVariable(ITEM_ID_PARAM) UUID itemId
    ) {
        itemsService.togglePurchaseItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
