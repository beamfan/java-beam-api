package ch.bitmate;

import ch.bitmate.model.TransactionStatus;
import ch.bitmate.model.TransactionStatusType;
import ch.bitmate.model.WalletStatus;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
    private JsonParser jsonParser = new JsonParser();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public BeamClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String callBeamApi(String request) {
        try {
            String response = post(String.format("http://%s:%s%s", host, port, endpoint), request);

            if (response == null){
                return null;
            }

            // Throw error as a RuntimeException if it exists
            throwErrorFromResponse(response);

            return response;
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

    private void throwErrorFromResponse(String response) {
        JsonElement error = jsonParser.parse(response).getAsJsonObject().get("error");

        if (error != null) {
            String errorMessage = error.getAsJsonObject().get("message").toString();
            throw new RuntimeException("Error thrown by the API = " + errorMessage);
        }
    }

    /**
     * Returns wallet status, including available groth balances
     *
     * @return {@link WalletStatus} object containing current wallet status
     */
    public WalletStatus getWalletStatus() {
        String response = callBeamApi("{\"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"wallet_status\"}");

        String result = jsonParser.parse(response).getAsJsonObject().get("result").toString();
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
        List<TransactionStatus> txList = gson.fromJson(jsonParser.parse(response).getAsJsonObject().get("result"), listType);

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
        List<TransactionStatus> txList = gson.fromJson(jsonParser.parse(response).getAsJsonObject().get("result"), listType);

        return txList;
    }


    /**
     * Send a BEAM transaction.
     * @param address
     * @param amount
     * @param fee
     * @return {@link TransactionStatus} object containing txId and other details.
     */
    public TransactionStatus sendTransaction(String address, long amount, long fee) {
        String response = callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 1,\"method\":\"tx_send\", \"params\":{\"session\" : 1,\"value\" : " + amount + ",\"fee\" : " + fee + ",\"address\" : \"" + address + "\"}}");

        // Get the transaction we just created.
        JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
        String txId = jsonObject.get("result").getAsJsonObject().get("txId").getAsString();

        TransactionStatus transactionStatus = getTransaction(txId);

        return transactionStatus;
    }

    /**
     * Cancel a transaction. Usually used on "stuck"/failed transactions.
     * @param txid
     * @return true/false if the transaction was successfully cancelled
     */
    public boolean cancelTransaction(String txid) {
        String response = callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 1,\"method\":\"tx_cancel\", \"params\":{\"txId\" : \"" + txid + "\"}}");

        boolean result = jsonParser.parse(response).getAsJsonObject().get("result").getAsBoolean();

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

        String result = jsonParser.parse(response).getAsJsonObject().get("result").toString();
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

        JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
        boolean isValid = jsonObject.get("result").getAsJsonObject().get("is_valid").getAsBoolean();

        return isValid;
    }
}