package org.beanfiller.annotation.sample.find.internal;

import org.apache.commons.collections4.IteratorUtils;
import org.beanfiller.annotation.creator.AbstractTestInput;
import org.beanfiller.annotation.creator.FunctionTestsCreator;
import org.beanfiller.annotation.reader.SystemDefReader;
import org.beanfiller.annotation.sample.find.FindTestInput;
import org.cornutum.tcases.FunctionInputDef;
import org.cornutum.tcases.FunctionTestDef;
import org.cornutum.tcases.IVarDef;
import org.cornutum.tcases.SystemInputDef;
import org.cornutum.tcases.SystemTestDef;
import org.cornutum.tcases.Tcases;
import org.cornutum.tcases.TestCase;
import org.cornutum.tcases.generator.GeneratorOptions;
import org.cornutum.tcases.generator.GeneratorSet;
import org.cornutum.tcases.generator.TupleGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Comprehensive Test using the sample.FindTestInput class as SystemTestDefinition/FunctionDefinition
 */
public class FindTestInputTest {


    public static final String SYSTEM = "findSystem";
    private GeneratorSet genDef;
    private SystemTestDef baseDef;
    private GeneratorOptions options;

    @Before
    public void setUp() {
        genDef = GeneratorSet.basicGenerator();

        baseDef = null;
        options = new GeneratorOptions();
    }



    @Test
    public void testSystemDefFromAnnotations() {
        SystemInputDef systemDef = new SystemDefReader().readSystemDefFromFunctionDefs(SYSTEM, FindTestInput.class);
        assertNotNull(systemDef);
        assertThat(systemDef.getName(), equalTo(SYSTEM));
        List<FunctionInputDef> functionInputDefs = IteratorUtils.toList(systemDef.getFunctionInputDefs());
        assertThat(toString(systemDef), functionInputDefs.size(), equalTo(1));

        FunctionInputDef fun1Def = functionInputDefs.get(0);
        assertThat(fun1Def.getName(), equalTo(FindTestInput.FUNCTION_NAME));

        List<IVarDef> varDefs = IteratorUtils.toList(fun1Def.getVarDefs());
        assertThat(varDefs.size(), equalTo(3));

        assertNotNull(fun1Def.findVarPath("filenameDefined"));
        assertNotNull(fun1Def.findVarPath("pattern.size"));
        assertNotNull(fun1Def.findVarPath("file.exists"));
    }


    @Test
    public void testTestDefFromAnnotations() {
        SystemInputDef systemDef = new SystemDefReader().readSystemDefFromFunctionDefs(SYSTEM, FindTestInput.class);

        /* generate testcases */

        SystemTestDef testDef = Tcases.getTests(systemDef, genDef, baseDef, options);
        assertThat(testDef.getName(), equalTo(SYSTEM));

        List<FunctionTestDef> testDefsList = IteratorUtils.toList(testDef.getFunctionTestDefs());
        assertThat(toString(testDef), testDefsList.size(), equalTo(1));
        FunctionTestDef fun1TestDef = testDefsList.get(0);
        assertThat(fun1TestDef.getName(), equalTo(FindTestInput.FUNCTION_NAME));

        List<TestCase> testCaseList = IteratorUtils.toList(fun1TestDef.getTestCases());
        // check total number
        assertThat(testCaseList.size(), equalTo(10));
        // check failure number
        assertThat(testCaseList.stream().filter(testCase -> testCase.getType() == TestCase.Type.FAILURE).count(), equalTo(5L));
        // Check id
        for (int i = 0; i < testCaseList.size(); i++) {
            assertThat(testCaseList.get(i).getId(), equalTo(i));
        }
    }


    @Test
    public void testInstanceCreation() {
        SystemInputDef systemDef = new SystemDefReader().readSystemDefFromFunctionDefs(SYSTEM, FindTestInput.class);

        /* generate testcases */

        SystemTestDef testDef = Tcases.getTests(systemDef, genDef, baseDef, options);
        List<FunctionTestDef> testDefsList = IteratorUtils.toList(testDef.getFunctionTestDefs());
        FunctionTestDef fun1TestDef = testDefsList.get(0);
        List<TestCase> testCaseList = IteratorUtils.toList(fun1TestDef.getTestCases());

        /* generate test instances */
        List<FindTestInput> findList = new FunctionTestsCreator<>(FindTestInput.class)
                .createDefs();

        assertThat(findList.size(), equalTo(testCaseList.size()));
        // check failure number
        List<FindTestInput> failures = findList.stream().filter(AbstractTestInput::isFailure).collect(Collectors.toList());
        assertEquals(failures.size(), 5);
        failures.forEach(FindTestInput -> {
            assertTrue(FindTestInput.having().getVarBindingAnnotationKeys().hasNext());
        });

        // Check id
        for (int i = 0; i < findList.size(); i++) {
            assertThat(findList.get(i).getTestCaseId(), equalTo(i));
            System.out.println(findList.get(i));
        }
    }


    @Test
    public void testTestDefFromAnnotations2Tupel() {
        SystemInputDef systemDef = new SystemDefReader().readSystemDefFromFunctionDefs(SYSTEM, FindTestInput.class);

        /* generate testcases */
        genDef.addGenerator(FindTestInput.FUNCTION_NAME, new TupleGenerator(2));
        SystemTestDef testDef = Tcases.getTests(systemDef, genDef, baseDef, options);
        assertThat(testDef.getName(), equalTo(SYSTEM));

        List<FunctionTestDef> testDefsList = IteratorUtils.toList(testDef.getFunctionTestDefs());
        assertThat(toString(testDef), testDefsList.size(), equalTo(1));
        FunctionTestDef fun1TestDef = testDefsList.get(0);
        assertThat(fun1TestDef.getName(), equalTo(FindTestInput.FUNCTION_NAME));

        List<TestCase> testCaseList = IteratorUtils.toList(fun1TestDef.getTestCases());
        // check total number
        assertThat(testCaseList.size(), equalTo(22));
        // check failure number
        assertThat(testCaseList.stream().filter(testCase -> testCase.getType() == TestCase.Type.FAILURE).count(), equalTo(5L));
        // Check id
        for (int i = 0; i < testCaseList.size(); i++) {
            assertThat(testCaseList.get(i).getId(), equalTo(i));
        }
    }

    private static String toString(SystemInputDef def) {
        return def.toString() + IteratorUtils.toList(def.getFunctionInputDefs());
    }

    private static String toString(SystemTestDef def) {
        return def.toString() + IteratorUtils.toList(def.getFunctionTestDefs());
    }

}