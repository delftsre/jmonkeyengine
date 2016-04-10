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
package com.jme3.renderer;

import java.util.Arrays;

/**
 * A specialized data-structure used to optimize state changes of "slot"
 * based state. 
 */
public class IDList {

    private int[] newList = new int[16];
    private int[] oldList = new int[16];
    private int newLen = 0;
    private int oldLen = 0;

    /**
     * Reset all states to zero
     */
    public void reset(){
        newLen = 0;
        oldLen = 0;
        Arrays.fill(newList, 0);
        Arrays.fill(oldList, 0);
    }

    /**
     * Adds an index to the new list.
     * If the index was not in the old list, false is returned,
     * if the index was in the old list, it is removed from the old
     * list and true is returned.
     * 
     * @param idx The index to move
     * @return True if it existed in old list and was removed
     * from there, false otherwise.
     */
    public boolean moveToNew(int idx){
        if (newLen == 0 || newList[newLen-1] != idx)
            // add item to newList first
            newList[newLen++] = idx;

        // find idx in oldList, if removed successfuly, return true.
        for (int i = 0; i < oldLen; i++){
            if (oldList[i] == idx){
                // found index in slot i
                // delete index from old list
                oldLen --;
                for (int j = i; j < oldLen; j++){
                    oldList[j] = oldList[j+1];
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Copies the new list to the old list, and clears the new list.
     */
    public void copyNewToOld(){
        System.arraycopy(newList, 0, oldList, 0, newLen);
        oldLen = newLen;
        newLen = 0;
    }
    
	/**
	 * @return the newList
	 */
	public int[] getNewList() {
		return newList;
	}

	/**
	 * @param newList the newList to set
	 */
	public void setNewList(int[] newList) {
		this.newList = newList;
	}

	/**
	 * @return the oldList
	 */
	public int[] getOldList() {
		return oldList;
	}

	/**
	 * @param oldList the oldList to set
	 */
	public void setOldList(int[] oldList) {
		this.oldList = oldList;
	}

	/**
	 * @return the newLen
	 */
	public int getNewLen() {
		return newLen;
	}

	/**
	 * @param newLen the newLen to set
	 */
	public void setNewLen(int newLen) {
		this.newLen = newLen;
	}

	/**
	 * @return the oldLen
	 */
	public int getOldLen() {
		return oldLen;
	}

	/**
	 * @param oldLen the oldLen to set
	 */
	public void setOldLen(int oldLen) {
		this.oldLen = oldLen;
	}
	
	public int getOldListElem(int i){
		return oldList[i];
	}
	
	public int getNewListElem(int i){
		return newList[i];
	}
}
