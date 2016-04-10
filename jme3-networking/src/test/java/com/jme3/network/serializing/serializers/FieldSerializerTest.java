package com.jme3.network.serializing.serializers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.jme3.network.serializing.Serializer;

public class FieldSerializerTest {

	public static class TestObject implements java.io.Serializable {
		int val;

		public TestObject() {
		}

		public TestObject(int val) {
			this.val = val;
		}

		@Override
		public boolean equals(Object obj) {
			return this.val == ((TestObject) obj).val;
		}
	}
	
	public static class TestObjectNoConstructor implements java.io.Serializable {
		int val;

		public TestObjectNoConstructor(int val) {
			this.val = val;
		}

		@Override
		public boolean equals(Object obj) {
			return this.val == ((TestObjectNoConstructor) obj).val;
		}
	}
	
	public static class TestObjectPrivateConstructor implements java.io.Serializable {
		int val;

		private TestObjectPrivateConstructor() {
		}
		
		public TestObjectPrivateConstructor(int val) {
			this.val = val;
		}

		@Override
		public boolean equals(Object obj) {
			return this.val == ((TestObjectPrivateConstructor) obj).val;
		}
	}
	
	public static class TestObjectNoFields implements java.io.Serializable {
		public TestObjectNoFields() { }
		
		@Override
		public boolean equals(Object obj) {
			return obj instanceof TestObjectNoFields;
		}
	}
	
	public static class TestObjectWithSuperclass extends TestObject implements java.io.Serializable {
		String mes;

		public TestObjectWithSuperclass() {
		}

		public TestObjectWithSuperclass(int val, String mes) {
			this.val = val;
			this.mes = mes;
		}

		@Override
		public boolean equals(Object obj) {
			return this.val == ((TestObject) obj).val &&
					this.mes.equals(((TestObjectWithSuperclass) obj).mes);
		}
	}
	
	public static class TestObjectWithNestedCollections implements java.io.Serializable {
		ArrayList<Map<String, Integer[]>> list;
		
		public TestObjectWithNestedCollections() {			
		}
		
		public TestObjectWithNestedCollections(ArrayList<Map<String, Integer[]>> list) {			
			this.list = list;
		}
		
		@Override
		public boolean equals(Object obj) {
			ArrayList<Map<String, Integer[]>> otherList = ((TestObjectWithNestedCollections) obj).list;
			if(list.size() != otherList.size()) return false;
			
			for(int i = 0; i < list.size(); i++) {
				Map<String, Integer[]> map = list.get(i);
				Map<String, Integer[]> otherMap = otherList.get(i);
				
				if(map == null) {
					if(otherMap == null) continue;
					else return false;
				}
				
				if(map.size() != otherMap.size()) return false;
				
				Iterator<String> it = map.keySet().iterator();
				while(it.hasNext()) {
					String key = it.next();
					
					Integer[] arr = map.get(key);
					Integer[] otherArr = otherMap.get(key);
					
					if(!Arrays.equals(arr, otherArr)) return false;
				}
				
			}
						
			return true;
		}		
	}
	
	public static class TestObjectWithException implements java.io.Serializable {
		RuntimeException exception;
		
		public TestObjectWithException() {			
		}
		
		public TestObjectWithException(RuntimeException exception) {			
			this.exception = exception;
		}
		
		@Override
		public boolean equals(Object obj) {
			return exception.equals(((TestObjectWithException) obj).exception);
		}
	}

