package Structure;

public class WrongFileFormatException extends Exception{
    @Override
    public String toString() {
        return "Cannot read data: wrong file format";
    }
}
