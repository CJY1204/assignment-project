package FitnessClubSystem;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Trainer Class
 * ---------------------------------------------------
 * Represents a fitness trainer in the system.
 * 
 * Responsibilities:
 * - Store trainer personal information
 * - Validate login credentials
 * - View assigned classes and members
 * - Track member progress
 * 
 * Relationship:
 * - ASSOCIATION with FitnessClass (Trainer teaches classes)
 * - ASSOCIATION with Member (Trainer tracks members)
 * 
 * @author Low Kar Wai
 * @version 2.0
 */

public class Trainer {

    private String trainerId;
    private String password;
    private String name;

    // private ArrayList<Member> assignedMembers; (存储被分配给该教练的会员)

    /*
     * Parameterized constructor for Trainer
     * 
     * @param trainerId Unique trainer identifier
     * @param password  Login password
     * @param name      Trainer's full name
     */
    public Trainer(String trainerId, String password, String name) {
        this.trainerId = trainerId;
        this.password = password;
        this.name = name;
        // TODO: 2. 在构造函数中初始化 ArrayList
        // this.assignedMembers = new ArrayList<>();
    }

    // ==================== 新增：登录验证方法 ====================
    
    /*
     * Validates login credentials
     * 
     * @param inputId       Trainer ID entered by user
     * @param inputPassword Password entered by user
     * @return true if credentials match, false otherwise
     */
    public boolean validateCredentials(String inputId, String inputPassword) {
        return this.trainerId.equals(inputId) && this.password.equals(inputPassword);
    }

    // ==================== Getters ====================
    
    public String getTrainerId() { 
        return trainerId; 
    }
    
    public String getUserId() { 
        return trainerId;  // For backward compatibility with old code
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public String getName() { 
        return name; 
    }

    // ==================== Display Methods ====================

    /*
     * Displays the trainer menu
     */
    public void displayMenu() {
        System.out.println("\n=== Trainer Menu (ID: " + trainerId + ") ===");
        System.out.println("1. View My Class Schedule"); // 查看自己被分配了哪些课
        System.out.println("2. View Assigned Members");  // 查看自己的学员名单
        System.out.println("3. Track Member Progress");  // 查看学员的健康数据(Health Stats)
        System.out.println("4. Logout");
    }

    // ==================== Business Logic Methods ====================

    /*
     * View all classes assigned to this trainer
     * 
     * @param classes List of all fitness classes
     */
    public void viewMySchedule(ArrayList<FitnessClass> classes) {
        System.out.println("--- My Class Schedule ---");
        if (classes == null || classes.isEmpty()) {
            System.out.println("No classes scheduled.");
            return;
        }
        boolean any = false;
        for (FitnessClass c : classes) {
            if (c.getInstructor() != null
                    && c.getInstructor().getUserId().equalsIgnoreCase(trainerId)) {
                c.displayClassInfo();
                any = true;
            }
        }
        if (!any) {
            System.out.println("You are not assigned to any class yet.");
        }
    }

    /*
     * View all members enrolled in this trainer's classes
     * 
     * @param classes List of all fitness classes
     */
    public void viewAssignedMembers(ArrayList<FitnessClass> classes) {
        System.out.println("--- Your Member List ---");
        if (classes == null || classes.isEmpty()) {
            System.out.println("No classes in the system.");
            return;
        }
        ArrayList<String> seenMemberIds = new ArrayList<>();
        ArrayList<Member> roster = new ArrayList<>();
        for (FitnessClass c : classes) {
            if (c.getInstructor() != null
                    && c.getInstructor().getUserId().equalsIgnoreCase(trainerId)) {
                for (Member m : c.getBookedMembers()) {
                    if (!seenMemberIds.contains(m.getMemberID())) {
                        seenMemberIds.add(m.getMemberID());
                        roster.add(m);
                    }
                }
            }
        }
        if (roster.isEmpty()) {
            System.out.println("No members enrolled in your classes yet.");
            return;
        }
        for (Member m : roster) {
            System.out.println("ID: " + m.getMemberID() + " | Name: " + m.getName());
        }
    }

    /*
     * Track progress of a specific member
     * Displays BMI, weight, height, and BMI category
     * 
     * @param input   Scanner for user input
     * @param members List of all members
     * @param classes List of all fitness classes
     */
    public void trackMemberProgress(Scanner input, ArrayList<Member> members,
                                    ArrayList<FitnessClass> classes) {
        if (members == null || members.isEmpty()) {
            System.out.println("No members in the system.");
            return;
        }
        System.out.print("Enter Member ID: ");
        String mid = input.next().trim();
        Member found = null;
        for (Member m : members) {
            if (m.getMemberID().equalsIgnoreCase(mid)) {
                found = m;
                break;
            }
        }
        if (found == null) {
            System.out.println("Member not found.");
            return;
        }
        if (!isMemberInMyClasses(found, classes)) {
            System.out.println("This member is not enrolled in any of your classes.");
            return;
        }
        System.out.println("--- Tracking Progress for Member: " + found.getMemberID() + " ---");
        System.out.println("Name: " + found.getName());
        System.out.println("Age: " + found.getAge());
        System.out.printf("Height: %.2f m | Weight: %.2f kg%n", found.getHeight(), found.getWeight());
        double bmi = found.calculateBMI();
        System.out.printf("BMI: %.2f (%s)%n", bmi, getBmiCategory(bmi));
    }

    /*
     * Check if a member is enrolled in any of this trainer's classes
     * 
     * @param m       Member to check
     * @param classes List of all fitness classes
     * @return true if member is in trainer's class, false otherwise
     */
    private boolean isMemberInMyClasses(Member m, ArrayList<FitnessClass> classes) {
        if (classes == null) {
            return false;
        }
        for (FitnessClass c : classes) {
            if (c.getInstructor() != null
                    && c.getInstructor().getUserId().equalsIgnoreCase(trainerId)) {
                for (Member bm : c.getBookedMembers()) {
                    if (bm.getMemberID().equalsIgnoreCase(m.getMemberID())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
     * Get BMI category based on WHO classification
     * 
     * @param bmi BMI value
     * @return Category as String
     */
    private String getBmiCategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25.0) {
            return "Normal";
        } else if (bmi < 30.0) {
            return "Overweight";
        }
        return "Obese";
    }

    // TODO: 5. 辅助方法: 被分配新会员
    // 当 Admin 分配会员给这个教练时，调用此方法。
    // public void addMember(Member m) { ... }

    // ==================== Object Overrides ====================

    /*
     * Compares this Trainer to another object
     * Two Trainers are considered equal if they have the same trainerId
     * 
     * @param obj Object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trainer trainer = (Trainer) obj;
        return trainerId.equals(trainer.trainerId);
    }

    /*
     * Returns a string representation of the Trainer object
     * 
     * @return Formatted string with trainer details
     */
    @Override
    public String toString() {
        return String.format("Trainer[ID=%s, Name=%s]", trainerId, name);
    }

}