import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComplexCalculator {
    private static final Logger LOGGER = LogManager.getLogger(ComplexCalculator.class);
    //private static Logger log = LogManager.getLogger(ComplexCalculator.class); 
    public ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        LOGGER.info("Adding {} and {}", a, b);
        return a.add(b);
    }

    public ComplexNumber subtract(ComplexNumber a, ComplexNumber b) {
        LOGGER.info("Subtracting {} and {}", a, b);
        return a.subtract(b);
    }

    public ComplexNumber multiply(ComplexNumber a, ComplexNumber b) {
        LOGGER.info("Multiplying {} and {}", a, b);
        return a.multiply(b);
    }

    public ComplexNumber divide(ComplexNumber a, ComplexNumber b) {
        LOGGER.info("Dividing {} by {}", a, b);
        if (b.getReal() == 0 && b.getImaginary() == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a.divide(b);
    }
}
