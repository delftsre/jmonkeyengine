/*
 * Copyright (c) 2009-2012 jMonkeyEngine
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
package com.jme3.network.serializing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import org.nustaq.serialization.FSTConfiguration;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.jme3.network.AbstractMessage;
import com.jme3.network.message.ChannelInfoMessage;
import com.jme3.network.message.ClientRegistrationMessage;

/**
 * The main serializer class, which will serialize objects such that
 *  they can be sent across the network. Serializing classes should extend
 *  this to provide their own serialization.
 *
 * @author Lars Wesselius
 */
//@SuppressWarnings("all")
public class Serializer {            
    protected static final FSTConfiguration fstConf = FSTConfiguration.getDefaultConfiguration();
    
    private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
        	Kryo kryo = new Kryo();
        	// Registration goes here. Removed because of difficulties of making this thread safe
            return kryo;
        };
    };    
    
    
    static {
    	//com.esotericsoftware.minlog.Log.TRACE();
    	
    	registerClass(AbstractMessage.class);
        registerClass(ClientRegistrationMessage.class);
        registerClass(ChannelInfoMessage.class);
    }
    
    public static void registerClass(Class c) {
    	fstConf.registerClass(c);
    }
    
    public static <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
    	return readObject(data);
    }
    
    /**
     * Uses Java serialization
     */
    @SuppressWarnings("unchecked")
    public static <T> T readObjectFST(ByteBuffer data) throws IOException {
    	byte[] b = new byte[data.remaining()];
    	data.get(b);
        T object = (T) fstConf.asObject(b);
        return object;
    }

    /**
     * Uses Java serialization
     */
    @SuppressWarnings("unchecked")
    public static <T> T readObject(ByteBuffer data) throws IOException {
    	byte[] b = new byte[data.remaining()];
    	data.get(b);
       	ByteArrayInputStream bytesIn = new ByteArrayInputStream(b);
        ObjectInputStream ois = new ObjectInputStream(bytesIn);
        T object;
		try {
			object = (T) ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new SerializerException( "Error reading object", e);
		}
        ois.close();
        return object;
    }
    
    
    /** 
     * Uses kryo serialization
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> T readObjectKryo(ByteBuffer data) throws IOException {
    	byte[] b = new byte[data.remaining()];
    	data.get(b);
        
        Input kryoInput = new Input(b);
        
        Object object = kryos.get().readClassAndObject(kryoInput);
        
        kryoInput.close(); // kryo's input must be closed as well.
        
        return (T) object;
    }
    
    
    /**
     * Uses Java serialization
     * @param buffer
     * @param object
     * @throws IOException
     */
    public static void writeObjectFST(ByteBuffer buffer, Object object) throws IOException {
    	byte[] bytes = fstConf.asByteArray(object);
    	buffer.put(bytes);
    }
    
    /**
     * Uses Java serialization
     * @param buffer
     * @param object
     * @throws IOException
     */
    public static void writeObject(ByteBuffer buffer, Object object) throws IOException {    	
    	ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bytesOut);
        oos.writeObject(object);
        oos.flush();
        byte[] bytes = bytesOut.toByteArray();
        bytesOut.close();
        oos.close();
        buffer.put(bytes);
    }
    
    /**
     * Uses Kryo serialization
     * @param buffer
     * @param object
     * @throws IOException
     */
    public static void writeObjectKryo(ByteBuffer buffer, Object object) throws IOException {
        Output kryoOutput = new Output(buffer.array());
        
        kryos.get().writeClassAndObject(kryoOutput, object); // oos.writeObject(object);

        buffer.put(kryoOutput.toBytes());
        kryoOutput.flush();
        kryoOutput.close(); // kryo output must be closed as well
    }

}
