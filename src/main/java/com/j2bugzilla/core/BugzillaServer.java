/**
 * 
 */
package com.j2bugzilla.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.j2bugzilla.api.AttachmentRepository;
import com.j2bugzilla.api.BugRepository;
import com.j2bugzilla.api.Bugzilla;
import com.j2bugzilla.api.CommentRepository;
import com.j2bugzilla.api.ConnectionException;
import com.j2bugzilla.api.GlobalFields;
import com.j2bugzilla.api.Product;
import com.j2bugzilla.api.ProductRepository;
import com.j2bugzilla.api.BugzillaException;
//import com.j2bugzilla.base.BugzillaMethod;
import com.j2bugzilla.api.XmlExceptionHandler;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

/**
 * @author Tristan
 *
 */
public class BugzillaServer extends Bugzilla {
	
	/**
	 * The {@link XmlRpcClient} handles all requests to Bugzilla by transforming method names and
	 * parameters into properly formatted XML documents, which it then transmits to the host.
	 */
	private XmlRpcClient client;
	
	/**
	 * @see com.j2bugzilla.api.Bugzilla#connectTo(java.lang.String)
	 */
	@Override
	public void connectTo(String host) {
		this.connectTo(host, null, null);
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#connectTo(java.net.URI)
	 */
	@Override
	public void connectTo(URI host) {
		this.connectTo(host, null, null);
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#connectTo(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void connectTo(String host, String httpUser, String httpPass) {
		URI hostURI;
		try {
			hostURI = new URI(host);
		} catch (URISyntaxException use) {
			// TODO Auto-generated catch block
			throw new ConnectionException("Failed to parse URI (URISyntaxException) - URI was: " + host, use);
		}
		connectTo(hostURI, httpUser, httpPass);
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#connectTo(java.net.URI, java.lang.String, java.lang.String)
	 */
	@Override
	public void connectTo(URI host, String httpUser, String httpPass) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		if (httpUser != null) {
			config.setBasicUserName(httpUser);
			config.setBasicPassword(httpPass);
		}
		URL hostURL;
		try {
				hostURL = host.toURL();
		} catch (MalformedURLException e) {
				throw new ConnectionException("Failed to convert host URI to URL: " + host.toString(), e);
		}
		config.setServerURL(hostURL);
		
		client = new XmlRpcClient();
		client.setConfig(config);

		XmlRpcTransportFactory factory = new TransportWithCookiesFactory(client);
		client.setTransportFactory(factory);
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#getBugRepository()
	 */
	@Override
	public BugRepository getBugRepository() {
		BugRepoImpl br = new BugRepoImpl();
		br.setBugzillaServer(this);
		return br;
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#getAttachmentRepository()
	 */
	@Override
	public AttachmentRepository getAttachmentRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#getCommentRepository()
	 */
	@Override
	public CommentRepository getCommentRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#getProductRepository()
	 */
	@Override
	public ProductRepository getProductRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#getLegalValuesFor(com.j2bugzilla.api.GlobalFields)
	 */
	@Override
	public Set<String> getLegalValuesFor(GlobalFields field) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#getVersion()
	 */
	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#getAccessibleProducts()
	 */
	@Override
	public Set<Product> getAccessibleProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#logIn(java.lang.String, java.lang.String)
	 */
	@Override
	public void logIn(String user, String pass) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#logOut()
	 */
	@Override
	public void logOut() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Allows the API to execute any properly encoded XML-RPC method.
	 * If the method completes properly, returns the Map of the results.
	 * It is up to the implementation class calling {@code fire} to know how to read it.
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
