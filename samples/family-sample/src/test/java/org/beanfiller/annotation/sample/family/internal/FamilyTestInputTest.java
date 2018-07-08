package org.beanfiller.annotation.sample.family.internal;

import org.apache.commons.collections4.IteratorUtils;
import org.beanfiller.annotation.creator.AbstractTestInput;
import org.beanfiller.annotation.creator.FunctionTestsCreator;
import org.beanfiller.annotation.reader.SystemDefReader;
import org.beanfiller.annotation.sample.family.FamilyTestInput;
import org.cornutum.tcases.FunctionInputDef;
import org.cornutum.tcases.FunctionTestDef;
import org.cornutum.tcases.IVarDef;
import org.cornutum.tcases.PropertySet;
import org.cornutum.tcases.SystemInputDef;
import org.cornutum.tcases.SystemTestDef;
import org.cornutum.tcases.Tcases;
import org.cornutum.tcases.TestCase;
import org.cornutum.tcases.VarSet;
import org.cornutum.tcases.VarValueDef;
import org.cornutum.tcases.generator.GeneratorSet;
import org.cornutum.tcases.generator.TupleGenerator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.beanfiller.annotation.reader.VarDefReader.INITIALIZE_TESTCASE_VARNAME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Comprehensive Test using the sample.FamilyTestInput class as SystemTestDefinition/FunctionDefinition
 */
public class FamilyTestInputTest {


    public static final String SYSTEM = "familySystem";
    private GeneratorSet genDef;
    private SystemTestDef baseDef;
    private Tcases.Options options;

    @Before
    public void setUp() {
        genDef = GeneratorSet.basicGenerator();

        baseDef = null;
        options = new Tcases.Options();
    }



    @Test
    public void testInputDefFromAnnotations() {
        SystemInputDef systemDef = new SystemDefReader().readSystemDefFromFunctionDefs(SYSTEM, FamilyTestInput.class);
        assertNotNull(systemDef);
        assertThat(systemDef.getName(), equalTo(SYSTEM));
        List<FunctionInputDef> functionInputDefs = IteratorUtils.toList(systemDef.getFunctionInputDefs());
        assertThat(toString(systemDef), functionInputDefs.size(), equalTo(1));

        FunctionInputDef fun1Def = functionInputDefs.get(0);
        assertThat(fun1Def.getName(), equalTo(FamilyTestInput.FUNCTION_NAME));

        List<IVarDef> varDefs = IteratorUtils.toList(fun1Def.getVarDefs());
        assertThat(varDefs.size(), equalTo(1));

        assertNotNull(fun1Def.findVarPath("family"));
        // core
        IVarDef coreDef = fun1Def.findVarPath("family.core");
        assertNotNull(coreDef);
        IVarDef coreInitDef = coreDef.find(new String[]{INITIALIZE_TESTCASE_VARNAME});
        assertNotNull(coreInitDef);
        assertTrue(coreInitDef.getValues().next().getProperties().contains("family-core" + INITIALIZE_TESTCASE_VARNAME));

        // core.parent
        IVarDef parentDef = fun1Def.findVarPath("family.core.parent");
        assertNotNull(parentDef);
        IVarDef parentInitDef = parentDef.find(new String[]{INITIALIZE_TESTCASE_VARNAME});
        assertNotNull(parentInitDef);
        PropertySet parentTrueValProperties = parentInitDef.getValues().next().getProperties();
        assertTrue(parentTrueValProperties.contains("family-core-parent" + INITIALIZE_TESTCASE_VARNAME));

        IVarDef parentFirstnameDef = fun1Def.findVarPath("family.core.parent.firstName");
        assertNotNull(parentFirstnameDef);
        assertFalse(parentFirstnameDef.getCondition().satisfied(new PropertySet()));
        assertFalse(parentFirstnameDef.getCondition().satisfied(new PropertySet(
                "family-core-parent" + INITIALIZE_TESTCASE_VARNAME)));
        assertTrue(parentFirstnameDef.getCondition().satisfied(new PropertySet(
                "family-core-parent" + INITIALIZE_TESTCASE_VARNAME,
                "family-core" + INITIALIZE_TESTCASE_VARNAME)));

        // firstChild
        IVarDef firstChildDef = fun1Def.findVarPath("family.firstChild");
        assertNotNull(firstChildDef);
        assertFalse(firstChildDef.getCondition().satisfied(new PropertySet()));
        assertFalse(firstChildDef.getCondition().satisfied(new PropertySet("ONE_CHILD")));
        assertFalse(firstChildDef.getCondition().satisfied(new PropertySet(
                "ONE_CHILD",
                "family-core-parent" + INITIALIZE_TESTCASE_VARNAME)));

        IVarDef firstChildInitDef = firstChildDef.find(new String[]{INITIALIZE_TESTCASE_VARNAME});
        assertNotNull(firstChildInitDef);
        assertTrue(firstChildInitDef.getCondition().satisfied(new PropertySet()));
        VarValueDef firstChildInitTrueVal = firstChildInitDef.getValues().next();
        PropertySet firstChildTrueValProperties = firstChildInitTrueVal.getProperties();
        assertTrue(firstChildTrueValProperties.contains("family-firstChild" + INITIALIZE_TESTCASE_VARNAME));
        assertNull(firstChildInitTrueVal.getCondition());

    }

