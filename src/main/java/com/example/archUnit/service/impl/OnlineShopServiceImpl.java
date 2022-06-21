package com.example.archUnit.service.impl;

import com.example.archUnit.domain.Product;
import com.example.archUnit.persistance.dao.ProductDao;
import com.example.archUnit.service.OnlineShopService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OnlineShopServiceImpl implements OnlineShopService {

//  @Autowired
  ProductDao productDao;

  @Override
  public Product getProductByName(String name) {
    return productDao.findByName(name);
  }
}
