package org.beanfiller.annotation.builders;

import org.cornutum.tcases.VarValueDef;
import org.cornutum.tcases.conditions.ICondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VarValueDefBuilder {

    private final VarValueDef.Type type;
    private final String valueAsString;

    private Map<String, String> annotations = new HashMap<>();
    private ICondition condition;
    private List<String> properties = new ArrayList<>();

    public VarValueDefBuilder(String valueAsString, VarValueDef.Type type) {
        this.valueAsString = valueAsString;
        this.type = type;
    }


    public VarValueDefBuilder addAnnotations(Map<String, String> annotations) {
        this.annotations.putAll(annotations);
        return this;
    }


    public VarValueDefBuilder condition(ICondition condition) {
        this.condition = condition;
        return this;
    }

    public VarValueDefBuilder addProperties(String[] properties) {
        this.properties.addAll(Arrays.asList(properties));
        return this;
    }

    public VarValueDef build() {
        VarValueDef varDef = new VarValueDef(valueAsString, type);
        varDef.addProperties(properties);
        for (Map.Entry<String, String> a : annotations.entrySet()) {
            varDef.setAnnotation(a.getKey(), a.getValue());
        }
        if (condition != null) {
            varDef.setCondition(condition);
        }
        return varDef;
    }
}
