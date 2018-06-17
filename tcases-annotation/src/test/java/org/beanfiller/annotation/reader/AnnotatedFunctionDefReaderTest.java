package org.beanfiller.annotation.reader;

import org.beanfiller.annotation.annotations.FunctionDef;
import org.beanfiller.annotation.annotations.Var;
import org.beanfiller.annotation.creator.SystemTestDefCreator;
import org.cornutum.tcases.FunctionInputDef;
import org.cornutum.tcases.SystemTestDef;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnnotatedFunctionDefReaderTest {

    private final SystemTestDef fooSystem = new SystemTestDefCreator("fooSystem", AnnotatedFunctionDefReaderTest.Sample1.class).create();

    @FunctionDef("functionFooName")
    private static class Sample1 {

        private static Boolean ignoreStatic;

        @Var
        private Boolean fooVar;

    }

    @Test
    public void testDefaults() {
        FunctionInputDef inputDef = AnnotatedFunctionDefReader.withDefaultAnnotations()
                .readFunctionInputDef(Sample1.class);
        assertThat(inputDef.getName()).isEqualTo("functionFooName");
        assertThat(inputDef.getVarDefs()).hasSize(1);
    }

    @Test
    public void testWithReaders() {
        assertThatThrownBy(() -> AnnotatedFunctionDefReader.withReaders())
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> AnnotatedFunctionDefReader.withReaders((VarDefReader[]) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void readFunctionDefs() {
    }

    @Test
    public void readFunctionInputDef() {
    }
}