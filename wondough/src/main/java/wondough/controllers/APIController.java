package wondough.controllers;

import java.util.*;
import java.sql.SQLException;

import static spark.Spark.*;
import spark.*;
import wondough.*;
import static wondough.SessionUtil.*;

/**
* Contains route handlers for API endpoints.
* @author The Intern
*/
public class APIController {
    /** Lists all transactions for a user. */
    public static Route getTransactions = (Request request, Response response) -> {
        // allow requests from anywhere
        response.header("Access-Control-Allow-Origin", "*");

        // retrieve the access token from the request
        String token = request.queryParams("token");

        try {
            Integer user = Program.getInstance().getDbConnection().isValidAccessToken(token);

            if(user == null) {
                return "Not a valid access token!";
            }

            return Program.getInstance().getDbConnection().getTransactions(user);
        }
        catch(SQLException ex) {
            return ex.toString();
        }
    };

    /** Transfers money from the user's account to another. */
    public static Route postTransaction = (Request request, Response response) -> {
        // allow requests from anywhere
        response.header("Access-Control-Allow-Origin", "*");

        // retrieve the access token from the request
        String token = request.queryParams("token");

        try {
            Integer user = Program.getInstance().getDbConnection().isValidAccessToken(token);

            if(user == null) {
                return "Not a valid access token!";
            }

            Integer recipient = Program.getInstance().getDbConnection().findUserByName(request.queryParams("recipient"));

            if(recipient == null) {
                halt(400, "Not a valid recipient!");
            }

            return Program.getInstance().getDbConnection().createTransaction(
                user, recipient, request.queryParams("description"), Float.parseFloat(request.queryParams("amount")));

            // create transaction
        }
        catch(SQLException ex) {
            return ex.toString();
        }
    };
}
