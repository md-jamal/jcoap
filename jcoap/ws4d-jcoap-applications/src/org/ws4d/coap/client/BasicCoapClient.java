package org.ws4d.coap.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.ws4d.coap.Constants;
import org.ws4d.coap.connection.BasicCoapChannelManager;
import org.ws4d.coap.interfaces.CoapChannelManager;
import org.ws4d.coap.interfaces.CoapClient;
import org.ws4d.coap.interfaces.CoapClientChannel;
import org.ws4d.coap.interfaces.CoapRequest;
import org.ws4d.coap.interfaces.CoapResponse;
import org.ws4d.coap.messages.CoapEmptyMessage;
import org.ws4d.coap.messages.CoapRequestCode;

/**
 * @author Christian Lerche <christian.lerche@uni-rostock.de>
 */

//the class is implementing the coapclient interface
public class BasicCoapClient implements CoapClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = Constants.COAP_DEFAULT_PORT;
    static int counter = 0;
    CoapChannelManager channelManager = null;
    CoapClientChannel clientChannel = null;

    public static void main(String[] args) {
        System.out.println("Start CoAP Client");
	//created a new object of the same class
        BasicCoapClient client = new BasicCoapClient();
	//getInstance will return the object of the BasicCoapChannelManager
        client.channelManager = BasicCoapChannelManager.getInstance();
        client.runTestClient();
    }
    
    public void runTestClient(){
    	try {
			//returns a coapclient channel(BasicCoapClientChannel)
			clientChannel = channelManager.connect(this, InetAddress.getByName(SERVER_ADDRESS), PORT);
			//creating a Coap Request Message with reliability(CON)
			//GET method is used to retrieve resources from  nodes
			CoapRequest coapRequest = clientChannel.createRequest(true, CoapRequestCode.GET);
			//coapRequest.setContentType(CoapMediaType.octet_stream);
			//Next few lines are mainly for setting the message fields
			coapRequest.setToken("ABCD".getBytes());
			coapRequest.setUriHost("123.123.123.123");
			coapRequest.setUriPort(1234);
			coapRequest.setUriPath("/sub1/sub2/sub3/");
			coapRequest.setUriQuery("a=1&b=2&c=3");
			coapRequest.setProxyUri("http://proxy.org:1234/proxytest");
			//sending the Message
			clientChannel.sendMessage(coapRequest);
			System.out.println("Sent Request");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void onConnectionFailed(CoapClientChannel channel, boolean notReachable, boolean resetByServer) {
		System.out.println("Connection Failed");
	}

	@Override
	public void onResponse(CoapClientChannel channel, CoapResponse response) {
		System.out.println("Received response");
		System.out.println(response.toString());

	}
}
