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
	BugzillaServer bugSer;
	
	BugRepoImpl bri;
	
	@Test
	public void runTest()
	{
		doAnswer(new Answer<Void>()
				{
					public Void answer(InvocationOnMock invocation)
							throws Throwable {
						// TODO Auto-generated method stub
						return null;
					}
				}).when(bugSer).execute("Bug.get", new HashMap<Object, Object>());
	}
}
