package Game.Test.Transaction;

import Game.Test.util.DataAccessObject;

import java.sql.Connection;
import java.util.ArrayList;

public class TransactionDAO extends DataAccessObject<Transaction> {
    public TransactionDAO(Connection connection){super(connection);}
    @Override
    public Transaction findById(long id) {
        return null;
    }

    @Override
    public ArrayList<Transaction> findAll() {
        return null;
    }

    @Override
    public Transaction create(Transaction dto) {
        return null;
    }

    @Override
    public Transaction update(Transaction dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
