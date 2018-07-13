package net.indeed.test.sample;

import org.beanfiller.annotation.creator.FunctionTestsCreator;

import java.util.List;

/**
 * @author tkruse
 */

public class ProctorTest {

    public static void main(String[] args) {
        int tupleSize = 2;
        List<ProctorInputs> testCases = new FunctionTestsCreator<>(ProctorInputs.class)
                .tupleGenerator(tupleSize)
                .createDefs();

        testCases.forEach(test -> System.out.println(test));
    }

}
