package FitnessClubSystem;

/*
 * BasicMembership Class
 * ------------------------
 * Represents the Basic tier membership.
 * Unique attribute: maxMonthlyBookings
 * 
 * @author Cheng Jun Yu
 * @version 2.0
 */
public class BasicMembership extends Membership {

    private static final double DEFAULT_PRICE = 50.0;
    private int maxMonthlyBookings;

    /*
     * Parameterized constructor for BasicMembership
     * @param maxMonthlyBookings Maximum number of class bookings allowed per month
     */
    public BasicMembership(int maxMonthlyBookings) {
        super("Basic", DEFAULT_PRICE);
        this.maxMonthlyBookings = maxMonthlyBookings;
    }

    /*
     * Gets the maximum monthly bookings allowed
     * @return maxMonthlyBookings
     */
    public int getMaxMonthlyBookings() {
        return maxMonthlyBookings;
    }

    /*
     * Sets the maximum monthly bookings allowed
     * @param maxMonthlyBookings New maximum value
     */
    public void setMaxMonthlyBookings(int maxMonthlyBookings) {
        this.maxMonthlyBookings = maxMonthlyBookings;
    }

    @Override
    public double calculateFee() {
        return price;
    }

    @Override
    public boolean hasPriorityBooking() {
        return false;
    }

    @Override
    public boolean hasSaunaAccess() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Max Bookings/Month: %d", maxMonthlyBookings);
    }

}