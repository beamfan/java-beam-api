package ch.bitmate.model;

import com.google.gson.annotations.SerializedName;

public class WalletStatus {
    private long available;
    private long locked;
    private long maturing;
    private long receiving;
    private long sending;

    @SerializedName("current_height")
    private long currentHeight;

    @SerializedName("current_state_hash")
    private String currentStateHash;

    @SerializedName("prev_state_hash")
    private String prevStateHash;

    public long getAvailable() {
        return available;
    }

    public long getCurrentHeight() {
        return currentHeight;
    }

    public long getLocked() {
        return locked;
    }

    public long getMaturing() {
        return maturing;
    }

    public long getReceiving() {
        return receiving;
    }

    public long getSending() {
        return sending;
    }

    public String getCurrentStateHash() {
        return currentStateHash;
    }

    public String getPrevStateHash() {
        return prevStateHash;
    }

    @Override
    public String toString() {
        return "WalletStatus{" +
                "available=" + available +
                ", locked=" + locked +
                ", maturing=" + maturing +
                ", receiving=" + receiving +
                ", sending=" + sending +
                ", currentHeight=" + currentHeight +
                ", currentStateHash='" + currentStateHash + '\'' +
                ", prevStateHash='" + prevStateHash + '\'' +
                '}';
    }
}
