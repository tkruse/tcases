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

package org.beanfiller.annotation.generator;

import org.cornutum.tcases.generator.TupleCombiner;
import org.cornutum.tcases.generator.TupleGenerator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;


public class TupleGeneratorBuilderTest {

    @Test
    public void build() {
        TupleGeneratorBuilder builder = new TupleGeneratorBuilder();
        TupleGenerator generator = builder.build();
        assertThat(generator.getCombiners()).isEmpty();
        assertThat(generator.getRandomSeed()).isNull();

        builder.randomSeed(123L);
        generator = builder.build();
        assertThat(generator.getCombiners()).isEmpty();
        assertThat(generator.getRandomSeed()).isEqualTo(123L);

        assertThatThrownBy(() -> new TupleGeneratorBuilder().combiner(null)).isInstanceOf(NullPointerException.class);

        TupleCombiner combinerMock = mock(TupleCombiner.class);
        builder = new TupleGeneratorBuilder().combiner(combinerMock);
        generator = builder.build();
        assertThat(generator.getCombiners()).containsOnly(combinerMock);
    }
}