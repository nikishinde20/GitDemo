package files;
import static io.restassured.RestAssured.given;

import java.util.concurrent.ConcurrentHashMap.KeySetView;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;


public class oAuthTest {
	public static void main(String[] args) throws InterruptedException {
		
//		System.setProperty("webdrive.chrome.driver", "C:\\Users\\Abhishek Modi\\Downloads\\chromedriver_win32\\chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
//		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//		driver.findElement(By.cssSelector("innput[type='email']")).sendKeys("srinath19830@gmail.com");
//		driver.findElement(By.cssSelector("innput[type='email']")).sendKeys(Keys.ENTER);
//		Thread.sleep(3000);
//		driver.findElement(By.cssSelector("innput[type='password']")).sendKeys("123456");
//		driver.findElement(By.cssSelector("innput[type='password']")).sendKeys(Keys.ENTER);
//		Thread.sleep(4000);

		// Redirect URL received after Google OAuth login
		// Paste NEW redirect URL here (generate fresh every run)
        String url =
                "https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=NEW_CODE_HERE&scope=email+openid";

        // Extract authorization code
        String partialCode = url.split("code=")[1];
        String code = partialCode.split("&")[0];

        System.out.println("Auth Code: " + code);

        // STEP 1: Get Access Token
        String tokenResponse =
        	    given()
        	        .urlEncodingEnabled(false)
        	        .header("Content-Type", "application/x-www-form-urlencoded")
        	        .formParam("code", code)
        	        .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
        	        .formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
        	        .formParam("grant_type", "authorization_code")
        	        .formParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
        	    .when()
        	        .post("https://oauth2.googleapis.com/token")
        	    .asString();

        System.out.println("Token Response: " + tokenResponse);

        JsonPath js = new JsonPath(tokenResponse);
        String accessToken = js.getString("access_token");

        System.out.println("Access Token: " + accessToken);

        // STEP 2: Call secured API
        String response =
        	    given()
        	        .queryParam("access_token", accessToken)
        	    .when()
        	        .get("https://rahulshettyacademy.com/getCourse.php")
        	    .asString();
        System.out.println("Final Response:");
        System.out.println(response);
    }
}