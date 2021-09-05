package BL.Exceptions;

public class ReportGeneratorException extends Exception{
    public ReportGeneratorException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
