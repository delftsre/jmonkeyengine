/*
 * Copyright (c) 2009-2016 jMonkeyEngine
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
package com.jme3.util;

import java.util.Comparator;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 *
 * @author Jaroslav Ševčík
 */
public class ListSortTest extends ListSort<Object>{
    Comparator<Integer> intComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int x = o1 - o2;
                return (x == 0) ? 0 : (x > 0) ? 1 : -1;
            }
        };
    
    @Test
    public void testSortingIntegers () {
        Integer[] array = new Integer[]{5, 6, 2, 9, 10, 11, 12, 8, 3, 12, 3, 7, -12, 32, 458, 12, -5, 3, 78, 45, 12, 32, 58, 45, 65, -45, 98, 45, 65, 2, 3, 47, 21, 35};
        Integer[] sortedArray = new Integer[]{-45, -12, -5, 2, 2, 3, 3, 3, 3, 5, 6, 7, 8, 9, 10, 11, 12, 12, 12, 12, 21, 32, 32, 35, 45, 45, 45, 47, 58, 65, 65, 78, 98, 458};
        ListSort ls = new ListSort();
        ls.allocateStack(34);
        ls.sort(array, intComparator);
        
        assertArrayEquals(sortedArray, array);
    }
    
    @Test
    public void testSortingDoubles () {
        Double[] array = new Double[]{5.0, 6.0, 2.5, 9.0, 10.0, 11.0, 12.0, 8.0, -3.0, 12.0, 3.0, 7.0, -12.0, 32.0, 458.0, 12.0, 5.0, 3.0, 78.0, -45.0, 12.0, 32.0, 58.0, 45.0, 65.0, 45.0, 98.0, 45.0, 65.0, 2.0, 3.0, 47.0, 21.0, 35.0};
        Double[] sortedArray = new Double[]{-45.0, -12.0, -3.0, 2.0, 2.5, 3.0, 3.0, 3.0, 5.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 12.0, 12.0, 12.0, 21.0, 32.0, 32.0, 35.0, 45.0, 45.0, 45.0, 47.0, 58.0, 65.0, 65.0, 78.0, 98.0, 458.0};
        ListSort ls = new ListSort();
        ls.allocateStack(34);
        ls.sort(array, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                double x = o1 - o2;
                return (x == 0) ? 0 : (x > 0) ? 1 : -1;
            }
        });
        
        assertArrayEquals(sortedArray, array);
    }
    
    @Test
    public void testSortingStability () {
        int arrayLength = 11;
        Integer[] array = new Integer[arrayLength];
        Integer[] sortedArray = new Integer[arrayLength];
        sortedArray[0] = array[0] = new Integer(-8);
        sortedArray[1] = array[5] = new Integer(-3);
        sortedArray[2] = array[6] = new Integer(-3);
        sortedArray[3] = array[7] = new Integer(-3);
        sortedArray[4] = array[8] = new Integer(-3);
        sortedArray[5] = array[2] = new Integer(0);
        sortedArray[6] = array[3] = new Integer(0);
        sortedArray[7] = array[1] = new Integer(5);
        sortedArray[8] = array[4] = new Integer(5);
        sortedArray[9] = array[9] = new Integer(5);
        sortedArray[10] = array[10] = new Integer(8);
        
        ListSort ls = new ListSort();
        ls.allocateStack(arrayLength);
        ls.sort(array, intComparator);
        for (int i = 0; i < arrayLength; i++) {
            assert array[i] == sortedArray[i];
        }
    }
    
    @Test
    public void testGalloppingLeft () {
        ListSort ls = new ListSort();
        ls.setComparator(intComparator);
        ls.allocateStack(15);
        Integer[] array1 = new Integer[]{4, 7, 8, 14, 16, 23, 25, 30};
        Integer[] array2 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 4, 7, 8, 14, 16, 23, 25, 30};
        Integer[] array3 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 4, 7, 8, 14, 16, 25, 25, 30};
        Integer[] array4 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 4, 7, 8, 14, 26, 27, 28, 30};
        int indexA = 0;
        int lenA = 8;
        int key = 25;
        int k = ls.gallop(key, array1, indexA, lenA, lenA - 1, ListSort.GALLOP_LEFT);
//        array[k-1] < key <= array[k]
        Assert.assertEquals(6, k);
        
        key = 24;
        k = ls.gallop(key, array1, indexA, lenA, lenA - 1, ListSort.GALLOP_LEFT);
        Assert.assertEquals(6, k);
        
        key = 21;
        k = ls.gallop(key, array1, indexA, lenA, lenA - 1, ListSort.GALLOP_LEFT);
        Assert.assertEquals(5, k);
        
        lenA = 7;
        int indexB = 7;
        int lenB = 8;
        k = ls.gallop(array2[indexA + lenA - 1], array2, indexB, lenB, lenB - 1, ListSort.GALLOP_LEFT);
