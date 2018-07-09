import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class JUQuickCheckTest {
    @Property
    public void concatenationLength(String s1, String s2) {
        System.out.println(s1 + " " + s2);
        assertEquals(s1.length() + s2.length(), (s1 + s2).length());
    }
}
