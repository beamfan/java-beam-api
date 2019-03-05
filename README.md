# BeamClient java library
A Java library for the Beam cryptocurrency Wallet API.

# Building
* mvn clean install
* Note: For the tests to pass, you must be running an instance of Beam's wallet-api on localhost, port 10001. To skip tests. Add -DskipTests to the command line.

# Usage
 Include Maven dependency (update version as necessary):

```
<dependency>
            <groupId>ch.bitmate</groupId>
            <artifactId>beamclient</artifactId>
            <version>1.1-SNAPSHOT</version>
</dependency>
```

# Example
1. Cancel transactions stuck In Progress
```
// Setup beam client
BeamClient beamClient = new BeamClient("localhost", 10001);
// Get list of transactions in progress
List<TransactionStatus> transactions = beamClient.getTransactions(TransactionStatusType.IN_PROGRESS);
// Cancel each transaction
transactions.forEach(transaction -> beamClient.cancelTransaction(transaction.getTxId()));

```

# To-do
- [x] create_address
- [x] validate_address
- [x] tx_send
- [x] tx_status
- [ ] tx_split
- [x] wallet_status
- [ ] get_utxo
- [x] tx_list
- [x] tx_cancel

# Dependencies
* Gson
* OkHttp
* JUnit (for testing)
