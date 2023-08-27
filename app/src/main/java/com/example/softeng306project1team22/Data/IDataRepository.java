package com.example.softeng306project1team22.Data;

import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.IItem;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDataRepository {

    public CompletableFuture<List<Category>> getCategories();

    public Category getCategoryById(String id);

    public CompletableFuture<List<IItem>> getAllItems();

    public CompletableFuture<List<Category>> fetchCategoryData();

    public CompletableFuture<List<IItem>> fetchAllItems();

    public CompletableFuture<List<IItem>> fetchFromCollection(String categoryName, Class<?> itemClass);

    public CompletableFuture<IItem> fetchItemById(String productCategory, String productId);

    public CompletableFuture<List<IItem>> fetchRecentlyViewed(String collectionName);

    public CompletableFuture<List<IItem>> getReccomended(String categoryName, Class<?> itemClass, String filter);

    public void addItemToCart(String productId, String productCategory, String price, String quantity);

    public void addItemToRecentlyViewed(IItem item);

    public CompletableFuture<HashMap<IItem, String>> getCartDocuments();

    public void clearCart();

    public void deleteItemById(String id);

    public void modifyItemQuantity(String productId, int quantityValue);
}
