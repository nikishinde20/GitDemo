
import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	public static void main(String[] args) {
		JsonPath js = new JsonPath(payload.CoursePrice());
	
	
		//Print No of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println("Courses count: "+count);
		
		
		//Print Purchase amount
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("\nPurchase amount: "+totalAmount);
		
		//Print title of the first course
		String fistCourse = js.get("courses[0].title");
		System.out.println("\nFirst Course: "+fistCourse);
		
		
		// Print all course titles and their respective Prices
		for(int i=0;i<count;i++)
		{
			String coursesTitles=js.get("courses["+i+"].title");
			System.out.println("\nAll Cources Title: "+coursesTitles);
			
			int coursesPrices=js.get("courses["+i+"].price");
			System.out.println("All Cources Price: "+coursesPrices);
		}
		
		// Print no of copies sold by RPA Course
		for(int i=0;i<count;i++)
		{
			String coursesTitles=js.get("courses["+i+"].title");
			if(coursesTitles.equalsIgnoreCase("RPA"))
			{
				//copies sold
				int copies = js.get("courses["+i+"].copies");
				System.out.println("\nCopies: "+copies);
				break;
			}
		}
		
	}

	
}
