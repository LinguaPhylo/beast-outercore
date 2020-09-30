package outercore.parameter;

import java.io.PrintStream;

import beast.core.Description;
import beast.core.Input;

/**
 * @author Alexei Drummond (ported by Fabio K. Mendes)
 */

@Description("A real-valued parameter represents a value (or array of values if the dimension is larger than one) " +
        "in the state space that can be changed by operators.")
public class KeyRealParameter extends KeyParameter.KeyBase<Double> {
    final public Input<Double> lowerValueInput = new Input<>("lower", "lower value for this parameter (default -infinity)");
    final public Input<Double> upperValueInput = new Input<>("upper", "upper value for this parameter (default +infinity)");

    public KeyRealParameter() {
    }

    public KeyRealParameter(final Double[] values) {
        super(values);
    }

    /**
     * Constructor used by Input.setValue(String) *
     */
    public KeyRealParameter(final String value) {
        init(0.0, 0.0, value, 1);
    }

    @Override
    public void initAndValidate() {
        if (lowerValueInput.get() != null) {
            m_fLower = lowerValueInput.get();
        } else {
            m_fLower = Double.NEGATIVE_INFINITY;
        }
        if (upperValueInput.get() != null) {
            m_fUpper = upperValueInput.get();
        } else {
            m_fUpper = Double.POSITIVE_INFINITY;
        }
        super.initAndValidate();
    }

    @Override
    Double getMax() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    Double getMin() {
        return Double.NEGATIVE_INFINITY;
    }

    /** Valuable implementation follows **/
    @Override
    public Double getValue() {
        return values[0];
    }

    @Override
    public double getArrayValue() {
        return values[0];
    }

    @Override
    public double getArrayValue(final int index) {
        return values[index];
    }

    /**
     * Loggable implementation *
     */
    @Override
    public void log(final long sample, final PrintStream out) {
        final KeyRealParameter var = (KeyRealParameter) getCurrent();
        final int values = var.getDimension();
        for (int value = 0; value < values; value++) {
            out.print(var.getValue(value) + "\t");
        }
    }

    /**
     * StateNode methods *
     */
    @Override
    public int scale(final double scale) {
        int nScaled = 0;

        for (int i = 0; i < values.length; i++) {
            if (values[i] == 0.0)
                continue;

            values[i] *= scale;
            nScaled += 1;

            if (values[i] < m_fLower || values[i] > m_fUpper) {
                throw new IllegalArgumentException("parameter scaled out of range");
            }
        }

        return nScaled;
    }

    @Override
    void fromXML(final int dimension, final String lower, final String upper, final String[] valuesString) {
        setLower(Double.parseDouble(lower));
        setUpper(Double.parseDouble(upper));
        values = new Double[dimension];
        for (int i = 0; i < valuesString.length; i++) {
            values[i] = Double.parseDouble(valuesString[i]);
        }
    }
}
