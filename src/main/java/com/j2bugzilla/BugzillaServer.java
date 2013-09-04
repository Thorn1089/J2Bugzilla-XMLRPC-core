/**
 * 
 */
package com.j2bugzilla;

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
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransport;
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
	
	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#connectTo(java.lang.String)
	 */
	@Override
	public void connectTo(String host) {
		this.connectTo(host, null, null);
	}

	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#connectTo(java.net.URI)
	 */
	@Override
	public void connectTo(URI host) {
		this.connectTo(host, null, null);
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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
		
        /*
         * Here, we override the default behavior of the transport factory to properly
         * handle cookies for authentication
         */
		XmlRpcTransportFactory factory = new XmlRpcSunHttpTransportFactory(client) {
			
			private final XmlRpcTransport transport = new TransportWithCookies(client);
			
			public XmlRpcTransport getTransport() {
				return transport;
			}
		};
		client.setTransportFactory(factory);
	}

	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#getBugRepository()
	 */
	@Override
	public BugRepository getBugRepository() {
		return new BugRepoImpl(this);
	}

	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#getAttachmentRepository()
	 */
	@Override
	public AttachmentRepository getAttachmentRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#getCommentRepository()
	 */
	@Override
	public CommentRepository getCommentRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#getProductRepository()
	 */
	@Override
	public ProductRepository getProductRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#getLegalValuesFor(com.j2bugzilla.api.GlobalFields)
	 */
	@Override
	public Set<String> getLegalValuesFor(GlobalFields field) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#getVersion()
	 */
	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#getAccessibleProducts()
	 */
	@Override
	public Set<Product> getAccessibleProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.j2bugzilla.api.Bugzilla#logIn(java.lang.String, java.lang.String)
	 */
	@Override
	public void logIn(String user, String pass) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
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
	 * If the method completes properly, the {@link BugzillaMethod#setResultMap(Map)}
	 * method will be called, and the implementation class will provide
	 * methods to access any data returned. 
	 * 
	 * @param pMap The parameters for the call.
	 * @param method The name of the method to call.
	 * @return resultMap A map of the results. The caller of {@code fire()} is responsible for handling the conversion of this data into an object.
	 * @throws BugzillaException If the XML-RPC library returns a fault, a {@link BugzillaException}
	 * with a descriptive error message for that fault will be thrown.
	 */
	@SuppressWarnings("unchecked")//Must cast Object from client.execute()
	public Map<Object, Object> fire(String method, Map<Object, Object> pMap) throws BugzillaException {
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

	/**
	 * We need a transport class which will correctly handle cookies set by Bugzilla. This private
	 * subclass will appropriately set the Cookie HTTP headers.
	 * 
	 * @author Tom
	 *
	 */
	private static final class TransportWithCookies extends XmlRpcSunHttpTransport {

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
		 * This is the meat of these two overrides -- the HTTP header data now includes the 
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
	
}
