package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

	public String executePost()
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
