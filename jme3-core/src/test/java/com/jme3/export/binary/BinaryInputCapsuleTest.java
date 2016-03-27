	package com.jme3.export.binary;
	
	import java.io.IOException;
	import java.nio.ByteBuffer;
import java.util.ArrayList;
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
	
	public BinaryClassField field13(){
		BinaryClassField field = new BinaryClassField("string",(byte) 5, (byte) 5);	
	cObj.nameFields.put("string", field);
		return field;
	}
	
	public BinaryClassField field2(){
		BinaryClassField field = new BinaryClassField("hi",(byte) 7, (byte) 5);	
	cObj.nameFields.put("hi", field);
		return field;
	} 
	 
	
	@Test
	public void testReadBitSet1() throws IOException {
		BitSet bit = new BitSet();
		bit.set(5);
		BinaryClassField field = field13();
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
		BinaryClassField field = field2();
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
		BinaryClassField field = field13();
		b.fieldData.put((byte) 5, bit2); 
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		BitSet results = b.readBitSet(s,bit1);
		Assert.assertTrue(results.get(15));
		Assert.assertFalse(results.get(10));
   }
   
   @Test
   public void testReadBoolean1() throws IOException {
		BinaryClassField field = field13();
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertTrue(!b.fieldData.containsKey(field.alias));
		Assert.assertTrue(b.readBoolean(s,true));	
		Assert.assertFalse(b.readBoolean(s,false));	
   }
   
   @Test
   public void testReadBoolean2() throws IOException {
		BinaryClassField field = field2();
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		Assert.assertTrue(b.readBoolean(s,true));
		Assert.assertFalse(b.readBoolean(s,false));
   }
   
   @Test
   public void testReadBoolean3() throws IOException {
		BinaryClassField field = field13();
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
	   BinaryClassField field = field13();
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	   boolean[] result = b.readBooleanArray(s,bool);	
	   Assert.assertTrue(result[0]);	
	   Assert.assertFalse(result[1]);
   }
   
   @Test
   public void testReadBooleanArray2() throws IOException {
	   boolean[] bool = {true,false};
		BinaryClassField field = field2();
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
	   BinaryClassField field = field13();
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
	   BinaryClassField field = field13();
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
	   BinaryClassField field = field2();
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
	   BinaryClassField field = field13();
	   b.fieldData.put((byte) 5, bool2); 
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertFalse(!b.fieldData.containsKey(field.alias));
	   boolean[][] result = b.readBooleanArray2D(s,bool);
	   Assert.assertFalse(result[0][0]);	
	   Assert.assertFalse(result[0][1]);	
	   Assert.assertTrue(result[1][0]);	
	   Assert.assertTrue(result[1][1]);
   }
   
   @Test
   public void testReadByteArray1() throws IOException {
	   byte[] byteV = {(byte) 8, (byte) 4};
	   BinaryClassField field = field13();
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	   byte[] result = b.readByteArray(s,byteV);	
	   Assert.assertEquals(byteV,result);
   }
   
   @Test
   public void testReadByteArray2() throws IOException {
	    byte[] byteV = {(byte) 8, (byte) 4};
		BinaryClassField field = field2();
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		byte[] result = b.readByteArray(s,byteV);
		Assert.assertEquals(byteV,result);
   }
   
   @Test
   public void testReadByteArray3() throws IOException {
	   byte[] byte1 = {(byte) 8, (byte) 4};
	   byte[] byte2 = {(byte) 3, (byte) 6};
	   BinaryClassField field = field13();
	   b.fieldData.put((byte) 5, byte2); 
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertFalse(!b.fieldData.containsKey(field.alias));
	   byte[] result = b.readByteArray(s,byte1);
	   Assert.assertEquals(result,byte2);
	   Assert.assertNotEquals(result,byte1);
   }
	   
	@Test
	public void testReadByteArray2D1() throws IOException {
	   byte[][] byteV = {{(byte) 8, (byte) 4},{(byte) 3,(byte) 6}};
	   BinaryClassField field = field13();
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	   byte[][] result = b.readByteArray2D(s,byteV);	
	   Assert.assertArrayEquals(result, byteV);
	 }
	
	@Test
	public void testReadByteArray2D2() throws IOException {
		byte[][] byteV = {{(byte) 8, (byte) 4},{(byte) 3,(byte) 6}};
		BinaryClassField field = field2();
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		byte[][] result = b.readByteArray2D(s,byteV);
		Assert.assertArrayEquals(byteV,result);
	}
	
	@Test
	public void testReadByteArray2D3() throws IOException {
	   byte[][] byte1 = {{(byte) 8, (byte) 4},{(byte) 3,(byte) 6}};
	   byte[][] byte2 = {{(byte) 8, (byte) 4},{(byte) 2,(byte) 6}};
	   BinaryClassField field = field13();
	   b.fieldData.put((byte) 5, byte2); 
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertFalse(!b.fieldData.containsKey(field.alias));
	   byte[][] result = b.readByteArray2D(s,byte1);
	   Assert.assertArrayEquals(byte2, result);
	   Assert.assertNotEquals(result,byte1);
	}
	
	@Test
	public void testReadByteBuffer1() throws IOException {
	   byte[] byte1 = {(byte) 8, (byte) 4};
	   ByteBuffer byteBuffer = ByteBuffer.allocate(10);
	   byteBuffer.put(byte1);
	   BinaryClassField field = field13();
	   Assert.assertFalse(cObj.nameFields.get("string") == null);
	   Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	   ByteBuffer result = b.readByteBuffer(s,byteBuffer);	
	   Assert.assertEquals(result, byteBuffer);
	 }
	
	@Test
	public void testReadByteBuffer2() throws IOException {
		byte[] byte1 = {(byte) 8, (byte) 4};
		ByteBuffer byteBuffer = ByteBuffer.allocate(10);
		byteBuffer.put(byte1);
		BinaryClassField field = field2();
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		ByteBuffer result = b.readByteBuffer(s,byteBuffer);	
		Assert.assertEquals(result, byteBuffer);
	}
	
	@Test
	public void testReadByteBuffer3() throws IOException {
		byte[] byte1 = {(byte) 8, (byte) 4};
		byte[] byte2 = {(byte) 7, (byte) 3};
		ByteBuffer byteBuffer1 = ByteBuffer.allocate(10);
		byteBuffer1.put(byte1);
		ByteBuffer byteBuffer2 = ByteBuffer.allocate(8);
		byteBuffer2.put(byte2);
		BinaryClassField field = field13();
		b.fieldData.put((byte) 5, byteBuffer2); 
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		ByteBuffer result = b.readByteBuffer(s,byteBuffer1);
		Assert.assertEquals(byteBuffer2.array(), result.array());
		Assert.assertNotEquals(result.array(),byteBuffer1.array());
	}
	
	@Test
	public void testReadByteBufferArrayList1() throws IOException {
		ArrayList<ByteBuffer> bufferList = new ArrayList<ByteBuffer>();
	    byte[] byte1 = {(byte) 8, (byte) 4};
	    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
	    byteBuffer.put(byte1);
	    bufferList.add(byteBuffer);
	    BinaryClassField field = field13();
	    Assert.assertFalse(cObj.nameFields.get("string") == null);
	    Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	    ArrayList<ByteBuffer> result = b.readByteBufferArrayList(s,bufferList);	
	    Assert.assertEquals(result, bufferList);
	 }
	
	@Test
	public void testReadByteBufferArrayList2() throws IOException {
		ArrayList<ByteBuffer> bufferList = new ArrayList<ByteBuffer>();
	    byte[] byte1 = {(byte) 8, (byte) 4};
	    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
	    byteBuffer.put(byte1);
	    bufferList.add(byteBuffer);
		BinaryClassField field = field2();
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		ArrayList<ByteBuffer> result = b.readByteBufferArrayList(s,bufferList);	
	    Assert.assertEquals(result, bufferList);
	}
	
	@Test
	public void testReadByteBufferArrayList3() throws IOException {
		ArrayList<ByteBuffer> bufferList1 = new ArrayList<ByteBuffer>();
	    byte[] byte1 = {(byte) 8, (byte) 4};
	    ByteBuffer byteBuffer1 = ByteBuffer.allocate(10);
	    byteBuffer1.put(byte1);
	    bufferList1.add(byteBuffer1);
	    ArrayList<ByteBuffer> bufferList2 = new ArrayList<ByteBuffer>();
	    byte[] byte2 = {(byte) 6, (byte) 4};
	    ByteBuffer byteBuffer2 = ByteBuffer.allocate(10);
	    byteBuffer2.put(byte2);
	    bufferList2.add(byteBuffer2);
		BinaryClassField field = field13();
		b.fieldData.put((byte) 5, bufferList2); 
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		ArrayList<ByteBuffer> result = b.readByteBufferArrayList(s,bufferList1);
		Assert.assertEquals(bufferList2.get(0).array(), result.get(0).array());
		Assert.assertNotEquals(bufferList1.get(0).array(),result.get(0).array());
	}
	
	@Test
	public void testDouble1() throws IOException {
		double d = (double) 3;
	    BinaryClassField field = field13();
	    Assert.assertFalse(cObj.nameFields.get("string") == null);
	    Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	    double result = b.readDouble(s,d);	
	    Assert.assertEquals(Double.compare(d, result),0);
	 }
	
	@Test
	public void testDouble2() throws IOException {
		double d = (double) 3;
		BinaryClassField field = field2();
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		double result = b.readDouble(s,d);	
		Assert.assertEquals(Double.compare(d, result),0);
	}
	
	@Test
	public void testDouble3() throws IOException {
		double d1 = (double) 3;
		double d2 = (double) 5;
		BinaryClassField field = field13();
		b.fieldData.put((byte) 5, d2); 
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		Double result = b.readDouble(s,d1);	
		 Assert.assertEquals(Double.compare(d2, result),0);
	    Assert.assertNotEquals(result, d1);
	}
	
	@Test
	public void testDoubleArray1() throws IOException {
		double[] d = {(double) 3,(double) 5};
	    BinaryClassField field = field13();
	    Assert.assertFalse(cObj.nameFields.get("string") == null);
	    Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	    double[] result = b.readDoubleArray(s,d);	
	    Assert.assertEquals(result, d);
	 }
	
	@Test
	public void testDoubleArray2() throws IOException {
		double[] d = {(double) 3,(double) 5};
		BinaryClassField field = field2();
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		double[] result = b.readDoubleArray(s,d);	
		Assert.assertEquals(result, d);
	}
	
	@Test
	public void testDoubleArray3() throws IOException {
		double[] d1= {(double) 3,(double) 5};
		double[] d2 = {(double) 7,(double) 5};
		BinaryClassField field = field13();
		b.fieldData.put((byte) 5, d2); 
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		double[] result = b.readDoubleArray(s,d1);	
	    Assert.assertEquals(result, d2);
	    Assert.assertNotEquals(result, d1);
	}
	
	@Test
	public void testDoubleArray2D1() throws IOException {
		double[][] d = {{(double) 3,(double) 5},{(double) 3,(double) 5}};
	    BinaryClassField field = field13();
	    Assert.assertFalse(cObj.nameFields.get("string") == null);
	    Assert.assertTrue(!b.fieldData.containsKey(field.alias));
	    double[][] result = b.readDoubleArray2D(s,d);	
	    Assert.assertArrayEquals(result, d);
	}
	
	@Test
	public void testDoubleArray2D2() throws IOException {
		double[][] d = {{(double) 3,(double) 5},{(double) 3,(double) 5}};
		BinaryClassField field = field2();
		b.fieldData.put((byte) 7, (byte)6);
		Assert.assertTrue(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		double[][] result = b.readDoubleArray2D(s,d);	
	    Assert.assertArrayEquals(result, d);
	}
	
	@Test
	public void testDoubleArray2D3() throws IOException {
		double[][] d1 = {{(double) 3,(double) 5},{(double) 3,(double) 5}};
		double[][] d2 = {{(double) 3,(double) 8},{(double) 3,(double) 5}};
		BinaryClassField field = field13();
		b.fieldData.put((byte) 5, d2); 
		Assert.assertFalse(cObj.nameFields.get("string") == null);
		Assert.assertFalse(!b.fieldData.containsKey(field.alias));
		double[][] result = b.readDoubleArray2D(s,d1);	
	    Assert.assertArrayEquals(result, d2);
	    Assert.assertNotEquals(result, d1);
	}
	
	
	
	
	
	   
	   
	   
	   
	   
		
		
		
		
	
	}
