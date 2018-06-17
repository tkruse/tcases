package org.beanfiller.annotation.builders;

import org.cornutum.tcases.IVarDef;
import org.cornutum.tcases.VarSet;
import org.cornutum.tcases.conditions.ICondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VarSetBuilder {


    private final String name;
    private List<IVarDef> members = new ArrayList<>();
    private Map<String, String> annotations = new HashMap<>();
    private ICondition condition;

    private VarSetBuilder(String name) {
        this.name = name;
    }

    public static VarSetBuilder varSet(String name) {
        return new VarSetBuilder(name);
    }

    public VarSetBuilder addMember(IVarDef member) {
        members.add(member);
        return this;
    }


    public VarSetBuilder addAnnotations(Map<String, String> annotations) {
        this.annotations.putAll(annotations);
        return this;
    }


    public VarSetBuilder condition(ICondition condition) {
        this.condition = condition;
        return this;
    }

    public VarSet build() {
        VarSet varDef = new VarSet(name);
        for (IVarDef member : members) {
            varDef.addMember(member);
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
