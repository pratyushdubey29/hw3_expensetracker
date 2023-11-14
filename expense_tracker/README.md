# hw1- Manual Review

The homework will be based on this project named "Expense Tracker",where users will be able to add/remove daily transaction. 

## Compile

To compile the code from terminal, use the following command:
```
cd src
javac ExpenseTrackerApp.java
java ExpenseTracker
```

You should be able to view the GUI of the project upon successful compilation. 

## Java Version
This code is compiled with ```openjdk 17.0.7 2023-04-18```. Please update your JDK accordingly if you face any incompatibility issue.

## Undo functionality
Undo functionality was added to the ExpenseTrackerApp. This allows us to delete transactions one at a time. Below are the cases covered:

- If no transactions are present currently, *Undo* must be disabled.
- If at least one transaction is present, *Undo* must be enabled.
  - A pop-up box turns up asking for the index of transaction that is to be deleted.
  - The transactions in the table already have the serial number besides them.
  - If a valid transaction number is entered, that transaction is deleted and the last row showing the sum of transactions reflect this update.
  - If an invalid transaction number is entered, a pop-up displays the message `The index is not valid.`. The state of the transactions table remains unaltered.

## Test cases
Appropriate test cases have been added that covers the below cases:

- Add Transaction `testAddValidTransaction`:
  - Steps: Add a transaction with amount 50.00 and category "food"
  - Output: Transaction is added to the table, Total Cost is updated
- Invalid Input Handling `testAddInvalidTransaction`:
  - Steps: Attempt to add a transaction with an invalid amount or category
  - Output: Error messages are displayed, transactions and Total Cost remain unchanged
- Filter by Amount `testAmountFilter`:
  - Steps: Add multiple transactions with different amounts, apply amount filter
  - Output: Only transactions matching the amount are highlighted.
- Filter by Category `testCategoryFilter`:
  - Steps: Add multiple transactions with different categories, apply category filter
  - Output: Only transactions matching the category are returned (and will be highlighted)
- Undo Disallowed `testUndoBtnDisabledForEmptyTransactions`:
  - Steps: Attempt to undo when the transactions list is empty
  - Output: Either UI widget is disabled or an error code (exception) is returned (thrown).
- Undo Allowed `testUndoBtnEnabledForNonEmptyTransactions`:
  - Steps: Add a transaction, undo the addition
  - Output: Transaction is removed from the table, Total Cost is updated
