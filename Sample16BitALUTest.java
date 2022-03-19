import org.junit.Assert;
import org.junit.Test;

import java.beans.Transient;
import java.util.function.BiFunction;

import static edu.gvsu.dlunit.DLUnit.*;

/**
 * Test cases for a signed 16-bit ALU.
 * <p>
 * IMPORTANT:  These test cases do *not* thoroughly test the circuit.  You need to
 * re-name this class and add more tests!
 * <p>
 * <p>
 * Created by kurmasz on 8/8/16.
 */
public class Sample16BitALUTest {

  public static class OpCodes {
    public static final int ADDU = 0;
    public static final int SUBU = 1;
    public static final int AND  = 2;
    public static final int OR   = 3;
    public static final int NOT  = 4;
    public static final int XOR  = 5;
    public static final int LUI  = 6;
    public static final int SLTU = 7;
    public static final int ADD  = 8;
    public static final int SUB  = 9;
    public static final int SLT  = 15;
  }


  @Test
  public void testAddu() {
    setPinUnsigned("InputA", 53400);
    setPinUnsigned("InputB", 53500);
    setPinUnsigned("Op", OpCodes.ADDU);
    run();
    Assert.assertEquals("Addition Output", (53400 + 53500) % 65536, readPinUnsigned("Output"));

    // Overflow for unsigned addition is false by definition
    Assert.assertEquals("Addition Overflow", false, readPin("Overflow"));
  }

  @Test
  public void testAddu2() {
    setPinUnsigned("InputA", 123);
    setPinUnsigned("InputB", 456);
    setPinUnsigned("Op", OpCodes.ADDU);
    run();
    Assert.assertEquals("Addition Output", (123 + 456) % 65536, readPinUnsigned("Output"));

    // Overflow for unsigned addition is false by definition
    Assert.assertEquals("Addition Overflow", false, readPin("Overflow"));
  }


  @Test
  public void testAddition() {
    setPinSigned("InputA", 23);
    setPinSigned("InputB", 44);
    setPinUnsigned("Op", OpCodes.ADD);
    run();
    Assert.assertEquals("Addition Output", 23 + 44, readPinSigned("Output"));
    Assert.assertEquals("Addition Overflow", false, readPin("Overflow"));
  }

  @Test
  public void testAddition2() {
    setPinSigned("InputA", -23);
    setPinSigned("InputB", 44);
    setPinUnsigned("Op", OpCodes.ADD);
    run();
    Assert.assertEquals("Addition Output", -23 + 44, readPinSigned("Output"));
    Assert.assertEquals("Addition Overflow", false, readPin("Overflow"));
  }
  @Test
  public void testAddition3() {
    setPinSigned("InputA", -23);
    setPinSigned("InputB", -78);
    setPinUnsigned("Op", OpCodes.ADD);
    run();
    Assert.assertEquals("Addition Output", -23 + -78, readPinSigned("Output"));
    Assert.assertEquals("Addition Overflow", false, readPin("Overflow"));
  }

  @Test
  public void testSubtraction() {
    setPinSigned("InputA", 24);
    setPinSigned("InputB", 45);
    setPinUnsigned("Op", OpCodes.SUB);
    run();
    Assert.assertEquals("Subtraction Output", 24 - 45, readPinSigned("Output"));
    Assert.assertEquals("Subtraction Overflow", false, readPin("Overflow"));
  }
  @Test
  public void testSubtraction2() {
    setPinSigned("InputA", -24);
    setPinSigned("InputB", 45);
    setPinUnsigned("Op", OpCodes.SUB);
    run();
    Assert.assertEquals("Subtraction Output", -24 - 45, readPinSigned("Output"));
    Assert.assertEquals("Subtraction Overflow", false, readPin("Overflow"));
  }
  @Test
  public void testSubtraction3() {
    setPinSigned("InputA", 24);
    setPinSigned("InputB", -45);
    setPinUnsigned("Op", OpCodes.SUB);
    run();
    Assert.assertEquals("Subtraction Output", 24 - (-45), readPinSigned("Output"));
    Assert.assertEquals("Subtraction Overflow", false, readPin("Overflow"));
  }
  @Test
  public void testSubtraction4() {
    setPinSigned("InputA", 5000);
    setPinSigned("InputB", -10000);
    setPinUnsigned("Op", OpCodes.SUB);
    run();
    Assert.assertEquals("Subtraction Output", 5000 - (-10000), readPinSigned("Output"));
    Assert.assertEquals("Subtraction Overflow", false, readPin("Overflow"));
  }

