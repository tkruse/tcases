package org.beanfiller.annotation.builders;

import org.cornutum.tcases.VarDef;
import org.cornutum.tcases.VarValueDef;
import org.cornutum.tcases.conditions.ICondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VarDefBuilder {


    private final String name;
    private List<VarValueDef> varValueDefs = new ArrayList<>();
    private Map<String, String> annotations = new HashMap<>();
    private ICondition condition;

    private VarDefBuilder(String name) {
        this.name = name;
    }

    public static VarDefBuilder varDef(String name) {
        return new VarDefBuilder(name);
    }

    public VarDefBuilder addValue(VarValueDef varValueDef) {
        varValueDefs.add(varValueDef);
        return this;
    }

    public VarDefBuilder addAnnotations(Map<String, String> annotations) {
        this.annotations.putAll(annotations);
        return this;
    }


    public VarDefBuilder condition(ICondition condition) {
        this.condition = condition;
        return this;
    }

    public VarDef build() {
        VarDef varDef = new VarDef(name);
        for (VarValueDef varValueDef : varValueDefs) {
            varDef.addValue(varValueDef);
        }
        for (Map.Entry<String, String> a : annotations.entrySet()) {
            varDef.setAnnotation(a.getKey(), a.getValue());
        }
        if (condition != null) {
            varDef.setCondition(condition);
        }
        return varDef;
    }
}
