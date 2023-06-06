package com.xMart.retailstorediscounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.xMart.retailstorediscounts.entity.Cart;
import com.xMart.retailstorediscounts.entity.Customer;
import com.xMart.retailstorediscounts.entity.CustomerType;
import com.xMart.retailstorediscounts.entity.Employee;
import com.xMart.retailstorediscounts.entity.Product;
import com.xMart.retailstorediscounts.repository.CustomerRepository;
import com.xMart.retailstorediscounts.repository.ProductRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class RetailStoreDiscountsApplicationTests {

	final private static int port = 8080;
	final private static String baseUrl = "http://localhost:";
	final private static double DELTA = 1e-15;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void testCustomerCreation() {
		HttpHeaders headers = createHeaders();
		Employee employee = new Employee(1, "Bhushan", true);
		Customer customer = new Customer(1, "Bhushan", LocalDate.now(), CustomerType.EMPLOYEE, employee, 789456123L);
		HttpEntity<Customer> request = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> responseEntity = this.restTemplate
				.postForEntity(baseUrl + port + "/customers/customer", request, Customer.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(1, responseEntity.getBody().getId());
		assertEquals("Bhushan", responseEntity.getBody().getName());
		assertEquals(CustomerType.EMPLOYEE, responseEntity.getBody().getCustomerType());
		assertEquals(employee, responseEntity.getBody().getEmployee());

		Optional<Customer> cust = customerRepository.findById(1);
		assertTrue(cust.isPresent());
		assertEquals(1, cust.get().getId());

	}

	@Test
	public void testProductsCreation() {
		Product product1 = new Product(1, "Salt", "Groceries", 20.0);
		Product product2 = new Product(2, "Shirt", "Apparels", 250.0);
		HttpHeaders headers = createHeaders();
		HttpEntity<Product> request = new HttpEntity<>(product1, headers);
		ResponseEntity<Product> responseEntity = this.restTemplate.postForEntity(baseUrl + port + "/products/product",
				request, Product.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(1, responseEntity.getBody().getId());
		assertEquals("Salt", responseEntity.getBody().getProductName());
		assertEquals(20.0, responseEntity.getBody().getPrice(), DELTA);
		request = new HttpEntity<>(product2, headers);
		responseEntity = this.restTemplate.postForEntity(baseUrl + port + "/products/product", request, Product.class);

		List<Product> products = productRepository.findAll();
		assertEquals(2, products.size());
	}

	@Test
	public void testCartCreation() {
		Employee employee = new Employee(1, "Bhushan", true);
		Customer customer = new Customer(1, "Bhushan", LocalDate.now(), CustomerType.EMPLOYEE, employee, 789456123L);
		Product product1 = new Product(1, "Salt", "Groceries", 20.0);
		Product product2 = new Product(2, "Shirt", "Apparels", 250.0);
		HttpHeaders headers = createHeaders();
		this.restTemplate.postForEntity(baseUrl + port + "/customers/customer", new HttpEntity<>(customer, headers), Customer.class);
		this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product1, headers), Product.class);
		this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product2, headers), Product.class);
		
		ResponseEntity<Cart> responseEntity = this.restTemplate.postForEntity(baseUrl + port + "/carts/cart?customerId=1", headers, Cart.class);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(1, responseEntity.getBody().getCartId());
		assertEquals(customer, responseEntity.getBody().getCustomer());
	}
	
	@Test
	public void testCartCreationAndProductsAddition() {
		Employee employee = new Employee(1, "Bhushan", true);
		Customer customer = new Customer(1, "Bhushan", LocalDate.now(), CustomerType.EMPLOYEE, employee, 789456123L);
		Product product1 = new Product(1, "Salt", "Groceries", 20.0);
		Product product2 = new Product(2, "Shirt", "Apparels", 250.0);
		HttpHeaders headers = createHeaders();
		ResponseEntity<Customer> responseCustomer = this.restTemplate.postForEntity(baseUrl + port + "/customers/customer", new HttpEntity<>(customer, headers), Customer.class);
		ResponseEntity<Product> responseProduct1 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product1, headers), Product.class);
		ResponseEntity<Product> responseProduct2 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product2, headers), Product.class);
		
		ResponseEntity<Cart> responseCart = this.restTemplate.postForEntity(baseUrl + port + "/carts/cart?customerId="+responseCustomer.getBody().getId(), headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct1.getBody().getId()+"&quantity=2", headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct2.getBody().getId()+"&quantity=1", headers, Cart.class);
		ResponseEntity<Cart> responseCartUpdated = this.restTemplate.getForEntity(baseUrl + port + "/carts/cart/"+responseCart.getBody().getCartId(), Cart.class);
		assertEquals(HttpStatus.OK, responseCartUpdated.getStatusCode());
		assertEquals(responseCart.getBody().getCartId(), responseCartUpdated.getBody().getCartId());
		assertEquals(2, responseCartUpdated.getBody().getCartItems().size());
		assertEquals(290.0, responseCartUpdated.getBody().getCartTotal(), DELTA);
		assertEquals(customer, responseCartUpdated.getBody().getCustomer());
	}
	
	@Test
	public void testNetPayableAmountCalculation() {
		Employee employee = new Employee(1, "Bhushan", true);
		Customer customer = new Customer(1, "Bhushan", LocalDate.now(), CustomerType.EMPLOYEE, employee, 789456123L);
		Product product1 = new Product(1, "Salt", "Groceries", 20.0);
		Product product2 = new Product(2, "Shirt", "Apparels", 250.0);
		HttpHeaders headers = createHeaders();
		ResponseEntity<Customer> responseCustomer = this.restTemplate.postForEntity(baseUrl + port + "/customers/customer", new HttpEntity<>(customer, headers), Customer.class);
		ResponseEntity<Product> responseProduct1 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product1, headers), Product.class);
		ResponseEntity<Product> responseProduct2 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product2, headers), Product.class);
		
		ResponseEntity<Cart> responseCart = this.restTemplate.postForEntity(baseUrl + port + "/carts/cart?customerId="+responseCustomer.getBody().getId(), headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct1.getBody().getId()+"&quantity=2", headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct2.getBody().getId()+"&quantity=1", headers, Cart.class);
		ResponseEntity<Cart> responseCartUpdated = this.restTemplate.getForEntity(baseUrl + port + "/carts/cart/"+responseCart.getBody().getCartId(), Cart.class);
		ResponseEntity<Double> netPayable = this.restTemplate.getForEntity(baseUrl + port + "/carts/cart/"+responseCartUpdated.getBody().getCartId()+"/netPayable", Double.class);
		assertEquals(HttpStatus.OK, netPayable.getStatusCode());
		assertEquals(205.0, netPayable.getBody().doubleValue(), DELTA);
		
		/*
		 * 
		Items added to cart 
		Product   Category      Price     Quantity    Discount
		Shirt	  Apparels      250.0       1          75  (30% as customer is employee)
 		Salt      Groceries     20.0        2          0   (No discount on Groceries)
 		
 		Total     =             290.0
 		Standard Discount =     10.0   ((290/100)*5)
 		Net payable =      290 - 75 - 10    = 205.0  
 		*  
 		*/
	}
	
	@Test
	public void testNetPayableAmountCalculationForAffiliate() {
		Employee employee = new Employee(1, "Bhushan", true);
		Customer customer = new Customer(1, "Bhushan", LocalDate.now(), CustomerType.AFFILIATE, employee, 789456123L);
		Product product1 = new Product(1, "Salt", "Groceries", 20.0);
		Product product2 = new Product(2, "Shirt", "Apparels", 250.0);
		HttpHeaders headers = createHeaders();
		ResponseEntity<Customer> responseCustomer = this.restTemplate.postForEntity(baseUrl + port + "/customers/customer", new HttpEntity<>(customer, headers), Customer.class);
		ResponseEntity<Product> responseProduct1 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product1, headers), Product.class);
		ResponseEntity<Product> responseProduct2 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product2, headers), Product.class);
		
		ResponseEntity<Cart> responseCart = this.restTemplate.postForEntity(baseUrl + port + "/carts/cart?customerId="+responseCustomer.getBody().getId(), headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct1.getBody().getId()+"&quantity=2", headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct2.getBody().getId()+"&quantity=1", headers, Cart.class);
		ResponseEntity<Cart> responseCartUpdated = this.restTemplate.getForEntity(baseUrl + port + "/carts/cart/"+responseCart.getBody().getCartId(), Cart.class);
		ResponseEntity<Double> netPayable = this.restTemplate.getForEntity(baseUrl + port + "/carts/cart/"+responseCartUpdated.getBody().getCartId()+"/netPayable", Double.class);
		assertEquals(HttpStatus.OK, netPayable.getStatusCode());
		assertEquals(255.0, netPayable.getBody().doubleValue(), DELTA);
		
		/*
		 * 
		Items added to cart 
		Product   Category      Price     Quantity    Discount
		Shirt	  Apparels      250.0       1          25  (10% as customer is affiliate)
 		Salt      Groceries     20.0        2          0   (No discount on Groceries)
 		
 		Total     =             290.0
 		Standard Discount =     10.0   ((290/100)*5)
 		Net payable =      290 - 25 - 10    = 255.0  
 		*  
 		*/
	}
	
	@Test
	public void testNetPayableAmountCalculationForOthers() {
		Employee employee = null;
		Customer customer = new Customer(1, "Bhushan", LocalDate.now(), CustomerType.OTHERS, employee, 789456123L);
		Product product1 = new Product(1, "Salt", "Groceries", 20.0);
		Product product2 = new Product(2, "Shirt", "Apparels", 250.0);
		HttpHeaders headers = createHeaders();
		ResponseEntity<Customer> responseCustomer = this.restTemplate.postForEntity(baseUrl + port + "/customers/customer", new HttpEntity<>(customer, headers), Customer.class);
		ResponseEntity<Product> responseProduct1 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product1, headers), Product.class);
		ResponseEntity<Product> responseProduct2 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product2, headers), Product.class);
		
		ResponseEntity<Cart> responseCart = this.restTemplate.postForEntity(baseUrl + port + "/carts/cart?customerId="+responseCustomer.getBody().getId(), headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct1.getBody().getId()+"&quantity=2", headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct2.getBody().getId()+"&quantity=1", headers, Cart.class);
		ResponseEntity<Cart> responseCartUpdated = this.restTemplate.getForEntity(baseUrl + port + "/carts/cart/"+responseCart.getBody().getCartId(), Cart.class);
		ResponseEntity<Double> netPayable = this.restTemplate.getForEntity(baseUrl + port + "/carts/cart/"+responseCartUpdated.getBody().getCartId()+"/netPayable", Double.class);
		assertEquals(HttpStatus.OK, netPayable.getStatusCode());
		assertEquals(280.0, netPayable.getBody().doubleValue(), DELTA);
		
		/*
		 * 
		Items added to cart 
		Product   Category      Price     Quantity    Discount
		Shirt	  Apparels      250.0       1          0   (No discount for customer type as others)
 		Salt      Groceries     20.0        2          0   (No discount on Groceries)
 		
 		Total     =             290.0
 		Standard Discount =     10.0   ((290/100)*5)
 		Net payable =      290 - 10    = 280.0  
 		*  
 		*/
	}
	
	@Test
	public void testNetPayableAmountCalculationForCustomerOver2Yrs() {
		Employee employee = null;
		Customer customer = new Customer(1, "Bhushan", LocalDate.now().minusYears(3), CustomerType.OTHERS, employee, 789456123L);
		Product product1 = new Product(1, "Salt", "Groceries", 20.0);
		Product product2 = new Product(2, "Shirt", "Apparels", 250.0);
		HttpHeaders headers = createHeaders();
		ResponseEntity<Customer> responseCustomer = this.restTemplate.postForEntity(baseUrl + port + "/customers/customer", new HttpEntity<>(customer, headers), Customer.class);
		ResponseEntity<Product> responseProduct1 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product1, headers), Product.class);
		ResponseEntity<Product> responseProduct2 = this.restTemplate.postForEntity(baseUrl + port + "/products/product", new HttpEntity<>(product2, headers), Product.class);
		
		ResponseEntity<Cart> responseCart = this.restTemplate.postForEntity(baseUrl + port + "/carts/cart?customerId="+responseCustomer.getBody().getId(), headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct1.getBody().getId()+"&quantity=2", headers, Cart.class);
		this.restTemplate.postForEntity(baseUrl + port + "/carts/"+responseCart.getBody().getCartId()+"/products?productId="+responseProduct2.getBody().getId()+"&quantity=1", headers, Cart.class);
		ResponseEntity<Cart> responseCartUpdated = this.restTemplate.getForEntity(baseUrl + port + "/carts/cart/"+responseCart.getBody().getCartId(), Cart.class);
		ResponseEntity<Double> netPayable = this.restTemplate.getForEntity(baseUrl + port + "/carts/cart/"+responseCartUpdated.getBody().getCartId()+"/netPayable", Double.class);
		assertEquals(HttpStatus.OK, netPayable.getStatusCode());
		assertEquals(267.5, netPayable.getBody().doubleValue(), DELTA);
		
		/*
		 * 
		Items added to cart 
		Product   Category      Price     Quantity    Discount
		Shirt	  Apparels      250.0       1          12.5   (5% discount for customer for over 2 years)
 		Salt      Groceries     20.0        2          0   (No discount on Groceries)
 		
 		Total     =             290.0
 		Standard Discount =     10.0   ((290/100)*5)
 		Net payable =      290 - 12.5 - 10    = 267.5  
 		*  
 		*/
	}
		
	private HttpHeaders createHeaders() {
		HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
