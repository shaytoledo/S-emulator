package semulator.console;

import semulator.engine.adapter.translate.Engine;
import semulator.engine.adapter.translate.EngineImpl;
import semulator.engine.logic.execution.ProgramExecutorImpl;

import java.nio.file.Path;
import java.util.Scanner;

public class ConsoleUI {

    ProgramExecutorImpl executor;
    private static final Scanner in = new Scanner(System.in);
    private static final Engine engine = new EngineImpl();

    public void run() {
        while (true) {
            printMenu();
            String choice = in.nextLine().trim();
            switch (choice) {
                case "1" -> loadXml();
                case "2" -> showProgram();
                case "3" -> expand();
                case "4" -> runOnce();
                case "5" -> history();
                case "6" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please choose 1-6.");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("""
                1) Load XML
                2) Show Program
                3) Expand
                4) Run
                5) History
                6) Exit
                """);
        System.out.print("Choose an option: ");
    }

    private static void loadXml() {
        System.out.print("Enter XML path: ");
        String path = in.nextLine().trim();
        try {
            Engine.LoadReport r = engine.loadProgram(Path.of(path));
            if (r.ok()) System.out.println("Program loaded successfully.");
            else {
                System.out.println("Failed to load program:");
                r.errors().forEach(err -> System.out.println(" - " + err));
            }
        } catch (Exception e) {
            System.out.println("Failed to load program: " + e.getMessage());
        }
    }

    private static void showProgram() { }

    private static void expand() { }

    private static void runOnce() { }

    private static void history() { }
}
