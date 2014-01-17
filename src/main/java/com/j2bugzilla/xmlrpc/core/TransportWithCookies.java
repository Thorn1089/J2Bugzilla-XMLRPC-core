package com.j2bugzilla.xmlrpc.core;

import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;

/**
 * The {@link TransportWithCookies} decorates the {@link XmlRpcSunHttpTransport} with a simple
 * mechanism to preserve cookies across HTTP requests. Likely a proper RPC protocol would not depend
 * on cookies but instead on either Basic Auth or some other mechanism.
 * 
 * @author Tom
 *
 */
public class TransportWithCookies extends XmlRpcSunHttpTransport {

	private final Set<String> cookies = new HashSet<String>();
	
	/**
	 * Creates a new transport that wraps the given {@link XmlRpcClient}.
	 * @param pClient An {@code XmlRpcClient} used to make the requests.
	 */
	public TransportWithCookies(XmlRpcClient pClient) {
		super(pClient);
	}

	@Override
	protected void initHttpHeaders(XmlRpcRequest request) throws XmlRpcClientException {
		super.initHttpHeaders(request);
		StringBuilder cookieHeader = new StringBuilder();
		for(String cookie : cookies) {
			cookieHeader.append(cookie).append(',');
		}
		setRequestHeader("Cookie", cookieHeader.toString());
	}
	
	@Override
	protected void close() throws XmlRpcClientException {
		final URLConnection conn = getURLConnection();
		Map<String, List<String>> headers = conn.getHeaderFields();
		if(headers.containsKey("Set-Cookie")) {
			List<String> newCookies = headers.get("Set-Cookie");
			cookies.addAll(newCookies);
		}
		super.close();
	}

}
