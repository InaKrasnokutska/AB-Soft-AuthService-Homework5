package net.absoft;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.absoft.data.Response;
import net.absoft.services.AuthenticationService;
import org.testng.annotations.Test;

public class AuthenticationServiceTest extends BaseTest {

  //public static int count =0;

  private AuthenticationService authenticationService;

  @BeforeClass (groups = "positive")
  public void setUp(){
    authenticationService = new AuthenticationService();
    System.out.println("setup");
  }

  //@AfterMethod
  //public void tearDown() {
  //  System.out.println("teardown);
  //}

  @Test(
    description = "Test Successful Authentication",
    groups = "positive"
    //invocationCount = 4,
    //invocationTimeOut = 3000
    //threadPoolSize = 2
    //successPercentage = 60
  )
  @Parameters({"email-address", "password"})
  public void testSuccessfulAuthentication(@Optional("user1@test.com") String email, 
        @Optional("password1") String password) {
    Response response = authenticationService.authenticate(email-address, password);
    assertEquals( response.getCode(), 200, "Response code should be 200");
    assertTrue(validateToken(response.getMessage()),
        "Token should be the 32 digits string. Got: " + response.getMessage());
    System.out.println("testSuccessfulAuthentication");
    Thread.sleep(2000);
    //count++;
    //assertNotEquals(count, 4);
    //fail("FAILING TEST");
  }

  @DataProvider (name = "invalidLogins", parallel = true)
  public Object[][] invalidLogins() {
    return new Object[][] {
      new Object[] {"user1@test.com", "wrong_password1", new Response(401, "Invalid email or password")}
      new Object[] {"", "password1", new Response(400, "Email should not be empty string")}
      new Object[] {"user1@test.com", "", new Response(400, "Password should not be empty string")}
      new Object[] {"user1", "password1", new Response(400, "Invalid email")}
    }
  }

  @Test(
    //enabled = false,
    groups = negative,
    dataProvider = "invalidLogins"
  )
  public void testInvalidAuthentication(String email, String password, Response expectedResponse) { //testAuthenticationWithWrongPassword() {
    validateErrorResponse(
        authenticationService.authenticate("user1@text.com", "wrong_password1"),
        401, 
        "Invalid email or password"
    );
    Thread.sleep(2000);
    //System.out.println("testAuthenticationWithWrongPassword");
  }

  private void validateErrorResponse(Response actualResponse, int code, String message) {
    SoftAssert sa = new SoftAssert();
    //sa.assertEquals(actualResponse, expectedResponse, "Unexpected response);
    sa.assertEquals(actualResponse.getCode(), expectedResponse.getCode(), "Response code should be 401");
    sa.assertEquals(actualResponse.getMessage(), expectedResponse.getMessage(),
        "Response message should be \"Invalid email or password\"");
    //sa.assertEquals(response.getCode(), code, "Response code should be 401");
    //sa.assertEquals(response.getMessage(), message,
    //    "Response message should be \"Invalid email or password\"");
    sa.asserAll();
  }

/*
  @Test(
    priority = 3,
    groups = negative
    //dependsOnMethods = "testAuthenticationWithEmptyPassword",
    //alwaysRun = true
  )
  public void testAuthenticationWithEmptyEmail() {
    Response expectedResponse = new Response(400, "Email should not be empty string");
    Response actualResponse = authenticationService.authenticate("", "password1");
    assertEquals(actualResponse, expectedResponse, "Unexpected response");
    System.out.println("testAuthenticationWithEmptyEmail");
  }

  @Test(
    groups = negative
    //timeOut = 2000 
  )
  public void testAuthenticationWithInvalidEmail() { //throws InterruptedException {
    Response response = authenticationService.authenticate("user1", "password1");
    assertEquals(response.getCode(), 400, "Response code should be 200");
    assertEquals(response.getMessage(), "Invalid email",
        "Response message should be \"Invalid email\"");
    //Thread.sleep(3000);
    System.out.println("testAuthenticationWithInvalidEmail");
  }

  @Test(
  groups = negative,
  priority = 2
  //dependsOnMethods = {"testAuthenticationWithInvalidEmail"}
  )
  public void testAuthenticationWithEmptyPassword() {
    Response response = authenticationService.authenticate("user1@test", "");
    assertEquals(response.getCode(), 400, "Response code should be 400");
    assertEquals(response.getMessage(), "Password should not be empty string",
        "Response message should be \"Password should not be empty string\"");
    System.out.println("testAuthenticationWithEmptyPassword");
  }

*/

  private boolean validateToken(String token) {
    final Pattern pattern = Pattern.compile("\\S{32}", Pattern.MULTILINE);
    final Matcher matcher = pattern.matcher(token);
    return matcher.matches();
  }
}