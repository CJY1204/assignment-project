package FitnessClubSystem;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Admin Class
 * ---------------------------------------------------
 * Represents a system administrator.
 * 
 * Responsibilities:
 * - Store admin login information
 * - Validate login credentials
 * - Manage members, equipment, and classes
 * - Generate revenue reports
 * 
 * Relationship:
 * - ASSOCIATION with Member, Equipment, FitnessClass, Trainer
 * 
 * @author Tang Zhi Hao
 * @version 2.0
 */

public class Admin {

    private String adminId;
    private String password;
    Scanner input = new Scanner(System.in);

    /*
     * Parameterized constructor for Admin
     * 
     * @param adminId  Unique admin identifier
     * @param password Login password
     */
    public Admin(String adminId, String password) {
        this.adminId = adminId;
        this.password = password;
    }

    // ==================== 新增：登录验证方法 ====================

    /*
     * Validates login credentials
     * 
     * @param inputId       Admin ID entered by user
     * @param inputPassword Password entered by user
     * @return true if credentials match, false otherwise
     */
    public boolean validateCredentials(String inputId, String inputPassword) {
        return this.adminId.equals(inputId) && this.password.equals(inputPassword);
    }

    // ==================== Getters ====================

    public String getAdminId() {
        return adminId;
    }

    public String getUserId() {
        return adminId;  // For backward compatibility with old code
    }

    public String getPassword() {
        return password;
    }

    // ==================== Display Methods ====================

    /*
     * Displays the admin menu
     */
    public void displayMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. Manage Members (View Active/Expired)");
        System.out.println("2. Manage Equipment (Flag Maintenance)");
        System.out.println("3. Manage Staff (Assign Trainer to Classes)");
        System.out.println("4. Generate Revenue Report");
        System.out.println("5. Logout");
    }

    // ==================== Business Logic Methods ====================

    /*
     * Manage member status
     * Allows admin to view all members and update their status (Active/Expired)
     * 
     * @param members List of all members
     */
    public void manageMembers(ArrayList<Member> members) {

        System.out.println("\n--- Member List ---");

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        System.out.printf("%-8s | %-15s | %-10s | %-10s\n", "ID", "Name", "Type", "Status");
        System.out.println("------------------------------------------------------------");
        for (Member m : members) {
            System.out.printf("%-8s | %-15s | %-10s | %-10s\n",
                    m.getMemberID(),
                    m.getName(),
                    m.getMembership().getMembershipName(),
                    m.getStatus());
        }

        // 2. 选择目标
        System.out.print("\nEnter Member ID to update (or '0' to go back): ");
        String targetId = input.next();

        if (targetId.equals("0")) return;

        // 3. 查找逻辑
        Member foundMember = null;
        for (Member m : members) {
            if (m.getMemberID().equalsIgnoreCase(targetId)) {
                foundMember = m;
                break;
            }
        }

        if (foundMember == null) {
            System.out.println("Error: Member with ID " + targetId + " not found.");
            return;
        }

        // 4. 状态更新判定 (用循环强制要求输入正确)
        boolean updated = false;
        while (!updated) {
            System.out.println("\nUpdating Status for: " + foundMember.getName());
            System.out.println("Current Status: " + foundMember.getStatus());
            System.out.println("Select New Status: [1] Active  [2] Expired  [3] Cancel");
            System.out.print("Choice: ");

            String choice = input.next();
            switch (choice) {
                case "1":
                    foundMember.setStatus("Active");
                    updated = true;
                    System.out.println("Successfully set to Active.");
                    break;
                case "2":
                    foundMember.setStatus("Expired");
                    updated = true;
                    System.out.println("Successfully set to Expired.");
                    break;
                case "3":
                    System.out.println("Update cancelled.");
                    return;
                default:
                    System.out.println("Invalid choice! Please enter 1, 2, or 3.");
            }
        }

        // 5. 重要：提示数据已更改
        System.out.println("Note: Status updated in memory. Data will be finalized on system exit.");
    }

    /*
     * Manage equipment
     * Allows admin to view, add, update, and delete equipment
     * 
     * @param equipments List of all equipment
     */
    public void manageEquipment(ArrayList<Equipment> equipments) {
        Scanner input = new Scanner(System.in);
        if (equipments == null) {
            return;
        }

        String sub;
        do {
            Equipment.displayEquipmentList(equipments);
            System.out.println("--- Actions ---");
            System.out.println("1. Update status (maintenance flag)");
            System.out.println("2. Add new equipment");
            System.out.println("3. Delete equipment");
            System.out.println("0. Back to admin menu");
            System.out.print("Enter choice: ");
            sub = input.next().trim();

            switch (sub) {
                case "1":
                    Equipment.updateEquipmentStatusOnly(equipments, input);
                    break;
                case "2":
                    Equipment.addNewEquipment(equipments, input);
                    break;
                case "3":
                    Equipment.deleteEquipment(equipments, input);
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (!sub.equals("0"));
    }

    /*
     * Manage class assignments
     * Allows admin to create classes, set schedules, assign trainers, and delete classes
     * 
     * @param classes  List of all fitness classes
     * @param trainers List of all trainers
     */
    public void assignClass(ArrayList<FitnessClass> classes, ArrayList<Trainer> trainers) {
        Scanner input = new Scanner(System.in);
        String choice;

        while (true) {
            FitnessClass.printClassDetail(classes, trainers);
            System.out.println("\n=======================");
            System.out.println("1. Create Classes");
            System.out.println("2. Set Schedule");
            System.out.println("3. Assign Trainer");
            System.out.println("4. Delete Classes");
            System.out.println("5. Return to Admin menu");
            System.out.println("=======================");
            System.out.print("Enter choice: ");
            choice = input.next();

            switch (choice) {
                case "1":
                    FitnessClass.createClass(classes);
                    break;
                case "2":
                    FitnessClass.setSchedule(classes);
                    break;
                case "3":
                    FitnessClass.assignTrainerToClass(classes, trainers);
                    break;
                case "4":
                    FitnessClass.deleteClass(classes);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-5.");
            }
        }
    }

    /*
     * Generate monthly revenue report
     * Displays total revenue and membership breakdown
     * 
     * @param members List of all members
     */
    public void generateMonthlyRevenueReport(ArrayList<Member> members) {
        double totalRevenue = 0;
        int goldCount = 0, silverCount = 0, basicCount = 0;

        for (Member m : members) {
            totalRevenue += m.getMembership().getPrice();
            // 进阶逻辑: 统计各等级人数
            String type = m.getMembership().getMembershipName();
            if (type.equalsIgnoreCase("Gold")) {
                goldCount++;
            } else if (type.equalsIgnoreCase("Silver")) {
                silverCount++;
            } else {
                basicCount++;
            }
        }

        System.out.println("\n===== Monthly Revenue Report =====");
        System.out.println("Total Active Members: " + members.size());
        System.out.println(" - Gold: " + goldCount + " | Silver: " + silverCount + " | Basic: " + basicCount);
        System.out.println("Total Monthly Revenue: RM " + totalRevenue);
        System.out.println("==================================");
    }

    // ==================== Object Overrides ====================

    /*
     * Compares this Admin to another object
     * Two Admins are considered equal if they have the same adminId
     * 
     * @param obj Object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Admin admin = (Admin) obj;
        return adminId.equals(admin.adminId);
    }

    /*
     * Returns a string representation of the Admin object
     * 
     * @return Formatted string with admin details
     */
    @Override
    public String toString() {
        return String.format("Admin[ID=%s]", adminId);
    }

}