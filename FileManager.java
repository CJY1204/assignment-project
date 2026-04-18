package FitnessClubSystem;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * FileManager
 * ------------------------
 * Handles TXT file storage.
 * Demonstrates EXCEPTION HANDLING.
 * ASSOCIATION with all data classes.
 * 
 * @author Low Kar Wai
 * @version 2.0
 */
public class FileManager {

    // ==================== Members ====================
    
    public void saveAllMembers(ArrayList<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("members.txt", false))) {
            for (Member m : members) {
                String data = m.getMemberID() + "|" +
                            m.getPassword() + "|" +
                            m.getName() + "|" +
                            m.getAge() + "|" +
                            m.getHeight() + "|" +
                            m.getWeight() + "|" +
                            m.getMembership().getMembershipName() + "|" +
                            m.getStatus();
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error: Could not save members to file.");
        }
    }

    public ArrayList<Member> loadMembers() {
        ArrayList<Member> members = new ArrayList<>();
        try {
            File file = new File("members.txt");
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split("\\|");

                String id = data[0];
                String password = data[1];
                String name = data[2];
                int age = Integer.parseInt(data[3]);
                double height = Double.parseDouble(data[4]);
                double weight = Double.parseDouble(data[5]);
                String membershipType = data[6];
                String status = data[7];

                Membership membership = null;

                if (membershipType.equalsIgnoreCase("Basic")) {
                    membership = new BasicMembership(10);
                } else if (membershipType.equalsIgnoreCase("Silver")) {
                    membership = new SilverMembership(true);
                } else if (membershipType.equalsIgnoreCase("Gold")) {
                    membership = new GoldMembership(true, true);
                }

                Member member = new Member(id, password, name, age, height, weight, membership, status);
                members.add(member);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading members file.");
        }
        return members;
    }

    // ==================== Bookings ====================
    
    public void saveBookings(ArrayList<Booking> bookings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("bookings.txt"))) {
            for (Booking b : bookings) {
                writer.println(b.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    public ArrayList<Booking> loadBookings(ArrayList<Member> members, ArrayList<FitnessClass> classes) {
        ArrayList<Booking> bookings = new ArrayList<>();
        File file = new File("bookings.txt");
        if (!file.exists()) return bookings;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length == 6) {
                    String bId = parts[0];
                    String mId = parts[1];
                    String cId = parts[2];
                    LocalDate sDate = LocalDate.parse(parts[3]);
                    String status = parts[4];
                    String tier = parts[5];

                    Member foundMember = null;
                    for (Member m : members) {
                        if (m.getMemberID().equals(mId)) {
                            foundMember = m;
                            break;
                        }
                    }

                    FitnessClass foundClass = null;
                    for (FitnessClass c : classes) {
                        if (c.getClassId().equals(cId)) {
                            foundClass = c;
                            break;
                        }
                    }

                    if (foundMember != null && foundClass != null) {
                        Booking b = new Booking(bId, foundMember, foundClass, sDate, tier);
                        b.setStatus(status);
                        if (status.equalsIgnoreCase("Confirmed")) {
                            foundClass.addMember(foundMember);
                        }
                        bookings.add(b);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
        return bookings;
    }

    // ==================== Fitness Classes ====================
    
    public void addFitnessClass(FitnessClass fitnessClass) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fitnessclasses.txt", true))) {
            String instructorId = (fitnessClass.getInstructor() != null) ? fitnessClass.getInstructor().getUserId() : "TBA";
            String dateStr = (fitnessClass.getDate() != null) ? fitnessClass.getDate().toString() : "TBA";

            writer.write(fitnessClass.getClassId() + "|" +
                        fitnessClass.getClassName() + "|" +
                        dateStr + "|" +
                        fitnessClass.getTimeSlot() + "|" +
                        instructorId + "|" +
                        fitnessClass.getCurrentEnrollment() + "|" +
                        fitnessClass.getMaxCapacity());
            writer.newLine();
            System.out.println("Fitness class added: " + fitnessClass.getClassName());
        } catch (IOException e) {
            System.out.println("Error saving fitness class data.");
        }
    }

    public void updateFitnessClasses(ArrayList<FitnessClass> fitnessClasses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fitnessclasses.txt"))) {
            for (FitnessClass fc : fitnessClasses) {
                String instructorId = (fc.getInstructor() != null) ? fc.getInstructor().getUserId() : "TBA";
                String dateStr = (fc.getDate() != null) ? fc.getDate().toString() : "TBA";

                writer.write(fc.getClassId() + "|" +
                            fc.getClassName() + "|" +
                            dateStr + "|" +
                            fc.getTimeSlot() + "|" +
                            instructorId + "|" +
                            fc.getCurrentEnrollment() + "|" +
                            fc.getMaxCapacity());
                writer.newLine();
            }
            System.out.println("Fitness classes updated successfully.");
        } catch (IOException e) {
            System.out.println("Error updating fitness classes data.");
        }
    }

    public ArrayList<FitnessClass> loadFitnessClasses(ArrayList<Trainer> trainers) {
        ArrayList<FitnessClass> fitnessClasses = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            File file = new File("fitnessclasses.txt");
            if (!file.exists()) {
                System.out.println("Fitness classes file not found: fitnessclasses.txt");
                return fitnessClasses;
            }

            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split("\\|");
                if (data.length >= 7) {
                    String classId = data[0];
                    String className = data[1];
                    LocalDate date = data[2].equals("TBA") ? null : LocalDate.parse(data[2], formatter);
                    String timeSlot = data[3];
                    String instructorId = data[4];
                    int currentEnrollment = Integer.parseInt(data[5]);
                    int maxCapacity = Integer.parseInt(data[6]);

                    Trainer instructor = null;
                    if (!instructorId.equals("TBA")) {
                        for (Trainer t : trainers) {
                            if (t.getUserId().equals(instructorId)) {
                                instructor = t;
                                break;
                            }
                        }
                    }

                    FitnessClass fc = new FitnessClass(classId, className, maxCapacity);
                    fc.setSchedule(date, timeSlot);
                    fc.setInstructor(instructor);
                    fc.setCurrentEnrollment(currentEnrollment);
                    fitnessClasses.add(fc);
                } else {
                    System.out.println("Skipping invalid fitness class entry: " + line);
                }
            }
            reader.close();
            System.out.println("Loaded " + fitnessClasses.size() + " fitness classes.");
        } catch (Exception e) {
            System.out.println("Error loading fitness classes file: " + e.getMessage());
        }
        return fitnessClasses;
    }

    // ==================== Trainers ====================
    
    public void saveTrainer(Trainer trainer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("trainers.txt", true))) {
            writer.write(trainer.getUserId() + "|" + trainer.getPassword() + "|" + trainer.getName());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving trainer data.");
        }
    }

    public void updateTrainers(ArrayList<Trainer> trainers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("trainers.txt"))) {
            for (Trainer trainer : trainers) {
                writer.write(trainer.getUserId() + "|" + trainer.getPassword() + "|" + trainer.getName());
                writer.newLine();
            }
            System.out.println("Trainer data updated successfully.");
        } catch (IOException e) {
            System.out.println("Error updating trainer data.");
        }
    }

    public ArrayList<Trainer> loadTrainers() {
        ArrayList<Trainer> trainers = new ArrayList<>();
        try {
            File file = new File("trainers.txt");
            if (!file.exists()) {
                System.out.println("Trainers file not found: trainers.txt");
                return trainers;
            }
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split("\\|");
                if (data.length >= 3) {
                    String id = data[0];
                    String password = data[1];
                    String name = data[2];
                    Trainer trainer = new Trainer(id, password, name);
                    trainers.add(trainer);
                } else {
                    System.out.println("Skipping invalid trainer entry: " + line);
                }
            }
            reader.close();
            System.out.println("Loaded " + trainers.size() + " trainers.");
        } catch (Exception e) {
            System.out.println("Error loading trainers file.");
        }
        return trainers;
    }

    // ==================== Equipment ====================
    
    public ArrayList<Equipment> loadEquipments() {
        ArrayList<Equipment> equipments = new ArrayList<>();
        try {
            File file = new File("equipments.txt");
            if (!file.exists()) {
                System.out.println("Equipment file not found: equipments.txt");
                return equipments;
            }
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split("\\|");
                if (data.length == 3) {
                    String id = data[0];
                    String name = data[1];
                    String status = data[2];
                    Equipment equipment = new Equipment(id, name, status);
                    equipments.add(equipment);
                } else {
                    System.out.println("Skipping invalid equipment entry: " + line);
                }
            }
            reader.close();
            System.out.println("Loaded " + equipments.size() + " equipment items.");
        } catch (Exception e) {
            System.out.println("Error loading equipments file: " + e.getMessage());
        }
        return equipments;
    }

    public void updateEquipments(ArrayList<Equipment> equipments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("equipments.txt"))) {
            for (Equipment e : equipments) {
                writer.write(e.toFileString());
                writer.newLine();
            }
            System.out.println("Equipment data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving equipment data: " + e.getMessage());
        }
    }

    public void addEquipment(Equipment equipment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("equipments.txt", true))) {
            writer.write(equipment.toFileString());
            writer.newLine();
            System.out.println("Equipment added: " + equipment.getName());
        } catch (IOException e) {
            System.out.println("Error adding equipment: " + e.getMessage());
        }
    }

    // ==================== Admins ====================
    
    public ArrayList<Admin> loadAdmins() {
        ArrayList<Admin> admins = new ArrayList<>();
        try {
            File file = new File("admins.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.isEmpty()) continue;

                String[] data = line.split("\\|");
                String id = data[0].trim();
                String password = data[1].trim();
                Admin admin = new Admin(id, password);
                admins.add(admin);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading admin file.");
        }
        return admins;
    }

    // ==================== Save All ====================
    
    public void saveAll(ArrayList<Member> members,
                    ArrayList<Trainer> trainers,
                    ArrayList<Equipment> equipments,
                    ArrayList<FitnessClass> classes,
                    ArrayList<Booking> bookings) {
        try {
            System.out.println("Saving all data to files...");
            saveAllMembers(members);
            updateTrainers(trainers);
            updateEquipments(equipments);
            updateFitnessClasses(classes);
            saveBookings(bookings);
            System.out.println("All data saved successfully!");
        } catch (Exception e) {
            System.out.println("Error saving all data: " + e.getMessage());
        }
    }

}