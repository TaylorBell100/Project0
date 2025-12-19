package org.example;


import org.example.controller.PokedexController;
import org.example.controller.TrainerController;
import org.example.util.InputHandler;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        PokedexController pController = new PokedexController();
        TrainerController tController = new TrainerController();
        System.out.println("===============================");
        System.out.println("=== NATIONAL POKEDEX SYSTEM ===");
        System.out.println("===============================");
        System.out.println("");

        boolean running = true;
        while(running){
            printMenu();
            int choice = InputHandler.getIntInput("Make a choice: ");
            switch(choice){
                case 1 -> pController.handleInput0();
                case 2 -> tController.handleInput();
                case 0 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }//case0
                default -> {
                    System.out.println("Not a vail entry.");
                }
            }//switch
        }//while running
    }//main method

    private static void printMenu(){
        System.out.println("=================");
        System.out.println("=== MAIN MENU ===");
        System.out.println("1. Pokedex Services");
        System.out.println("2. Trainer Services");
        System.out.println("0. Exit");
    }// print menu
}// main class