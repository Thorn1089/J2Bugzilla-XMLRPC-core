package com.j2bugzilla.xmlrpc.integration;

import org.junit.Test;

import com.j2bugzilla.api.Bug;
import com.j2bugzilla.api.BugRepository;
import com.j2bugzilla.xmlrpc.core.BugzillaImpl;

public class TestGetBugIntegrated {
	@Test
	public void testGetBug(){
		BugzillaImpl bi = new BugzillaImpl();
		bi.connectTo("https://landfill.bugzilla.org/bugzilla-4.4-branch/xmlrpc.cgi");
		bi.logIn("trianos@gmail.com", "password");
		
		BugRepository br = bi.getBugRepository();
		
		Bug buggy = br.get(10983).get();
		System.out.println(buggy.getStatus());
		System.out.println(buggy.getSummary());
		bi.logOut();
	}
}