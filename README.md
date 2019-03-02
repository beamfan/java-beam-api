# BeamClient java library
A Java API Wrapper for the Beam Cryptocurrency Wallet.

# Dependencies
* Gson
* OkHttp
* JUnit (for testing)

# How to Build
* Install Maven
* Navigate to directory and type "mvn clean install". This will run the tests and install the JAR into your local repository.
* Note: For the tests to pass, you must be running an instance of Beam's wallet-api on localhost, port 10001. To skip tests. Add -DskipTests to the command line.