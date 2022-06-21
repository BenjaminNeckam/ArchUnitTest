package com.example.archUnit.persistance.dao;

import com.example.archUnit.domain.Product;

public interface ProductDao {
  Product findByName(String productName);
}
