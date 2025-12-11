package com.java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.entity.Product;
import com.java.repository.ProductRepository;

@Controller
public class ProductDetailController extends CommonController {

	@Autowired
	ProductRepository productRepository;

	// get productDetail

    @GetMapping("/productDetail")
    public String productDetail(@RequestParam("productId") Integer productId, Model model) {

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return "redirect:/not-found";
        }

        if (product.getCategory() == null) {
            model.addAttribute("product", product);
            model.addAttribute("productByCategory", List.of());
            listproduct10(model);
            return "site/productDetail";
        }

        model.addAttribute("product", product);

        productByCategory(model, product.getCategory().getCategoryId());
        listproduct10(model);

        return "site/productDetail";
    }

	// Gợi ý sản phẩm cùng loại
	public void productByCategory(Model model, Integer categoryId) {
		List<Product> products = productRepository.productsByCategory(categoryId);
		model.addAttribute("productByCategory", products);

	}

	// list product ở trang chủ
	@ModelAttribute("listProduct10")
	public List<Product> listproduct10(Model model) {
		List<Product> productList = productRepository.listProduct10();
		model.addAttribute("productList", productList);

		return productList;
	}

}
