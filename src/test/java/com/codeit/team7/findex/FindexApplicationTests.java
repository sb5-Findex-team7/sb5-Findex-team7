package com.codeit.team7.findex;

import com.codeit.team7.findex.util.OpenApiUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FindexApplicationTests {

  @Autowired
  OpenApiUtil openApiUtil;

  @Test
  void contextLoads() {
    System.out.println(openApiUtil.fetchStockMarketIndex(null));
  }

}
