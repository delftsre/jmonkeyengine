/*
 * Copyright (c) 2011 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jme3test.network;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.network.*;
import com.jme3.network.kernel.KernelException;
import com.jme3.network.serializing.Serializer;

import java.awt.Label;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("serial")
public class TestThroughput implements Serializable {

    private static AtomicInteger counter = new AtomicInteger(0);
    private static AtomicInteger serverCounter = new AtomicInteger(0);
    // Change this flag to test UDP instead of TCP
    private static boolean testReliable = false;
    private static int n; // Number of different test messages
    private static int ms; // Number of milliseconds to run
    private static Random rand = new Random();
    
    private static Server server;
    private static Client client;
    
    private static Class[] messageClasses = new Class[] {
    		EmptyTestMessage.class,
    		SimpleTestMessage.class,
    		PhysicsTestMessage.class,
    		HugeTestMessage.class
    };
    private static Class[] dataClasses = new Class[] {
    		Vector3f.class,
    		Matrix3f.class,
    		HugeTestMessage.HugeTestSubMessage.class,
    		ArrayList.class,
    		HashMap.class,
    		Date.class
    };
    
    public static class TestThroughputListener implements MessageListener<MessageConnection> {
        private boolean isOnServer;
    	
        public TestThroughputListener(boolean isOnServer) {
            this.isOnServer = isOnServer;        	
        }
        
        @Override
        public void messageReceived(MessageConnection source, AbstractMessage msg) {
            if (!isOnServer) {
                counter.incrementAndGet();
            } else {
                if (source == null) {
                    System.out.println("Received a message from a not fully connected source, msg:" + msg);
                } else {
                    try {
                        msg.setReliable(testReliable);
                    	source.send(msg);
                        serverCounter.incrementAndGet();
                    } catch (KernelException e) {
                    	// This happens when there are still messages in the pipeline from a closed client
                    	// Just discard the message
                    }                	
                }
            }
        }
    }

    
    public static void main(String[] args) throws IOException, InterruptedException {
    	for(int i = 0; i < messageClasses.length; i++) {
    		Serializer.registerClass(messageClasses[i]);
    	}
    	for(int i = 0; i < dataClasses.length; i++) {
    		Serializer.registerClass(dataClasses[i]);
    	}
    	
        server = Network.createServer(5110, 5110);
        server.start();

        client = Network.connectToServer("localhost", 5110);
        client.start();

        client.addMessageListener(new TestThroughputListener(false), messageClasses);
        server.addMessageListener(new TestThroughputListener(true), messageClasses);

        Thread.sleep(100);
        n = 1000;
        ms = 10 * 1000;
        
        System.out.println("Running tests for " + messageClasses.length + " types of messages with " + (ms/1000) + " seconds");
        System.out.println();
        
        // Because the tests keep influencing other tests, just run one at a time.. 
        testEmpty();     
//        testSimple();
//        testPhysics();
//        testHuge();
                
        System.exit(0);
    }
    
    private static void runThroughputTest(AbstractMessage... messages) {
    	counter.set(0);
        serverCounter.set(0);
        
		long start = System.currentTimeMillis();
		int i = 0;
		while((System.currentTimeMillis() - start) < ms) {
            client.send(messages[i % messages.length]);
			i++;
        }
    
        System.out.println("Sent " + i + " messages in " + (ms/1000) + " seconds.");
        System.out.println("Of these, " + serverCounter.get() + " were recieved by the server and " 
        			+ counter.get() + " by the client.");
        try{
	        Thread.sleep(1000);
	        System.out.println("A second later, " + serverCounter.get() + " were recieved by the server and " 
	    			+ counter.get() + " by the client.");
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
    }
    
    private static void runSerializationTest(AbstractMessage message) {
    	ByteBuffer buffer = ByteBuffer.allocate(32767 + 2);
    	try {
    		Serializer.writeObject(buffer, message);
    	} catch (IOException e) {
    		System.err.println("Serialization failed!");
    		e.printStackTrace();
    	}
    	System.out.println("Serialization size is " + buffer.position());
    }

	private static void testEmpty() {
		System.out.println("Running EmptyTestMessage tests");
		
		EmptyTestMessage message = new EmptyTestMessage();
		
		runSerializationTest(message);
		runThroughputTest(message);
		
		System.out.println();
	}	

	private static void testSimple() {
		System.out.println("Running SimpleTestMessage tests");
		
		SimpleTestMessage[] messages = new SimpleTestMessage[n];
		for(int i = 0; i < n; i++) {
			messages[i] = new SimpleTestMessage(UUID.randomUUID().toString());
		}
		
		runSerializationTest(messages[0]);
		runThroughputTest(messages);
		
		System.out.println();
	}

	private static void testPhysics() {
		System.out.println("Running PhysicsTestMessage tests");
		
		PhysicsTestMessage[] messages = new PhysicsTestMessage[n];
		for(int i = 0; i < n; i++) {
			messages[i] = PhysicsTestMessage.constructRandom();
		}
        
		runSerializationTest(PhysicsTestMessage.constructExample());
		runThroughputTest(messages);
		
		System.out.println();
	}
	
	private static void testHuge() {
		System.out.println("Running HugeTestMessage tests");
		
		HugeTestMessage[] messages = new HugeTestMessage[n];
		
		for(int i = 0; i < n; i++) {			
			messages[i] = HugeTestMessage.constructRandom();
		}
		
		runSerializationTest(HugeTestMessage.constructExample());
		runThroughputTest(messages);
		
		System.out.println();
	}
	
    
	private static Vector3f randomVector(float scale) {
		return new Vector3f(rand.nextFloat() * scale, 
							rand.nextFloat() * scale,
							rand.nextFloat() * scale);
	}
	
	private static Matrix3f randomMatrix(float scale) {
		return new Matrix3f(rand.nextFloat() * scale, rand.nextFloat() * scale, rand.nextFloat() * scale,
							rand.nextFloat() * scale, rand.nextFloat() * scale, rand.nextFloat() * scale,
							rand.nextFloat() * scale, rand.nextFloat() * scale, rand.nextFloat() * scale);
	}
    
    public static class EmptyTestMessage extends AbstractMessage implements Serializable {
        public EmptyTestMessage() {
            setReliable(testReliable);
        }
        
        public static EmptyTestMessage constructRandom() {
        	return new EmptyTestMessage();
        }
        
        public static EmptyTestMessage constructExample() {
        	return new EmptyTestMessage();
        }
    }
    
    public static class SimpleTestMessage extends AbstractMessage implements Serializable {
    	public String message;

    	public SimpleTestMessage() {
    		// Required by Kryo. Could use constructRandom.
    	}
    	
        public SimpleTestMessage(String message) {
            setReliable(testReliable);
            
        	this.message = message;
        }
        
        public static SimpleTestMessage constructRandom() {
        	return new SimpleTestMessage(UUID.randomUUID().toString());
        }
        
        public static SimpleTestMessage constructExample() {
        	return new SimpleTestMessage("c0dc2435-dc88-496d-9b4a-ef2ef1e537c9");
        }
    }    
    
    public static class PhysicsTestMessage extends AbstractMessage implements Serializable {
    	public Vector3f vec1;
    	public Vector3f vec2;
    	public Vector3f vec3;
    	public Vector3f vec4;
    	public Vector3f vec5;
    	public Matrix3f mat1;
    	public Matrix3f mat2;

    	public PhysicsTestMessage() {
    		// Required by Kryo. Could use constructRandom.
    	}
    	
		public PhysicsTestMessage(
				Vector3f vec1, Vector3f vec2, Vector3f vec3, Vector3f vec4, Vector3f vec5,
				Matrix3f mat1, Matrix3f mat2) {
            setReliable(testReliable);
            
			this.vec1 = vec1;
			this.vec2 = vec2;
			this.vec3 = vec3;
			this.vec4 = vec4;
			this.vec5 = vec5;
			this.mat1 = mat1;
			this.mat2 = mat2;
		}
        
        public static PhysicsTestMessage constructRandom() {
        	Vector3f vec1 = randomVector(1);
	        Vector3f vec2 = randomVector(0.01f);
	        Vector3f vec3 = randomVector(5);
	        Vector3f vec4 = randomVector(1000);
	        Vector3f vec5 = randomVector((int) Float.MAX_VALUE);
	        Matrix3f mat1 = randomMatrix(1);
	        Matrix3f mat2 = randomMatrix((int) Float.MAX_VALUE);
			return new PhysicsTestMessage(vec1, vec2, vec3, vec4, vec5, mat1, mat2);
        }
        
        public static PhysicsTestMessage constructExample() {
        	Vector3f vec1 = new Vector3f(0.433166980743408203125f,0.82575452327728271484375f,0.955823123455047607421875f);
	        Vector3f vec2 = new Vector3f(0.009725025855004787445068359375f,0.0054381941445171833038330078125f,0.00021268546697683632373809814453125f);
	        Vector3f vec3 = new Vector3f(2.938496112823486328125f,4.7902927398681640625f,1.794808864593505859375f);
	        Vector3f vec4 = new Vector3f(328.64569091796875f,990.1982421875f,775.70758056640625f);
	        Vector3f vec5 = new Vector3f(1186151296f,850664832f,1532781312f);
	        Matrix3f mat1 = new Matrix3f(0.9948008060455322265625f,0.499847590923309326171875f,0.045433461666107177734375f,
	        							0.288867175579071044921875f,0.8376448154449462890625f,0.264260709285736083984375f,
	        							0.730224311351776123046875f,0.4005107879638671875f,0.97741806507110595703125f);
	        Matrix3f mat2 = new Matrix3f(1221519616f,1262525056f,1983939200f,
	        							556515072f,1268844544f,1640567936f,
	        							20781696f,1712417920f,1353835648f);
			return new PhysicsTestMessage(vec1, vec2, vec3, vec4, vec5, mat1, mat2);
        }
    }    

    public static class HugeTestMessage extends AbstractMessage implements Serializable {
    	
    	public static class HugeTestSubMessage extends PhysicsTestMessage implements Serializable {
    		public Vector3f vec6;
    		public Matrix3f mat3;
    		
    		public HugeTestSubMessage(Vector3f vec1, Vector3f vec2, Vector3f vec3,
    				Vector3f vec4, Vector3f vec5, Vector3f vec6,
    				Matrix3f mat1, Matrix3f mat2, Matrix3f mat3) {
    			super(vec1, vec2, vec3, vec4, vec5, mat1, mat2);
    			this.vec6 = vec6;
    			this.mat3 = mat3;
    		}

        	public HugeTestSubMessage() {
        		// Required by Kryo. Could use constructRandom.
        	}
        	
    		public static HugeTestSubMessage constructRandom() {
    			return new HugeTestSubMessage(
    					randomVector(1), randomVector(0.5f), randomVector(24),
    					randomVector(1000), randomVector(0.0001f), randomVector(Float.MAX_VALUE),
    					randomMatrix(1), randomMatrix(0.2f), randomMatrix(1000));
            }
            
            public static HugeTestSubMessage constructExample() {
            	Vector3f vec1 = new Vector3f(0.734558284282684326171875f,0.1037347316741943359375f,0.9635913372039794921875f);
    	        Vector3f vec2 = new Vector3f(0.2863779366016387939453125f,0.1739477217197418212890625f,0.121182739734649658203125f);
    	        Vector3f vec3 = new Vector3f(23.9283580780029296875f,1.685794830322265625f,4.0823535919189453125f);
    	        Vector3f vec4 = new Vector3f(571.7216796875f,493.94287109375f,21.1187591552734375f);
    	        Vector3f vec5 = new Vector3f(0.00004635152072296477854251861572265625f,0.0000645517720840871334075927734375f,0.0000068765875766985118389129638671875f);
    	        Vector3f vec6 = new Vector3f(7894541938613643216668711816955691008f,133026838198553518533820343453506076672f,250347972238109762423186708926434377728f);
    	        Matrix3f mat1 = new Matrix3f(0.5152451992034912109375f,0.554454267024993896484375f,0.301480352878570556640625f,
    	        		0.585283696651458740234375f,0.126667499542236328125f,0.00066840648651123046875f,
    	        		0.629179894924163818359375f,0.099936783313751220703125f,0.1898162364959716796875f);
    	        Matrix3f mat2 = new Matrix3f(0.13529066741466522216796875f,0.06602752208709716796875f,0.17806760966777801513671875f,
    	        		0.0544088147580623626708984375f,0.12109376490116119384765625f,0.19446246325969696044921875f,
    	        		0.108363606035709381103515625f,0.16804240643978118896484375f,0.13968805968761444091796875f);
    	        Matrix3f mat3 = new Matrix3f(122.839752197265625f,210.7856903076171875f,347.761627197265625f,
    	        		175.275390625f,127.128662109375f,401.360382080078125f,
    	        		428.85064697265625f,857.57647705078125f,433.4407958984375f);
            	return new HugeTestSubMessage(vec1, vec2, vec3, vec4, vec5, vec6, mat1, mat2, mat3);
            }
    	}
    	
    	public HugeTestSubMessage submsg;
    	public ArrayList<Map<String, Integer[]>> list;
    	public Date date;
    	
    	public HugeTestMessage() {
    		// Required by Kryo. Could use constructRandom.
    	}
    	    	
        public HugeTestMessage(HugeTestSubMessage submsg, ArrayList<Map<String, Integer[]>> list, Date date) {
        	setReliable(testReliable);
        	
			this.submsg = submsg;
			this.list = list;
			this.date = date;
		}
        
        public static HugeTestMessage constructRandom() {
        	HugeTestSubMessage submsg = HugeTestSubMessage.constructRandom();
        	
        	ArrayList<Map<String, Integer[]>> list = new ArrayList<Map<String, Integer[]>>();
			while(rand.nextFloat() < 0.9f) {
				HashMap<String, Integer[]> map = new HashMap<String, Integer[]>();
				while(rand.nextFloat() < 0.7f) {
					int ints = rand.nextInt(10);
					Integer[] arr = new Integer[ints];
					for(int i = 0; i < ints; i++) {
						arr[i] = rand.nextInt();
					}
					map.put(UUID.randomUUID().toString(), arr);
				}
				list.add(map);				
			}
        	
			Date date = new Date();
			
        	return new HugeTestMessage(submsg, list, date);
        }
        
        public static HugeTestMessage constructExample() {
        	HugeTestSubMessage submsg = HugeTestSubMessage.constructExample();
        	
        	ArrayList<Map<String, Integer[]>> list = new ArrayList<Map<String, Integer[]>>();
        	HashMap<String, Integer[]> map1 = new HashMap<String, Integer[]>();
        	map1.put("b1a6403e-1b60-48dc-849c-12eb8ad27d06", new Integer[] {1859738404, 1694268120, -1129680451, -1224667903, -661151019, -2096401476, -1501844014, 1839364723});
        	map1.put("6303d3a8-7506-4a4e-8f13-9e4cd6c7fbcb", new Integer[] {-1856653715, 476417908, -716406513, 1513345772, 1471991271});
        	map1.put("90ab696b-1448-4cd0-a8a0-0558f58e8097", new Integer[] {-735676597, -1073278648, -1076764924});
        	map1.put("55d2d19f-f1cd-4d82-b1f2-0f5f262c2719", new Integer[] {706411505, 73831233, -356557658, -1101021211});
        	list.add(map1);
        	
        	HashMap<String, Integer[]> map2 = new HashMap<String, Integer[]>();
        	map2.put("fbe8aca5-425b-4c1b-81fe-e2b45a883751", new Integer[] {-1986031668, -216366978, 2143748480});
        	map2.put("37b45cd6-aa30-4144-ab24-a2296324f0d7", new Integer[] {1840420678, -148582777});
        	map2.put("ef12c9ac-bf53-4054-977e-45a22997210d", new Integer[] {313946288, -2100994421});
        	list.add(map2);
        	
        	HashMap<String, Integer[]> map3 = new HashMap<String, Integer[]>();
        	map3.put("9f9f7b7a-40bd-4fbc-96a5-0555a320d2da", new Integer[]{});
        	map3.put("234b3e08-8d6d-4861-b86c-b05769be3fcc", new Integer[] {162012643});
        	list.add(map3);
        	
        	HashMap<String, Integer[]> map4 = new HashMap<String, Integer[]>();
        	map4.put("a27b0043-5358-4a60-9e37-1ad743687cac", new Integer[] {-1108614336, 1077521187, -1551597258, -167932021, -668704169, -1738237249, 1133662364, 1717389473});
        	list.add(map4);
        	
        	Date date = new Date(1459681195625l);
        	
        	return new HugeTestMessage(submsg, list, date);
        }
    }
}
