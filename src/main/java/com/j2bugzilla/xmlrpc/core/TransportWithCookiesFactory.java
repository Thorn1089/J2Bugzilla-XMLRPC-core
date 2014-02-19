package com.j2bugzilla.xmlrpc.core;

import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcClient;

        /**
         * Here, we override the default behavior of the transport factory to properly
         * handle cookies for authentication
         */
public class TransportWithCookiesFactory extends XmlRpcSunHttpTransportFactory{
	private XmlRpcTransport transport;
	
	public TransportWithCookiesFactory(XmlRpcClient client){
		super(client);
		transport = new TransportWithCookies(client);
	}
	
	@Override
	public XmlRpcTransport getTransport() {
		return transport;
	}
}
