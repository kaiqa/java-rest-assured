package stepdef;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

public class CouchDBTest {

    public static Response response;
    public String endpoint = "";
    public String referenceId;
    public String Id;
    public String path;

    @Given("a url of (.*)")
    public void url(String url_param){
        endpoint = url_param;
    }

    @Given("path (.*)")
    public void pathTest(String path_param) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
      path = path_param;
        System.out.println(" ---- my path: " + path);
    }

    @Then("^I send a get request and the response contains error \"([^\"]*)\" and reason \"([^\"]*)\"$")
    public void i_send_a_get_request_and_the_response_contains_error_and_reason(String arg1, String arg2) throws Throwable {
        get(endpoint + path).then().body("error", is(arg1), "reason", is(arg2));
    }

    @Given("^I send a post request and the \"([^\"]*)\" is \"([^\"]*)\" and the \"([^\"]*)\" is \"([^\"]*)\" and the \"([^\"]*)\" is \"([^\"]*)\" and the \"([^\"]*)\" is \"([^\"]*)\"$")
    public void i_send_a_post_request_and_the_is_and_the_is_and_the_is_and_the_is(String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8) throws Throwable {

        JSONObject jsonObj = new JSONObject()
                .put(arg1,arg2)
                .put(arg3, arg4)
                .put(arg5, arg6)
                .put(arg7, arg8);

        given().
                contentType("application/json").
                body(jsonObj.toString()).   // use jsonObj toString method
                when().
                post(endpoint + path).
                then().extract().response().
                then().assertThat().statusCode(201);
    }


    @Then("^I store the first reference ID I can find$")
    public void getRefId() throws Throwable {

       response =
               when().
                        get(endpoint + path).
                        then().
                        extract().response(); // extract the response

        List<String> rows2 = response.path("rows.value.refID");
        referenceId = rows2.get(0);
        System.out.println("=== reference ID is: " + referenceId);

    }
    @Then("^I store the first ID I can find$")
    public void getPostId() throws Throwable {

        response =
                when().
                        get(endpoint + path).
                        then().
                        extract().response(); // extract the response

        List<String> rows2 = response.path("rows.value.id");
        Id = rows2.get(0);
        System.out.println("===  ID is: " + Id);

    }
    @Given("^the first entry ID exists$")
    public void the_first_entry_ID_exists() throws Throwable {

                when().
                        get(endpoint + path + Id).
                        then().extract().response().
                        then().assertThat().statusCode(200);
    }
    @Given("^I log in as user:\"([^\"]*)\" with password:\"([^\"]*)\"$")
    public void i_log_in_as_user_with_password(String arg1, String arg2) throws Throwable {

        JSONObject jsonObj = new JSONObject()

                .put("username", arg1)
                .put("password", arg2);

        given().
                contentType("application/json").
                body(jsonObj.toString()).   // use jsonObj toString method
                when().post(endpoint + path).
                then().extract().response().
                then().assertThat().statusCode(200);

        System.out.println(" ===  VALUE is: " + jsonObj.toString());
        System.out.println(" ===  user is: " + arg1);
        System.out.println(" ===  password is: " + arg2);
        System.out.println(" ===  url is: " + endpoint + path);
        System.out.println(" ===  status line is: " + response.getStatusLine().toString());
        System.out.println(" ===  body is: " + response.getHeaders().toString());

    }
    @Then("^I delete the first entry$")
    public void i_delete_the_first_entry() throws Throwable {

        given().

                when().delete(endpoint + path + Id + "?rev=" + referenceId).
                then().extract().response().
                then().assertThat().statusCode(200);
        System.out.println(" ===  status line is: " + response.getStatusLine().toString());
        System.out.println(" ===  status line is: " + response.getStatusCode());
        System.out.println(" ===  ID is: " + Id);
        System.out.println(" ===  url is: " + endpoint + path + Id);
    }
    @Then("^I create entries with a cvs file$")
    public void i_create_entries_with_a_cvs_file() throws Throwable {
      //  String response = RestAssured.given().multiPart("file2", new File("src/testing.json")).
                // /Users/kai/AndroidStudioProjects/mParrot_bak/google-translate-spreadsheet
//               given().contentType("application/json").
//                       when().multiPart("file2", new File("src/testing.json")).
//                //        given().contentType("multipart/mixed").
//                when().post(endpoint + path).then().extract().asString();
//        System.out.println("Response is : " + response);



        String jsonBody = generateStringFromResource("src/testing.json");
        given().
                contentType("application/json").
                body(jsonBody.toString()).   // use jsonObj toString method
                when().
                post(endpoint + path).
                then().extract().response().
                then().assertThat().statusCode(201);

    }
    public String generateStringFromResource(String path) throws IOException {

        return new String(Files.readAllBytes(Paths.get(path)));

    }



}
