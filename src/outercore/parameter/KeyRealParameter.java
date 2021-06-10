package outercore.parameter;

import beast.core.Input;
import beast.core.parameter.RealParameter;

import java.util.List;

/**
 * the extension of {@link RealParameter}, where "dimNames" is same as "keys"
 * but works for column names of 2d matrix.
 * It is proposing to be deprecated after BEAST 2.6.6.
 */
public class KeyRealParameter extends RealParameter implements KeyParameter<Double> {

    public final Input<String> dimNamesInput = new Input<>("dimNames",
            "the unique dimension names for the dimensions of this parameter, " +
                    "which can also be column names when values are 2d matrix. ",
            null, Input.Validate.XOR, keysInput);

    private List<String> dimNames = null; // List.of creates unmodifiable list
    private java.util.Map<String, Integer> dimNameToIndexMap = null;


    @Override
    public void initAndValidate() {
        // call super before initDimNames
        super.initAndValidate();

        if (dimNamesInput.get() != null) {
            String[] dimNamesArr = dimNamesInput.get().split(" ");
            // Unmodifiable List
            this.dimNames = List.of(dimNamesArr);
        }

        dimNameToIndexMap = initDimNames(dimNames,this);
    }

    /**
     * @param i index
     * @return the unique key for the i'th value.
     */
    public String getKey(int i) {
        if (dimNames != null) return dimNames.get(i);
        return super.getKey(i);
    }

    /** TODO return List<String>
     * @return the array of keys (a unique string for each dimension) that parallels the parameter index.
     */
    public String[] getKeys() {
        if (dimNames != null) return dimNames.toArray(String[]::new);
        return super.getKeys();
    }

    /**
     * @param key unique key for a value
     * @return the value associated with that key, or null
     */
    public Double getValue(String key) {
        if (dimNames != null) {
            return getValue(dimNameToIndexMap.get(key));
        }
        return super.getValue(key);
    }

    public Double[] getRowValues(String key) {
        String[] keys = this.getKeys();

        if (keys.length == getRowCount()) {
            for (int row = 0; row < keys.length; row++) {
                if (keys[row].equals(key)) {
                    return getRowValues(row);
                }
            }
        }

        return null;
    }

}
