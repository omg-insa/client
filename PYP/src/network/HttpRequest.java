package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import lib.JSONParser;

import org.json.JSONObject;

public class HttpRequest {
	private URL url;
	private String urlParameters;
	private HttpURLConnection connection = null;

	public HttpRequest() {
		super();
	}

	public HttpRequest(URL url, String urlParameters) {
		super();
		this.urlParameters = urlParameters;
		try {
			this.url = url;
			connection = (HttpURLConnection)this.url.openConnection();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public HttpRequest(String url, String urlParameters) throws MalformedURLException {
		this(new URL(url), urlParameters);
	}

	public JSONObject executePost()
	{
		try {
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", "" + 
					Integer.toString(urlParameters.getBytes().length)); 

			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush();
			wr.close();

			//Get Response
			int code = connection.getResponseCode();
			
			switch(code)
			{
			case 400:
			case 200:
				InputStream is = connection.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				String line;
				StringBuffer response = new StringBuffer(); 
				while((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\r');
				}
				rd.close();
				return JSONParser.getJSONObject(response.toString());
			case 405:
				throw new Exception("Only POST is allowed!");
			default:
				throw new Exception("Response code unknown");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
	
	public String executeGet()
	{
		try {
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded"); 

			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			wr.flush();
			wr.close();

			//Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer(); 
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}


}
