package fr.insalyon.pyp.network;

import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONObject;
import fr.insalyon.pyp.tools.AppTools;

public class ServerConnection {

	private static ServerConnection serverConnection = null;

	private String requestUrl;

	//base
	private static final String LOCAL_URL = "http://10.0.2.2:8080/api/";
	private static final String REMOTE_URL = "http://planyourpty.appspot.com/api/";

	static final boolean LOCAL = false;

	//services
	public static final String LOGIN = "login/";
	public static final String REGISTER = "register/";
	public static final String GET_SECRET_QUESTION_FOR_RECOVERY = "get_secret_question_for_recovery/";
	public static final String CHECK_SECRET_ANSWER = "check_secret_answer/";
	public static final String UPDATE_PASSWORD = "update_password/";
	public static final String UPDATE_QUESTION = "update_secret_question/";
    public static final String GET_QUESTION_SECRET = "get_secret_question/";

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

	public JSONObject connect(String service, List<NameValuePair> parameters) throws Exception {
		HttpRequest request = null;
		JSONObject returnObj = null;
		setRequestUrl(getRequestUrl() + service);
		try {
		if(service.equals(LOGIN))
			{
				request = new HttpRequest(requestUrl, parameters);
				returnObj = request.executePost();
				AppTools.debug(returnObj.toString());
			}
	
		if(service.equals(REGISTER))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(UPDATE_PASSWORD))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(UPDATE_QUESTION))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_QUESTION_SECRET))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		return returnObj;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getRequestUrl() {
		return requestUrl;
	}

	private void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
}
