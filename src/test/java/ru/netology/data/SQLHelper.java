package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static QueryRunner runner = new QueryRunner();

    private SQLHelper() {
    }

    public static Connection getConn() throws SQLException {
        var url = System.getProperty("db.url");
        var user = System.getProperty("db.user");
        var password = System.getProperty("db.password");
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException err){
            err.printStackTrace();
        }
        return null;
    }

    public static DataHelper.CreditCardData getCreditCardData() {
        var cardDataSQL = "SELECT * FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            var result = runner.query(conn, cardDataSQL,
                    new BeanHandler<>(DataHelper.CreditCardData.class));
            return result;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static DataHelper.PaymentCardData getPaymentCardData() {
        var cardDataSQL = "SELECT * FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            var result = runner.query(conn, cardDataSQL,
                    new BeanHandler<>(DataHelper.PaymentCardData.class));
            return result;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var conn = getConn();
        runner.execute(conn, "DELETE FROM order_entity");
        runner.execute(conn, "DELETE FROM payment_entity");
        runner.execute(conn, "DELETE FROM credit_request_entity");
    }
}