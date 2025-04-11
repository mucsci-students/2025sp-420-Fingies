package org.fingies.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a method of a class in a UML Class Diagram
 */
public class Method extends Attribute {

    private String name;
    private String returnType;
    private List<Parameter> params;

    public Method(String name, String returnType) {
        validateCharacters(name);
        validateCharacters(returnType);
        this.name = name;
        this.returnType = returnType;
        this.params = new ArrayList<>();
    }

    public Method(String name, String returnType, List<String> parameterNames, List<String> parameterTypes) {
        this(name, returnType);
        if (parameterNames.size() != parameterTypes.size()) {
            throw new IllegalArgumentException("Every parameter must have one type");
        }
        for (int i = 0; i < parameterNames.size(); i++) {
            addParameter(parameterNames.get(i), parameterTypes.get(i));
        }
    }
    
    /**
     * Copy ctor
     * @param method The method to copy from.
     */
    public Method(Method method)
    {
    	this(method.name, method.returnType);
        for (int i = 0; i < method.params.size(); i++) {
            addParameter(method.params.get(i).getName(), method.params.get(i).getType());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void renameAttribute(String name) {
        validateCharacters(name);
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        validateCharacters(returnType);
        this.returnType = returnType;
    }

    public Parameter getParameter(String name) {
        return params.stream()
                .filter(param -> param.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean addParameter(String name, String type) {
        validateCharacters(name);
        validateCharacters(type);
        if (parameterExists(name)) {
            throw new IllegalArgumentException("Method " + getName() + " already has a parameter named " + name);
        }
        params.add(new Parameter(name, type));
        return true;
    }

    public boolean addParameters(List<String> parameterNames, List<String> parameterTypes) {
        if (parameterNames.size() != parameterTypes.size()) {
            throw new IllegalArgumentException("Every parameter must have one type");
        }
        for (int i = 0; i < parameterNames.size(); i++) {
            addParameter(parameterNames.get(i), parameterTypes.get(i));
        }
        return true;
    }

    public boolean removeParameter(String name) {
        return params.removeIf(param -> param.getName().equals(name));
    }

    public boolean removeParameters(List<String> parameterNames) {
        for (String name : parameterNames) {
            if (!parameterExists(name)) {
                throw new IllegalArgumentException("Method " + getName() + " doesn't have a parameter named " + name);
            }
        }
        params.removeIf(param -> parameterNames.contains(param.getName()));
        return true;
    }

    public boolean renameParameter(String oldName, String newName) {
        validateCharacters(newName);
        if (parameterExists(newName)) {
            throw new IllegalArgumentException("Method " + getName() + " already has a parameter called " + newName);
        }
        Parameter param = getParameter(oldName);
        if (param != null) {
            params.set(params.indexOf(param), new Parameter(newName, param.getType()));
            return true;
        }
        throw new IllegalArgumentException("Method " + getName() + " doesn't have a parameter called " + oldName);
    }

    public boolean parameterExists(String name) {
        return getParameter(name) != null;
    }

    public List<String> getParameterNames() {
        return params.stream().map(Parameter::getName).toList();
    }

    public List<String> getParameterTypes() {
        return params.stream().map(Parameter::getType).toList();
    }

    public List<Parameter> getParameters() {
        return params;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Method method = (Method) obj;
        return name.equals(method.name) && returnType.equals(method.returnType) && params.equals(method.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, params);
    }

    public String toTypes()
    {
        String paramString = params.stream()
                .map(param -> param.getType())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        return name + " (" + paramString + ")";
    }

    @Override
    public String toString() {
        String paramString = params.stream()
                .map(param -> param.getType() + " " + param.getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        return returnType + " " + name + " (" + paramString + ")";
    }
}
