package com.example.archUnit.service;

import com.example.archUnit.domain.Product;

public interface OnlineShopService {
  Product getProductByName(String name);
}
