package com.jme3.export.binary;

import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryClassObject;

public class BinaryInputCapsuleTest {
	String s;
	BinaryImporter importer;
	BinaryClassObject cObj;
	Savable savable;
	BinaryInputCapsule b;
	//HashMap<Byte, Object> fieldData;
	

	 @Before
	    public void setUp() throws Exception {
			s = "string";       
			importer =  new BinaryImporter();
			cObj = new BinaryClassObject();
			savable = Mockito.mock(Savable.class);
			b = new BinaryInputCapsule(importer,savable,cObj);
			b.fieldData = new HashMap<Byte, Object>();
		
	    }
	
   @Test
   public void testReadBitSet1() throws IOException {
		BitSet bit = new BitSet();
		bit.set(5);
		cObj.nameFields = new HashMap<String, BinaryClassField>();
		BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
		cObj.nameFields.put("string", field);
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertTrue(!b.fieldData.containsKey(field.alias));
		BitSet results = b.readBitSet(s,bit);
		Assert.assertTrue(results.get(5));
		Assert.assertFalse(results.get(4));	
   }
   
   @Test
   public void testReadBitSet2() throws IOException {
		BitSet bit = new BitSet();
		bit.set(5);
		cObj.nameFields = new HashMap<String, BinaryClassField>();
		BinaryClassField field = new BinaryClassField("hi",(byte) 7, (byte) 5);	
		cObj.nameFields.put("hi", field);
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		BitSet results = b.readBitSet(s,bit);
		Assert.assertTrue(results.get(5));
		Assert.assertFalse(results.get(4));	
   }
   
   @Test
   public void testReadBitSet3() throws IOException {
		BitSet bit1 = new BitSet();
		bit1.set(10);
		BitSet bit2 = new BitSet();
		bit2.set(15);
		cObj.nameFields = new HashMap<String, BinaryClassField>();
		BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
		cObj.nameFields.put("string", field);
		b.fieldData.put((byte) 5, bit2); 
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		BitSet results = b.readBitSet(s,bit1);
		Assert.assertTrue(results.get(15));
		Assert.assertFalse(results.get(10));
   }
   
   
   
   
	
	
	
	

}
