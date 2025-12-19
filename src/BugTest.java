

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

public class BugTest {
	public static void main(String args[])
	{
		//Create a bug
		RestAssured.baseURI="https://nikishinde20-1764048943246.atlassian.net/";
		String createIssueResponse =  given()
		.header("Content-Type","application/json")
		.header("Authorization","Basic bmlraXNoaW5kZTIwQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBsMmdLSzl4ZUEwVExkcHRpTV9janJJTG1GUDB6RGhsTnVHV3U0QVFacF9nak91QkV6TjlpVXpra0RHNHUxSTUwbGlIaVhvZ3hTdDM1SXBvOXNWVmYxajlWU19ubzQxeFJMTS1nbU1uZ1lPRUJIcWJBelk2Ym1PcE5jSEg3eExFX1lBakNkN3FpU3kzSEN1RlRuQ2tOLVMxRnBuZ0NIeDNoVW12aFRXRzZBcEk9QjVFNTgxNjI=")
		.body("{\r\n"
				+ "    \"fields\": {\r\n"
				+ "       \"project\":\r\n"
				+ "       {\r\n"
				+ "          \"key\": \"SCRUM\"\r\n"
				+ "       },\r\n"
				+ "       \"summary\": \"Website items are not working through automation from Rest Assured\",\r\n"
				+ "       \"issuetype\": {\r\n"
				+ "          \"name\": \"Bug\"\r\n"
				+ "       }\r\n"
				+ "   }\r\n"
				+ "}\r\n"
				+ "")
		.log().all()
		.post("rest/api/3/issue")
		.then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		
		JsonPath js = new JsonPath(createIssueResponse);
		
		String issueId = js.getString("id");
		System.out.println(issueId);
		
		
		// Add attachment
		given()
		.log().headers()
		.pathParam("key", issueId)
		.header("X-Atlassian-Token", "no-check")
		.header("Authorization","Basic bmlraXNoaW5kZTIwQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBsMmdLSzl4ZUEwVExkcHRpTV9janJJTG1GUDB6RGhsTnVHV3U0QVFacF9nak91QkV6TjlpVXpra0RHNHUxSTUwbGlIaVhvZ3hTdDM1SXBvOXNWVmYxajlWU19ubzQxeFJMTS1nbU1uZ1lPRUJIcWJBelk2Ym1PcE5jSEg3eExFX1lBakNkN3FpU3kzSEN1RlRuQ2tOLVMxRnBuZ0NIeDNoVW12aFRXRzZBcEk9QjVFNTgxNjI=")
		.multiPart("file",new File("C:\\Users\\Abhishek Modi\\Pictures\\Screenshots\\Screenshot 2025-07-04 111653.png"))
		.post("rest/api/3/issue/{key}/attachments")
		.then().log().headers().assertThat().statusCode(200);
		
		
	}
	
}
