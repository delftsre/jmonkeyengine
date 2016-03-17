package com.jme3.renderer;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;


/**
 * Mocked IDListPlainTextPrinter class.
 * @author Tr0k
 */
public class IDListPlainTextPrinterJMockitoTest {
	
	@Test
	public void printExpectedList(){
		//All System.out.println() statements will come to outContent stream
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		//Filling list
		IDList list = new IDList();
		list.setNewLen(5);
		list.setNewList(new int[] {1,2,3,4,5});
		list.setOldLen(5);
		list.setOldList(new int[] {6,7,8,9,10});
		
		IListPrinter printer = new IDListPlainTextPrinter(list);
		printer.printList();
		
		String separator = System.getProperty( "line.separator" );
		String expectedOutput = "New List: "+"1, 2, 3, 4, 5"+separator+
								"Old List: "+"6, 7, 8, 9, 10"+separator;
		assertEquals(outContent.toString(), expectedOutput);
	}
	
	@Test
	public void printMockedList(){
		//All System.out.println() statements will come to outContent stream
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		//Filling list
		IDList mockList = Mockito.mock(IDList.class);
		when(mockList.getNewLen()).thenReturn(4);
		when(mockList.getOldLen()).thenReturn(4);
		when(mockList.getNewList()).thenReturn(new int[] {1,2,3,4});
		when(mockList.getOldList()).thenReturn(new int[] {6,7,8,9});
		//Method returns arg + 1
		when(mockList.getNewListElem(anyInt())).thenAnswer(new Answer<Integer>() {
			  public Integer answer(InvocationOnMock invocation) throws Throwable {
			    Object[] args = invocation.getArguments();
			    return (int) args[0] + 1;
			  }
			});
		//Method returns arg + 5
		when(mockList.getOldListElem(anyInt())).thenAnswer(new Answer<Integer>() {
			  public Integer answer(InvocationOnMock invocation) throws Throwable {
			    Object[] args = invocation.getArguments();
			    return (int) args[0] + 6;
			  }
			});
		
		IListPrinter printer = new IDListPlainTextPrinter(mockList);
		printer.printList();

		//Verify method execution
		verify(mockList, atLeast(5)).getNewLen();
		verify(mockList, atLeast(5)).getOldLen();
		verify(mockList, times(4)).getNewListElem(anyInt());
		verify(mockList, times(4)).getOldListElem(anyInt());
		verify(mockList, never()).reset();
		verify(mockList, never()).moveToNew(anyInt());
		verify(mockList, never()).copyNewToOld();
		
		//Check result
		String separator = System.getProperty( "line.separator" );
		String expectedOutput = "New List: "+"1, 2, 3, 4"+separator+
								"Old List: "+"6, 7, 8, 9"+separator;
		assertEquals(outContent.toString(), expectedOutput);
	}
}
