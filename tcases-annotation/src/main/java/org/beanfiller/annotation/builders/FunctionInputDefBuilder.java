package org.beanfiller.annotation.builders;

import org.cornutum.tcases.FunctionInputDef;
import org.cornutum.tcases.IVarDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionInputDefBuilder {

    private final String name;
    private Map<String, String> annotations = new HashMap<>();
    private List<IVarDef> varDefs = new ArrayList<>();

    private FunctionInputDefBuilder(String name) {
        this.name = name;
    }

    public static FunctionInputDefBuilder function(String name) {
        return new FunctionInputDefBuilder(name);
    }

    public FunctionInputDefBuilder addAnnotation(String key, String value) {
        this.annotations.put(key, value);
        return this;
    }

    public FunctionInputDefBuilder addAnnotations(Map<String, String> annotations) {
        this.annotations.putAll(annotations);
        return this;
    }

    public FunctionInputDefBuilder addVarDef(IVarDef varDef) {
        this.varDefs.add(varDef);
        return this;
    }

    public FunctionInputDef build() {
        FunctionInputDef functionInputDef = new FunctionInputDef(name);
        for (Map.Entry<String, String> annotation : annotations.entrySet()) {
            functionInputDef.setAnnotation(annotation.getKey(), annotation.getValue());
        }
        for(IVarDef varDef : varDefs) {
            functionInputDef.addVarDef(varDef);
        }
        return functionInputDef;
    }
}
