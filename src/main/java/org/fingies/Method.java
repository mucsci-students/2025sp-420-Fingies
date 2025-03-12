package org.fingies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a method of a class in a UML Class Diagram
 * @author Kevin Dichter, Lincoln Craddock, Tristan Rush
 */
public class Method extends Attribute {

    private String name;
    private String return_type;
    private ArrayList<Parameter> params;

    public Method (String name, String return_type)
    {
        validateCharacters(name);
        validateCharacters(return_type);
        this.name = name;
        this.return_type = return_type;
        params = new ArrayList<Parameter>();
    }

    public Method (String name, String return_type, Map<String, String> parameters)
    {
        this(name, return_type);
        for (String parameter : parameters.keySet())
        {
            validateCharacters(parameter);
            validateCharacters(parameters.get(parameter));
            if (!addParameter(parameter, parameters.get(parameter)))
            {
                throw new IllegalArgumentException("Methods cannot contain duplicate params");
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void renameAttribute(String name) {
        this.name = name;
    }

    /**
     * Gets the return type of the method
     * @return the return type for the method
     */
    public String getReturnType() {
        return return_type;
    }

    /**
     * Sets the return type of the method
     * @param return_type the return type to set to.
     */
    public void setReturnType(String return_type) {
        validateCharacters(return_type);
        this.return_type = return_type;
    }

    /**
     * Returns a parameter based on the String parameter
     * @param parameter parameter to be looked for
     * @return the parameter based on the String parameter
     */
    public Parameter getParameter (String parameter)
    {
        for (Parameter param : params)
        {
            if (param.getName().equals(parameter))
                return param;
        }
        return null;
    }

    /**
     * Adds a single parameter to the list of params
     * @param name name of parameter
     * @param type data type for the parameter
     * @return true if the parameter was added, false otherwise
     */
    public boolean addParameter (String name, String type)
    {
        validateCharacters(name);
        if (!parameterExists(name))
        {
            params.add(new Parameter(name, type));
            return true;
        }
        throw new IllegalArgumentException("Method " + getName() + " already has a parameter named " + name);
    }

    /**
     * Adds parameters to the method
     * @param newparams a map of Parameter name -> Parameter data type
     * @return true if all parameters added, false otherwise.
     */
    public boolean addParameters (Map<String, String> newparams) {
        for (String newParam : newparams.keySet())
        {
            validateCharacters(newParam);
            if (parameterExists(newParam))
            {
                throw new IllegalArgumentException("Method " + getName() + " already has a parameter named " + newParam);
            }
            Parameter p = new Parameter(newParam, newparams.get(newParam));
            params.add(p);
        }
        return true;
    }

    /**
     * Removes a single parameter from the list of params
     * @param name name of parameter to be removed
     * @return true if the parameter was removed, false otherwise
     */
    public boolean removeParameter (String name)
    {
        return params.remove(getParameter(name));
    }

    /**
     * Removes a list of specifided params from the list of all params, if vall alid
     * @param junkparams list of params to be removed
     * @return true if all junkparams successfully removed, false if not
     */
    public boolean removeParameters (List<String> junkparams)
    {
        // authenticate validity of entire list
        for (String activeParam : junkparams)
        {
            if (!parameterExists(activeParam))
            {
            	throw new IllegalArgumentException("Method " + getName() + " doesn't have a parameter named " + activeParam);
            }
        }
        // if authenication passed, execute specified victims
        for (String victimParam : junkparams) {
            params.remove(getParameter(victimParam));
        }
        return true;
    }

    /**
     * Changes the name of a parameter
     * @param oldName name of parameter to be changed
     * @param newName new name to be given to old parameter
     * @return true if the parameter is renamed, false otherwise
     */
    public boolean renameParameter (String oldName, String newName)
    {
        validateCharacters(newName);
        Parameter oldParam = getParameter(oldName);
        Parameter newParam = getParameter(newName);

        if (newParam != null)
        	throw new IllegalArgumentException("Method " + getName() + " already has a parameter called " + newName);
        if (oldParam != null)
        {
            params.set(params.indexOf(oldParam), new Parameter(newName, oldParam.getType()));
            return true;
        }
        throw new IllegalArgumentException("Method " + getName() + " doesn't have a parameter called " + oldName);
    }

    /**
     * Checks to see if a parameter exists within the list of params
     * @param name name of parameter to be checked
     * @return true if the parameter exists, false otherwise
     */
    public boolean parameterExists (String name)
    {
        return params.contains(getParameter(name));
    }

    /**
     * Returns the list of params as a hashset
     * @return list of params as a hashset
     */
    public List<String> getParameterNames()
    {
        return params.stream().map(x -> x.getName()).toList();
    }

    /**
     * Returns a map of parameter details
     * @return a map of parameter name : parameter type
     */
    public Map<String, String> getParameters() {
        HashMap<String, String> paramDetails = new HashMap<>();
        for (Parameter param : params) {
            paramDetails.put(param.getName(), param.getType());
        }
        return paramDetails;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same object reference
        if (obj == null || getClass() != obj.getClass()) return false; // Different class

        Method method = (Method) obj;
        return getName().equals(method.getName()) && params.equals(method.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), params);
    }

    @Override
    public String toString()
    {
        String str = getName() + " (";
        if (!params.isEmpty())
        {
            for (Parameter parameter : params)
        {
            str += parameter.getName() + ", ";
        }
        str = str.substring(0, str.length() - 2); // trim off the extra comma
        }
        str += ")";
        return str;
    }
}
