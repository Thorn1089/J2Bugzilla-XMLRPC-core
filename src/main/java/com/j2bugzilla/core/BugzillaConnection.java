package com.j2bugzilla.core;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

import com.j2bugzilla.api.BugzillaException;
import com.j2bugzilla.api.ConnectionException;
import com.j2bugzilla.api.XmlExceptionHandler;
import com.j2bugzilla.xmlrpc.core.TransportWithCookiesFactory;

/**
 * 
 * @author Tristan
 *
 * BugzillaConnection is the actual object which connects to and communicates 
 * with the server. Generally, these are instantiated and passed to the Repos 
 * (BugRepoImpl, ProductRepoImpl. etc.) in order for them to make calls.
 */
public class BugzillaConnection {

	private XmlRpcClient client;
	
	public BugzillaConnection(XmlRpcClient client)
	{
		this.client = client;
	}
	
	/**
	 * Allows the API to execute any properly encoded XML-RPC method.
	 * If the method completes properly, returns the Map of the results.
	 * It is up to the implementation class calling {@code execute} to know how to read it.
	 * 
	 * @param pMap The parameters for the call.
	 * @param method The name of the method to call.
	 * @return resultMap A map of the results. The caller of {@code fire()} is responsible for handling the conversion of this data into an object.
	 * @throws BugzillaException If the XML-RPC library returns a fault, a {@link BugzillaException}
	 * with a descriptive error message for that fault will be thrown.
	 */
	@SuppressWarnings("unchecked")//Must cast Object from client.execute()
	public Map<Object, Object> execute(String method, Map<Object, Object> pMap) throws BugzillaException {
		if(client == null) { 
			throw new IllegalStateException("Cannot execute a method without connecting!");
		}//We are not currently connected to an installation
		
		Object[] obj = {pMap};
		try {
			Object results = client.execute(method, obj);
			if(!(results instanceof Map<?, ?>)) { results = Collections.emptyMap(); }
			Map<Object, Object> readOnlyResults = Collections.unmodifiableMap((Map<Object, Object>)results);
			return readOnlyResults;
		} catch (XmlRpcException e) {
			BugzillaException wrapperException = XmlExceptionHandler.handleFault(e);
			throw wrapperException;
		}
	}
}