package convertisseurs;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class DecodeurCanalTableVeriteClass {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {true, true, true,      true},      // 111 -> 1
                {true, true, false,     false},     // 110 -> 0
                {true, false, true,     true},      // 101 -> 1
                {true, false, false,    true},      // 100 -> 1
                {false, true, true,     false},     // 011 -> 0
                {false, true, false,    false},     // 010 -> 0
                {false, false, true,    true},      // 001 -> 1
                {false, false, false,   false}      // 000 -> 0
        });
    }

    private boolean Entry0, Entry1, Entry2, ExpectedOutput;

    public DecodeurCanalTableVeriteClass(boolean r0, boolean r1, boolean r2, boolean s) {
        this.Entry0 = r0;
        this.Entry1 = r1;
        this.Entry2 = r2;
        this.ExpectedOutput = s;
    }

    @Test
    public void test() {
                System.out.println("Testing: " + Entry0 + "-" + Entry1 + "-" + Entry2 + " | Expected: " + ExpectedOutput);
        DecodeurCanal decodCanal = new DecodeurCanal();
        boolean actualOutput = decodCanal.tableDeVerite(Entry0, Entry1, Entry2);
        Assert.assertEquals(ExpectedOutput, actualOutput);
    }
}
