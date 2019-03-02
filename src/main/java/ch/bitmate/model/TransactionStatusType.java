package ch.bitmate.model;

public enum TransactionStatusType {
    PENDING(0),
    IN_PROGRESS(1),
    CANCELLED(2),
    COMPLETED(3),
    FAILED(4),
    REGISTERING(5);

    private final int code;

    TransactionStatusType(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}