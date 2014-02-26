/**
 * @author Tristan Marks
 * 
 */

package com.j2bugzilla.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.j2bugzilla.api.Bug;
import com.j2bugzilla.api.BugRepository;
import com.j2bugzilla.api.BugzillaException;
import com.j2bugzilla.api.Product;
import com.j2bugzilla.api.SearchBy;
import com.j2bugzilla.api.SearchParam;

public class BugRepoImpl implements BugRepository {

	private BugzillaConnection bc;
	
	public BugRepoImpl(BugzillaConnection bc) {
		this.bc = bc;
	}
	
	public int create(Bug bug) {
		Map<Object, Object> result, params = new HashMap<Object, Object>();
		
		/*
		 * Required parameters
		 * Wondering if we should raise an exception or let Bugzilla fault.
		 * I think Bugzilla fault.
		 */
		
		if (bug.getProduct().isPresent())
			params.put("product", bug.getProduct().get().getName());
		if (bug.getComponent().isPresent())
			params.put("component", bug.getComponent().get());
		if (bug.getSummary().isPresent())
			params.put("summary", bug.getSummary().get());
		if (bug.getVersion().isPresent())
			params.put("version", bug.getVersion().get());
		
		
		/*
		 *  Defaulted parameters, can be omitted.
		 *
		 */
		
		/* Not included: description.
		 * An installation can require the description field, we won't know until we make the call.
		 * We don't support it in Bug right now.
		 */
		 //params.put("description", "");
		
		if (bug.getOperatingSystem().isPresent())
			params.put("op_sys", bug.getOperatingSystem().get());
		if (bug.getPlatform().isPresent())
			params.put("platform", bug.getPlatform().get());
		if (bug.getPriority().isPresent())
			params.put("priority", bug.getPriority().get());
		if (bug.getSeverity().isPresent())
			params.put("severity", bug.getSeverity().get());
		
		//Everything else we currently support.
		
		if (bug.getAlias().isPresent())
			params.put("alias", bug.getAlias().get());
		if (bug.getStatus().isPresent())
			params.put("status", bug.getStatus().get());
		if (bug.getResolution().isPresent())
			params.put("resolution", bug.getResolution().get());
		
		result = bc.execute("Bug.create", params);
		
		String newID = (String)result.get("id");
		return Integer.parseInt(newID);
	}
	

	/**
	 * @inheritDoc
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Optional<Bug> get(int id) throws BugzillaException {
		Map<Object, Object> result, params = new HashMap<Object, Object>();

		//Bug.get requires as input:
		//"ids" -- a list of IDs/aliases for bugs to retrieve
		Object[] bugIds = new Object[]{id};
		params.put("ids", bugIds);
		result = bc.execute("Bug.get", params);
		if (result != null)
		{
			Map<String, Object>[] bugs = (HashMap<String, Object>[])result.get("bugs");
			Map<String, Object> bug1 = bugs[0];
			
			
			//Bug.get returns the name of the product.
			//We need all the information about a product to make it a Product object.
			//So this requires another call.
			String pname = (String)bug1.get("product");
			
			params = new HashMap<Object, Object>();
			String[] namesArray = {pname};
			params.put("names", namesArray);
			result = bc.execute("Product.get", params);
		
			Map<String, Object>[] prods = (HashMap<String, Object>[])result.get("products");
			Map<String,Object> prodMap = (HashMap<String, Object>)prods[0];

			Product prod = ProductRepoImpl.productFromMap(prodMap);
			
			Bug returnBug = BugRepoImpl.bugFromMap(bug1, prod);
			return Optional.of(returnBug);
		}
		
		
		return Optional.<Bug>absent();
		
	}
	
	/**
	 * Creates a {@link com.j2bugzilla.api.Bug} out of a Map resulting from a call to Bug.create or Bug.get.
	 * It's private because it should just be an implementation detail. We don't want consumers using it.
	 * 
	 * @param m A Map representing a single bug. Basically, an element of the "bugs" array in the XML result of a call.
	 * @param p a Product, which will be added to the created {@code Bug}. A little awkward, but honestly the best way I could think of.
	 * @return A newly created Bug.
	 */
	private static Bug bugFromMap(Map<String, Object> m, Product p)
	{
		Bug returnBug = new Bug();
		
		returnBug.setAlias((String)m.get("alias"));
		returnBug.setComponent((String)m.get("component"));
		returnBug.setOperatingSystem((String)m.get("op_sys"));
		returnBug.setPlatform((String)m.get("platform"));
		returnBug.setPriority((String)m.get("priority"));
		returnBug.setResolution((String)m.get("resolution"));
		returnBug.setSeverity((String)m.get("severity"));
		returnBug.setStatus((String)m.get("status"));
		returnBug.setSummary((String)m.get("summary"));
		
		//In this version, "version" is in the regular bug map; in other versions it might be under the "internals" map which is in the bug's map.
		//At time of this comment, don't know what version this IS... we will figure it out later...
		returnBug.setVersion((String)m.get("version"));
		returnBug.setProduct(p);
		//
		return returnBug;
	}

