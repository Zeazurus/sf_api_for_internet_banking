package org.bankapi.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bankapi.sql.PostgreSQLDatabase;
import org.bankapi.utils.ResponseJSON;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class UserController {
    private final PostgreSQLDatabase db;
    private final ObjectMapper objectMapper;

    public UserController(PostgreSQLDatabase db) {
        this.db = db;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/users/{userId}/getBalance")
    public String getBalance(@PathVariable int userId) throws JsonProcessingException {
        try {
            double balance = db.getBalance(userId);
            return objectMapper.writeValueAsString(new ResponseJSON(balance, null));
        } catch (SQLException e) {
            return objectMapper.writeValueAsString(new ResponseJSON(-1, e.getMessage()));
        }
    }

    @PostMapping("/users/{userId}/putMoney")
    public String putMoney(@PathVariable int userId, @RequestParam double amount) throws JsonProcessingException {
        try {
            db.putMoney(userId, amount);
            return objectMapper.writeValueAsString(new ResponseJSON(1, null));
        } catch (SQLException e) {
            return objectMapper.writeValueAsString(new ResponseJSON(0, e.getMessage()));
        }
    }

    @PostMapping("/users/{userId}/takeMoney")
    public String takeMoney(@PathVariable int userId, @RequestParam double amount) throws JsonProcessingException {
        try {
            if (db.checkBalance(userId, amount)) {
                db.takeMoney(userId, amount);
                return objectMapper.writeValueAsString(new ResponseJSON(1, null));
            } else {
                return objectMapper.writeValueAsString(new ResponseJSON(0, "Insufficient balance."));
            }

        } catch (SQLException e) {
            return objectMapper.writeValueAsString(new ResponseJSON(0, e.getMessage()));
        }
    }

    @GetMapping("/users/{userId}/getOperationList")
    public ResponseEntity<Object> getOperationList(@PathVariable int userId,
                                                   @RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate) {
        // TODO: implement operation list retrieval
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{fromUserId}/transferMoney")
    public String transferMoney(@PathVariable int fromUserId,
                                                @RequestParam int toUserId,
                                                @RequestParam double amount) throws JsonProcessingException {
        try {
            var response = takeMoney(fromUserId, amount);
            if (!response.contains("Insufficient balance.")) {
                putMoney(toUserId, amount);
                return objectMapper.writeValueAsString(new ResponseJSON(0, null));
            } else {
                return objectMapper.writeValueAsString(new ResponseJSON(-1, "Insufficient balance."));
            }
        } catch (JsonProcessingException e) {
            return objectMapper.writeValueAsString(new ResponseJSON(-1, e.getMessage()));
        }
    }
}
