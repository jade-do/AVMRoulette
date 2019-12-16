package russionroulette;

public class BankRoll {
	private double balance;
	
	public BankRoll() {
		this.balance = 0.0;
	}
	
	public void update(double amount) {
		this.balance = amount;
	}
	
	public double retrieve() {
		return this.balance;
	}
	
	public double add(double amount) {
		this.balance += amount;
		return this.balance;
	}
	
	public double deduct(double amount) {
		this.balance -= amount;
		return this.balance;
	}
}
