package com.j2bugzilla.xmlrpc.core;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

import com.j2bugzilla.api.AttachmentRepository;
import com.j2bugzilla.api.BugRepository;
import com.j2bugzilla.api.Bugzilla;
import com.j2bugzilla.api.CommentRepository;
import com.j2bugzilla.api.GlobalFields;
import com.j2bugzilla.api.Product;
import com.j2bugzilla.api.ProductRepository;

/**
 * The {@link BugzillaImpl} class is responsible for implementing the interface defined by the main
 * {@link Bugzilla} class. It provides an entry point for all other API functionality.
 * @author Tom
 *
 */
public class BugzillaImpl extends Bugzilla {

	private XmlRpcClient client;
	
	@Override
	public void connectTo(String host) {
		URI uri = URI.create(host);
		connectTo(uri);
	}

	@Override
	public void connectTo(URI host) {
		connectTo(host, null, null);
	}

	@Override
	public void connectTo(String host, String httpUser, String httpPass) {
		URI uri = URI.create(host);
		connectTo(uri, httpUser, httpPass);
	}

	@Override
	public void connectTo(URI host, String httpUser, String httpPass) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		if(httpUser != null && httpPass != null) {
			config.setBasicUserName(httpUser);
			config.setBasicPassword(httpPass);
		}
		try {
			config.setServerURL(host.toURL());
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Could not parse the provided URI", e);
		}
		client = new XmlRpcClient();
		client.setConfig(config);
		client.setTransportFactory(new XmlRpcTransportFactory() {
			private final XmlRpcTransport transport = new TransportWithCookies(client);
			public XmlRpcTransport getTransport() {
				return transport;
			}
		});
	}

	@Override
	public BugRepository getBugRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttachmentRepository getAttachmentRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentRepository getCommentRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductRepository getProductRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getLegalValuesFor(GlobalFields field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Product> getAccessibleProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logIn(String user, String pass) {
		if(client == null) {
			throw new IllegalStateException("Connect to a URI before logging in");
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("login", user);
		params.put("password", pass);
		try {
			client.execute("User.login", Collections.singletonList(params));
		} catch (XmlRpcException e) {
			throw new BugzillaTransportException("Could not log in", e);
		}
	}

	@Override
	public void logOut() {
		if(client == null) {
			throw new IllegalStateException("Connect to a URI before logging out");
		}
		
		try {
			client.execute("User.logout", Collections.singletonList(new HashMap<String, String>()));
		} catch (XmlRpcException e) {
			throw new BugzillaTransportException("Could not log out", e);
		}
	}
}