	@Test
	public void serializationTestWithRegisterClass() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		TestObject obj = new TestObject(500);
//		Serializer.registerClass(TestObject.class);
		Serializer.writeObject(buffer, obj);
		buffer.rewind();
		assertEquals(obj, Serializer.readObject(buffer));
	}
	
	@Test
	public void serializationTestWithInitialize() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		ByteBuffer buffer2 = ByteBuffer.allocate(1000);
		TestObject obj = new TestObject(1);
		Serializer.writeObject(buffer, obj);
		int bufferSize = buffer.position();
		buffer.rewind();
		assertEquals(obj, Serializer.readObject(buffer));
		
		Serializer.writeObject(buffer2, obj);
		int buffer2Size = buffer2.position();
		buffer2.rewind();
		assertEquals(obj, Serializer.readObject(buffer2));

		System.out.println("serializationTestWithInitialize");
		System.out.println(buffer2Size - bufferSize);
		System.out.println((double) bufferSize / buffer2Size);
	}
	
	@Test
	public void serializationTestWithPrivateConstructor() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		TestObjectPrivateConstructor obj = new TestObjectPrivateConstructor(240);
		Serializer.writeObject(buffer, obj);
		buffer.rewind();
		assertEquals(obj, Serializer.readObject(buffer));
	}
	
	@Test
	public void serializationTestWithNoFields() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		TestObjectNoFields obj = new TestObjectNoFields();
		Serializer.writeObject(buffer, obj);
		buffer.rewind();
		assertEquals(obj, Serializer.readObject(buffer));
	}
	
	@Test
	public void serializationTestWithSuperclass() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		TestObjectWithSuperclass obj = new TestObjectWithSuperclass(24, "This is a message");

		Serializer.writeObject(buffer, obj);
		buffer.rewind();
		assertEquals(obj, Serializer.readObject(buffer));
	}
	
	@Test
	public void serializationTestWithNestedCollectionsEmptyList() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		
		ArrayList<Map<String, Integer[]>> list = new ArrayList<Map<String, Integer[]>>();		
		TestObjectWithNestedCollections obj = new TestObjectWithNestedCollections(list);		
		Serializer.writeObject(buffer, obj);
		buffer.rewind();
		assertEquals(obj, Serializer.readObject(buffer));		
	}

	@Test
	public void serializationTestWithNestedCollectionsEmptyMaps() throws IOException {		
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		ArrayList<Map<String, Integer[]>> list = new ArrayList<Map<String, Integer[]>>();
		list.add(new HashMap<String, Integer[]>());
		list.add(null);
		TestObjectWithNestedCollections obj = new TestObjectWithNestedCollections(list);
		Serializer.writeObject(buffer, obj);
		buffer.rewind();
		assertEquals(obj, Serializer.readObject(buffer));
	}

	@Test
	public void serializationTestWithNestedCollectionsEmptyArrays() throws IOException {		
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		ArrayList<Map<String, Integer[]>> list = new ArrayList<Map<String, Integer[]>>();
		HashMap<String, Integer[]> map = new HashMap<String, Integer[]>();
		map.put("asdas", new Integer[0]);
		map.put("", new Integer[]{});
		map.put(null, null);
		list.add(map);
		TestObjectWithNestedCollections obj = new TestObjectWithNestedCollections(list);
		Serializer.writeObject(buffer, obj);
		buffer.rewind();
		assertEquals(obj, Serializer.readObject(buffer));
	}

	@Test
	public void serializationTestWithNestedCollectionsFull() throws IOException {		
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		ArrayList<Map<String, Integer[]>> list = new ArrayList<Map<String, Integer[]>>();
		
		HashMap<String, Integer[]> map = new HashMap<String, Integer[]>();
		map.put("f34", new Integer[]{ 24, 8, -1 });
		map.put("aaa", null);
		map.put(null, new Integer[]{ 0 });
		map.put("", new Integer[0]);
		
		HashMap<String, Integer[]> map2 = new HashMap<String, Integer[]>();
		map2.put("f34r3", new Integer[]{ 324234332 });
		
		list.add(map);
		list.add(map2);
		list.add(new HashMap<String, Integer[]>());
		list.add(null);
		list.add(map);
		
		TestObjectWithNestedCollections obj = new TestObjectWithNestedCollections(list);
		Serializer.writeObject(buffer, obj);
		buffer.rewind();
		assertEquals(obj, Serializer.readObject(buffer));
	}
	
	// Doesn't work with Kryo!
//	@Test 
//	public void serializationTestWithException() throws IOException {		
//		ByteBuffer buffer = ByteBuffer.allocate(1000);
//		RuntimeException exception = new RuntimeException("Nothing happened!");
//		TestObjectWithException obj = new TestObjectWithException(exception);
//		Serializer.writeObject(buffer, obj);
//		buffer.rewind();
//		assertEquals(obj, Serializer.readObject(buffer));
//	}
	
//	@Test(expected=RuntimeException.class)
//	public void checkClassTest() {
//		Serializer.initialize(TestObjectNoConstructor.class);		
//	}
//	

}
