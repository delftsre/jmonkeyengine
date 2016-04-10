/**
 * 
 */
package com.jme3.renderer;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * @author tr0k
 *
 */
public class IDListTest {
	
	private IDList idList;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		idList = new IDList();
	}

	/**
	 * Should reset all states to zero when all states are zero.
	 * Test method for {@link com.jme3.renderer.IDList#reset()}.
	 */
//	@Test
//	public void testResetWhenZeroStates() {
//		idList.reset();
//		assertEquals(idList.newLen, 0);
//		assertEquals(idList.oldLen, 0);
//		assertArrayEquals(idList.newList, new int[16]);
//		assertArrayEquals(idList.oldList, new int[16]);
//	}
//	
//	/**
//	 * Should reset all states to zero when all states are non zero.
//	 * Test method for {@link com.jme3.renderer.IDList#reset()}.
//	 */
//	@Test
//	public void testResetWhenNonZeroStates() {
//		//setup state
//		idList.newLen = 5;
//		idList.oldLen = 11;
//		Arrays.fill(idList.newList, 1);
//		Arrays.fill(idList.oldList, 3);
//		
//		//reset state
//		idList.reset();
//		assertEquals(idList.newLen, 0);
//		assertEquals(idList.oldLen, 0);
//		assertArrayEquals(idList.newList, new int[16]);
//		assertArrayEquals(idList.oldList, new int[16]);
//	}
//
//	/**
//	 * Adds an index to the new list when index was not in old list.
//	 * Test method for {@link com.jme3.renderer.IDList#moveToNew(int)}.
//	 */
//	@Test
//	public void testMoveToNewWhenIndexNotInOldList() {
//		idList.newLen = 0;
//		idList.oldLen = 0;
//		
//		//the index was not in the old list, false is returned
//		boolean state = idList.moveToNew(5);
//		assertFalse(state);
//	}
//	
//	/**
//	 * Adds an index to the new list when index is in old list.
//	 * Test method for {@link com.jme3.renderer.IDList#moveToNew(int)}.
//	 */
//	@Test
//	public void testMoveToNewWhenIndexInOldList() {
//		idList.newLen = 1;
//		idList.newList = new int[16];
//		idList.oldLen = 5;
//		idList.oldList = new int[]{1,3,5,6,7};
//		
//		//the index was in the old list, true is returned
//		boolean state = idList.moveToNew(5);
//		assertTrue(state);
//		assertEquals(idList.oldLen, 4);
//		assertEquals(idList.newList[1], 5);
//	}
//
//	/**
//	 * Copies the new list to the old list, and clears the new list.
//	 * Test method for {@link com.jme3.renderer.IDList#copyNewToOld()}.
//	 */
//	@Test
//	public void testCopyNewToOld() {
//		idList.newList = new int[]{1,2,3,4};
//		idList.newLen = 4;
//		idList.oldList = new int[]{5,6,7,8};
//		idList.oldLen = 4;
//		idList.copyNewToOld();
//		
//		assertArrayEquals(idList.newList, new int[]{1,2,3,4});
//		assertEquals(idList.newLen, 0);
//		assertArrayEquals(idList.oldList, new int[]{1,2,3,4});
//		assertEquals(idList.oldLen, 4);
//		
//	}
//	
//	/**
//	 * Copies the new list to the old list, and clears the new list when old has less length.
//	 * In this case is uncaptured exception and func do not copy list.
//	 * Test method for {@link com.jme3.renderer.IDList#copyNewToOld()}.
//	 */
//	@Test(expected = ArrayIndexOutOfBoundsException.class)
//	public void testCopyNewToOldWhenOldHasLessLen() {
//		idList.newList = new int[]{1,2,3,4};
//		idList.newLen = 4;
//		idList.oldList = new int[]{5,6,7};
//		idList.oldLen = 4;
//		idList.copyNewToOld();
//	}
}
