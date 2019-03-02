# BeamClient java library
A Java API Wrapper for the Beam Cryptocurrency Wallet.

# How to Build
* mvn clean install
* Note: For the tests to pass, you must be running an instance of Beam's wallet-api on localhost, port 10001. To skip tests. Add -DskipTests to the command line.

# To-do
- [ ] create_address
- [x] validate_address
- [ ] tx_send
- [x] tx_status
- [ ] tx_split
- [ ] tx_list
- [ ] tx_cancel
- [x] wallet_status
- [ ] get_utxo
- [x] tx_list
- [ ] tx_cancel

# Dependencies
* Gson
* OkHttp
* JUnit (for testing)
