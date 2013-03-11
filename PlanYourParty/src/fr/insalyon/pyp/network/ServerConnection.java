package fr.insalyon.pyp.network;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import fr.insalyon.pyp.tools.AppTools;

public class ServerConnection {

	private static ServerConnection serverConnection = null;

	private String requestUrl;

	// base
	private static final String LOCAL_URL = "http://localhost:8000/api/";
	private static final String REMOTE_URL = "http://planyourpty.appspot.com/api/";

	static final boolean LOCAL = false;

	// services
	public static final String LOGIN = "login/";
	public static final String REGISTER = "register/";
	public static final String GET_SECRET_QUESTION_FOR_RECOVERY = "get_secret_question_for_recovery/";
	public static final String UPDATE_PASSWORD_AFTER_RECOVERY = "update_password_after_recovery/";
	public static final String CHECK_SECRET_ANSWER = "check_secret_answer/";
	public static final String UPDATE_PASSWORD = "update_password/";
	public static final String UPDATE_QUESTION = "update_secret_question/";
	public static final String GET_QUESTION_SECRET = "get_secret_question/";
	public static final String GETEVT = "get_places/";
	public static final String ADD_EVENT_INFO = "add_event_info/";
    public static final String UPDATE_PERSONAL_INFO = "update_user_info/";
    public static final String GET_PERSONAL_INFO = "get_full_user_info/";
    public static final String GET_INTRESTS_LIST = "get_intrests/";
    public static final String UPDATE_USER_INTREST = "update_intrests/";
    public static final String ADD_LOCAL_PLACE = "add_local_place/";
    public static final String GET_CURRENT_ADDRESS = "get_current_address/";
    public static final String GET_PERSONAL_EVENTS = "get_personal_events/";
    public static final String GET_EVENTS = "get_events/";
    public static final String GET_PLACES = "get_places/";
    public static final String SAVE_EVENT_PLACE = "save_event_place/";
    public static final String GET_EVENT_STATUS = "get_event_status/";
    public static final String CLOSE_OPEN_EVENT = "close_event/";
    public static final String DELETE_EVENT = "delete_event/";
    public static final String GET_EVENT_INFO = "get_event_info/";
    public static final String SAVE_EVENT_INTREST = "save_event_intrest/";
    public static final String GET_EVENT_INTREST = "get_event_intrest/";
    public static final String GET_LOCAL_PLACE = "get_local_place/";
    public static final String GET_EVENT_FULL_INFO = "get_event_full_info/";

	private ServerConnection() {
		if (LOCAL)
			setRequestUrl(LOCAL_URL);
		else
			setRequestUrl(REMOTE_URL);
	}

	public static ServerConnection GetServerConnection() {
		if (serverConnection == null)
			return new ServerConnection();
		else
			return serverConnection;
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
		if(service.equals(GET_INTRESTS_LIST))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_PERSONAL_EVENTS)){
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(DELETE_EVENT)){
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_LOCAL_PLACE)){
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(CLOSE_OPEN_EVENT)){
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		
		if(service.equals(GET_EVENT_STATUS)){
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(UPDATE_USER_INTREST)){
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_EVENT_INFO)){
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GETEVT))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(ADD_EVENT_INFO))
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
		if(service.equals(GET_SECRET_QUESTION_FOR_RECOVERY))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(UPDATE_PERSONAL_INFO))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(CHECK_SECRET_ANSWER))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(UPDATE_PASSWORD_AFTER_RECOVERY))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_PERSONAL_INFO))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(ADD_LOCAL_PLACE))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_CURRENT_ADDRESS))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_PLACES))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(SAVE_EVENT_PLACE))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(SAVE_EVENT_INTREST))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_EVENT_INTREST))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_EVENTS))
		{
			request = new HttpRequest(requestUrl, parameters);
			returnObj = request.executePost();
			AppTools.debug(returnObj.toString());
		}
		if(service.equals(GET_EVENT_FULL_INFO))
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
