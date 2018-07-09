import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.ShrinkingMode;

public class JkwiqTests {

    static int count = 0;

    @Property // is checked for 1000 random values provided by @ForAll generator
    boolean every_third_element_starts_with_Fizz(@ForAll("divisibleBy3") int i) {
        System.out.println(count++);
        return fizzBuzz(i).startsWith("Fizz");
    }

    @Property
    boolean every_fifth_element_ends_with_Buzz(@ForAll("divisibleBy5") int i) {
        System.out.println(i);
        return fizzBuzz(i).endsWith("Buzz");
    }

    @Provide
    Arbitrary<Integer> divisibleBy5() {
        // provides a single random number between 1 and 1000 on each call
        return Arbitraries.integers().between(1, 1000).filter(i -> i % 5 == 0);
    }

    @Provide
    Arbitrary<Integer> divisibleBy3() {
        // provides a single random number between 1 and 1000 on each call
        return Arbitraries.integers().between(1, 1000).filter(i -> i % 3 == 0);
    }

    @Property(shrinking = ShrinkingMode.BOUNDED)
    boolean absoluteValueOfAllNumbersIsPositive(@ForAll int anInteger) {
        return anInteger <= 0;
    }

    /**
     * Code under test
     * @return fizzBUzz 0-100
     */
    private String fizzBuzz(int i) {
            boolean divBy3 = i % 3 == 0;
            boolean divBy5 = i % 5 == 0;

            return divBy3 && divBy5 ? "FizzBuzz"
                    : divBy3 ? "Fizz"
                    : divBy5 ? "Buzz"
                    : String.valueOf(i);
    }
}