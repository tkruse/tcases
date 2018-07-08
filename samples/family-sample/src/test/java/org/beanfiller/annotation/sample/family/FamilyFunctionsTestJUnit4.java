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

package org.beanfiller.annotation.sample.family;

import org.beanfiller.annotation.creator.FunctionTestsCreator;
import org.beanfiller.annotation.reader.VarDefReader;
import org.cornutum.tcases.IVarDef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class FamilyFunctionsTestJUnit4 {
    private final FamilyTestInput inputs;

    public FamilyFunctionsTestJUnit4(FamilyTestInput function) {
        this.inputs = function;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<FamilyTestInput> data() {
        return new FunctionTestsCreator<>(FamilyTestInput.class)
                .tupleGenerator(2)
                .createDefs() // the magic happens here
                .stream()
                // map abstract input to concrete values (here to improve test label)
                .collect(Collectors.toList());
    }

    @Test
    public void runTcasesTests() throws Exception {
        System.out.println(inputs);
    }

}
