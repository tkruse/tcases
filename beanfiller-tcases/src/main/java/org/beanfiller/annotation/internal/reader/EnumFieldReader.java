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

import org.beanfiller.annotation.annotations.Value;
import org.beanfiller.annotation.annotations.Var;
import org.cornutum.tcases.VarValueDef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.beanfiller.annotation.internal.reader.ValueUtil.createNullVarValueDef;
import static org.beanfiller.annotation.internal.reader.ValueUtil.createValidVarValueDef;
import static org.beanfiller.annotation.internal.reader.ValueUtil.createVarValueDef;

/**
 * Define values for an enum field annotated with @Var
 */
class EnumFieldReader {

    private EnumFieldReader() {
    }

    /**
     * Creates VarValue for each enum constant, with additional properties if the Var annotation provides any.
     */
    @Nonnull
    static List<VarValueDef> readVarValueDefsForEnumField(
            @Nonnull Class<? extends Enum<?>> enumClass,
            @Nullable Var varAnnotation,
            @Nullable String[] conditions) {
        List<VarValueDef> varValueDefs = new ArrayList<>();
        if (enumClass.getFields().length == 0) {
            throw new IllegalStateException("Enum '" + enumClass
                    + "' has no values.");
        }

        Set<String> excludes = new HashSet<>();
        if (varAnnotation != null) {
            excludes.addAll(Arrays.asList(varAnnotation.exclude()));
        }
        for (Field enumField : enumClass.getFields()) {
            String enumFieldName = enumField.getName();
            excludes.remove(enumFieldName);
            if (varAnnotation != null && Arrays.asList(varAnnotation.exclude()).contains(enumFieldName)) {
                continue;
            }
            VarValueDef value = getVarValueDefForEnum(enumClass, varAnnotation, enumFieldName, conditions);

            varValueDefs.add(value);
        }
        if (varAnnotation != null && varAnnotation.nullable()) {
            varValueDefs.add(getVarValueDefForEnum(enumClass, varAnnotation, null, conditions));
        }
        if (!excludes.isEmpty()) {
            throw new IllegalStateException("Unknown excluded values " + excludes);
        }
        return varValueDefs;
    }

    @Nonnull
    private static VarValueDef getVarValueDefForEnum(
            @Nonnull Class<? extends Enum<?>> enumClass,
            @Nullable Var varAnnotation,
            @Nullable String enumFieldName,
            @Nullable String[] conditions) {
        VarValueDef value = null;
        if (varAnnotation != null && varAnnotation.value().length > 0) {
            Set<String> values = new HashSet<>();
            for (Value varValue : varAnnotation.value()) {
                try {
                    enumClass.getField(varValue.value());
                } catch (NoSuchFieldException e) {
                    if (!varValue.isNull()) {
                        throw new IllegalStateException("@Value value '" + varValue.value()
                                + "' not a known key in Enum " + enumClass.getName() + " and not isNull", e);
                    }
                }

                if (enumFieldName == null) {
                    value = createVarValueDef("NA", varValue, conditions, true);
                } else if (enumFieldName.equals(varValue.value())) {
                    value = createVarValueDef(enumFieldName, varValue, conditions, varValue.isNull());
                    if (!values.add(varValue.value())) {
                        throw new IllegalStateException("@Value value '" + varValue.value() + "' duplicate");
                    }
                }
            }
        }
        if (value == null) {
            if (enumFieldName == null) {
                value = createNullVarValueDef(conditions);
            } else {
                value = createValidVarValueDef(enumFieldName, conditions);
            }
        }
        return value;
    }

}
