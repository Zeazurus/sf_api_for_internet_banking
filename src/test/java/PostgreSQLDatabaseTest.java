import org.bankapi.sql.OperationEntity;
import org.bankapi.sql.PostgreSQLDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PostgreSQLDatabaseTest {
    private static PostgreSQLDatabase db;

    @BeforeAll
    public static void setup() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/bank";
        String user = "postgres";
        String password = "admin";
        db = new PostgreSQLDatabase(url, user, password);
        db.setMoney(1, 1000.0);
        db.setMoney(2, 500.0);
        db.transact();
    }

    @Test
    public void testCheckBalance() throws SQLException {
        assertTrue(db.checkBalance(1, 1));
        assertFalse(db.checkBalance(2, Integer.MAX_VALUE));
    }

    @Test
    public void testGetBalance() throws SQLException {
        assertEquals(800, db.getBalance(2));
        assertThrows(SQLException.class, () -> db.getBalance(-1));
    }

    @Test
    public void testPutMoney() throws SQLException {
        db.putMoney(2, 200.0);
        var balance = db.getBalance(2);
        db.transact();

        assertEquals(700, balance);
        assertThrows(SQLException.class, () -> db.putMoney(-1, 100.0));
    }

    @Test
    public void testTransferMoney() throws SQLException {
        db.transferMoney(1, 2, 100);
        var balance1 = db.getBalance(1);
        var balance2 = db.getBalance(2);
        db.transact();

        assertEquals(700, balance1);
        assertEquals(800, balance2);
        assertThrows(SQLException.class, () -> db.transferMoney(-1, -1, Integer.MAX_VALUE));
    }

    @Test
    public void testTakeMoney() throws SQLException {
        db.takeMoney(1, 200.0);
        var balance = db.getBalance(1);
        db.transact();

        assertEquals(800.0, balance);
        assertThrows(SQLException.class, () -> db.takeMoney(-1, 100.0));
    }

    @Test
    public void testMarkOperation() throws SQLException {
        db.markOperation(1, 1,100);
        db.transact();

        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        Timestamp afterTime = Timestamp.valueOf(LocalDateTime.now().minusMinutes(1));
        assertNotEquals(new ArrayList<OperationEntity>(), db.getOperationListWithDate(1, afterTime, currentTime));
        assertEquals(new ArrayList<OperationEntity>(), db.getOperationListWithoutDate(-1));
    }

    @Test
    public void testGetOperationList() throws SQLException {
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        Timestamp afterTime = Timestamp.valueOf(LocalDateTime.now().minusYears(10));
        assertNotEquals(new ArrayList<OperationEntity>(), db.getOperationListWithDate(1, afterTime, currentTime));
        assertEquals(new ArrayList<OperationEntity>(), db.getOperationListWithoutDate(-1));
    }
}