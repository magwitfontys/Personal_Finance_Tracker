package com.example.backend.pl.api;

import com.example.backend.bll.dto.TransactionDTO;
import com.example.backend.bll.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * GET /api/transactions?userId=123
     * Returns all transactions for the specified user.
     * Frontend: transactions page uses this to display the list.
     */
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(
            @RequestParam(required = true) Integer userId
    ) {
        List<TransactionDTO> transactions = transactionService.getAllUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/search?userId=123&description=grocery&categoryId=5&txnType=EXPENSE
     * Search/filter transactions by optional criteria.
     * Frontend: transactions page uses this when filters are applied.
     */
    @GetMapping("/search")
    public ResponseEntity<List<TransactionDTO>> searchTransactions(
            @RequestParam(required = true) Integer userId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String txnType
    ) {
        List<TransactionDTO> results = transactionService.searchUserTransactions(
                userId, description, categoryId, txnType
        );
        return ResponseEntity.ok(results);
    }

    /**
     * POST /api/transactions
     * Creates a new transaction.
     * Frontend: add-transaction page posts to this endpoint.
     * 
     * Expected JSON body:
     * {
     *   "userId": 123,
     *   "categoryId": 5,
     *   "amount": 250.00,
     *   "txnType": "EXPENSE",   // or "INCOME"
     *   "txnDate": "2024-01-15",
     *   "description": "Weekly shopping"
     * }
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionDTO request) {
        // Basic validation
        if (request.getUserId() == null || request.getCategoryId() == null 
                || request.getAmount() == null || request.getTxnType() == null
                || request.getTxnDate() == null) {
            throw new IllegalArgumentException("Missing required fields: userId, categoryId, amount, txnType, txnDate");
        }

        // Normalize txnType to uppercase
        String type = request.getTxnType().trim().toUpperCase();
        if (!type.equals("INCOME") && !type.equals("EXPENSE")) {
            throw new IllegalArgumentException("txnType must be INCOME or EXPENSE");
        }
        request.setTxnType(type);

        TransactionDTO created = transactionService.save(request);
        logger.info("action=CREATE_TRANSACTION, userId={}, transactionId={}, type={}, amount={}, result=SUCCESS", 
                    request.getUserId(), created.getTransactionId(), created.getTxnType(), created.getAmount());
        return ResponseEntity
                .created(URI.create("/api/transactions/" + created.getTransactionId()))
                .body(created);
    }

    /**
     * DELETE /api/transactions/delete-all?userId=123
     * Deletes all transactions for a specific user.
     * Frontend: transactions page calls this when user confirms delete all.
     * NOTE: This endpoint must come BEFORE the /{id} endpoint to avoid route matching conflicts.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAllTransactions(@RequestParam(required = true) Integer userId) {
        transactionService.deleteAllByUserId(userId);
        logger.info("action=DELETE_ALL_TRANSACTIONS, userId={}, result=SUCCESS", userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/transactions/{id}
     * Deletes a transaction by ID.
     * Frontend: transactions page calls this when user confirms deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Integer id) {
        transactionService.deleteById(id);
        logger.info("action=DELETE_TRANSACTION, transactionId={}, result=SUCCESS", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /api/transactions/{id}
     * Updates an existing transaction by ID.
     */
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<?> updateTransaction(@PathVariable Integer id, @Valid @RequestBody TransactionDTO request) {
        if (request.getUserId() == null || request.getCategoryId() == null
                || request.getAmount() == null || request.getTxnType() == null
                || request.getTxnDate() == null) {
            throw new IllegalArgumentException("Missing required fields: userId, categoryId, amount, txnType, txnDate");
        }

        String type = request.getTxnType().trim().toUpperCase();
        if (!type.equals("INCOME") && !type.equals("EXPENSE")) {
            throw new IllegalArgumentException("txnType must be INCOME or EXPENSE");
        }
        request.setTxnType(type);
        request.setTransactionId(id);

        TransactionDTO updated = transactionService.save(request);
        logger.info("action=UPDATE_TRANSACTION, userId={}, transactionId={}, type={}, amount={}, result=SUCCESS", 
                    request.getUserId(), id, updated.getTxnType(), updated.getAmount());
        return ResponseEntity.ok(updated);
    }


}