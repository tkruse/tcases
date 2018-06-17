/* Copyright 2018 The Beanfiller Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.beanfiller.annotation.reader;

import org.apache.commons.collections4.IteratorUtils;
import org.cornutum.tcases.FunctionInputDef;
import org.cornutum.tcases.IVarDef;
import org.beanfiller.annotation.annotations.FunctionDef;
import org.beanfiller.annotation.annotations.Var;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


public class AnnotatedFunctionDefDefReaderTest {

    @Test
    public void testReadFunctionInputDef() {

        AnnotatedFunctionDefReader reader = AnnotatedFunctionDefReader.withDefaultAnnotations();

        FunctionInputDef funDef = reader.readFunctionInputDef(EmptyClass.class);
        assertThat(funDef).isNotNull();
        assertThat(funDef.getName()).isEqualTo("EmptyClass");
        assertThat(funDef.getVarDefs().hasNext()).isFalse();

        funDef = reader.readFunctionInputDef(EmptyClass2.class);
        assertThat(funDef).isNotNull();
        assertThat(funDef.getName()).isEqualTo("EmptyClass2");
        assertThat(funDef.getVarDefs().hasNext()).isFalse();

        try {
            reader.readFunctionInputDef(InvalidAnnotation.class);
            fail("Excpected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        funDef = reader.readFunctionInputDef(Sample1.class);
        assertThat(funDef.getName()).isEqualTo("altName");
        assertThat(funDef.getAnnotation("foo")).isEqualTo("fooV");
        assertThat(funDef.getAnnotation("bar")).isEqualTo("barV");
        List<IVarDef> varDefs = IteratorUtils.toList(funDef.getVarDefs());
        assertThat(varDefs).hasSize(1);
        assertThat(varDefs.get(0).getName()).isEqualTo("varDef");
    }

    private static class EmptyClass {
    }

    @FunctionDef(" ")
    private static class EmptyClass2 {
    }

    private static class InvalidAnnotation {
        @Var
        public static Boolean staticField;
    }

    @FunctionDef(value = "altName", having = {
            "foo:fooV",
            "bar:barV"
    })
    private static class Sample1 {
        private static Boolean ignoreStatic;

        @Var
        private Boolean varDef;

    }

}