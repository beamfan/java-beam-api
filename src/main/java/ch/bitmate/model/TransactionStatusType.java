package ch.bitmate.model;

import com.google.gson.annotations.SerializedName;

public enum TransactionStatusType {
    @SerializedName("0") PENDING(0),
    @SerializedName("1") IN_PROGRESS(1),
    @SerializedName("2") CANCELLED(2),
    @SerializedName("3") COMPLETED(3),
    @SerializedName("4") FAILED(4),
    @SerializedName("5") REGISTERING(5);

    private final int code;

    TransactionStatusType(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}