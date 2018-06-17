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

package org.beanfiller.annotation.internal.reader;

import org.apache.commons.collections4.IteratorUtils;
import org.beanfiller.annotation.annotations.Value;
import org.beanfiller.annotation.annotations.Var;
import org.cornutum.tcases.PropertySet;
import org.cornutum.tcases.TestCase;
import org.cornutum.tcases.VarValueDef;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.beanfiller.annotation.internal.reader.VarValueDefReader.readVarValueDefs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class VarValueDefReaderTest {

    @Test
    public void testReadVarValueDefsString() throws Exception {
        Class<StringSamples> fieldSamplesClass = StringSamples.class;

        try {
            getVarValueDefs("invalidNoVar", fieldSamplesClass, null);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        try {
            getVarValueDefs("invalidNoValue", fieldSamplesClass, null);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        assertThat(getVarValueDefs("aStringWithVar1Value", fieldSamplesClass, null),
                hasSize(1));

        List<VarValueDef> aBooleanOnceFailure = getVarValueDefs("aStringOnceFailure", fieldSamplesClass, null);
        assertThat(aBooleanOnceFailure, hasSize(1));
        assertThat(aBooleanOnceFailure.get(0).getType(), equalTo(VarValueDef.Type.FAILURE));

        try {
            getVarValueDefs("invalidDuplicate", fieldSamplesClass, null);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        List<VarValueDef> aStringWithVar2Value
                = getVarValueDefs("aStringWithVar2Value", fieldSamplesClass, null);
        assertThat(aStringWithVar2Value, hasSize(2));
        VarValueDef fooValue = aStringWithVar2Value.get(0);
        VarValueDef barValue = aStringWithVar2Value.get(1);

        assertThat(fooValue.getType(), equalTo(VarValueDef.Type.ONCE));
        assertThat(barValue.getType(), equalTo(VarValueDef.Type.FAILURE));

        assertTrue(fooValue.getProperties().contains("fooProp"));
        assertFalse(fooValue.getProperties().contains("barProp"));
        assertFalse(barValue.getProperties().contains("fooProp"));
        assertTrue(barValue.getProperties().contains("barProp"));

        List<String> fooAnnotations = IteratorUtils.toList(fooValue.getAnnotations());
        assertThat(fooAnnotations, equalTo(Arrays.asList("fooHasKey")));
        assertThat(fooValue.getAnnotation("fooHasKey"), equalTo("fooHasValue"));

        List<String> barAnnotations = IteratorUtils.toList(barValue.getAnnotations());
        assertThat(barAnnotations, equalTo(Arrays.asList("barHasKey")));
        assertThat(barValue.getAnnotation("barHasKey"), equalTo("barHasValue"));

        assertTrue(fooValue.getCondition().satisfied(new PropertySet("fooWhen")));
        assertFalse(fooValue.getCondition().satisfied(new PropertySet()));
        assertFalse(barValue.getCondition().satisfied(new PropertySet("fooWhen")));
        assertTrue(barValue.getCondition().satisfied(new PropertySet()));
    }

    private List<VarValueDef> getVarValueDefs(String name, Class<?> fieldSamplesClass1, String[] conditions) throws NoSuchFieldException {
        return readVarValueDefs(FieldWrapper.of(fieldSamplesClass1.getDeclaredField(name)), conditions);
    }

    @Test
    public void testReadVarValueDefsBoolean() throws Exception {
        Class<BooleanFieldSamples> fieldSamplesClass = BooleanFieldSamples.class;
        assertThat(getVarValueDefs("aBoolean", fieldSamplesClass, null),
                hasSize(3));
        assertThat(getVarValueDefs("aPrimitiveBoolean", fieldSamplesClass, null),
                hasSize(2));
        assertThat(getVarValueDefs("aBooleanWithVar", fieldSamplesClass, null),
                hasSize(3));
        assertThat(getVarValueDefs("aBooleanWithVar1Value", fieldSamplesClass, null),
                hasSize(3));
        assertThat(getVarValueDefs("aBooleanWithVar1ValueExclude0", fieldSamplesClass, null),
                hasSize(2));

        List<VarValueDef> aBooleanOnceFailure = getVarValueDefs("aBooleanOnceFailure", fieldSamplesClass, null);
        assertThat(aBooleanOnceFailure, hasSize(3));
        assertThat(aBooleanOnceFailure.get(0).getType(), equalTo(VarValueDef.Type.FAILURE));

        try {
            getVarValueDefs("invalidDuplicate", fieldSamplesClass, null);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        try {
            getVarValueDefs("invalidUnknown", fieldSamplesClass, null);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        List<VarValueDef> aBooleanWithVar2Value
                = getVarValueDefs("aBooleanWithVar2Value", fieldSamplesClass, null);
        assertThat(aBooleanWithVar2Value, hasSize(3));
        VarValueDef trueValue = aBooleanWithVar2Value.get(0);
        VarValueDef falseValue = aBooleanWithVar2Value.get(1);

        assertThat(trueValue.getType(), equalTo(VarValueDef.Type.ONCE));
        assertThat(falseValue.getType(), equalTo(VarValueDef.Type.FAILURE));

        assertTrue(trueValue.getProperties().contains("trueProp"));
        assertFalse(trueValue.getProperties().contains("falseProp"));
        assertFalse(falseValue.getProperties().contains("trueProp"));
        assertTrue(falseValue.getProperties().contains("falseProp"));

        List<String> trueAnnotations = IteratorUtils.toList(trueValue.getAnnotations());
        assertThat(trueAnnotations, equalTo(Arrays.asList("trueHasKey")));
        assertThat(trueValue.getAnnotation("trueHasKey"), equalTo("trueHasValue"));

        List<String> falseAnnotations = IteratorUtils.toList(falseValue.getAnnotations());
        assertThat(falseAnnotations, equalTo(Arrays.asList("falseHasKey")));
        assertThat(falseValue.getAnnotation("falseHasKey"), equalTo("falseHasValue"));

        assertTrue(trueValue.getCondition().satisfied(new PropertySet("trueWhen")));
        assertFalse(trueValue.getCondition().satisfied(new PropertySet()));
        assertFalse(falseValue.getCondition().satisfied(new PropertySet("trueWhen")));
        assertTrue(falseValue.getCondition().satisfied(new PropertySet()));
    }

    @Test
    public void testReadVarValueDefsEnum() throws Exception {
        Class<EnumFieldSamples> fieldSamplesClass = EnumFieldSamples.class;

        try {
            getVarValueDefs("enum0Field", fieldSamplesClass, null);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        assertThat(getVarValueDefs("enum1Field", fieldSamplesClass, null),
                hasSize(1));
        assertThat(getVarValueDefs("enum3Field", fieldSamplesClass, null),
                hasSize(3));
        assertThat(getVarValueDefs("enum3FieldVar", fieldSamplesClass, null),
                hasSize(3));
        assertThat(getVarValueDefs("enum3FieldVarWithNull", fieldSamplesClass, null),
                hasSize(4));

        List<VarValueDef> aBooleanOnceFailure = getVarValueDefs("aBooleanOnceFailure", fieldSamplesClass, null);
        assertThat(aBooleanOnceFailure, hasSize(1));
        assertThat(aBooleanOnceFailure.get(0).getType(), equalTo(VarValueDef.Type.FAILURE));


        try {
            getVarValueDefs("invalidDuplicate", fieldSamplesClass, null);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        try {
            getVarValueDefs("invalidUnknown", fieldSamplesClass, null);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
            assert e != null;
        }

        List<VarValueDef> aEnumWithVar2Value
                = getVarValueDefs("enum3FieldVarFull", fieldSamplesClass, null);
        assertThat(aEnumWithVar2Value, hasSize(3));
        VarValueDef a1Value = aEnumWithVar2Value.get(0);
        VarValueDef a2Value = aEnumWithVar2Value.get(1);
        VarValueDef a3Value = aEnumWithVar2Value.get(2);

        assertThat(a1Value.getType(), equalTo(VarValueDef.Type.ONCE));
        assertThat(a2Value.getType(), equalTo(VarValueDef.Type.FAILURE));

        assertTrue(a1Value.getProperties().contains("a1Prop"));
        assertFalse(a1Value.getProperties().contains("a2Prop"));
        assertFalse(a2Value.getProperties().contains("a1Prop"));
        assertTrue(a2Value.getProperties().contains("a2Prop"));
        assertFalse(a3Value.getProperties().contains("a2Prop"));
        assertFalse(a3Value.getProperties().contains("a1Prop"));

        List<String> a1Annotations = IteratorUtils.toList(a1Value.getAnnotations());
        assertThat(a1Annotations, equalTo(Arrays.asList("a1HasKey")));
        assertThat(a1Value.getAnnotation("a1HasKey"), equalTo("a1HasValue"));

        List<String> a2Annotations = IteratorUtils.toList(a2Value.getAnnotations());
        assertThat(a2Annotations, equalTo(Arrays.asList("a2HasKey")));
        assertThat(a2Value.getAnnotation("a2HasKey"), equalTo("a2HasValue"));

        assertTrue(a1Value.getCondition().satisfied(new PropertySet("aWhen")));
        assertFalse(a1Value.getCondition().satisfied(new PropertySet()));
        assertFalse(a2Value.getCondition().satisfied(new PropertySet("aWhen")));
        assertTrue(a2Value.getCondition().satisfied(new PropertySet()));
        assertTrue(a3Value.getCondition().satisfied(new PropertySet()));
        assertTrue(a3Value.getCondition().satisfied(new PropertySet("aWhen")));
    }

    private static class StringSamples {
        public String invalidNoVar;

        @Var
        public String invalidNoValue;

        @Var(@Value("foo"))
        public String aStringWithVar1Value;

        @Var({
                @Value(value = "foo",
                        properties = {"fooProp"},
                        once = true,
                        when = "fooWhen",
                        having = "fooHasKey:fooHasValue"),
                @Value(value = "bar",
                        properties = {"barProp"},
                        whenNot = "fooWhen",
                        type = TestCase.Type.FAILURE,
                        having = "barHasKey:barHasValue")
        })
        public String aStringWithVar2Value;

        @Var({@Value(value = "fail", once = true, type = TestCase.Type.FAILURE)})
        public String aStringOnceFailure;

        @Var({@Value("fail"), @Value("fail")})
        public String invalidDuplicate;
    }

    private static class BooleanFieldSamples {

        public boolean aPrimitiveBoolean;

        public Boolean aBoolean;

        @Var
        public Boolean aBooleanWithVar;

        @Var(@Value("true"))
        public Boolean aBooleanWithVar1Value;

        @Var(exclude = "NA")
        public Boolean aBooleanWithVar1ValueExclude0;

        @Var({
                @Value(value = "true",
                        properties = {"trueProp"},
                        once = true,
                        when = "trueWhen",
                        having = "trueHasKey:trueHasValue"),
                @Value(value = "false",
                        properties = {"falseProp"},
                        whenNot = "trueWhen",
                        type = TestCase.Type.FAILURE,
                        having = "falseHasKey:falseHasValue")
        })
        public Boolean aBooleanWithVar2Value;

        @Var({@Value(value = "true", once = true, type = TestCase.Type.FAILURE)})
        public Boolean aBooleanOnceFailure;

        @Var({@Value("true"), @Value("true")})
        public Boolean invalidDuplicate;

        @Var({@Value("unknown")})
        public Boolean invalidUnknown;
    }

    private static class EnumFieldSamples {

        public Enum0Sample enum0Field;
        public Enum1Sample enum1Field;
        public Enum3Sample enum3Field;

        @Var
        public Enum3Sample enum3FieldVar;

        @Var(@Value("NA"))
        public Enum3Sample enum3FieldVarWithNull;

        @Var({
                @Value(value = "A1",
                        properties = {"a1Prop"},
                        once = true,
                        when = "aWhen",
                        having = "a1HasKey:a1HasValue"),
                @Value(value = "A2",
                        properties = {"a2Prop"},
                        type = TestCase.Type.FAILURE,
                        whenNot = "aWhen",
                        having = "a2HasKey:a2HasValue")}
        )
        public Enum3Sample enum3FieldVarFull;
        @Var({@Value(value = "A1", once = true, type = TestCase.Type.FAILURE)})
        public Enum1Sample aBooleanOnceFailure;
        @Var({@Value("A1"), @Value("A1")})
        public Enum3Sample invalidDuplicate;
        @Var({@Value("A4")})
        public Enum3Sample invalidUnknown;

        public enum Enum0Sample {
        }

        public enum Enum1Sample {
            A1
        }

        public enum Enum3Sample {
            A1, A2, A3
        }
    }
}