//        array[indexB + k-1] < key <= array[indexB + k]
        Assert.assertEquals(6, k);
        
        k = ls.gallop(array3[indexA + lenA - 1], array3, indexB, lenB, lenB - 1, ListSort.GALLOP_LEFT);
        Assert.assertEquals(5, k);
        
        k = ls.gallop(array4[indexA + lenA - 1], array4, indexB, lenB, lenB - 1, ListSort.GALLOP_LEFT);
        Assert.assertEquals(4, k);
    }
    
    @Test
    public void testGalloppingRight () {
        ListSort ls = new ListSort();
        ls.setComparator(intComparator);
        ls.allocateStack(15);
        Integer[] array1 = new Integer[]{2, 3, 5, 8, 16, 20, 25};
        Integer[] array2 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 3, 7, 8, 14, 16, 23, 25, 30};
        Integer[] array3 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 4, 7, 8, 14, 16, 23, 25, 30};
        Integer[] array4 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 5, 7, 8, 14, 16, 23, 25, 30};
        Integer[] array5 = new Integer[]{2, 3, 5, 5, 16, 20, 25, 5, 7, 8, 14, 16, 23, 25, 30};
        int indexA = 0;
        int lenA = 7;
        int key = 3;
        int k = ls.gallop(key, array1, indexA, lenA, 0, ListSort.GALLOP_RIGHT);
//        array[k-1] <= key < array[k]
        Assert.assertEquals(2, k);
        
        key = 4;
        k = ls.gallop(key, array1, indexA, lenA, lenA - 1, ListSort.GALLOP_RIGHT);
        Assert.assertEquals(2, k);
        
        key = 5;
        k = ls.gallop(key, array1, indexA, lenA, lenA - 1, ListSort.GALLOP_RIGHT);
        Assert.assertEquals(3, k);
        
        
        int indexB = 7;
        k = ls.gallop(array2[indexB], array2, indexA, lenA, 0, ListSort.GALLOP_RIGHT);
        Assert.assertEquals(2, k);
        
        k = ls.gallop(array3[indexB], array3, indexA, lenA, 0, ListSort.GALLOP_RIGHT);
        Assert.assertEquals(2, k);
        
        k = ls.gallop(array4[indexB], array4, indexA, lenA, 0, ListSort.GALLOP_RIGHT);
        Assert.assertEquals(3, k);
        
        k = ls.gallop(array5[indexB], array5, indexA, lenA, 0, ListSort.GALLOP_RIGHT);
        Assert.assertEquals(4, k);
    }
    
//    @Test
//    public void testGallopLeft () {
//        ListSort ls = new ListSort();
//        ls.setComparator(intComparator);
//        ls.allocateStack(15);
//        Integer[] array1 = new Integer[]{4, 7, 8, 14, 16, 23, 25, 30};
//        Integer[] array2 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 4, 7, 8, 14, 16, 23, 25, 30};
//        Integer[] array3 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 4, 7, 8, 14, 16, 25, 25, 30};
//        Integer[] array4 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 4, 7, 8, 14, 26, 27, 28, 30};
//        int indexA = 0;
//        int lenA = 8;
//        int key = 25;
//        int k = ls.gallopLeft(key, array1, indexA, lenA, lenA - 1);
////        array[k-1] < key <= array[k]
//        Assert.assertEquals(6, k);
//        
//        key = 24;
//        k = ls.gallopLeft(key, array1, indexA, lenA, lenA - 1);
//        Assert.assertEquals(6, k);
//        
//        key = 21;
//        k = ls.gallopLeft(key, array1, indexA, lenA, lenA - 1);
//        Assert.assertEquals(5, k);
//        
//        lenA = 7;
//        int indexB = 7;
//        int lenB = 8;
//        k = ls.gallopLeft(array2[indexA + lenA - 1], array2, indexB, lenB, lenB - 1);
////        array[indexB + k-1] < key <= array[indexB + k]
//        Assert.assertEquals(6, k);
//        
//        k = ls.gallopLeft(array3[indexA + lenA - 1], array3, indexB, lenB, lenB - 1);
//        Assert.assertEquals(5, k);
//        
//        k = ls.gallopLeft(array4[indexA + lenA - 1], array4, indexB, lenB, lenB - 1);
//        Assert.assertEquals(4, k);
//    }
//    
//    @Test
//    public void testGallopRight () {
//        ListSort ls = new ListSort();
//        ls.setComparator(intComparator);
//        ls.allocateStack(15);
//        Integer[] array1 = new Integer[]{2, 3, 5, 8, 16, 20, 25};
//        Integer[] array2 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 3, 7, 8, 14, 16, 23, 25, 30};
//        Integer[] array3 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 4, 7, 8, 14, 16, 23, 25, 30};
//        Integer[] array4 = new Integer[]{2, 3, 5, 8, 16, 20, 25, 5, 7, 8, 14, 16, 23, 25, 30};
//        Integer[] array5 = new Integer[]{2, 3, 5, 5, 16, 20, 25, 5, 7, 8, 14, 16, 23, 25, 30};
//        int indexA = 0;
//        int lenA = 7;
//        int key = 3;
//        int k = ls.gallopRight(key, array1, indexA, lenA, 0);
////        array[k-1] <= key < array[k]
//        Assert.assertEquals(2, k);
//        
//        key = 4;
//        k = ls.gallopRight(key, array1, indexA, lenA, lenA - 1);
//        Assert.assertEquals(2, k);
//        
//        key = 5;
//        k = ls.gallopRight(key, array1, indexA, lenA, lenA - 1);
//        Assert.assertEquals(3, k);
//        
//        
//        int indexB = 7;
//        k = ls.gallopRight(array2[indexB], array2, indexA, lenA, 0);
//        Assert.assertEquals(2, k);
//        
//        k = ls.gallopRight(array3[indexB], array3, indexA, lenA, 0);
//        Assert.assertEquals(2, k);
//        
//        k = ls.gallopRight(array4[indexB], array4, indexA, lenA, 0);
//        Assert.assertEquals(3, k);
//        
//        k = ls.gallopRight(array5[indexB], array5, indexA, lenA, 0);
//        Assert.assertEquals(4, k);
//    }
    
    
}
