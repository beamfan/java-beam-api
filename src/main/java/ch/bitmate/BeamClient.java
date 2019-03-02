package ch.bitmate;

import ch.bitmate.model.Balance;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class BeamClient {
    private String ip;
    private int port;
    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Gson gson = new Gson();

    public BeamClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private String callBeamApi(String request) {
        System.out.println("Request = " + request);
        try {
            return post("http://" + ip + ":" + port + "/api/wallet", request);
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

    public Balance getBalance() {
        String response = callBeamApi("{\"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"wallet_status\"}");

        if (response == null){
            return null;
        }

        String result = new JsonParser().parse(response).getAsJsonObject().get("result").toString();
        Balance balance = gson.fromJson(result, Balance.class);

        return balance;
    }

    public String getUtxos() {
        return callBeamApi("{\"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"get_utxo\"}");
    }

    public String getTxList() {
        return callBeamApi("{\"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"tx_list\", \"params\": {\"filter\": {\"status\": 1}}}");
    }


    public String sendDust(String address, long amount) {
        String result = callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 1,\"method\":\"tx_send\", \"params\":{\"session\" : 128,\"value\" : " + amount + ",\"fee\" : 10,\"address\" : \"" + address + "\", \"comment\": \"hi\"}}");

        return result;
    }

    public String cancelTx(String txid) {
        String result = callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 1,\"method\":\"tx_cancel\", \"params\":{\"txId\" : \"" + txid + "\"}}");

        return result;
    }

    public String getStatus(String txId){
        return callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 4,\"method\":\"status\", \"params\":{\t\"txId\" : \"" + txId + "\" }}");
    }

    public boolean validateAddress(String walletAddress) {
        String validateResult = callBeamApi("{\"jsonrpc\":\"2.0\", \"id\": 4,\"method\":\"validate_address\", \"params\":{\t\"address\" : \"" + walletAddress + "\" }}");


        JsonObject jsonObject = new JsonParser().parse(validateResult).getAsJsonObject();
        boolean isValid = jsonObject.get("result").getAsJsonObject().get("is_valid").getAsBoolean();

        return isValid;
    }
}