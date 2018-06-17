package org.beanfiller.annotation.creator;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.beanfiller.annotation.annotations.FunctionDef;
import org.beanfiller.annotation.annotations.Var;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionTestsCreatorTest {

    @FunctionDef("functionFooName")
    private static class Sample1 {

        @Var
        private Boolean fooVar;

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
    }


    @Test
    public void createDefs() {
        List<Sample1> funDefs = new FunctionTestsCreator<>(Sample1.class).createDefs();
        assertThat(funDefs).hasSize(3);
        // TODO: More asserts
    }
}