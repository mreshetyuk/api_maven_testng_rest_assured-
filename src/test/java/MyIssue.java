import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;



import static com.jayway.restassured.RestAssured.given;

public class MyIssue {

    String sessionID = "";
    String key="";
    String newCommentId;


    @BeforeClass
    public void login() {
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        String body = "{\n" +
                "    \"username\": \"maximreshetyuk\",\n" +
                "    \"password\": \"maximreshetyuk\"\n" +
                "}";
        sessionID = given().
                contentType("application/json").
                body(body).
                when().
                post("/rest/auth/1/session").
                then().
                extract().
                path("session.value");
        // System.out.println("SESSIONID=" + sessionID);
    }

    @Test(priority = 10)
    public void createIssue(){
        // RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        String body =  "{\n" +
                "  \"fields\": { \n" +
                "  \"project\": { \n" +
                "  \"id\": \"10315\" \n"+
                "},\n"+
                "  \"summary\": \"Max_test\",\n"+
                "         \"issuetype\": {\n"+
                "      \"id\": \"10004\"\n"+
                "  },\n"+
                "  \"assignee\": {\n"+
                "      \"name\": \"maximreshetyuk\"\n"+
                "  },\n"+
                "  \"reporter\": {\n"+
                "      \"name\": \"maximreshetyuk\"\n"+
                "  }\n"+
                "}\n"+
                "}";

        key = given().
                        contentType("application/json").
                        header("Cookie", "JSESSIONID="+sessionID).
                        when().
                        body(body).
                        post("/rest/api/2/issue").
                        then().
                        assertThat().statusCode(201).extract().path("key");
        System.out.println("Key=" + key);


    }

    @Test(priority = 20)
    public void Add_comment (){
        // RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        String body =  "{\n" +
                "  \"body\": \"Adding  comment to issue  \" \n"+
                        "}";

            newCommentId =  given().
                        contentType("application/json").
                        header("Cookie", "JSESSIONID="+sessionID).
                        when().
                        body(body).
                        post("/rest/api/2/issue/" + key + "/comment").
            then().assertThat().statusCode(201).
            extract().path("id");

        System.out.println("ID=" + newCommentId);
    }




    @Test (priority = 30)
    public void deleteComment(){


        given().
                contentType("application/json").
                header("Cookie", "JSESSIONID="+sessionID).
                when().
                //body(body).
                delete("/rest/api/2/issue/" + key + "/comment/" + newCommentId).
                then().
                assertThat().statusCode(204);



    }







    @Test(priority = 40)
    public void deleteIssue(){


                given().
                        contentType("application/json").
                        header("Cookie", "JSESSIONID="+sessionID).
                        when().
                        //body(body).
                        delete("/rest/api/2/issue/"+ key).
                        then().
                        assertThat().statusCode(204);



    }



}