package jme3tools.navigation;

import org.junit.Test;

/**
 * Check if the {@link StringUtil} class is working correctly.
 * 
 * @author Enrique Correa
 */
public class StringUtilTest {

  StringUtil sUtil = new StringUtil();

  @Test
  public void padNumTest() {
    // Long Values
    assert sUtil.padNum(1 << 30, 3).equals("1073741824");
    assert sUtil.padNum(1 << 30, 4).equals("1073741824");
    assert sUtil.padNum(1 << 30, 14).equals("    1073741824");
    assert sUtil.padNum(1 << 30, 18).equals("        1073741824");
    assert sUtil.padNum(1 << 30, -1).equals("1073741824");
    // Integer Values
    assert sUtil.padNum(1048576, 3).equals("1048576");
    assert sUtil.padNum(1048576, 4).equals("1048576");
    assert sUtil.padNum(1048576, 11).equals("    1048576");
    assert sUtil.padNum(1048576, 15).equals("        1048576");
    assert sUtil.padNum(1048576, -1).equals("1048576");
    // Double Values
  }

  @Test
  public void padNumZeroTest() {
    // Long Values
    assert sUtil.padNumZero(1 << 30, 3).equals("1073741824");
    assert sUtil.padNumZero(1 << 30, 4).equals("1073741824");
    assert sUtil.padNumZero(1 << 30, 14).equals("00001073741824");
    assert sUtil.padNumZero(1 << 30, 18).equals("000000001073741824");
    assert sUtil.padNumZero(1 << 30, -1).equals("1073741824");
    // Integer Values
    assert sUtil.padNumZero(1048576, 3).equals("1048576");
    assert sUtil.padNumZero(1048576, 4).equals("1048576");
    assert sUtil.padNumZero(1048576, 11).equals("00001048576");
    assert sUtil.padNumZero(1048576, 15).equals("000000001048576");
    assert sUtil.padNumZero(1048576, -1).equals("1048576");
    // Double Values
  }

  @Test
  public void boolArrToStrTest() {
    boolean[] array1 = { true, false, true, false, false };
    boolean[] array2 = { true, true, true, true, true };
    assert sUtil.boolArrToStr(array1).equals("10100");
    assert sUtil.boolArrToStr(array2).equals("11111");
  }

  @Test
  public void padStringRightTest() {
    assert sUtil.padStringRight("hello world", 10).equals("hello world");
    assert sUtil.padStringRight("hello world", 12).equals("hello world ");
    assert sUtil.padStringRight("hello world", 13).equals("hello world  ");
  }

    @Test
  public void prettyNumTest() {
    assert sUtil.prettyNum(10.5000434).equals("10.50");
    assert sUtil.prettyNum(3.500305345).equals("3.50");
  }
}