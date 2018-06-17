package org.beanfiller.annotation.creator;

import org.beanfiller.annotation.annotations.Value;
import org.beanfiller.annotation.annotations.Var;
import org.beanfiller.annotation.internal.reader.VarValueDefReaderTest;
import org.cornutum.tcases.TestCase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionBasedInstanceCreatorTest {



    @Test
    public void createDef() {
        InstanceCreator creator = ReflectionBasedInstanceCreator.withDefaultMapper();
        TestCase testCase = new TestCase(1);
        OutputAnnotationContainer outputAnnotations = new OutputAnnotationContainer();
        Sample instance = creator.createDef(testCase, Sample.class, outputAnnotations);
        assertThat(instance).isInstanceOf(Sample.class);

        // TODO: More tests
    }

    private static class Sample {
        public String invalidNoVar;

        public String invalidNoValue;

        public boolean aPrimitiveBoolean;

        public Boolean invalidUnknown;

        public ReflectionBasedInstanceCreatorTest.Sample.Enum3Sample enum3FieldVar;


        public enum Enum3Sample {
            A1, A2, A3
        }
    }
}