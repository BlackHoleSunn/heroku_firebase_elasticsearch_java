

import java.awt.List;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;

public class ElasticSearchHelpers 
{
	// Below are some helpful curl commands you can run in terminal to enter data and test data
	//http://www.elasticsearchtutorial.com/elasticsearch-in-5-minutes.html
//	curl -XPUT 'https://2pxahvjc:1nstazytud2d39hb@apple-4398644.us-east-1.bonsaisearch.net/blog/post/1' -d '
//	{ 
//	    "user": "dilbert", 
//	    "postDate": "2011-12-15", 
//	    "body": "Search is hard. Search should be easy." ,
//	    "title": "On search"
//	}'
//	curl -XPUT 'https://2pxahvjc:1nstazytud2d39hb@apple-4398644.us-east-1.bonsaisearch.net/blog/user/dilbert' -d '{ "name" : "Dilbert Brown" }'	
//	curl -XGET https://2pxahvjc:1nstazytud2d39hb@apple-4398644.us-east-1.bonsaisearch.net/_cat/indices/
//	curl 'https://2pxahvjc:1nstazytud2d39hb@apple-4398644.us-east-1.bonsaisearch.net/blog/post/_search?q=user:dilbert&pretty=true'
	
	
	public static void ElasticSearchPutMessage(String server, Message message)
	{
		 try 
		 {
	            HttpClient httpClient = HttpClientBuilder.create().build();

	            HttpPut request = new HttpPut(server + "/messages/message/" + message.title.replace(' ', '_'));
	            
	            StringEntity jsonData = new StringEntity("{\"title\":\"" + message.title + "\", \"body\":\"" + message.body + "\"}", "UTF-8");
	            request.setEntity(jsonData);		
	            HttpResponse response = httpClient.execute(request);
	            System.out.println(response.toString());
	            
	            HttpEntity entity = response.getEntity();
	            System.out.println(entity.toString());
	            
	            String json_string = EntityUtils.toString(response.getEntity());
	            JSONObject temp1 = new JSONObject(json_string);

		} 
		catch (Exception e) 
		{
				System.out.println("ERROR:ElasticSearchPut " + e.toString());
				e.printStackTrace();
		}
	}
	
	public static void ElasticSearchPutMessage(String server, String key, ChatItem chatItem)
	{
		 try 
		 {
	            HttpClient httpClient = HttpClientBuilder.create().build();

	            HttpPut request = new HttpPut(server + "/messages/message/" + key);
	            
	            StringEntity jsonData = new StringEntity("{\"title\":\"" + chatItem.Date + "\", \"body\":\"" + chatItem.Message + "\"}", "UTF-8");
	            request.setEntity(jsonData);		
	            HttpResponse response = httpClient.execute(request);
	            System.out.println(response.toString());
	            
	            HttpEntity entity = response.getEntity();
	            System.out.println(entity.toString());
	            
	            String json_string = EntityUtils.toString(response.getEntity());
	            JSONObject temp1 = new JSONObject(json_string);

		} 
		catch (Exception e) 
		{
				System.out.println("ERROR:ElasticSearchPut " + e.toString());
				e.printStackTrace();
		}
	}
	
	
	public static ArrayList<String> ElasticSearch(String serverUrl, String queryBody)
    {
		ArrayList<String> retList = new ArrayList<String>();
        
        try 
        {
            HttpClient httpClient = HttpClientBuilder.create().build();

            //String httpRequestString = "http://localhost:9200/blog/post/_search?q=body:Lorem&pretty=true";
            String httpRequestString = serverUrl + "/messages/message/_search?q=body:" + queryBody + "&pretty=true";
            
            HttpPost request = new HttpPost(httpRequestString);

            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            
            HttpEntity entity = response.getEntity();
            System.out.println(entity.toString());
           
            String json_string = EntityUtils.toString(response.getEntity());
            JSONObject temp1 = new JSONObject(json_string);
            JSONObject hitsJsonObject = temp1.getJSONObject("hits");
            JSONArray hitsJsonArray = hitsJsonObject.getJSONArray("hits");
            
            if(hitsJsonArray.length() > 0)
            {
            	Object id = hitsJsonArray.getJSONObject(0).get("_id");
            	retList.add((String)id);
            }           
        } 
        catch (Exception e) 
        {
        	System.out.println("ERROR ElasticSearch " + e.toString());
        	e.printStackTrace();
        }
        return retList;
    }
	
	public static void ElasticSearchWithJavaAPI()
	{
		
	}
	
}
