package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Hospital Manager application
// This code references the AccountNotRobust and JSONSerialization demo projects given by the course.
public class HospitalManager {
    private static final String MEDICAL_RECORD_STORE = "./data/medicalrecords.json";
    private static final String PATIENT_LIST_STORE = "./data/patients.json";
    private static final String APPOINTMENT_STORE = "./data/appointments.json";

    private JsonWriter medicalRecordWriter;
    private JsonReader medicalRecordReader;

    private JsonWriter patientListWriter;
    private JsonReader patientListReader;

    private JsonWriter appointmentWriter;
    private JsonReader appointmentReader;

    private Scanner input;

    // lists that need to be initialized
    private PatientList pl;
    private MedicalRecordList ml;
    private InquiryList il;
    private DoctorList dl;
    private AppointmentList al;

    // run the application
    // EFFECTS: runs the hospital management application, if username and password match those given by the system
    public HospitalManager() throws FileNotFoundException {

        // initializes JSON writer and JSON reader
        medicalRecordWriter = new JsonWriter(MEDICAL_RECORD_STORE);
        medicalRecordReader = new JsonReader(MEDICAL_RECORD_STORE);

        patientListWriter = new JsonWriter(PATIENT_LIST_STORE);
        patientListReader = new JsonReader(PATIENT_LIST_STORE);

        appointmentWriter = new JsonWriter(APPOINTMENT_STORE);
        appointmentReader = new JsonReader(APPOINTMENT_STORE);

        runHospitalManager();
    }

    // command-line interface
    // MODIFIES: this
    // EFFECTS: processes user input
    public void runHospitalManager() {
        boolean keepGoing = true;
        String command;

        // initialize objects used by the program
        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        // thank you message :)
        System.out.println("\nThank you for using MyHospitalManager!");
    }

    // initialization of variables
    // MODIFIES: this
    // EFFECTS: initializes list of patients and medical records, along with scanner
    public void init() {
        pl = new PatientList();
        ml = new MedicalRecordList();
        il = new InquiryList();
        dl = new DoctorList();
        al = new AppointmentList();
        il.initInquiries();
        dl.initDoctors();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // show options
    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\n");
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> check in patient");
        System.out.println("\t2 -> check out patient");
        System.out.println("\t3 -> show all patients");
        System.out.println("\t4 -> make new medical record");
        System.out.println("\t5 -> show all medical records");
        System.out.println("\t6 -> book an appointment");
        System.out.println("\t7 -> remove an appointment");
        System.out.println("\t8 -> show all appointments");
        System.out.println("\t9 -> show all doctors");
        System.out.println("\t10 -> show all inquiries");
        // This saves all currently checked-in patients, medical records, and appointments to file
        System.out.println("\t11 -> save state to file");
        // This loads all previously checked-in patients, medical records, and appointments to file
        System.out.println("\t12 -> load state from file");
        System.out.println("\tq -> quit");
    }

