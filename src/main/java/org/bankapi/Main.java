package org.bankapi;

import org.bankapi.sql.PostgreSQLDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        // Загружаем параметры из файла config.properties
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("src/main/resources/config.properties");
        props.load(in);
        in.close();

        // Получаем значения параметров
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        PostgreSQLDatabase db = new PostgreSQLDatabase(url, user, password);

        int userId = 1;

        try {
            System.out.println("User " + userId + " balance: " + db.getBalance(userId));
            db.putMoney(userId, 100);
            System.out.println("User " + userId + " balance after putting money (+100): " + db.getBalance(userId));
            db.takeMoney(userId, 50);
            System.out.println("User " + userId + " balance after taking money (-50): " + db.getBalance(userId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
