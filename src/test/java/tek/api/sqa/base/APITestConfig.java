package tek.api.sqa.base;

import com.aventstack.extentreports.testng.listener.ExtentITestListenerAdapter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import tek.api.model.PrimaryAccount;
import tek.api.utility.Asserter;
import tek.api.utility.DataGenerator;
import tek.api.utility.EndPoints;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners({ ExtentITestListenerAdapter.class })
public class APITestConfig extends BaseConfig {
	public Asserter asserter = new Asserter(); 
	private DataGenerator dataGenerator = new DataGenerator();

	@BeforeMethod
	public void setupApiTest() {
		// First Step Setup BaseURL to RestAssured
		System.out.println("Setting up Test");
		RestAssured.baseURI = getBaseUrl();
	}

	public String getValidToken() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("username", "supervisor");
		requestBody.put("password", "tek_supervisor");
		RequestSpecification request = RestAssured.given().body(requestBody);
		request.contentType(ContentType.JSON);
		Response response = request.when().post(EndPoints.TOKEN_GENERATION.getValue());
		response.prettyPrint();
		Assert.assertEquals(response.getStatusCode(), 200);
		String token = response.jsonPath().get("token");
		return token;
	}

	public RequestSpecification getRequestWithValidToken() {
		String token = getValidToken();
		return RestAssured.given()
				.header("Authorization", "Bearer " + token)
				.contentType(ContentType.JSON);
	}

	public PrimaryAccount createAccountData() {
		PrimaryAccount account = new PrimaryAccount();
		String firstName = dataGenerator.getFirstName();
		String lastName = dataGenerator.getLastName();
		account.setEmail(dataGenerator.getEmail(firstName, lastName, "tekschool.us"));
		account.setFirstName(firstName);
		account.setLastName(lastName);
		account.setTitle("Mr.");
		account.setGender("FEMALE");
		account.setMaritalStatus("MARRIED");
		account.setEmploymentStatus(dataGenerator.getJobTitle());
		account.setDateOfBirth(dataGenerator.getDateOfBirth());
		return account;
	}
}
