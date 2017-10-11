import org.junit.Test;

import static org.junit.Assert.*;

public class JUnitTest
{
    @Test
    public void test0() {
        assertTrue((Main.add(1, 1)==2));
    }
    @Test
    public void test1() {
        assertTrue((Main.add(1, 2)==3));
    }
    @Test
    public void test2() {
        assertTrue((Main.add(1, 3)==4));
    }
    @Test
    public void test3() {
        assertTrue((Main.add(1, 4)==5));
    }
}