    @Test
    public void testTestDefFromAnnotations() {
        SystemInputDef systemDef = new SystemDefReader().readSystemDefFromFunctionDefs(SYSTEM, FamilyTestInput.class);

        /* generate testcases */
        SystemTestDef testDef = Tcases.getTests(systemDef, genDef, baseDef, options);
        assertThat(testDef.getName(), equalTo(SYSTEM));

        List<FunctionTestDef> testDefsList = IteratorUtils.toList(testDef.getFunctionTestDefs());
        assertThat(toString(testDef), testDefsList.size(), equalTo(1));
        FunctionTestDef fun1TestDef = testDefsList.get(0);
        assertThat(fun1TestDef.getName(), equalTo(FamilyTestInput.FUNCTION_NAME));

        List<TestCase> testCaseList = IteratorUtils.toList(fun1TestDef.getTestCases());
        // check total number
        assertThat(testCaseList.size(), equalTo(11));
        // check failure number
        assertThat(testCaseList.stream().filter(testCase -> testCase.getType() == TestCase.Type.FAILURE).count(), equalTo(0L));
        // Check id
        for (int i = 0; i < testCaseList.size(); i++) {
            System.out.println(testCaseList.get(i));
            assertThat(testCaseList.get(i).getId(), equalTo(i));
        }
    }


    @Test
    public void testInstanceCreation() {
        SystemInputDef systemDef = new SystemDefReader().readSystemDefFromFunctionDefs(SYSTEM, FamilyTestInput.class);

        /* generate testcases */

        SystemTestDef testDef = Tcases.getTests(systemDef, genDef, baseDef, options);
        List<FunctionTestDef> testDefsList = IteratorUtils.toList(testDef.getFunctionTestDefs());
        FunctionTestDef fun1TestDef = testDefsList.get(0);
        List<TestCase> testCaseList = IteratorUtils.toList(fun1TestDef.getTestCases());

        /* generate test instances */
        List<FamilyTestInput> familyList = new FunctionTestsCreator<>(FamilyTestInput.class)
                .createDefs();

        assertThat(familyList.size(), equalTo(testCaseList.size()));
        // check failure number
        List<FamilyTestInput> failures = familyList.stream().filter(AbstractTestInput::isFailure).collect(Collectors.toList());
        assertEquals(0, failures.size());
        failures.forEach(FamilyTestInput -> {
            assertTrue(FamilyTestInput.having().getVarBindingAnnotationKeys().hasNext());
        });

        // Check id
        for (int i = 0; i < familyList.size(); i++) {
            assertThat(familyList.get(i).getTestCaseId(), equalTo(i));
            System.out.println(familyList.get(i));
        }
    }


    @Ignore
    @Test
    public void testTestDefFromAnnotations2Tupel() {
        SystemInputDef systemDef = new SystemDefReader().readSystemDefFromFunctionDefs(SYSTEM, FamilyTestInput.class);

        /* generate testcases */
        genDef.addGenerator(FamilyTestInput.FUNCTION_NAME, new TupleGenerator(2));
        SystemTestDef testDef = Tcases.getTests(systemDef, genDef, baseDef, options);
        assertThat(testDef.getName(), equalTo(SYSTEM));

        List<FunctionTestDef> testDefsList = IteratorUtils.toList(testDef.getFunctionTestDefs());
        assertThat(toString(testDef), testDefsList.size(), equalTo(1));
        FunctionTestDef fun1TestDef = testDefsList.get(0);
        assertThat(fun1TestDef.getName(), equalTo(FamilyTestInput.FUNCTION_NAME));

        List<TestCase> testCaseList = IteratorUtils.toList(fun1TestDef.getTestCases());
        // check total number
        assertThat(testCaseList.size(), equalTo(19));
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