  @Test
  public void ltSigned() {
    setPinSigned("InputA", 5);
    setPinSigned("InputB", 6);
    setPinUnsigned("Op", OpCodes.SLT);
    run();
    Assert.assertEquals("Signed Less Than Output", 1, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }
  @Test
  public void ltSigned2() {
    setPinSigned("InputA", -20);
    setPinSigned("InputB", 6);
    setPinUnsigned("Op", OpCodes.SLT);
    run();
    Assert.assertEquals("Signed Less Than Output", 1, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }

  @Test
  public void ltSigned3() {
    setPinSigned("InputA", 32767);
    setPinSigned("InputB", -1);
    setPinUnsigned("Op", OpCodes.SLT);
    run();
    Assert.assertEquals("Signed Less Than Output", 0, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }
  @Test
  public void ltSigned4() {
    setPinSigned("InputA", 8);
    setPinSigned("InputB", 6);
    setPinUnsigned("Op", OpCodes.SLT);
    run();
    Assert.assertEquals("Signed Less Than Output", 0, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }
  @Test
  public void ltSigned5() {
    setPinSigned("InputA", -100);
    setPinSigned("InputB", -200);
    setPinUnsigned("Op", OpCodes.SLT);
    run();
    Assert.assertEquals("Signed Less Than Output", 0, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }
  @Test
  public void ltSigned6() {
    setPinSigned("InputA", -500);
    setPinSigned("InputB", -20);
    setPinUnsigned("Op", OpCodes.SLT);
    run();
    Assert.assertEquals("Signed Less Than Output", 1, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }
  //CHECK OVERFLOW FOR LT


  public static void verifySigned(long a, long b, boolean checkOverflow) {
    long expected = (a < b) ? 1 : 0;

    setPinSigned("InputA", a);
    setPinSigned("InputB", b);
    setPinUnsigned("Op", OpCodes.SLT);
    run();
    String message = String.format(" of %d < %d (signed) ", a, b);
    Assert.assertEquals("Output" + message, expected, readPinUnsigned("Output"));
    if (checkOverflow) {
      Assert.assertEquals("Overflow" + message, false, readPin("Overflow"));
    }
  }


  @Test
  public void ltSigned_allPairs() {
    long[] values = {-32768, -32767, -1, 0, 1, 32766, 32767};
    for (long a : values) {
      for (long b : values) {
        verifySigned(a, b, true);
      }
    }
  }

  public static void verifyUnsigned(long a, long b, boolean checkOverflow) {
    long expected = (a < b) ? 1 : 0;

    setPinUnsigned("InputA", a);
    setPinUnsigned("InputB", b);
    setPinUnsigned("Op", OpCodes.SLTU);
    run();
    String message = String.format(" of %d < %d (unsigned) ", a, b);
    Assert.assertEquals("Output" + message, expected, readPinUnsigned("Output"));
    if (checkOverflow) {
      Assert.assertEquals("Overflow" + message, false, readPin("Overflow"));
    }
  }

  @Test
  public void ltUnsigned_allPairs() {
    long[] values = {0, 1, 2, 65534, 65535};
    for (long a : values) {
      for (long b : values) {
        verifyUnsigned(a, b, true);
      }
    }
  }

  private void verifyLogic(String name, int op, long a, long b, BiFunction<Long, Long, Long> func) {
    setPinUnsigned("InputA", a);
    setPinUnsigned("InputB", b);
    setPinUnsigned("Op", op);
    run();
    String message = String.format("0x%x %s 0x%x", a, name, b);
    Assert.assertEquals(message, (long)func.apply(a, b), readPinUnsigned("Output"));
    Assert.assertFalse(message + " overflow", readPin("Overflow"));
  }

  @Test
  public void testAnd() {
    verifyLogic("and", OpCodes.AND, 0xFF00, 0x0F0F, (a, b) -> a & b);
  }
  @Test
  public void testAnd2() {
    verifyLogic("and", OpCodes.AND, 0xFFA0, 0xAFF0, (a, b) -> a & b);
  }
  @Test
  public void testOr() {
    verifyLogic("or", OpCodes.OR, 0xFF00, 0x0F0F, (a, b) -> a | b);
  }
  @Test
  public void testOr2() {
    verifyLogic("or", OpCodes.OR, 0xFFAA, 0xAFAF, (a, b) -> a | b);
  }
  @Test
  public void testXor() {
    verifyLogic("xor", OpCodes.XOR, 0xFFA0, 0x0F0F, (a, b) -> a ^ b);
  }
  @Test
  public void testXor2() {
    verifyLogic("xor", OpCodes.XOR, 0x00AA, 0xAFAF, (a, b) -> a ^ b);
  }
  @Test
  public void testlui(){
    setPinSigned("InputA", 0x0001);
    setPinSigned("InputB", -1);
    setPinUnsigned("Op", OpCodes.LUI);
    run();
    Assert.assertEquals("Signed Less Than Output", 0x0100, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }

  @Test
  public void testlui2(){
    setPinSigned("InputA", 0x1111);
    setPinSigned("InputB", -1);
    setPinUnsigned("Op", OpCodes.LUI);
    run();
    Assert.assertEquals("Signed Less Than Output", 0x1100, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }

  @Test
  public void testlui3(){
    setPinSigned("InputA", 0x2200);
    setPinSigned("InputB", -1);
    setPinUnsigned("Op", OpCodes.LUI);
    run();
    Assert.assertEquals("Signed Less Than Output", 0x0000, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }


}
