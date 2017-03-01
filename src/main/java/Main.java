import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.Log;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

//import mavengroupid.mavenartifactid.ChatItem;
//import mavengroupid.mavenartifactid.ElasticSearchHelpers;
//import mavengroupid.mavenartifactid.Request;
//import mavengroupid.mavenartifactid.Response;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

	public static String ELASTICSEARCH_SERVER_URL = "https://2pxahvjc:1nstazytud2d39hb@apple-4398644.us-east-1.bonsaisearch.net";
	public static String ELASTICSEARCH_SERVER_URL_LOCAL = "http://localhost:9200";
	
  public static void main(String[] args) {

//    port(Integer.valueOf(System.getenv("PORT")));
//    staticFileLocation("/public");
//
//    get("/hello", (req, res) -> "Hello World");
//
//    get("/", (request, response) -> {
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("message", "Hello World!");
//
//        return new ModelAndView(attributes, "index.ftl");
//    }, new FreeMarkerEngine());
//
//    HikariConfig config = new  HikariConfig();
//    config.setJdbcUrl(System.getenv("JDBC_DATABASE_URL"));
//    final HikariDataSource dataSource = (config.getJdbcUrl() != null) ?
//      new HikariDataSource(config) : new HikariDataSource();
//
//    get("/db", (req, res) -> {
//      Map<String, Object> attributes = new HashMap<>();
//      try(Connection connection = dataSource.getConnection()) {
//        Statement stmt = connection.createStatement();
//        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
//        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
//        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
//
//        ArrayList<String> output = new ArrayList<String>();
//        while (rs.next()) {
//          output.add( "Read from DB: " + rs.getTimestamp("tick"));
//        }
//
//        attributes.put("results", output);
//        return new ModelAndView(attributes, "db.ftl");
//      } catch (Exception e) {
//        attributes.put("message", "There was an error: " + e);
//        return new ModelAndView(attributes, "error.ftl");
//      }
//    }, new FreeMarkerEngine());

	  System.out.println("Hello");
	  
	  System.out.println( "Main Started" );
      
      InitFirebase();

      
     
      
      InitRequestListener();
      
      ArrayList<String> searchMatches = ElasticSearchHelpers.ElasticSearch(ELASTICSEARCH_SERVER_URL, "A");
      
      Request request = new Request();
      request.bodyQuery = "A";
      request.node = "NOT SUPPORTED";
      request.requesterId = "Server";
      request.requestId = "SERVER_REQUEST";
      AddRequest(request);
      
      AddSearchMatchsToResponseQueue("TEST_REQUEST", "TESTREQUESTER", searchMatches);
      
      //AddDataToElasticSearch();
  }
  
  public static void AddRequest(Request request)
  {
  	FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance();
      DatabaseReference databaseReference = defaultDatabase.getReference("elasticSearchRequests/" + request.requestId);
      databaseReference.setValue(request);
  }
  
  public static void InitRequestListener()
  {
  	FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance();
      DatabaseReference databaseReference = defaultDatabase.getReference("elasticSearchRequests");
               
      databaseReference.addChildEventListener(new ChildEventListener()	
      {
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             Log.d("Data onChildAdded", dataSnapshot.getValue().toString());
             System.out.println( "Data onChildAdded" + dataSnapshot.getValue().toString());
             Request request = dataSnapshot.getValue(Request.class);
             ArrayList<String> searchMatches = ElasticSearchHelpers.ElasticSearch(ELASTICSEARCH_SERVER_URL, request.bodyQuery);
             
             AddSearchMatchsToResponseQueue(request.requestId, request.requesterId, searchMatches);
             dataSnapshot.getRef().removeValue();
             
             
             //ElasticSearchHelpers.ElasticSearchPutMessage(ELASTICSEARCH_SERVER_URL, dataSnapshot.getKey(), );
         }

         public void onChildChanged(DataSnapshot dataSnapshot, String s) 
         {
            
         }

         public void onChildRemoved(DataSnapshot dataSnapshot) 
         {

         }

         public void onChildMoved(DataSnapshot dataSnapshot, String s) 
         {

         }
         
         public void onCancelled(DatabaseError arg0) 
         {
      	   System.out.println("Data onCancelled" + arg0.getDetails());
             
         }
		});
  }
  
  public static void AddSearchMatchsToResponseQueue(String requestId, String requesterId, ArrayList<String> searchMatches) 
  {
		if(FirebaseInited)
		{
			Response response = new Response(requestId, requesterId, searchMatches);
			FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance();
          DatabaseReference databaseReference = defaultDatabase.getReference("elasticSearchResponses");
          
          databaseReference.push().setValue(response);            
		}
	}

	public static boolean FirebaseInited = false;
  public static void InitFirebase()
  {
  	System.out.println( "InitFirebase1");
      
      try 
      {
          if(!FirebaseInited) 
          {
          	System.out.println( "InitFirebase2");
              
              FileInputStream serviceAccount = new FileInputStream("BigSlickChat-8f3b5b2c8560.json");
              FirebaseOptions options = new FirebaseOptions.Builder()
                      .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                      .setDatabaseUrl("https://bigslickchat.firebaseio.com/")
                      .build();

              FirebaseApp.initializeApp(options);
              System.out.println( "InitFirebase3");
              
              
              
              FirebaseInited = true;
          }
          
          
          		
          System.out.println( "InitFirebase5");
          	
      }
      catch (Exception e)
      {
          System.out.print(e.toString());
          
      }		      

      System.out.println( "InitFirebase6");
              
  }
  
  private static void AddDataToElasticSearch()
  {
  	FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance();
      DatabaseReference databaseReference = defaultDatabase.getReference("messages/Bri/chatitems");
      //databaseReference.setValue("HELEELLELEEOEOEO from the server");
      System.out.println( "InitFirebase4");
      
      databaseReference.addChildEventListener(new ChildEventListener()	
      {
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             Log.d("Data onChildAdded", dataSnapshot.getValue().toString());
             System.out.println( "Data onChildAdded" + dataSnapshot.getValue().toString());
             
             ElasticSearchHelpers.ElasticSearchPutMessage(ELASTICSEARCH_SERVER_URL, dataSnapshot.getKey(), dataSnapshot.getValue(ChatItem.class));
         }

         public void onChildChanged(DataSnapshot dataSnapshot, String s) {
             Log.d("Data onChildChanged", dataSnapshot.getValue().toString());
         }

         public void onChildRemoved(DataSnapshot dataSnapshot) {
             Log.d("Data onChildRemoved", dataSnapshot.getValue().toString());

         }

         public void onChildMoved(DataSnapshot dataSnapshot, String s) {
             Log.d("Data onChildMoved", dataSnapshot.getValue().toString());

         }
         
			public void onCancelled(DatabaseError arg0) {
				System.out.println("listener was cancelled");
			}
		});
  }
}
