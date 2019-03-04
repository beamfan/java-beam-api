import ch.bitmate.BeamClient;
import ch.bitmate.model.TransactionStatus;
import ch.bitmate.model.TransactionStatusType;
import ch.bitmate.model.WalletStatus;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BeamClientTest {

    private final BeamClient beamClient = new BeamClient("localhost", 10001);

    @Test
    public void testWalletStatus() {
        WalletStatus walletStatus = beamClient.getWalletStatus();

        assertNotNull(walletStatus);

        System.out.println(walletStatus.toString());

        assertTrue(walletStatus.getDifficulty() > 0);
    }

    @Test
    public void testGetTransactions() {
        List<TransactionStatus> transactionStatus = beamClient.getTransactions();

        assertNotNull(transactionStatus);

        System.out.println(transactionStatus);
    }

    @Test
    public void testGetTransaction() {
        TransactionStatus transactionStatus = beamClient.getTransaction("2bef147d43bb4bf6abe9b107ea943f7c");

        assertNotNull(transactionStatus);

        System.out.println(transactionStatus);
    }

    @Test
    public void testGetTransactionsFilteredByType() {
        List<TransactionStatus> transactionStatus = beamClient.getTransactions(TransactionStatusType.COMPLETED);

        assertNotNull(transactionStatus);

        System.out.println(transactionStatus);
    }

    @Test(expected = RuntimeException.class)
    public void testCancelTransactionInvalid() {
        boolean result = beamClient.cancelTransaction("123");
        System.out.println(result);
    }

    @Test
    public void testSendTransaction() {
        TransactionStatus response = beamClient.sendTransaction("215f68b6d217fd687402353ff9318a8d1149ffe96d8ce2ae2f4cda3360fc0bc62", 111112, 10);
        System.out.println("Sent:"  + response);
    }

    @Test
    public void testCreateAndValidateAddress() {
        String address = beamClient.createAddress(0);
        System.out.println("New address = " + address);
        assertTrue(beamClient.validateAddress(address));
    }
}
