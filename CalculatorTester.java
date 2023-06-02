import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class CalculatorTester {
    public static void main(String[] args) {
        ComplexNumber num1 = new ComplexNumber(3, 2);
        ComplexNumber num2 = new ComplexNumber(1, 4);
        ComplexCalculator calculator = new ComplexCalculator();

        
        ComplexNumber resultAdd = calculator.add(num1, num2);
        System.out.println("The result of adding " + num1 + " and " + num2 + " is: " + resultAdd);

        
        ComplexNumber resultSubtract = calculator.subtract(num1, num2);
        System.out.println("The result of subtracting " + num1 + " and " + num2 + " is: " + resultSubtract);

        
        ComplexNumber resultMultiply = calculator.multiply(num1, num2);
        System.out.println("The result of multiplying " + num1 + " and " + num2 + " is: " + resultMultiply);

        
        ComplexNumber resultDivide = calculator.divide(num1, num2);
        System.out.println("The result of dividing " + num1 + " by " + num2 + " is: " + resultDivide);
    }
}