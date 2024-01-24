package exception;

public class DatabaseConnectionException extends Exception{
    public DatabaseConnectionException(String msg){
        super(msg);
    }
}
