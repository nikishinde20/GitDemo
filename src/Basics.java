 import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import files.ReUsableMethods;
import files.payload;


public class Basics {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

// validate if add place API is working as expected
		
		//given - all input details, 
		//when - submit the API -resource, http method, 
		//Then - validate the response
		// content of the file to String -> content of file can convert into Byte  -> Byte data to String
		
	//Add place -> update place with New Address -> Get place to validate if New address is present in response
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(payload.AppPlace())
		.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\Abhishek Modi\\OneDrive\\Documents\\addPlace.json"))))
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		System.out.println("Response: "+response);
		JsonPath js = new JsonPath(response); //for parsing Json
		String placeid = js.getString("place_id");
		
		System.out.println("placeid: "+placeid);
		
		
		
	//update place
		String newAddress="Summer winter walk, USA";
		given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeid+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("/maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated")).extract().response().toString();

	
	//Get place
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeid)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath js1 = ReUsableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		
		System.out.println("Actual Address: "+actualAddress);
		System.out.println("New Address: "+newAddress);
		Assert.assertEquals(actualAddress, newAddress);
		
	}
	

}
