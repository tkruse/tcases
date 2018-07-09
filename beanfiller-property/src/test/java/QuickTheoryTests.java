
import org.junit.Test;
import org.quicktheories.dsl.TheoryBuilder2;

import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.integers;

public class QuickTheoryTests {

    @Test
    public void addingTwoPositiveIntegersAlwaysGivesAPositiveInteger(){
        TheoryBuilder2<Integer, Integer> tBuilder = qt()
                .forAll(integers().allPositive()
                        , integers().allPositive());
        tBuilder.check((i,j) -> i + j > 0);
    }

}