package com.example.archUnit.service.impl;

import com.example.archUnit.controller.OnlineShopController;
import com.example.archUnit.domain.Product;
import com.example.archUnit.persistence.dao.ProductDao;
import com.example.archUnit.service.OnlineShopService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OnlineShopServiceImpl implements OnlineShopService {

  ProductDao productDao;

  @Override
  public Product getProductByName(String name) {
    return productDao.findByName(name);
  }
}
