package com.j2bugzilla.xmlrpc.integration;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runners.JUnit4;

import com.j2bugzilla.xmlrpc.core.BugzillaImpl;

@RunWith(JUnit4.class)
public class TestConnect {
	
	@Test
	public void testLogin(){
		BugzillaImpl bi = new BugzillaImpl();
		bi.connectTo("https://landfill.bugzilla.org/bugzilla-4.4-branch/xmlrpc.cgi");
		bi.logIn("trianos@gmail.com", "password");
		bi.logOut();
	}
}
