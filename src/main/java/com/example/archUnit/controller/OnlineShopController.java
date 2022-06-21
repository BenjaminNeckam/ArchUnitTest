package com.example.archUnit.controller;

import com.example.archUnit.domain.Product;
import com.example.archUnit.service.OnlineShopService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value="/api")
public class OnlineShopController {

  // @Autowired
  private OnlineShopService onlineShopService;

  @GetMapping("/product/{name}")
  public Product getProductByName(@PathVariable String name) {
    return onlineShopService.getProductByName(name);
  }


}
