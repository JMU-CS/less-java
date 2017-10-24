import org.junit.Test;
import static org.junit.Assert.*;
public class Main
{
    public static void main(String[] args)
    {
    }
    public static int add(int a, int b)
    {
        return (a+b);
    }
    @Test
    public void test0() {
        assertTrue((add(1, 1)==2));
    }
    @Test
    public void test1() {
        assertTrue((add(1, 2)==3));
    }
    @Test
    public void test2() {
        assertTrue((add(1, 3)==4));
    }
    @Test
    public void test3() {
        assertTrue((add(1, 4)==5));
    }
}
