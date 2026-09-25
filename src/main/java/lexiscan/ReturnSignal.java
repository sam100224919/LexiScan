package lexiscan;

public class ReturnSignal extends RuntimeException {

    private final Object value;

    public ReturnSignal(Object value) {
        super(null, null, false, false);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
