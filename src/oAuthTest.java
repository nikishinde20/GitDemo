
import static io.restassured.RestAssured.given;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;
public class oAuthTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] courseTitles = {"Selenium Webdriver Java","Cypress","Protractor"};

		String response = given()
		.formParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.formParams("grant_type","client_credentials")
		.formParams("scope","trust")
		.when().log().all()
		.post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").asString();
		
		System.out.println("Response: "+response);
		
		JsonPath js = new JsonPath(response);
		String accessToken = js.getString("access_token");
		
		 GetCourse gc = given()
		.queryParam("access_token", accessToken)
		.when().log().all()
		.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails?access_token=hXmXzEVqh6ZlgLyg3OikqA").as(GetCourse.class);
	
		//System.out.println("Response2: "+gc);
		
		System.out.println("getLinkedIn: "+gc.getLinkedIn());
		System.out.println("getInstructor: "+gc.getInstructor());
		System.out.println("getCourseTitle: "+gc.getCourses().getApi().get(1).getCourseTitle());
		List<Api> apiCourses = gc.getCourses().getApi();
		for(int i=0;i<apiCourses.size();i++)
		{
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println("getPrice: "+apiCourses.get(i).getPrice());
			}
		}
		
		
		ArrayList<String> a = new ArrayList<>();
		
		List<WebAutomation> webAutomation = gc.getCourses().getWebAutomation();
		for(int i=0; i<webAutomation.size();i++)
		{
			a.add(webAutomation.get(i).getCourseTitle());
			
		}
		
		List<String> expectedList = Arrays.asList(courseTitles);
		Assert.assertTrue(a.equals(expectedList));
	}

}
