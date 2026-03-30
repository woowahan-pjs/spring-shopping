package shopping.idempotency;

public class IdempotencyKey {

    private final String key;
    private Status status;

    public IdempotencyKey(String key) {
        this.key = key;
        this.status = Status.PENDING;
    }

    public String getKey() {
        return key;
    }

    public Status getStatus() {
        return status;
    }

    public void complete() {
        this.status = Status.COMPLETED;
    }

    public enum Status {
        PENDING, COMPLETED
    }
}
