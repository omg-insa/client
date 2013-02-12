package network;

import java.net.MalformedURLException;
import org.json.JSONObject;

public class ServerConnection {

	private static ServerConnection serverConnection = null;

	private String requestUrl;

	//base
	private static final String LOCAL_URL = "http://127.0.0.1:8000/";
	private static final String REMOTE_URL = "http://planyourpty.appspot.com/";

	static final boolean LOCAL = false;

	//services
	private static final String LOGIN = "login";

	private ServerConnection()
	{
		if(LOCAL)
			setRequestUrl(LOCAL_URL);
		else
			setRequestUrl(REMOTE_URL);
	}

	public static ServerConnection GetServerConnection()
	{
		if(serverConnection == null)
			return new ServerConnection();
		else return serverConnection;
	}

	public Object connect(String service, String parameters) {
		HttpRequest request = null;
		JSONObject returnObj = null;
		setRequestUrl(getRequestUrl() + service);
		try {
			if(service.equals(LOGIN))
			{
				request = new HttpRequest(requestUrl, parameters);
				returnObj = request.executePost();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnObj;
	}
	
	public Object connect(String service) {
		return this.connect(service, "");
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	private void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
}
