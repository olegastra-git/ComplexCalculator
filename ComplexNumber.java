import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComplexNumber {
    private double real;
    private double imaginary;
    private static final Logger LOGGER = LogManager.getLogger(ComplexNumber.class);

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(real + other.real, imaginary + other.imaginary);
    }

    public ComplexNumber subtract(ComplexNumber other) {
        return new ComplexNumber(real - other.real, imaginary - other.imaginary);
    }

    public ComplexNumber multiply(ComplexNumber other) {
        double realPart = real * other.real - imaginary * other.imaginary;
        double imaginaryPart = real * other.imaginary + imaginary * other.real;
        return new ComplexNumber(realPart, imaginaryPart);
    }

    public ComplexNumber divide(ComplexNumber other) {
        double denominator = other.real * other.real + other.imaginary * other.imaginary;
        double realPart = (real * other.real + imaginary * other.imaginary) / denominator;
        double imaginaryPart = (imaginary * other.real - real * other.imaginary) / denominator;
        return new ComplexNumber(realPart, imaginaryPart);
    }

    @Override
    public String toString() {
        return String.format("%.2f + %.2fi", real, imaginary);
    }
}
