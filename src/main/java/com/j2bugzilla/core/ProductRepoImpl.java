package com.j2bugzilla.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.j2bugzilla.api.Product;
import com.j2bugzilla.api.ProductFields;
import com.j2bugzilla.api.ProductRepository;

public class ProductRepoImpl implements ProductRepository {

	private BugzillaConnection bc = null;
	
	public ProductRepoImpl(BugzillaConnection bc)
	{
		this.bc = bc;
	}
	
	public int create(Product product) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Optional<Product> get(int id) {
		Map<Object,Object> prodParams = new HashMap<Object,Object>();
		prodParams.put("id", id);
		Map<Object,Object> resultMap = bc.execute("Product.get", prodParams);
		
		Object[] prodArray = (Object[])resultMap.get("products");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> prodHash = (Map<String,Object>)prodArray[0];
		
		Product prod = new Product(id, (String)prodHash.get("name"), (String)prodHash.get("description"));
		return Optional.of(prod);
		
		
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
	 * 
	 * Takes a {@code Map} representing a single Product, returned by the Product.get method of Bugzilla.
	 * That is, a single hash representing one product, or an item in the {@code products} array.
	 * See http://www.bugzilla.org/docs/tip/en/html/api/Bugzilla/WebService/Product.html#get
	 * @param prodMap
	 * @return
	 */
	protected static Product productFromMap(Map<String, Object> prodMap)
	{
		int id;
		String name, description;
		
		id = (int)prodMap.get("id");
		
		name = (String)prodMap.get("name");
		
		//TODO: the Web Service doc says this may contain HTML. We may need to handle that somehow...
		description = (String)prodMap.get("description");
		
		
		Product newProduct = new Product(id, name, description);
		
		return newProduct;
	}
}
