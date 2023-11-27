package com.mmall;

import com.mmall.service.ProductCategoryService;
import com.mmall.vo.ProductCategoryVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	private ProductCategoryService productCategoryService;

	@Test
	void contextLoads() {
		List<ProductCategoryVo> list = this.productCategoryService.buildProductCategoryMenu();
		int i = 0;
	}

}
