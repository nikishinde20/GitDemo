package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {

	@Test(dataProvider="BooksData")
	public void addBook(String isbn, String aisle)
	{
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().log().all().header("content-type","application/json").
		body(payload.Addbook(isbn,aisle)).
		when()
		.post("/Library/Addbook.php")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
	}
	
	@DataProvider(name="BooksData")
	public Object[][] getData()
	{
		//array= collection of elements
		//multidimensional array = collection ofarrays
		return new Object[][] {{"ojfwty","9363"}, {"cwetee","4253"}, {"cwetee","533"}};
	}
	
	
	@Test(dataProvider = "DeleteData")
	public void deleteBook(String id)
	{
	    RestAssured.baseURI = "http://216.10.245.166";

	    String response = given().log().all()
	            .header("Content-Type", "application/json")
	            .body("{ \"ID\" : \"" + id + "\"}")
	        .when()
	            .post("/Library/DeleteBook.php")
	        .then()
	            .assertThat().statusCode(200)
	            .extract().response().asString();

	    System.out.println("Delete Response: " + response);
	}
	
	
	@DataProvider(name = "DeleteData")
	public Object[][] getDeleteData()
	{
	    return new Object[][] {
	    	 {"ojfwty9363"},
	         {"cwetee4253"},
	         {"okmfet533"}
	    };
	}
}
