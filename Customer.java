public class Customer {
    private String Id;
    private String Password;
    private String Name;
    private String Address;
    private int CardNumber;
    private String SecurityQ;
    private String SecurityAns;
    private boolean LoggedIn;

    public Customer(String Id, String Password, String Name, String Address,
                    int CardNumber, String SecurityQ, String SecurityAns, boolean LoggedIn) {
        this.Id = Id;
        this.Password = Password;
        this.Name = Name;
        this.Address = Address;
        this.CardNumber = CardNumber;
        this.SecurityQ = SecurityQ;
        this.SecurityAns = SecurityAns;
        this.LoggedIn = LoggedIn;
    }

    public boolean AcceptPass(String Password) {
        return this.Password.equals(Password);
    }

    public boolean AcceptAws(String Answer) {
        return this.SecurityAns.equals(Answer);
    }

    public boolean isLoggedIn() {
        return this.LoggedIn;
    }

    public void login() {
        this.LoggedIn = true;
    }

    public void logout() {
        this.LoggedIn = false;
    }

    public void updateCard(int newCard) {
        this.CardNumber = newCard;
    }

    public String getId() {
        return Id;
    }

    public String getPass() {
        return Password;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public int getCardNumber() {
        return CardNumber;
    }

    public String getSecQ() {
        return SecurityQ;
    }

    public String getSecA() {
        return SecurityAns;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public void setCardNumber(int CardNumber) {
        this.CardNumber = CardNumber;
    }
}
