package com.example.archUnit.persistence.dao;

import com.example.archUnit.domain.Product;

public interface ProductDao {
  Product findByName(String productName);
}
