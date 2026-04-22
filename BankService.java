import java.util.Random;

public class BankService {
    private Random random;

    public BankService() {
        this.random = new Random();
    }

    public String chargeCard(int CardNumber, double amount) {
        return "Transaction approved for card " + CardNumber + " - Amount: $" + amount;
    }

    public boolean validateCard(int CardNumber) {
        return CardNumber > 0;
    }
}
