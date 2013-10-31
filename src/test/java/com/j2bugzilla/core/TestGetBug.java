package com.j2bugzilla.core;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.*;

import static org.mockito.Mockito.*;
import org.mockito.Mock;

import com.google.common.base.Optional;
import com.j2bugzilla.api.Bug;
import com.j2bugzilla.api.Product;

@RunWith(MockitoJUnitRunner.class)
public class TestGetBug {
	
	@Mock
	BugzillaConnection bc;
	
	BugRepoImpl bri;
	
	@Test
	public void runTest()
	{
		//
		// Build a returned Bug by hand here.
		//
		HashMap<Object, Object> testReturnBug = new HashMap<Object, Object>();
		testReturnBug.put("product", "J2Bugzilla");
		testReturnBug.put("alias", "i am an alias");
		testReturnBug.put("component", "J2BTest");
		testReturnBug.put("op_sys", "Windows");
		testReturnBug.put("platform", "LC3");
		testReturnBug.put("priority", "FIX IT NOW");
		testReturnBug.put("resolution", "");
		testReturnBug.put("severity", "Sort of Terribly Devastating");
		testReturnBug.put("status", "In Progress");
		testReturnBug.put("summary", "omg everything is breaking, who will save us from the dangers of bad code?!\n\nONE MAN... EVERYONE'S HERO... TRISTAN.");
		testReturnBug.put("version", "3");
		
		//Put it into an array...
		@SuppressWarnings("unchecked")
		HashMap<Object, Object>[] bugsArray = new HashMap[]{testReturnBug};
		
		//Then put the array into a hashmap, under the "bugs" element... just like it would be returned from the XML-RPC call.
		HashMap<Object, Object> xmlRpcReturnBug = new HashMap<Object, Object>();
		xmlRpcReturnBug.put("bugs", bugsArray);

		//And make BugzillaConnector.execute() return it.
		when(bc.execute(eq("Bug.get"), isA(Map.class))).thenReturn(xmlRpcReturnBug);
		
		//
		// Now construct a Product to return.
		//
		HashMap<Object, Object> testReturnProduct = new HashMap<Object, Object>();
		testReturnProduct.put("id", "3");
		testReturnProduct.put("name", "J2Bugzilla");
		testReturnProduct.put("description", "Access a Bugzilla installation from Java without worry about the under-the-hood XML-RPC");

		//Put it into an array...
		@SuppressWarnings("unchecked")
		HashMap<Object, Object>[] productArray = new HashMap[]{testReturnProduct};

		//Then put the array into a hashmap.
		HashMap<Object, Object> xmlRpcReturnProduct = new HashMap<Object, Object>();
		xmlRpcReturnProduct.put("products", productArray);
		
		//And make BugzillaConnector.execute() return it.
		when(bc.execute(eq("Product.get"), isA(Map.class))).thenReturn(xmlRpcReturnProduct);
		
		
		bri = new BugRepoImpl(bc);
		Optional<Bug> returnedBug = bri.get(1234);
		assertNotEquals(returnedBug.orNull(), null);
		
		assertEquals("i am an alias", returnedBug.get().getAlias().get());
		assertEquals("J2BTest", returnedBug.get().getComponent().get());
		assertEquals("Windows", returnedBug.get().getOperatingSystem().get());
		assertEquals("LC3", returnedBug.get().getPlatform().get());
		assertEquals("FIX IT NOW", returnedBug.get().getPriority().get());
		assertEquals("Sort of Terribly Devastating", returnedBug.get().getSeverity().get());
		assertEquals("In Progress", returnedBug.get().getStatus().get());
		assertEquals("3", returnedBug.get().getVersion().get());
		
		Product prod = returnedBug.get().getProduct().get();
		
		assertEquals("J2Bugzilla", prod.getName());
		
	}
}