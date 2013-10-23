/**
 * 
 */
package com.j2bugzilla.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
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

/**
 * @author Tristan
 *
 */
public class BugzillaServer extends Bugzilla {
	
	/**
	 * The {@link XmlRpcClient} handles all requests to Bugzilla by transforming method names and
	 * parameters into properly formatted XML documents, which it then transmits to the host.
	 */
	//private XmlRpcClient client;
	
	/**
	 * We store all the connection information - that is, the XmlRpcClient - in the BugzillaConnection object now.
	 * It gets passed to each Repository we create.
	 */
	private BugzillaConnection bc;
	
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
		bc = new BugzillaConnection();
		bc.connectTo(host, httpUser, httpPass);
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#getBugRepository()
	 */
	@Override
	public BugRepository getBugRepository() {
		checkLoggedIn();
		BugRepoImpl br = new BugRepoImpl(bc);
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
		checkLoggedIn();
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
		Map<Object, Object> params = new HashMap<Object,Object>();
		params.put("login", user);
		params.put("password", pass);
		Map<Object, Object> response = bc.execute("User.login", params);
		//We really don't need to store the user ID or the authenticator token -- the cookies work.
	}

	/**
	 * @see com.j2bugzilla.api.Bugzilla#logOut()
	 */
	@Override
	public void logOut() {
		// TODO Auto-generated method stub
	}

	/**
	 * Throws a ConnectionException if we are not logged in; otherwise, returns.
	 */
	private void checkLoggedIn()
	{
		// TODO Implement this check
		return;
	}
	
}
