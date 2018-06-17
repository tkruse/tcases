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

package org.beanfiller.annotation.creator;

import org.cornutum.tcases.FunctionTestDef;
import org.cornutum.tcases.SystemTestDef;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Given the output of Tcases, creates an instance of a JavaBean
 * with fields filled according to VarBindings.
 */
public class FunctionTestsCreator<T> extends AbstractTestCaseCreator<FunctionTestsCreator<T>> {

    private final Class<T> functionDefClass;

    public FunctionTestsCreator(@Nonnull Class<T> functionDefClass) {
        this.functionDefClass = functionDefClass;
    }

    @Nonnull
    public List<T> createDefs() {
        // get Testcases by defining a dummy system with one function and then reading the functionTest
        SystemTestDef systemTestDef = createSystemTestDef("UndefinedSystem");
        FunctionTestDef funDef = systemTestDef.getFunctionTestDefs().next();
        List<T> result = new ArrayList<>();
        funDef.getTestCases().forEachRemaining(testCase -> {
            OutputAnnotationContainer outputAnnotations = new OutputAnnotationContainer();
            outputAnnotations.addTestCaseAnnotations(funDef);

            // Intentionally allow overriding of values, according to Tcases specification
            outputAnnotations.addTestCaseAnnotations(systemTestDef);

            result.add(getInstanceCreator().createDef(testCase, functionDefClass, outputAnnotations));
        });
        return result;
    }

    @Nonnull
    public SystemTestDef createSystemTestDef(String systemName) {
        return new SystemTestDefCreator(systemName, functionDefClass)
                    .base(getBaseFile())
                    .generatorSet(getGeneratorSet())
                    .generatorOptions(getOptions())
                    .reader(getReader())
                    .create();
    }

    @Override
    @Nonnull
    protected FunctionTestsCreator<T> getInstance() {
        return this;
    }

}
