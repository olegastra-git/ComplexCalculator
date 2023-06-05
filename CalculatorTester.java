import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CalculatorTester {
    public static void main(String[] args) {
        ComplexNumber a = new ComplexNumber(1, 2);
        ComplexNumber b = new ComplexNumber(3, 4);
        ComplexCalculator calculator = new ComplexCalculator();
    
        ComplexNumber sum = calculator.add(a, b);
        LOGGER.info("{} + {} = {}", a, b, sum);
    
        ComplexNumber difference = calculator.subtract(a, b);
        LOGGER.info("{} - {} = {}", a, b, difference);
    
        ComplexNumber product = calculator.multiply(a, b);
        LOGGER.info("{} * {} = {}", a, b, product);
    
        ComplexNumber quotient = calculator.divide(a, b);
        LOGGER.info("{} / {} = {}", a, b, quotient);
    }