# Blockchain

Imported libraries include
Gson & Bouncy Castle

------------------------------------------------

Block.java

Includes constructor for blocks
public Block(String previousHash)

Calculates new hash based on blocks contents using sha256 algorithm

Increases nonce value until hash target is reached
i.e. if difficulty = 5,
hash = 00000______________________________

Transactions are added to Block

----------------------------------------------

Chain.java

Array of blocks
HashMap (list) of of all unspent transactions (UTXOs)

Bouncy castle is security provider

Creates new wallets,
coinbase wallet generates coins to send to other wallets

Boolean check for Chain validity;
  loop through blockchain to check hashes;
  compare registered hash and calculated hash;
  compare previous hash and registered previous hash;
  check if hash is solved;
  loop through blockchain transactions;

---------------------------------------------------------

StringUtil.java

Applies sha256 to a string/ inputs and returns results;

Applies ECDSA Signatures and returns results;

Verifies a string signature;

Short hand helper to turn Object into a json string;

Returns difficulty string target to compare to hash e.g. difficulty 5 will return 00000;

Takes in array of transactions and returns a merkle root;

-------------------------------------------------

Transaction.java

TransactionId = hash of transaction;

Public key/ address of sender and recipient;

Signature;

Constructor
(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs)
  
  Calculates transaction hash, which will be used as its Id;
  Signs all data we don't wish to be tampered with;
  Verifies the data we signed hasn't been tampered with;
  
  Processing transactions,
    Gathers transaction inputs (makes sure they are unspent);
    Checks if transaction is valid;
    Generate transaction outputs;
    Adds outputs to unspent list;
    Remove transaction inputs from UTXO lists as spent;
    
Sums UTXO values

Sums Outputs

----------------------------------------------------

TransactionInput.java

TransactionOutputId;
  Reference to transactionId
  
TransactionOutputs UTXO;
  Contains the Unspent transaction output
  
  -------------------------------------------------------------
  
  TransactionOutputs.java
  
  id = sha256 hash;
  
  PublicKey recipient;
  
  Amount of coins transferred to new owner;
  
  parentTransactionID = the id of the transaction this output was created in;
  
  Constructor
    (PublicKey recipient, float value, String parentTransactionId)
   
 Checks if coins belong to me
 
 -------------------------------------------------
 
 Wallet.java
 
 Generates Key pair
  Public and Private keys
  
stores balance of UTXOs owned by wallet 
  this.UTXOs
  
Generates and returns a new transaction from this wallet
  array list of inputs
