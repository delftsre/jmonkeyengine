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
public class IDListAfterRefactorTest {
	
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
	@Test
	public void testResetWhenZeroStates() {
		idList.reset();
		assertEquals(idList.getNewLen(), 0);
		assertEquals(idList.getOldLen(), 0);
		assertArrayEquals(idList.getNewList(), new int[16]);
		assertArrayEquals(idList.getOldList(), new int[16]);
	}
	
	/**
	 * Should reset all states to zero when all states are non zero.
	 * Test method for {@link com.jme3.renderer.IDList#reset()}.
	 */
	@Test
	public void testResetWhenNonZeroStates() {
		//setup state
		idList.setNewLen(5);
		idList.setOldLen(11);
		int[] newListArr = new int[16];
		Arrays.fill(newListArr, 1);
		idList.setNewList(newListArr);
		int[] oldListArr = new int[16];
		Arrays.fill(oldListArr, 3);
		idList.setNewList(oldListArr);
		
		//reset state
		idList.reset();
		assertEquals(idList.getNewLen(), 0);
		assertEquals(idList.getOldLen(), 0);
		assertArrayEquals(idList.getNewList(), new int[16]);
		assertArrayEquals(idList.getOldList(), new int[16]);
	}

	/**
	 * Adds an index to the new list when index was not in old list.
	 * Test method for {@link com.jme3.renderer.IDList#moveToNew(int)}.
	 */
	@Test
	public void testMoveToNewWhenIndexNotInOldList() {
		idList.setNewLen(0);
		idList.setOldLen(0);
		
		//the index was not in the old list, false is returned
		boolean state = idList.moveToNew(5);
		assertFalse(state);
	}
	
	/**
	 * Adds an index to the new list when index is in old list.
	 * Test method for {@link com.jme3.renderer.IDList#moveToNew(int)}.
	 */
	@Test
	public void testMoveToNewWhenIndexInOldList() {
		idList.setNewLen(1);
		idList.setNewList(new int[16]);
		idList.setOldLen(5);
		idList.setOldList(new int[]{1,3,5,6,7});
		
		//the index was in the old list, true is returned
		boolean state = idList.moveToNew(5);
		assertTrue(state);
		assertEquals(idList.getOldLen(), 4);
		assertEquals(idList.getNewListElem(1), 5);
	}

	/**
	 * Copies the new list to the old list, and clears the new list.
	 * Test method for {@link com.jme3.renderer.IDList#copyNewToOld()}.
	 */
	@Test
	public void testCopyNewToOld() {
		idList.setNewLen(4);
		idList.setNewList(new int[]{1,2,3,4});
		idList.setOldLen(4);
		idList.setOldList(new int[]{5,6,7,8});
		
		idList.copyNewToOld();
		
		assertArrayEquals(idList.getNewList(), new int[]{1,2,3,4});
		assertEquals(idList.getNewLen(), 0);
		assertArrayEquals(idList.getOldList(), new int[]{1,2,3,4});
		assertEquals(idList.getOldLen(), 4);
		
	}
	
	/**
	 * Copies the new list to the old list, and clears the new list when old has less length.
	 * In this case is uncaptured exception and func do not copy list.
	 * Test method for {@link com.jme3.renderer.IDList#copyNewToOld()}.
	 */
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testCopyNewToOldWhenOldHasLessLen() {
		idList.setNewLen(4);
		idList.setNewList(new int[]{1,2,3,4});
		idList.setOldLen(4);
		idList.setOldList(new int[]{5,6,7});
		
		idList.copyNewToOld();
	}
}
