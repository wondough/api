package sample.controllers;

import java.util.*;
import java.net.*;
import java.io.*;
import spark.*;
import sample.*;
import static sample.SessionUtil.*;

public class IndexController {
    // Serve the index page (GET request)
    public static Route serveIndexPage = (Request request, Response response) -> {
        if(getSessionCurrentUser(request) == null) {
            response.redirect("http://localhost:8000/auth?app=1&target=http://localhost:8080/oauth");
        }

        Map<String, Object> model = new HashMap<>();

        //response.header("Access-Control-Allow-Origin", "http://localhost:8000");

        return ViewUtil.render(request, model, "/velocity/index.vm");
    };

    // Serve the transaction page (GET request)
    public static Route serveAddTransactionPage = (Request request, Response response) -> {
        if(getSessionCurrentUser(request) == null) {
            response.redirect("http://localhost:8000/auth?app=1&target=http://localhost:8080/oauth");
        }

        Map<String, Object> model = new HashMap<>();

        //response.header("Access-Control-Allow-Origin", "http://localhost:8000");

        return ViewUtil.render(request, model, "/velocity/transaction.vm");
    };

    public static Route serveOAuthPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        String accessToken = exchangeToken(request.queryParams("token"));

        // check that an access token was received
        if(accessToken == null) {
            response.redirect("http://localhost:8000/auth?app=1&target=http://localhost:8080/oauth");
        }

        // store the access token in the session and make it available to
        // the browser so that the API can be accessed from JS
        request.session().attribute("currentUser", accessToken);
        response.cookie("accessToken", accessToken);

        // redirect back to the front page of the sample application to
        // discard of any remaining query parameters
        response.redirect("http://localhost:8080/");
        return null;
    };

    /**
    * Exchanges a request token for an access token.
    * @param requestToken The request token to exchange.
    */
    private static String exchangeToken(String requestToken) {
        try {
            // initialise the HTTP request
            URL url = new URL("http://localhost:8000/exchange");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");

            // write the request token to the request body
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            System.out.println(URLEncoder.encode("token") + "=" + URLEncoder.encode(requestToken));
            out.writeBytes(URLEncoder.encode("token") + "=" + URLEncoder.encode(requestToken));
            out.flush();
            out.close();

            // get the response status code
            int status = con.getResponseCode();

            if(status != 200) {
                return null;
            }

            // read the response, which should contain the access token
            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // close the HTTP connection
            con.disconnect();

            // return the access token
            return content.toString();
        }
        catch(IOException ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}
