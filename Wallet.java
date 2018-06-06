package bslCoin;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    
    public HashMap<String, TransactionOutputs> UTXOs = new HashMap<String, TransactionOutputs>(); // only UTXOs owned by this wallet
    
    public Wallet() {
        generateKeyPair();
    }
    
    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            
            //Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);; // 256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();
            
            // Set the public and private keys from keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    //returns balance and stores the UTXOs owned by this wallet in this.UTXOs
    public float getBalance() {
        float total = 0;
        for(Map.Entry<String, TransactionOutputs> item: Chain.UTXOs.entrySet()) {
            TransactionOutputs UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me (if coins belong to me)
                UTXOs.put(UTXO.id, UTXO); //add it to out list of unspent transactions
                total += UTXO.value;
                
            }
        }
        
        return total;
    }
    
    //Generates and returns a new transaction from this wallet
    public Transaction sendFunds(PublicKey _recipient, float value) {
        if(getBalance() < value) { //gather balance and check funds
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded");
            return null;
        }
        
        // create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
        float total = 0;
        for (Map.Entry<String, TransactionOutputs> item: UTXOs.entrySet()) {
            TransactionOutputs UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value) break;
        }
        
        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);
        
        for (TransactionInput input: inputs) {
            UTXOs.remove(input.transactionOutputId);
        }
        
        return newTransaction;
        
    }
    
}
