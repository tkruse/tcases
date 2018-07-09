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

package org.beanfiller.annotation.annotations;

import org.cornutum.tcases.TestCase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation single value for a variable within a @Var annotation.
 * Can be used on Fields of Type Enum, String, Boolean (Maybe more in the future).
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultValue {

    TestCase.Type type() default TestCase.Type.SUCCESS;

    /**
     * if value is used, testcase includes these properties
     */
    String[] properties() default {};

    /**
     * use value only if testcase has given properties
     */
    String[] when() default {};

    /**
     * use value only if testcase does not have given properties
     */
    String[] whenNot() default {};

    /**
     * hint to use this value only once in all vaid testcases
     */
    boolean once() default false;

    String[] having() default "";

}
