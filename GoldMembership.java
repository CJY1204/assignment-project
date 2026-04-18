package FitnessClubSystem;

/*
 * GoldMembership Class
 * ------------------------
 * Represents the Gold tier membership (highest tier).
 * Unique attributes: hasPriorityBooking, hasSaunaAccess
 * 
 * @author Cheng Jun Yu
 * @version 2.0
 */
public class GoldMembership extends Membership {

    private static final double DEFAULT_PRICE = 200.0;
    private boolean hasPriorityBooking;
    private boolean hasSaunaAccess;

    /*
     * Parameterized constructor for GoldMembership
     * @param hasPriorityBooking Whether the member has priority booking privilege
     * @param hasSaunaAccess Whether the member has sauna access
     */
    public GoldMembership(boolean hasPriorityBooking, boolean hasSaunaAccess) {
        super("Gold", DEFAULT_PRICE);
        this.hasPriorityBooking = hasPriorityBooking;
        this.hasSaunaAccess = hasSaunaAccess;
    }

    /*
     * Sets the priority booking privilege
     * @param hasPriorityBooking New priority booking value
     */
    public void setHasPriorityBooking(boolean hasPriorityBooking) {
        this.hasPriorityBooking = hasPriorityBooking;
    }

    /*
     * Sets the sauna access privilege
     * @param hasSaunaAccess New sauna access value
     */
    public void setHasSaunaAccess(boolean hasSaunaAccess) {
        this.hasSaunaAccess = hasSaunaAccess;
    }

    @Override
    public double calculateFee() {
        return price;
    }

    @Override
    public boolean hasPriorityBooking() {
        return hasPriorityBooking;
    }

    @Override
    public boolean hasSaunaAccess() {
        return hasSaunaAccess;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Priority Booking: %s | Sauna Access: %s",
            hasPriorityBooking ? "Yes" : "No",
            hasSaunaAccess ? "Yes" : "No");
    }

}