package com.j2bugzilla;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.j2bugzilla.api.Product;
import com.j2bugzilla.api.ProductFields;
import com.j2bugzilla.api.ProductRepository;

public class ProductRepoImpl implements ProductRepository {

	private BugzillaServer bServ = null;
	
	public ProductRepoImpl(BugzillaServer bServ)
	{
		this.bServ = bServ;
	}
	
	public int create(Product product) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Optional<Product> get(int id) {
		Map<Object,Object> prodParams = new HashMap<Object,Object>();
		prodParams.put("id", id);
		Map<Object,Object> resultMap = bServ.fire("Product.get", prodParams);
		
		Map<String,Object> resultStrObjMap = (HashMap<String,Object>)resultMap;
		
	}

	public Set<Product> getAll(int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(Product product) {
		// TODO Auto-generated method stub

	}

	public Set<String> getLegalValuesFor(ProductFields field, Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
