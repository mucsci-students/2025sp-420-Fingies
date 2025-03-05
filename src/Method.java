import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Method extends Attribute {
    private ArrayList<String> parameters;

    public Method (String name)
    {
        super(name);
        parameters = new ArrayList<String>();
    }

    public Method (String name, List<String> params)
    {
        super(name);
        parameters = new ArrayList<String>();
        for (String parameter : params)
        {
            validateCharacters(parameter);
            if (!addParameter(parameter))
            {
                throw new IllegalArgumentException("Duplicate parameters");
            }
        }
    }

    /**
     * Adds a single parameter to the list of parameters
     * @param name name of parameter
     * @return true if the parameter was added, false otherwise
     */
    public boolean addParameter (String name)
    {
        validateCharacters(name);
        if (!parameterExists(name))
        {
            parameters.add(name);
            return true;
        }
        return false;
    }

    /**
     * Removes a single parameter from the list of parameters
     * @param name name of parameter to be removed
     * @return true if the parameter was removed, false otherwise
     */
    public boolean removeParameter (String name)
    {
        return parameters.remove(name);
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
        if (parameters.contains(oldName) && !parameters.contains(newName))
        {
            parameters.set(parameters.indexOf(oldName), newName);
            return true;
        }
        return false;
    }

    /**
     * Checks to see if a parameter exists within the list of parameters
     * @param name name of parameter to be checked
     * @return true if the parameter exists, false otherwise
     */
    public boolean parameterExists (String name)
    {
        return parameters.contains(name);
    }

    /**
     * Returns the list of parameters as a hashset
     * @return list of parameters as a hashset
     */
    public ArrayList<String> getParameters ()
    {
        return parameters;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same object reference
        if (obj == null || getClass() != obj.getClass()) return false; // Different class

        Method method = (Method) obj;
        return getName().equals(method.getName()) && parameters.equals(method.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), parameters);
    }

    @Override
    public String toString()
    {
        String str = getName() + "(";
        for (String parameter : parameters)
        {
            str += parameter + ", ";
        }
        str = str.substring(0, str.length() - 2); // trim off the extra comma
        str += ")";
        return str;
    }
}
