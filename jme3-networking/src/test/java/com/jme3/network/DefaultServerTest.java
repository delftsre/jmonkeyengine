package com.jme3.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.jme3.network.kernel.tcp.SelectorKernel;
import com.jme3.network.base.DefaultServer;

public class DefaultServerTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void createServerWithNoKernels() {
		 DefaultServer ds = new DefaultServer( "Test", 1, null, null );
		 ds.close();
	}

	@Test(expected=IllegalStateException.class)
	public void createServerWithAdditionalChannel() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, null );
		 ds.addChannel(2345);
		 ds.close();
	}
	
	@Test
	public void createServer() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, null );
	}
	
	@Test
	public void createServerTwoChannels() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 SelectorKernel fast = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, fast );
	}
	
	@Test
	public void createServerTwoChannelsAddChannel() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 SelectorKernel fast = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, fast );
		 ds.addChannel(1234);
	}
	
	@Test
	public void createServerTwoChannelsAddManyChannels() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 SelectorKernel fast = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, fast );
		 for (int i = 1000; i < 10000; i++)
			 ds.addChannel(i);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createServerTwoChannelsAddChannelIllegalPort() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 SelectorKernel fast = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, fast );
		 ds.addChannel(68000);
	}
	
	@Test
	public void createServerAndRun() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, null );
		 ds.start();
		 ds.close();
	}
	
	@Test
	public void testcreateServerAndRunIsRunning() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, null );
		 ds.start();
		 assertTrue(ds.isRunning());
		 ds.close();
	}
	
	@Test(expected=IllegalStateException.class)
	public void createServerAndClose() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, null );
		 ds.close();
	}

	
	
	@Test
	public void testBroadcastMessage() {
		 SelectorKernel reliable = mock(SelectorKernel.class);
		 DefaultServer ds = new DefaultServer( "Test", 1, reliable, null );
		 ds.start();
		 ds.broadcast(new AbstractMessage() {
				private boolean reliable;
				@Override
				public AbstractMessage setReliable(boolean f) {
					this.reliable = f;
					return this;
				}
				
				@Override
				public boolean isReliable() {
					return reliable;
	            }
		  });
		 
		 ds.close();
	}
}
