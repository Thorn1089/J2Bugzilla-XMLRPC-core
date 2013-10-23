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

@RunWith(MockitoJUnitRunner.class)
public class TestGetBug {
	
	@Mock
	BugzillaConnection bc;
	
	BugRepoImpl bri;
	
	@Test
	public void runTest()
	{
		//Build a returned bug by hand here.
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
		
		//Then put the array into a hashmap... just like it would be returned.
		HashMap<Object, Object> xmlRpcReturnBug = new HashMap<Object, Object>();
		xmlRpcReturnBug.put("bugs", bugsArray);
		
		//Build arguments to the bc.execute() method, to be used in the mocks
		Map<Object, Object> bugGetArgs = new HashMap<Object, Object>()
		
		
		when(bc.execute("Bug.get", )).thenReturn(xmlRpcReturnBug);
	}
}
