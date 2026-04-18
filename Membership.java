package FitnessClubSystem;

/*
 * Abstract Membership Class
 * ------------------------
 * Demonstrates ABSTRACTION & POLYMORPHISM.
 * Different membership types override calculateFee().
 * 
 * @author Cheng Jun Yu
 * @version 2.0
 */
public abstract class Membership {

    protected String membershipName;
    protected double price;

    public Membership(String membershipName, double price) {
        this.membershipName = membershipName;
        this.price = price;
    }

    public String getMembershipName() {
        return membershipName;
    }

    public double getPrice() {
        return price;
    }

    public abstract double calculateFee();

    /*
     * Priority class booking privilege
     */
    public abstract boolean hasPriorityBooking();

    /*
     * Sauna access privilege
     */
    public abstract boolean hasSaunaAccess();

    /*
     * Returns a string representation of the Membership object
     */
    @Override
    public String toString() {
        return String.format("%s Membership (RM%.2f)", membershipName, price);
    }

}