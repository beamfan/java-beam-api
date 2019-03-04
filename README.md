# BeamClient java library
A Java library for the Beam cryptocurrency Wallet API.

# How to Build
* mvn clean install
* Note: For the tests to pass, you must be running an instance of Beam's wallet-api on localhost, port 10001. To skip tests. Add -DskipTests to the command line.

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
