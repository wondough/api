package sample;

import spark.Spark;
import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import sample.controllers.*;

/**
* This class contains the main entry point for the application.
* @author  The Intern
* @version 0.1
*/
public class Program {
    /** Stores the singleton instance of this class. */
    private static Program program;

    /** Gets the singleton instance of this class. */
    public static Program getInstance() {
        return program;
    }

    /** Gets the port this server is running on. */
    public int port() {
        return Spark.port();
    };

    private int wondoughPort;

    /** Get the port the Wondough API server is running on. */
    public int wondoughPort() {
        return this.wondoughPort;
    }

    /** Explicitly mark constructor as private so no instances of this
    * class can be created elsewhere. */
    private Program() {

    }

    /**
    * The main entry point for the application.
    * @param args The command-line arguments supplied by the OS.
    */
    public static void main(String[] args) {
        // initialise and run the program
        program = new Program();
        program.run();
    }

    private void loadWondoughPort() {
        try {
            String home = System.getProperty("user.home");
            File file = Paths.get(home, ".wondough-api").toFile();
            
            // the file should not exist 
            if(!file.exists()) {
                throw new RuntimeException("The Wondough API does not appear to be running.");
            }

            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            this.wondoughPort = Integer.parseInt(br.readLine());
            br.close();
            reader.close();
        }
        catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
    * Runs the program.
    */
    private void run() {
        this.loadWondoughPort();
        
        // we will run on a random port
        Spark.port(0);

        // tell the Spark framework where to find static files
        staticFiles.location("/static");

        // map routes to controllers
        get("/", IndexController.serveIndexPage);
        get("/transaction/new", IndexController.serveAddTransactionPage);
        get("/oauth", IndexController.serveOAuthPage);

        awaitInitialization();

        System.out.printf("\n\nRunning at http://localhost:%d", Spark.port());
    }
}
