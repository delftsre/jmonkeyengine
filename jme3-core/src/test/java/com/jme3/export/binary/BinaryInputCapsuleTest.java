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
		cObj.nameFields = new HashMap<String, BinaryClassField>();
	}
	 

	@Test
	public void testReadBitSet1() throws IOException {
		BitSet bit = new BitSet();
		bit.set(5);
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
		BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
		cObj.nameFields.put("string", field);
		b.fieldData.put((byte) 5, bit2); 
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		BitSet results = b.readBitSet(s,bit1);
		Assert.assertTrue(results.get(15));
		Assert.assertFalse(results.get(10));
   }
   
   @Test
   public void testReadBoolean1() throws IOException {
		BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
		cObj.nameFields.put("string", field);
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertTrue(!b.fieldData.containsKey(field.alias));
		Assert.assertTrue(b.readBoolean(s,true));	
		Assert.assertFalse(b.readBoolean(s,false));	
   }
   
   @Test
   public void testReadBoolean2() throws IOException {
		BinaryClassField field = new BinaryClassField("hi",(byte) 7, (byte) 5);	
		cObj.nameFields.put("hi", field);
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		Assert.assertTrue(b.readBoolean(s,true));
		Assert.assertFalse(b.readBoolean(s,false));
   }
   
   @Test
   public void testReadBoolean3() throws IOException {
		BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
		cObj.nameFields.put("string", field);
		b.fieldData.put((byte) 5, true); 
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		Assert.assertTrue(b.readBoolean(s,false));
		b.fieldData.put((byte) 5, false); 
		Assert.assertFalse(b.readBoolean(s,false));
   }
   
   @Test
   public void testReadBooleanArray1() throws IOException {
	   boolean[] bool = {true,false};
	   BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
	   cObj.nameFields.put("string", field);
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	   boolean[] result = b.readBooleanArray(s,bool);	
	   Assert.assertTrue(result[0]);	
	   Assert.assertFalse(result[1]);
   }
   
   @Test
   public void testReadBooleanArray2() throws IOException {
	   boolean[] bool = {true,false};
		BinaryClassField field = new BinaryClassField("hi",(byte) 7, (byte) 5);	
		cObj.nameFields.put("hi", field);
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		boolean[] result = b.readBooleanArray(s,bool);	
		Assert.assertTrue(result[0]);	
		Assert.assertFalse(result[1]);
   }
   
   @Test
   public void testReadBooleanArray3() throws IOException {
	   boolean[] bool = {false,false};
	   BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
	   cObj.nameFields.put("string", field);
	   boolean[] bool2 = {true,true};
	   b.fieldData.put((byte) 5, bool2); 
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertFalse(!b.fieldData.containsKey(field.alias));
	   boolean[] result = b.readBooleanArray(s,bool);
	   Assert.assertTrue(result[0]);
	   Assert.assertTrue(result[1]);
   }
   
   @Test
   public void testReadBooleanArray2D1() throws IOException {
	   boolean[][] bool = {{true, true},{false,false}};
	   BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
	   cObj.nameFields.put("string", field);
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	   boolean[][] result = b.readBooleanArray2D(s,bool);	
	   Assert.assertTrue(result[0][0]);	
	   Assert.assertTrue(result[0][1]);	
	   Assert.assertFalse(result[1][0]);	
	   Assert.assertFalse(result[1][1]);	
   }
   
   @Test
   public void testReadBooleanArray2D2() throws IOException {
	   boolean[][] bool = {{true, true},{false,false}};
	   BinaryClassField field = new BinaryClassField("hi",(byte) 7, (byte) 5);	
	   cObj.nameFields.put("hi", field);
	   b.fieldData.put((byte) 7, (byte)6);
	   Assert.assertTrue(cObj.nameFields.get("string") == null);
	   Assert.assertFalse(!b.fieldData.containsKey(field.alias));
	   boolean[][] result = b.readBooleanArray2D(s,bool);	
	   Assert.assertTrue(result[0][0]);	
	   Assert.assertTrue(result[0][1]);	
	   Assert.assertFalse(result[1][0]);	
	   Assert.assertFalse(result[1][1]);
   }
   
   @Test
   public void testReadBooleanArray2D3() throws IOException {
	   boolean[][] bool = {{true, true},{false,false}};
	   boolean[][] bool2 = {{false,false},{true, true}};
	   BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
	   cObj.nameFields.put("string", field);
	   b.fieldData.put((byte) 5, bool2); 
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertFalse(!b.fieldData.containsKey(field.alias));
	   boolean[][] result = b.readBooleanArray2D(s,bool);
	   Assert.assertFalse(result[0][0]);	
	   Assert.assertFalse(result[0][1]);	
	   Assert.assertTrue(result[1][0]);	
	   Assert.assertTrue(result[1][1]);
   }
   
   
   
   
   
	
	
	
	

}
