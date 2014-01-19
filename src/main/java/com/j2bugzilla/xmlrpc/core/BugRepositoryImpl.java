package com.j2bugzilla.xmlrpc.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.j2bugzilla.api.Bug;
import com.j2bugzilla.api.BugRepository;
import com.j2bugzilla.api.Product;
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
		params.put("product", bug.getProduct().transform(new Function<Product, String>() {
			@Override
			public String apply(Product input) {
				return input.getName();
			}
		}).orNull());
		params.put("component", bug.getComponent().orNull());
		params.put("version", bug.getVersion().orNull());
		params.put("status", bug.getStatus().orNull());
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
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", id);
		
		try {
			@SuppressWarnings("unchecked")
			Map<Object, Object> response = (Map<Object, Object>) client.execute("Bug.get", Collections.singletonList(params));
			Object[] bugs = (Object[]) response.get("bugs");
			if(bugs.length == 0) {
				return Optional.absent();
			} else {
				@SuppressWarnings("unchecked")
				Map<String, Object> bug = (Map<String, Object>) bugs[0];
				Bug newBug = parseBug(bug);
				return Optional.of(newBug);
			}
		} catch (XmlRpcException e) {
			throw new BugzillaTransportException("Could not retrieve bug", e);
		}
	}

	@Override
	public Set<Bug> getAll(int... ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		
		Set<Bug> foundBugs = new HashSet<Bug>();
		try {
			@SuppressWarnings("unchecked")
			Map<Object, Object> response = (Map<Object, Object>) client.execute("Bug.get", Collections.singletonList(params));
			Object[] bugs = (Object[]) response.get("bugs");
			for(Object bug : bugs) {
				@SuppressWarnings("unchecked")
				Map<String, Object> bugMap = (Map<String, Object>)bug;
				foundBugs.add(parseBug(bugMap));
			}
			return foundBugs;
		} catch (XmlRpcException e) {
			throw new BugzillaTransportException("Could not retrieve bugs", e);
		}
	}

	@Override
	public void update(Bug bug) {
		Map<String, Object> params = new HashMap<String, Object>();
		put(params, "priority", bug.getPriority());
		put(params, "severity", bug.getSeverity());
		put(params, "alias", bug.getAlias());
		put(params, "summary", bug.getSummary());
		put(params, "product", bug.getProduct().transform(new Function<Product, String>() {
			@Override
			public String apply(Product input) {
				return input.getName();
			}
		}));
		put(params, "component", bug.getComponent());
		put(params, "version", bug.getVersion());
		put(params, "status", bug.getStatus());
		put(params, "resolution", bug.getResolution());
		put(params, "op_sys", bug.getOperatingSystem());
		put(params, "platform", bug.getPlatform());
		
		try {
			client.execute("Bug.update", Collections.singletonList(params));
		} catch (XmlRpcException e) {
			throw new BugzillaTransportException("Could not update bug", e);
		}
	}

	@Override
	public Set<Bug> search(SearchParam... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void put(Map<String, Object> map, String key, Optional<? extends Object> value) {
		if(value.isPresent()) {
			map.put(key, value.get());
		}
	}
	
	private Bug parseBug(Map<String, Object> bug) {
		Bug newBug = new Bug((Integer) bug.get("id"));
		newBug.setPriority((String)bug.get("priority"));
		newBug.setSeverity((String)bug.get("severity"));
		newBug.setAlias((String)bug.get("alias"));
		newBug.setSummary((String)bug.get("summary"));
		//TODO Retrieve the product this bug belongs to by the name returned
		newBug.setComponent((String)bug.get("component"));
		
		/*
		 * Older versions of Bugzilla didn't return the version in the regular
		 * hash -- check the 'internals'
		 */
		if(bug.containsKey("version")) {
			newBug.setVersion((String)bug.get("version"));
		} else {
			@SuppressWarnings("unchecked")
			Map<String, Object> internals = (Map<String, Object>)bug.get("internals");
			Object version = internals.get("version");
			if(version instanceof Double) {
				newBug.setVersion(Double.toString((Double)version));
			} else if(version instanceof String) {
				newBug.setVersion((String)version);
			}
		}
		
		newBug.setStatus((String)bug.get("status"));
		newBug.setResolution((String)bug.get("resolution"));
		newBug.setOperatingSystem((String)bug.get("op_sys"));
		newBug.setPlatform((String)bug.get("platform"));
		return newBug;
	}


}
