package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;


@Repository
public class ProductManagerImpl implements ProductManager {

	private static final String BASE_URI = "http://localhost:8040";
	private static final String PRODUCT_URI = BASE_URI + "/product";
	private static final String CATEGORY_URI = BASE_URI + "/category";
    private static RestTemplate restTemplate = new RestTemplate();

	@Override
	public List<Product> getProducts() {
		Product[] products = restTemplate.getForObject(PRODUCT_URI + "s", Product[].class);
		List<Product> productList = Arrays.asList(products);
		List<Category> categoryList = this.getCategories();
		for (Product product : productList) {
			for (Category category : categoryList) {
				if(category.getId() == product.getCategory()) {
					product.setCategoryClass(category);
		        }
			}
		}
	    return productList;
	}
	
	@Override
	public List<Product> getProductsForSearchValues(String searchDescription,
			Double searchMinPrice, Double searchMaxPrice) {	
		if (searchDescription == null || searchDescription.equals("")) {
			return null;
		}
		
		Product[] products = restTemplate.getForObject(PRODUCT_URI + "s/search?value=" + searchDescription + "&min=" + searchMinPrice + "&max=" + searchMaxPrice, Product[].class);
		List<Product> productList = Arrays.asList(products);
		List<Category> categoryList = this.getCategories();
		for (Product product : productList) {
			for (Category category : categoryList) {
				if(category.getId() == product.getCategory()) {
					product.setCategoryClass(category);
		        }
			}
		}
	    return productList;
	}

	@Override
	public Product getProductById(int id) {
		Product product = restTemplate.getForObject(PRODUCT_URI + "/" + id, Product.class);
		Category category = this.getCategory(product.getCategory());
		product.setCategoryClass(category);
		return product;
	}

	@Override
	public Product getProductByName(String name) {
		if (name == null || name.equals("")) {
			return null;
		}
		Product product = restTemplate.getForObject(PRODUCT_URI + "?name=" + name, Product.class);
		Category category = this.getCategory(product.getCategory());
		product.setCategoryClass(category);
		return product;
	}
	
	@Override
	public int addProduct(String name, double parseDouble, int categoryId, String details) {
		
		Category category = this.getCategory(categoryId);
		
		if(category != null){
			Product newProduct;
			if(details == null){
				newProduct = new Product(name, parseDouble, categoryId);	
			} else{
				newProduct = new Product(name, parseDouble, categoryId, details);
			}
			
			restTemplate.postForLocation(PRODUCT_URI, newProduct);
		}
		return 1;
	}
	

	@Override
	public void deleteProductById(int id) {
		restTemplate.delete(PRODUCT_URI + "/" + id);
	}

	@Override
	public boolean deleteProductsByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<Category> getCategories() {
		Category[] categories = restTemplate.getForObject(BASE_URI + "/categories", Category[].class);
	    return Arrays.asList(categories);
	}

	@Override
	public Category getCategory(int id) {
		return restTemplate.getForObject(CATEGORY_URI + "/" + id, Category.class);
	}

	@Override
	public Category getCategoryByName(String name) {
		if (name == null || name.equals("")) {
			return null;
		}
		return restTemplate.getForObject(CATEGORY_URI + "?name=" + name, Category.class);
	}

	@Override
	public void addCategory(String name) {
		Category newCategory = new Category(name);
		restTemplate.postForLocation(CATEGORY_URI, newCategory);
	}

	@Override
	public void delCategoryById(int id) {
	
		restTemplate.delete(CATEGORY_URI + "/" + id);
	}

}
