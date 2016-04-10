package com.jme3.network.serializing;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jme3.network.AbstractMessage;
import com.jme3.network.Client;
import com.jme3.network.HostedConnection;
//import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;

public class SerializationIntegrationTest {
	
	public class TestParameters implements java.io.Serializable {
	    public boolean z = true;
	    public byte b = -88;
	    public char c = 'Y';
	    public short s = 9999;
	    public int i = 123;
	    public float f = -75.4e8f;
	    public long l = 9438345072805034L;
	    public double d = -854834.914703e88;
	    public int[] ia = new int[]{ 456, 678, 999 };
	
	    public List<Object> ls = new ArrayList<Object>();
	
	    public Map<String, SerializableObject> mp = new HashMap<String, SerializableObject>();
	
	    public Status status1 = Status.High;
	    public Status status2 = Status.Middle;
	
	    public Date date = new Date(System.currentTimeMillis());
	    
	    public TestParameters() {
	    	ls.add("hello");
	    	ls.add(new SerializableObject(-22));
	    	mp.put("abc", new SerializableObject(555));
	    }
	}

    public static class SerializableObject implements java.io.Serializable {

        private int val;

        public SerializableObject() {
        }

        public SerializableObject(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "SomeObject[val="+val+"]";
        }

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SerializableObject other = (SerializableObject) obj;
			if (val != other.val)
				return false;
			return true;
		}
    }

    public enum Status {
        High,
        Middle,
        Low;
    }

    public static class TestSerializationMessage extends AbstractMessage implements java.io.Serializable {

        boolean z;
        byte b;
        char c;
        short s;
        int i;
        float f;
        long l;
        double d;        
        int[] ia;
        List<Object> ls;
        Map<String, SerializableObject> mp;
        Status status1;
        Status status2;
        Date date;

        public TestSerializationMessage() {
        }

        public TestSerializationMessage(TestParameters p) {
        	super(true); // reliable
            z = p.z;
            b = p.b;
            c = p.c;
            s = p.s;
            i = p.i;
            f = p.f;
            l = p.l;
            d = p.d;
            ia = p.ia;
            ls = p.ls;
            mp = p.mp;
            status1 = p.status1;
            status2 = p.status2;
            date = p.date;
        }
    }
    
    public static class TestSerializationHandler implements MessageListener<HostedConnection> {
    	public TestSerializationMessage recievedMessage;
    	
        public void messageReceived(HostedConnection source, AbstractMessage m) {
        	recievedMessage = (TestSerializationMessage) m;
        }
    }
    
    @Test
    public void serializationTest() throws IOException, InterruptedException {
//        Serializer.registerClass(SerializableObject.class);
//        Serializer.registerClass(TestSerializationMessage.class);

        Server server = Network.createServer( 5110 );
        server.start();

        Client client = Network.connectToServer( "localhost", 5110 ); 
        client.start();
        
    	TestParameters params = new TestParameters();
    	TestSerializationMessage message = new TestSerializationMessage(params);
    	TestSerializationHandler handler = new TestSerializationHandler();
    	
        server.addMessageListener(handler, TestSerializationMessage.class);
        client.send(message);
        
        Thread.sleep(100);
        TestSerializationMessage recievedMessage = handler.recievedMessage;
        
        assertEquals(message.z, recievedMessage.z);
        assertEquals(message.b, recievedMessage.b);
        assertEquals(message.c, recievedMessage.c);
        assertEquals(message.s, recievedMessage.s);
        assertEquals(message.i, recievedMessage.i);
        assertEquals(message.f, recievedMessage.f, Float.MIN_VALUE);
        assertEquals(message.l, recievedMessage.l);
        assertEquals(message.d, recievedMessage.d, Double.MIN_VALUE);
        assertArrayEquals(message.ia, recievedMessage.ia);
        assertEquals(message.ls, recievedMessage.ls);
        assertEquals(message.mp, recievedMessage.mp);
        assertEquals(message.status1, recievedMessage.status1);
        assertEquals(message.status2, recievedMessage.status2);
        assertEquals(message.date, recievedMessage.date);
    }

}
