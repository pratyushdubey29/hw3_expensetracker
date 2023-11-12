// package test;

import java.util.Date;
import java.util.List;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import model.Transaction;
import view.ExpenseTrackerView;

import javax.swing.table.DefaultTableModel;

import static org.junit.Assert.*;


public class TestExample {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  private ExpenseTrackerController controller;

  @Before
  public void setup() {
    model = new ExpenseTrackerModel();
    view = new ExpenseTrackerView();
    controller = new ExpenseTrackerController(model, view);
  }

    public double getTotalCost() {
        double totalCost = 0.0;
        List<Transaction> allTransactions = model.getTransactions(); // Using the model's getTransactions method
        for (Transaction transaction : allTransactions) {
            totalCost += transaction.getAmount();
        }
        return totalCost;
    }


    public void checkTransaction(double amount, String category, Transaction transaction) {
	assertEquals(amount, transaction.getAmount(), 0.01);
        assertEquals(category, transaction.getCategory());
        String transactionDateString = transaction.getTimestamp();
        Date transactionDate = null;
        try {
            transactionDate = Transaction.dateFormatter.parse(transactionDateString);
        }
        catch (ParseException pe) {
            pe.printStackTrace();
            transactionDate = null;
        }
        Date nowDate = new Date();
        assertNotNull(transactionDate);
        assertNotNull(nowDate);
        // They may differ by 60 ms
        assertTrue(nowDate.getTime() - transactionDate.getTime() < 60000);
    }


    @Test
    public void testAddTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add a transaction
	double amount = 50.0;
	String category = "food";
        assertTrue(controller.addTransaction(amount, category));
    
        // Post-condition: List of transactions contains only
	//                 the added transaction	
        assertEquals(1, model.getTransactions().size());
    
        // Check the contents of the list
	Transaction firstTransaction = model.getTransactions().get(0);
	checkTransaction(amount, category, firstTransaction);
	
	// Check the total amount
        assertEquals(amount, getTotalCost(), 0.01);
    }


    @Test
    public void testRemoveTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add and remove a transaction
	double amount = 50.0;
	String category = "food";
        Transaction addedTransaction = new Transaction(amount, category);
        model.addTransaction(addedTransaction);
    
        // Pre-condition: List of transactions contains only
	//                the added transaction
        assertEquals(1, model.getTransactions().size());
	Transaction firstTransaction = model.getTransactions().get(0);
	checkTransaction(amount, category, firstTransaction);

	assertEquals(amount, getTotalCost(), 0.01);
	
	// Perform the action: Remove the transaction
        model.removeTransaction(addedTransaction);
    
        // Post-condition: List of transactions is empty
        List<Transaction> transactions = model.getTransactions();
        assertEquals(0, transactions.size());
    
        // Check the total cost after removing the transaction
        double totalCost = getTotalCost();
        assertEquals(0.00, totalCost, 0.01);
    }

    @Test
    public void testAddValidTransaction() {

        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());

        // Perform the action: Add 3 types of invalid transactions
        assertTrue(controller.addTransaction(50.0, "food"));

        Object[][] testAssertTable ={
                {1, 50.0, "food", new Date().getTime()},{"Total", null, null, 50.0}
        };
        DefaultTableModel tableModel = view.getTableModel();
        Date transactionDate = null;
        try {
            transactionDate = Transaction.dateFormatter.parse((String) tableModel.getValueAt(0, 3));
        } catch (ParseException e) {
            assertNotNull(transactionDate);
        }

        for (int i = 0; i < tableModel.getRowCount(); i++){
            for (int j = 0; j < tableModel.getColumnCount(); j++){
                if (i != tableModel.getRowCount() - 1 && j == tableModel.getColumnCount() -1){
                    assertTrue((long) testAssertTable[i][j] - transactionDate.getTime() < 60000);
                }
                else{
                    assertEquals(testAssertTable[i][j], tableModel.getValueAt(i, j));
                }
            }
        }

        // Post-condition: List of transactions contains one transaction and one total row
        assertEquals(2, view.getTableModel().getRowCount());

        // Check the total amount
        assertEquals(50, (double) view.getTableModel().getValueAt(1, 3), 0.01);
    }

    @Test
    public void testAddInvalidTransaction(){

        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());

        // Perform the action: Add 3 types of invalid transactions
        assertFalse(controller.addTransaction(50.0, "food-poisoning"));
        try{
            Transaction t = new Transaction(50.0, "food-poisoning");
        }
        catch (IllegalArgumentException exception){
            assertEquals("The category is not valid.", exception.getMessage());
        }

        assertFalse(controller.addTransaction(-50.0, "food"));
        try{
            Transaction t = new Transaction(-50.0, "food");
        }
        catch (IllegalArgumentException exception){
            assertEquals("The amount is not valid.", exception.getMessage());
        }

        // Post-condition: List of transactions contains no transactions
        assertEquals(0, model.getTransactions().size());

        // Check the total amount
        assertEquals(0, getTotalCost(), 0.01);
    }
}
