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

package org.beanfiller.annotation.sample.emoji;

import org.beanfiller.annotation.annotations.Value;
import org.beanfiller.annotation.annotations.Var;
import org.beanfiller.annotation.creator.FunctionTestsCreator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class EmojiTestInput {

    @Var(value = {
            @Value("^"),
            @Value("°"),
            @Value("ಠ"),
            @Value("≖"),
            @Value("•"),
            @Value("ˇ"),
            @Value("˘"),
            @Value("ᴗ"),
            @Value("☉"),
            @Value("╥")
    })
    char eyeSymmetric;

    @Var(value = {
            @Value("-"),
            @Value("_"),
            @Value("‿"),
            @Value("∇"),
            @Value("◡"),
            @Value("³"),
            @Value("ᴗ"),
            @Value("﹏"),
            @Value("."),
            @Value("∇"),
    })
    char snoot;

    public static void main(String[] args) {
        new FunctionTestsCreator<>(EmojiTestInput.class)
                .tupleGenerator(2)
                .createDefs()
                .stream()
                .forEach(test -> System.out.print('(' + test.eyeSymmetric + test.snoot + test.eyeSymmetric + ')'));

    }
}