	public Set<Bug> getAll(int... ids) {
		Set<Bug> retrievedBugs = new HashSet<Bug>();
		for (int bugId : ids)
		{
			Optional<Bug> b = get(bugId);
			if (b.isPresent())
			{
				retrievedBugs.add(b.get());
			}
		}
		return retrievedBugs;
	}

	public void update(Bug bug) {
		Map<Object, Object> result, params = new HashMap<Object, Object>();
		
		int[] ids = {bug.getId().get()};
		params.put("ids", ids);			//This will be the ID of the bug we are changing.
		
		//The rest are the values we're setting things to.
		
		if (bug.getAlias().isPresent())
			params.put("alias", bug.getAlias().get());
		
		if (bug.getComponent().isPresent())
			params.put("component", bug.getComponent().get());
		
		if (bug.getOperatingSystem().isPresent())
			params.put("opSys", bug.getOperatingSystem().get());
		
		if (bug.getPlatform().isPresent())
			params.put("platform", bug.getPlatform().get());
		
		if (bug.getPriority().isPresent())
			params.put("priority", bug.getPriority().get());
		
		if (bug.getResolution().isPresent())
			params.put("resolution", bug.getResolution().get());
		
		if (bug.getSeverity().isPresent())
			params.put("severity", bug.getSeverity().get());
		
		if (bug.getStatus().isPresent())
			params.put("status", bug.getStatus().get());
		
		if (bug.getSummary().isPresent())
			params.put("summary", bug.getSummary().get());
		
		if (bug.getVersion().isPresent())
			params.put("version", bug.getVersion().get());
		
		result = bc.execute("Bug.update", params);
		
		
	}

	/**
	 * Search for bugs in the Bugzilla database.
	 * This method takes an arbitrary number of {@code SearchParam}s denoting the fields to be searched.
	 * The Webservice method can take either an element, or a list of elements, for each criterion.
	 * When a list is given, bugs are returned which match any of the items given for the criterion.
	 * As such, multiples of any {@code SearchBy} can be passed in.
	 */
	public Set<Bug> search(SearchParam... params) {
		Map<Object, Object> results, parameters = new HashMap<Object, Object>();
		
		Map<SearchBy, List<String>> searchBys = new HashMap<SearchBy, List<String>>();
		
		for (SearchBy sb : SearchBy.values())
		{
			searchBys.put(sb, new ArrayList<String>());
		}
		
		for (SearchParam sp : params)
		{
			searchBys.get(sp.getDimension()).add(sp.getQuery());
		}
		
		for (SearchBy sb : SearchBy.values())
		{
			List<String> sbl = searchBys.get(sb);
			if (sbl.size() == 0) continue;
			
			else if (sbl.size() == 1)
			{
				parameters.put(sb.getName(), sbl.get(0));
			}
			
			else 
			{
				parameters.put(sb.getName(), sbl);
			}
		}
		
		results = bc.execute("Bug.search", parameters);
		
		Set<Bug> foundBugs = new HashSet<Bug>();
		
		@SuppressWarnings("unchecked")
		Map<String, Object>[] bugs = (Map<String, Object>[])results.get("bugs");
		
		for (Map<String, Object> bug : bugs)
		{
			String pname = (String)bug.get("product");
			
			parameters = new HashMap<Object, Object>();
			String[] namesArray = {pname};
			parameters.put("names", namesArray);
			Map<Object, Object> prodResult = bc.execute("Product.get", parameters);
		
			@SuppressWarnings("unchecked")
			Map<String, Object>[] prods = (HashMap<String, Object>[])prodResult.get("products");
			Map<String,Object> prodMap = (HashMap<String, Object>)prods[0];

			//There could be a way to make this a little more efficient.
			//Such as caching the products, and checking that cache before making yet another call.
			//After all, it could be the case that "product" was a specified search parameter...
			Product prod = ProductRepoImpl.productFromMap(prodMap);
			foundBugs.add(bugFromMap(bug, prod));
		}
		
		return foundBugs;
		
	}
}