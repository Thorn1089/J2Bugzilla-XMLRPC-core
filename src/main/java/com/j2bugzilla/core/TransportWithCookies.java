package com.j2bugzilla.core;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;


public final class TransportWithCookies extends XmlRpcSunHttpTransport {

	/**
	 * A {@code List} of cookies received from the installation, used for authentication
	 */
	private List<String> cookies = new ArrayList<String>();
	
	/**
	 * Creates a new {@link TransportWithCookies} object.
	 * @param pClient The {@link XmlRpcClient} that does the heavy lifting.
	 */
	public TransportWithCookies(XmlRpcClient pClient) {
		super(pClient);
	}
	
	private URLConnection conn;
	
	protected URLConnection newURLConnection(URL pURL) throws IOException {
		conn = super.newURLConnection(pURL);
	return conn;
	}
	
	/**
	 * This is the meat of the two overrides -- the HTTP header data now includes the 
	 * cookies received from the Bugzilla installation on login and will pass them every
	 * time a connection is made to transmit or receive data.
	 */
	protected void initHttpHeaders(XmlRpcRequest request) throws XmlRpcClientException {
	super.initHttpHeaders(request);
	if(cookies.size()>0) {
		StringBuilder commaSep = new StringBuilder();
		
		for(String str : cookies) {
			commaSep.append(str);
			commaSep.append(",");
		}
		setRequestHeader("Cookie", commaSep.toString());
		
	}
	
}
	
	protected void close() throws XmlRpcClientException {
		getCookies(conn);
	}
	
	/**
	 * Retrieves cookie values from the HTTP header of Bugzilla responses
	 * @param conn
	 */
	private void getCookies(URLConnection conn) {
		if(cookies.size()==0) {
		Map<String, List<String>> headers = conn.getHeaderFields();
		if(headers.containsKey("Set-Cookie")) {//avoid NPE
			List<String> vals = headers.get("Set-Cookie");
			for(String str : vals) {
				cookies.add(str);
			}
			
		}	
		
	}

}
	
}