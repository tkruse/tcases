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

import org.beanfiller.annotation.annotations.DefaultValue;
import org.beanfiller.annotation.annotations.FunctionDef;
import org.beanfiller.annotation.annotations.SystemDef;
import org.beanfiller.annotation.annotations.Value;
import org.beanfiller.annotation.annotations.Var;
import org.beanfiller.annotation.annotations.VarSet;
import org.beanfiller.annotation.creator.AbstractTestInput;

import static org.cornutum.tcases.TestCase.Type.FAILURE;

/**
 * Usage: family pattern file
 * <p>
 * Locates one or more instances of a given pattern in a text file.
 * <p>
 * All lines in the file that contain the pattern are written to standard output.
 * A line containing the pattern is written only once,
 * regardless of the number of times the pattern occurs in it.
 * <p>
 * The pattern is any sequence of characters whose length does not exceed
 * the maximum length of a line in the file.
 * To include a blank in the pattern, the entire pattern must be enclosed in quotes (").
 * To include a quotation mark in the pattern, two quotes in a row ("") must be used.
 */
@SystemDef({FamilyTestInput.class})
@FunctionDef(value = FamilyTestInput.FUNCTION_NAME,
        having = "description:The family functions")
public class FamilyTestInput extends AbstractTestInput {

    public static final String FUNCTION_NAME = "FamilyFunctions";

    @VarSet(notNull = true)
    Family family;

    private static class Family {
        public static final String ONE_CHILD = "ONE_CHILD";
        public static final String TWO_CHILDREN = "TWO_CHILDREN";
        @VarSet
        SingleOrCouple core;

        @Var(value = {
                @Value("0"),
                @Value(value = "1", properties = ONE_CHILD, once = true, when = "family-core__init"),
                //@Value(value = "2", properties = TWO_CHILDREN, once = true)
        })
        // @SimpleIntValues({0, 1, 2})
        int children;

        @VarSet(when = {ONE_CHILD, "family-core__init"})
        Person firstChild;

        //@VarSet(when = {TWO_CHILDREN})
        //Person secondChild;


//        // generate varDefs for n Person variable (children.person_1.firstname, ...)
//        // needs reflection access to Person.class (type erasure does not allow generics)
//        // For instantiation of value, also needs type information (target class insufficient)
//        @CollectionVar(type = Person.class{
//                @Value(value = "EMPTY", having = "generate:null"),
//                @Value("ONE", having = "generate:1:Person"),
//                @Value("MAX", having = "generate:5:Person"),
//                @Value("PLUS_ONE", having = "generate:6:Person")
//                })
//        List<Person> children;


        @Override
        public String toString() {
            return "Family{" +
                    "core=" + core +
                    ", children=" + children +
                    ", firstChild=" + firstChild +
                    //", secondChild=" + secondChild +
                    '}';
        }
    }

    private static class SingleOrCouple {
        @VarSet
        Person parent;
        @VarSet
        Person partner;

        @Override
        public String toString() {
            return "{" +
                    "parent=" + parent +
                    ", partner=" + partner +
                    '}';
        }
    }

    private static class Person {
        @Var
        NameCases firstName;

        @Override
        public String toString() {
            return "{" +
                    "firstName=" + firstName +
                    //", lastName=" + lastName +
                    '}';
        }
    }

    private enum NameCases {
        VALID,
        @DefaultValue(type = FAILURE)
        INVALID_EMPTY,
        @DefaultValue(type = FAILURE)
        INVALID_BAD_CHAR,
        @DefaultValue(type = FAILURE)
        INVALID_TOO_LONG
    }

    @Override
    public String toString() {
        return "family=" + family;
    }
}
