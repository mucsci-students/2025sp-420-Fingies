package org.fingies;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Method extends Attribute {

    //This name field is STRICTLY for JSON formatting.
    private String name;
    private ArrayList<Parameter> params;

    public Method (String name)
    {
        validateCharacters(name);
        this.name = name;
        params = new ArrayList<Parameter>();
    }

    public Method (String name, List<String> parameters)
    {
        this(name);
        for (String parameter : parameters)
        {
            validateCharacters(parameter);
            if (!addParameter(parameter))
            {
                throw new IllegalArgumentException("Methods cannot contain duplicate params");
            }
        }
        this.name = name;
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
     * Returns a field object specified by the field parameter
     * @param field name of the field
     * @return the field object specified by the field parameter or null if the object doesn't exist
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
     * @return true if the parameter was added, false otherwise
     */
    public boolean addParameter (String name)
    {
        validateCharacters(name);
        if (!parameterExists(name))
        {
            params.add(new Parameter(name));
            return true;
        }
        throw new IllegalArgumentException("Method " + getName() + " already has a parameter named " + name);
    }

    public boolean addParameters (List<String> newparams) {
        for (String newParam : newparams)
            {
                validateCharacters(newParam);
                if (parameterExists(newParam))
                {
                	throw new IllegalArgumentException("Method " + getName() + " already has a parameter named " + newParam);
                }
            }
        params.addAll(newparams.stream().map(x -> new Parameter(x)).toList());
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

        if (params.contains(newParam))
        	throw new IllegalArgumentException("Method " + getName() + " already has a parameter called " + newName);
        if (params.contains(oldParam))
        {
            params.set(params.indexOf(oldParam), newParam);
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
    public List<String> getParameters ()
    {
        return params.stream().map(x -> x.getName()).toList();
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
