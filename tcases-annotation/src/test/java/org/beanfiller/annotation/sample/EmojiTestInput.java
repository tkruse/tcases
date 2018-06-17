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

package org.beanfiller.annotation.sample;

import org.beanfiller.annotation.annotations.Value;
import org.beanfiller.annotation.annotations.Var;
import org.beanfiller.annotation.creator.FunctionTestsCreator;

public class EmojiTestInput {

    @Var(value = {
            @Value("^"), @Value("°"), @Value("ಠ"),
            @Value("≖"), @Value("•"), @Value("ˇ"),
            @Value("˘"), @Value("ᴗ"), @Value("\""),
            @Value("<"), @Value("╥")
    })
    char eyeSymmetric;

    @Var(value = {
            @Value("-"), @Value("_"), @Value("‿"),
            @Value("∇"), @Value("◡"), @Value("³"),
            @Value("ᴗ"), @Value("﹏"), @Value(".")
    })
    char snoot;

    @Var(value = {
            @Value("\\/"),
            @Value("ᕙᕗ"),
            @Value("ᕦᕤ"),
            @Value("┌ʃ"),
    })
    String arms;

    private String getFace() {
        return  (arms == null ? "" : arms.substring(0,1))
                + '(' + eyeSymmetric + snoot + eyeSymmetric + ')'
                + (arms == null ? "" : arms.substring(1,2));
    }

    public static void main(String[] args) {
        int tupleSize = 2;
        long combinations = new FunctionTestsCreator<>(EmojiTestInput.class)
                .tupleGenerator(tupleSize)
                .createDefs()
                .stream()
                .map(test -> {
                    String f = test.getFace();
                    System.out.print(f + "  ");
                    return f;
                }).count();

        System.out.println("\n" + combinations + " faces generated with independent " + tupleSize + "-tuples or properties");
    }
}
