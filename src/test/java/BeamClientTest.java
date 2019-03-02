import ch.bitmate.BeamClient;
import ch.bitmate.model.Balance;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BeamClientTest {

    private final BeamClient beamClient = new BeamClient("localhost", 10001);

    @Test
    public void testBalance() {
        Balance balance = beamClient.getBalance();

        System.out.println(balance.toString());

        assertNotNull(balance);
        assertTrue(balance.getDifficulty() > 0);
    }
}