    // Checkstyle was suppressed here because there was no better way of implementing all features of the program
    // process options
    // MODIFIES: this
    // EFFECTS: processes user command
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void processCommand(String command) {
        switch (command) {
            case "1":
                // check in a patient
                checkInPatient();
                break;
            case "2":
                // check out a patient
                checkOutPatient();
                break;
            case "3":
                // show all patients
                showAllPatients();
                break;
            case "4":
                // make a new medical record
                makeMedicalRecord();
                break;
            case "5":
                // show all medical records
                showMedicalRecords();
                break;
            case "6":
                // book an appointment for a patient
                bookAppointment();
                break;
            case "7":
                // remove an appointment from the list of booked appointments
                removeAppointment();
                break;
            case "8":
                // show all booked appointments
                showAllAppointments();
                break;
            case "9":
                // show all doctors
                showDoctors();
                break;
            case "10":
                // show all inquiries
                showInquiries();
                break;
            case "11":
                // save medical records to file
                saveState();
                break;
            case "12":
                // load medical records from file
                loadState();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // check in a patient
    // MODIFIES: this, patients
    // EFFECTS: creates a Patient p with user input, then adds it to list of patients
    private void checkInPatient() {
        Scanner myScanner = new Scanner(System.in);

        System.out.println("Please enter the name of the patient you wish to check in.");
        String tempName = myScanner.nextLine();

        System.out.println("Please enter the ID number of the patient you wish to check in.");
        int tempId = Integer.parseInt(myScanner.nextLine());

        // initializes the patient
        Patient p = new Patient(tempName, tempId);
        p.setId(tempId);

        // adds patient to list of patients
        pl.getPatientList().add(p);
        System.out.println("Successful!");
    }

    // check out a patient
    // REQUIRES: patients must have no duplicate names
    // MODIFIES: patients
    // EFFECTS: removes patient with given name from patients
    private void checkOutPatient() {
        Scanner myScanner = new Scanner(System.in);

        System.out.println("Please enter the name of the patient you wish to check out.");
        String toFind = myScanner.nextLine();

        for (Patient p : pl.getPatientList()) {
            if (p.getName().equals(toFind)) {
                pl.getPatientList().remove(p);
                System.out.println("Successful!");
                break;
            }
        }
    }

    // show all patients
    // EFFECTS: prints all patients currently checked in to the console
    private void showAllPatients() {
        for (Patient p : pl.getPatientList()) {
            System.out.println(p.getName() + " , " + p.getId() + "\n");
        }
    }

    // make new medical record
    // MODIFIES: this, medicalRecords
    // creates new medical record m from user input and adds it to list of medical records
    private void makeMedicalRecord() {
        Scanner myScanner = new Scanner(System.in);

        System.out.println("Please enter the patient's name.");
        String tempName = myScanner.nextLine();

        System.out.println("Please enter the patient's age.");
        int tempAge = Integer.parseInt(myScanner.nextLine());

        System.out.println("Please enter the patient's height.");
        int tempHeight = Integer.parseInt(myScanner.nextLine());

        System.out.println("Please enter the patient's weight.");
        int tempWeight = Integer.parseInt(myScanner.nextLine());

        System.out.println("Please enter the patient's blood type.");
        String tempBloodType = myScanner.nextLine();

        // constructs a new MedicalRecord object using values obtained from user input
        MedicalRecord m = new MedicalRecord(tempName, tempAge, tempHeight, tempWeight, tempBloodType);

        // adds medical record to list of medical records
        ml.getMedicalRecordList().add(m);
        System.out.println("Successful!");
    }

    // show medical records
    // EFFECTS: prints all medical records to console
    private void showMedicalRecords() {
        for (MedicalRecord m : ml.getMedicalRecordList()) {
            System.out.println("Name: " + m.getName());
            System.out.println("Age: " + m.getAge());
            System.out.println("Height: " + m.getHeight());
            System.out.println("Weight: " + m.getWeight());
            System.out.println("Blood Type: " + m.getBloodType());
            System.out.println("\n");
        }
    }

    // book an appointment
    // MODIFIES: this, appointments
    // EFFECTS: creates new appointment a from user input and adds it to list of appointments
    private void bookAppointment() {
        Scanner myScanner = new Scanner(System.in);

        System.out.println("Please enter the name of the patient you wish to book.");
        String tempName = myScanner.nextLine();

        System.out.println("Please enter the time you wish to have your appointment. [000: am/pm]");
        String tempTime = myScanner.nextLine();

        // constructs new Appointment from user input
        Appointment a = new Appointment(tempName, tempTime);

        // adds appointment to list of booked appointments
        al.getAppointmentList().add(a);
        System.out.println("Successful!");
    }

    // show all appointments
    // EFFECTS: prints all appointments to console
    private void showAllAppointments() {
        for (Appointment a : al.getAppointmentList()) {
            System.out.println(a.getName());
            System.out.println(a.getTime());
        }
    }

    // remove an appointment
    // REQUIRES: appointments must have no duplicate names
    // MODIFIES: appointments
    // EFFECTS: removes appointment with given name from appointment
    private void removeAppointment() {
        Scanner myScanner = new Scanner(System.in);

        System.out.println("Please enter the name of the appointment you wish to remove.");
        String toFind = myScanner.nextLine();

        for (Appointment a : al.getAppointmentList()) {
            if (a.getName().equals(toFind)) {
                al.getAppointmentList().remove(a);
                System.out.println("Successful!");
                break;
            }
        }
    }

    // show doctors
    // EFFECTS: prints all doctors to the console
    private void showDoctors() {
        for (Doctor d : dl.getDoctorList()) {
            System.out.println("Dr. " + d.getDoctorName() + ", " + d.getDepartment());
        }
    }

    // show inquiries
    // EFFECTS: prints all inquiries to the console
    private void showInquiries() {
        // Variable j starts indexing at 1, and keeps count of which entry was entered first
        int j = 0;

        for (Inquiry i : il.getInquiryList()) {
            // Variable j starts indexing at 1, and keeps count of which entry was entered first
            j += 1;
            System.out.println(j);
            System.out.println("Subject: " + i.getSubject());
            System.out.println("Date: " + i.getDate());
            System.out.println("Additional Remarks: ");
            System.out.println(i.getRemarks());
            System.out.println("\n");
        }
    }

    // EFFECTS: saves the current state of program to file
    private void saveState() {
        try {
            medicalRecordWriter.open();
            medicalRecordWriter.writeMedicalRecordList(ml);
            medicalRecordWriter.close();
            System.out.println("Saved medical records to " + MEDICAL_RECORD_STORE);

            patientListWriter.open();
            patientListWriter.writePatientList(pl);
            patientListWriter.close();
            System.out.println("Saved list of patients to " + PATIENT_LIST_STORE);

            appointmentWriter.open();
            appointmentWriter.writeAppointmentList(al);
            appointmentWriter.close();
            System.out.println("Saved list of appointments to " + APPOINTMENT_STORE);

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads previous state of program from file
    private void loadState() {
        try {
            ml = medicalRecordReader.readMedicalRecordList();
            System.out.println("Loaded medical records from " + MEDICAL_RECORD_STORE);

            pl = patientListReader.readPatientList();
            System.out.println("Loaded patient list from " + PATIENT_LIST_STORE);

            al = appointmentReader.readAppointmentList();
            System.out.println("Loaded appointment list from " + APPOINTMENT_STORE);

        } catch (IOException e) {
            System.out.println("File not found!");
        }
    }
}

