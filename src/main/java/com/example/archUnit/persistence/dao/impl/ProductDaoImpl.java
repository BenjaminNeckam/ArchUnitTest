package com.example.archUnit.persistence.dao.impl;

import com.example.archUnit.domain.Product;
import com.example.archUnit.persistence.dao.ProductDao;
import org.springframework.stereotype.Service;

@Service
public class ProductDaoImpl implements ProductDao {

  @Override
  public Product findByName(String productName) {
    return new Product(1L, productName);
  }
}
