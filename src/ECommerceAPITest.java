import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.MultiPartConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.protocol.RequestAddCookies;
import org.testng.Assert;

public class ECommerceAPITest {

	public static void main(String[] args) {
		RequestSpecification req = new  RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.setContentType(ContentType.JSON).build();
	
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("nikishinde20@gmail.com");
		loginRequest.setUserPassword("Abhiniki@22");
		
		RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
	
		LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").then().extract().response()
						.as(LoginResponse.class);
		
		System.out.println("Token: "+loginResponse.getToken());
		String token = loginResponse.getToken();
		
		System.out.println("UserId: "+loginResponse.getUserId());
		String userId = loginResponse.getUserId();
		
		
		// Add Product
		
		RequestSpecification addProductBaseReq = new  RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token)
				.build();
		
		RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq)
		.param("productName", "Laptop")
		.param("productAddedBy", userId)
		.param("productCategory", "fashion")
		.param("productSubCategory", "shirt")
		.param("productPrice", "11500")
		.param("productDescription", "Lenova")
		.param("productFor", "women")
		.multiPart("productImage", new File("C:\\Users\\Abhishek Modi\\Downloads\\Laptop.jpg"));
		
		String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product")
		.then().log().all().extract().response().asString();
		
		JsonPath js = new JsonPath(addProductResponse);
		String productId = js.get("productId");
		System.out.println(productId);
		
		// Create Order for Product

		RequestSpecification createOrderBaseReq = new  RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token).setContentType(ContentType.JSON).build();
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderedId(productId);
		
		List<OrderDetail> orderDetailList = new ArrayList<>();
		orderDetailList.add(orderDetail); 
		
		Orders orders = new Orders();
		orders.setOrders(orderDetailList);
		
		RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(orders);
		String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all().extract().asString();
		System.out.println("response Add Order: "+responseAddOrder);
		
		
		
		// Delete the Product
		
		RequestSpecification deleteProductBaseReq = new  RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token)
				.build();
		
		RequestSpecification deleteProdReq = given().log().all().spec(deleteProductBaseReq).pathParam("productId", productId);
		String deleteProductResponse = deleteProdReq.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all()
				.extract().response().asString();
		
		System.out.println("deleteProductResponse: "+deleteProductResponse);
		
		JsonPath js1 = new JsonPath(deleteProductResponse);
		Assert.assertEquals("Product Deleted Successfully", js1.get("message"));
		
		
	}
}
