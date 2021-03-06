package eu.chitic.syp.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import eu.chitic.syp.lib.JSONParser;
import eu.chitic.syp.tools.AppTools;
import eu.chitic.syp.tools.PYPContext;

public class HttpRequest {
	private String url;
	private List<NameValuePair> urlParameters;
	private HttpClient httpClient;

	public HttpRequest() {
	}

	public HttpRequest(String url, List<NameValuePair> urlParameters) {
		this.url = url;
		this.urlParameters = urlParameters;
		// Creating HTTP client
		this.httpClient = new DefaultHttpClient();
		// Creating HTTP Post
	}

	public JSONObject executePost() throws Exception {
		HttpPost httpPost = new HttpPost(this.url);
		// Url Encoding the POST parameters
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (UnsupportedEncodingException e) {
			// writing error to Log
			e.printStackTrace();
		}
		// Making HTTP Request
		try {
			HttpResponse response = httpClient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			switch (code) {
			case 400:
			case 200:
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String responseBody = EntityUtils.toString(entity);
					return JSONParser.getJSONObject(responseBody);
				}
				return null;
			case 403:
				SharedPreferences settings = PYPContext.getContext()
						.getSharedPreferences(AppTools.PREFS_NAME, 0);
				Editor editor = settings.edit();
				editor.remove("auth_token");
				editor.commit();
				throw new Exception("403");
			case 405:
				throw new Exception("405");
			default:
				throw new Exception("Unknown");
			}
		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject executeGet() throws Exception {
		boolean is_first = true;
		if (urlParameters != null) {
			for (NameValuePair el : urlParameters) {
				if (is_first) {
					is_first = false;
					this.url += '?';
				} else {
					this.url += '&';
				}
				this.url += el.getName() + '=' + el.getValue();

			}
		}
		HttpGet httpGet = new HttpGet(this.url);
		// Making HTTP Request
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			AppTools.debug("Status code :" + code);
			switch (code) {
			case 400:
			case 200:
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String responseBody = EntityUtils.toString(entity);
					return JSONParser.getJSONObject(responseBody);
				}
				return null;
			case 403:
				throw new Exception("403");
			case 405:
				throw new Exception("405");
			default:
				throw new Exception("Unknown");
			}
		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		}
		return null;
	}
}
