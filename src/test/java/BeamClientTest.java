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
    public void testBalance() {
        WalletStatus walletStatus = beamClient.getWalletStatus();

        System.out.println(walletStatus.toString());

        assertNotNull(walletStatus);
        assertTrue(walletStatus.getDifficulty() > 0);
    }

    @Test
    public void testTxList() {
        List<TransactionStatus> transactionStatus = beamClient.getTxList();

        System.out.println(transactionStatus);
    }
}
