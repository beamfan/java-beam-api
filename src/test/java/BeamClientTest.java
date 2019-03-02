import ch.bitmate.BeamClient;
import ch.bitmate.model.TransactionStatus;
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
}
