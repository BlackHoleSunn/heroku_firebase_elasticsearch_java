

import java.util.ArrayList;

public class Response 
{
	public String RequestId;
	public String RequesterId;
	public ArrayList<String> MessageIds;
	
	public Response(String requestId, String requesterId, ArrayList<String> messageIds)
	{
		RequestId = requestId;
		RequesterId = requesterId;
		MessageIds = messageIds;
	}
}
