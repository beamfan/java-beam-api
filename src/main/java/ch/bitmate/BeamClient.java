package ch.bitmate;

import ch.bitmate.model.TransactionStatus;
import ch.bitmate.model.TransactionStatusType;
import ch.bitmate.model.WalletStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BeamClient {
    private String host;
    private int port;
    private String endpoint = "/api/wallet";

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public BeamClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String callBeamApi(String request) {
        try {
            return post(String.format("http://%s:%s%s", host, port, endpoint), request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Returns wallet status, including available groth balances
     *
     * @return {@link WalletStatus} object containing current wallet status
     */
    public WalletStatus getWalletStatus() {
        String response = callBeamApi("{\"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"wallet_status\"}");

        if (response == null){
            return null;
        }

        String result = new JsonParser().parse(response).getAsJsonObject().get("result").toString();
        WalletStatus walletStatus = gson.fromJson(result, WalletStatus.class);

        return walletStatus;
    }

    public String getUtxos() {
        return callBeamApi("{\"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"get_utxo\"}");
    }

    /**
     * Returns list of all historical Beam transactions in the wallet.
     *
     * @return list of {@link TransactionStatus}
     */
    public List<TransactionStatus> getTransactions() {
        String response = callBeamApi("{\"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"tx_list\"}");

        Type listType = new TypeToken<ArrayList<TransactionStatus>>(){}.getType();
        List<TransactionStatus> txList = gson.fromJson(new JsonParser().parse(response).getAsJsonObject().get("result"), listType);

        return txList;
    }

    /**
     * Returns list of all historical Beam transactions in the wallet. Filtered by {@link ch.bitmate.model.TransactionStatusType}
     *
     * @return list of {@link TransactionStatus}
     */
    public List<TransactionStatus> getTransactions(TransactionStatusType transactionStatusType) {
        String response = callBeamApi("{\"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"tx_list\", \"params\": {\"filter\": {\"status\": " + transactionStatusType.code() + "}}}");

        Type listType = new TypeToken<ArrayList<TransactionStatus>>(){}.getType();
        List<TransactionStatus> txList = gson.fromJson(new JsonParser().parse(response).getAsJsonObject().get("result"), listType);

        return txList;
    }


    public String sendDust(String address, long amount) {
        String result = callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 1,\"method\":\"tx_send\", \"params\":{\"session\" : 128,\"value\" : " + amount + ",\"fee\" : 10,\"address\" : \"" + address + "\", \"comment\": \"hi\"}}");

        return result;
    }

    public String cancelTx(String txid) {
        String result = callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 1,\"method\":\"tx_cancel\", \"params\":{\"txId\" : \"" + txid + "\"}}");

        return result;
    }

    /**
     * Returns a {@link TransactionStatus} object for a given txId which contains fee, number of confirmations, height, kernel, and more
     *
     * @param txId
     * @return {@link TransactionStatus} containining fee, number of confirmations, height, kernel, and more
     */
    public TransactionStatus getTransaction(String txId){
        String response = callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 1,\"method\":\"tx_status\", \"params\":{\t\"txId\" : \"" + txId + "\" }}");

        if (response == null){
            return null;
        }

        String result = new JsonParser().parse(response).getAsJsonObject().get("result").toString();
        TransactionStatus transactionStatus = gson.fromJson(result, TransactionStatus.class);

        return transactionStatus;
    }

    /**
     * Determines if walletAddress is a valid Beam address.
     *
     * @param walletAddress
     * @return true/false if walletAddress is a valid beam address
     */
    public boolean validateAddress(String walletAddress) {
        String response = callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 1,\"method\":\"validate_address\", \"params\":{\t\"address\" : \"" + walletAddress + "\" }}");

        if (response == null){
            return false;
        }

        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        boolean isValid = jsonObject.get("result").getAsJsonObject().get("is_valid").getAsBoolean();

        return isValid;
    }
}