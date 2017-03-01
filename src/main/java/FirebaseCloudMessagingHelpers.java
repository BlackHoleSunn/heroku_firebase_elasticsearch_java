

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public class FirebaseCloudMessagingHelpers 
{
	/***
	 * Sends a message with FCM to a specific device. 
	 * Was working as expected except when app was closed after debugging. 
	 * To make it work in this case. Close the app, run it again and close. 
	 * Something about how stopping the debugger caused it to quit in a weird way that stops notifications.
	 * @param message
	 * @param serverKey this is found in Firebase->Project Settings->CloudMessaging->Server Key
	 * @param name
	 * @param receiverDeviceToken InstanceId.SharedInstance.Token if you are running the app on an iOS device.
	 */
	public static void SendMessage(String m, String serverKey, String receiverDeviceToken)
    {
        // Declaration of Message Parameters
        String message_url = new String("https://fcm.googleapis.com/fcm/send");
        String message_sender_id = receiverDeviceToken;
        String message_key = serverKey;
        // Send Protocol
        try 
        {
        	
	        // Generating a JSONObject for the content of the message
	        JSONObject message = new JSONObject();
	        message.put("message", m);
	        JSONObject protocol = new JSONObject();
	        protocol.put("to", message_sender_id);
	        protocol.put("data", message);

        
            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpPost request = new HttpPost(message_url);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", message_key);

            StringEntity params = new StringEntity(protocol.toString());
            request.setEntity(params);
            System.out.println(params);

            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
        } catch (Exception e) 
        {
        	 System.out.println("ERROR: " + e.toString());
        	 e.printStackTrace();
        }
    }
}
