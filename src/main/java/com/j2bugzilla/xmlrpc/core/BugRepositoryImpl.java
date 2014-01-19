package com.j2bugzilla.xmlrpc.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import com.google.common.base.Optional;
import com.j2bugzilla.api.Bug;
import com.j2bugzilla.api.BugRepository;
import com.j2bugzilla.api.SearchParam;

/**
 * The {@link BugRepositoryImpl} is an XML-RPC based implementation of the {@link BugRepository} interface. It allows
 * submission and retrieval of bugs from a Bugzilla instance via the XML-RPC web service.
 * @author Tom
 *
 */
public class BugRepositoryImpl implements BugRepository {

	private final XmlRpcClient client;
	
	/**
	 * Creates a new {@link Bug RepositoryImpl} that uses the given {@link XmlRpcClient} to communicate with
	 * the remote server.
	 * @param client An {@link XmlRpcClient} used to perform web service calls.
	 */
	public BugRepositoryImpl(final XmlRpcClient client) {
		if(client == null) {
			throw new IllegalArgumentException("Cannot create a repository with a null client");
		}
		this.client = client;
	}
	
	@Override
	public int create(Bug bug) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("priority", bug.getPriority().orNull());
		params.put("severity", bug.getSeverity().orNull());
		params.put("alias", bug.getAlias().orNull());
		params.put("summary", bug.getSummary().orNull());
		params.put("product", bug.getProduct().orNull());
		params.put("component", bug.getComponent().orNull());
		params.put("version", bug.getVersion().orNull());
		params.put("status", bug.getVersion().orNull());
		params.put("resolution", bug.getResolution().orNull());
		params.put("op_sys", bug.getOperatingSystem().orNull());
		params.put("platform", bug.getPlatform().orNull());
		
		try {
			Object response = client.execute("Bug.create", Collections.singletonList(params));
			@SuppressWarnings("unchecked")
			Map<Object, Object> responseMap =  (Map<Object, Object>)response;
			return (Integer) responseMap.get("id");
		} catch (XmlRpcException e) {
			throw new BugzillaTransportException("Could not report bug", e);
		}
	}

	@Override
	public Optional<Bug> get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Bug> getAll(int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Bug bug) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Bug> search(SearchParam... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
