/**
 * @author Tristan Marks
 * 
 */

package com.j2bugzilla.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.j2bugzilla.api.Bug;
import com.j2bugzilla.api.BugRepository;
import com.j2bugzilla.api.BugzillaException;
import com.j2bugzilla.api.Product;
import com.j2bugzilla.api.SearchParam;

public class BugRepoImpl implements BugRepository {
	
	private BugzillaServer bServ = null;
	
	protected BugRepoImpl(){}
	
	protected void setBugzillaServer(BugzillaServer bs)
	{
		this.bServ = bs;
	}

	public int create(Bug bug) {
		
		return 0;
	}

	/**
	 * @inheritDoc
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Optional<Bug> get(int id) {
		Map<Object, Object> result, params = new HashMap<Object, Object>();
		params.put("id", id);
		result = bServ.execute("Bug.get", params);
		if (result != null)
		{
			Map<String, Object> bug1 = (Map<String,Object>)result.get("bugs");
			
			
			//Bug.get returns the name of the product.
			//We need all the information about a product to make it a Product object.
			//So this requires another call.
			String pname = (String)bug1.get("product");
			
			params = new HashMap<Object, Object>();
			String[] namesArray = {pname};
			params.put("names", namesArray);
			result = bServ.execute("Product.get", params);
		
			Object[] prodArray = (Object[])result.get("products");
			Map<String,Object> prodMap = (Map<String, Object>)prodArray[0];

			Product prod = ProductRepoImpl.productFromMap(prodMap);
			
			Bug returnBug = BugRepoImpl.bugFromMap(bug1, prod);
			return Optional.<Bug>of(returnBug);
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
		//
		returnBug.setProduct((Product)m.get("product"));
		return returnBug;
	}

	public Set<Bug> getAll(int... ids) {
		HashSet<Bug> retrievedBugs = new HashSet<Bug>();
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
		// TODO Auto-generated method stub

	}

	public Set<Bug> search(SearchParam... params) {
		// TODO Auto-generated method stub
		return null;
	}
}