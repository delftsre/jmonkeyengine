package com.jme3.network;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.jme3.network.kernel.KernelException;

public class ThroughputTest {
	
	private long recvMsgs;
	private long msgsToSend = 1000;
	
	@Before
	public void setup() throws InterruptedException {
		recvMsgs = 0;
		Thread.sleep(500);
	}

	@Test
	public void testThroughputReliable() throws IOException, InterruptedException {
		testThroughPut(msgsToSend);
		assertEquals("must received as many messages as send ", msgsToSend, recvMsgs);
	}
	
	public void testThroughPut(long numMsg) throws IOException, InterruptedException {
//        Serializer.registerClass(TestMessage.class);
//        Serializer.registerClass(ComplexClass.class);
//        Serializer.registerClass(ComplexSubClass.class);
        
        Server server = null;
        Client client = null;
        TestThroughputC serverListener = null;
        
        boolean done = false;

        server = Network.createServer(5110, 5111);
        
        while(!done){
	        try {
	            server.start();
		        done = true;
	        } catch (KernelException e){
	        	Thread.sleep(100);
	        	continue;
	        } finally {
	        	Thread.sleep(100);
	        	client = Network.connectToServer("localhost", 5110, 5111);
	        	client.start();
	        	serverListener = new TestThroughputC(true);
	        }
        }
        
        TestMessage test = new TestMessage();;

    	client.addMessageListener(new TestThroughputC(false), TestMessage.class);
        server.addMessageListener(serverListener, TestMessage.class);

        Thread.sleep(1);
        
        for( int i = 0; i < numMsg; i++ ) {
    		client.send(test);
            Thread.sleep(0, 10);
        }

        Thread.sleep(50);
        
        client.close();
        server.close();
        
        serverListener = null;
        client = null;
        server = null;
        System.gc();
        Thread.sleep(2000);
        System.gc();
	}
	
    public static class TestMessage extends AbstractMessage implements java.io.Serializable {
    	
    	private String stringType = "nomnomnom";
    	private int[] arrayType = {1,2,3,4,5,6};
    	private String[] complexArrayType = {"hoi", "Doei!"};
    	private String[] complexArrayUnicodeType = {"hoi", "Doeëi!"};
    	private ArrayList<String> complexArrayListType = new ArrayList<>();
    	private ComplexSubClass A = new ComplexSubClass();
    	
    	{
    		complexArrayListType.add("nomnomnomëëË");
    	}
    	
    	public TestMessage() {
            setReliable(true);
        }
    	
    	public boolean testFields() {
    		int[] array = {1,2,3,4,5,6};
    		String[] complexArray = {"hoi", "Doei!"};
    		String[] complexUnicodeArray = {"hoi", "Doeëi!"};
    		ArrayList<String> arrayList = new ArrayList<>();
    		arrayList.add("nomnomnomëëË");
    		
    		assertEquals("nomnomnom" ,this.stringType);
    		assertArrayEquals(array, this.arrayType);
    		assertArrayEquals(complexArray, this.complexArrayType);
    		assertArrayEquals(complexUnicodeArray, this.complexArrayUnicodeType);
    		assertEquals(arrayList, this.complexArrayListType);
    		assertEquals(new ComplexSubClass(), this.A);
    		return true;
    	}
    }
	
	class TestThroughputC implements MessageListener<MessageConnection> { //extends MessageAdapter {

	    private boolean isOnServer;

	    public TestThroughputC(boolean isOnServer) {
	        this.isOnServer = isOnServer;
	    }
	    
	    public boolean isOnServer() { return isOnServer; }

	    @Override
	    public void messageReceived(MessageConnection source, AbstractMessage msg) {

	        if (!isOnServer) {
	            recvMsgs++;
	            assertTrue(((TestMessage) msg).testFields());
	            
	        } else {
	            if (source == null) {
	                System.out.println("Received a message from a not fully connected source, msg:" + msg);
	            } else {
	                source.send(msg);
	            }
	        }
	    }

	}

}
