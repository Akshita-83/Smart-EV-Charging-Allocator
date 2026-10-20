Smart EV Charging Allocator
Project Overview

Smart EV Charging Allocator is a Java-based simulation project that demonstrates how electric vehicles (EVs) can be assigned to charging stations using different scheduling strategies.

The project uses Object-Oriented Programming (OOP), queues, heaps, and event-driven simulation to manage vehicle arrivals, charging priorities, waiting times, and limited charging resources.

Objectives
Simulate an EV charging environment with limited charging stations.
Implement and compare different vehicle scheduling strategies.
Apply OOP principles to organize vehicles, chargers, queues, and priority heaps.
Analyze performance using waiting time, vehicles served, vehicles lost, and charger utilization.
Features
Simulates vehicle arrivals and charging durations.
Supports two charging stations.
Implements FIFO, Shortest Charge First, Lowest Battery First, and Emergency + Aging strategies.
Applies a maximum waiting patience of 10 simulated time units.
Models charging as non-preemptive: an ongoing charging session is not interrupted.
Displays simulation results and compares scheduling strategies.
Uses a priority heap to select vehicles according to scheduling rules.
Technologies Used
Programming Language: Java
JDK: Java Development Kit
Concepts: Object-Oriented Programming, Data Structures and Algorithms
OOP Concepts Used
Classes and Objects: Represent vehicles, chargers, waiting queues, and priority heaps.
Encapsulation: Groups related data and operations within classes.
Constructors: Initialize vehicle and charger objects.
Methods: Implement charging calculations, queue operations, heap operations, and simulations.
Abstraction: Separates simulation operations into methods and classes.
Data Structures and Algorithms
Queue: Manages waiting vehicles in arrival order.
Min-Heap / Priority Queue: Selects vehicles based on scheduling priorities.
Searching: Locates vehicles using their IDs.
Sorting: Sorts vehicle records by ID.
Greedy Scheduling: Selects vehicles according to the active scheduling strategy.
Event-Driven Simulation: Advances simulated time to the next vehicle arrival or charging completion.
Scheduling Strategies
FIFO (First In, First Out): Vehicles are considered in arrival order.
Shortest Charge First: Prioritizes vehicles with shorter estimated charging durations.
Lowest Battery First: Prioritizes vehicles with lower current battery percentages.
Emergency + Aging: Prioritizes emergency vehicles and then considers waiting time, with vehicle ID used for tie-breaking.
Project Structure

The project uses Java classes to organize the simulation:

EVChargingSimulation.java – Main simulation logic and strategy comparison.
Vehicle.java – Represents an electric vehicle and its charging information.
Charger.java – Represents a charging station and its availability.
VehicleQueue.java – Implements FIFO queue operations.
VehicleHeap.java – Implements priority-based vehicle selection.
README.md – Project documentation.
index.html, style.css, script.js – Web interface files, if used by the project.

The exact file responsibilities may vary depending on the implementation.

Prerequisites
Java Development Kit (JDK) installed.
A terminal such as PowerShell, Command Prompt, or a Unix shell.
Git, if you want to clone the repository.

Check your Java installation:

java -version
javac -version

How to Run
Clone the repository or download the project.
Open a terminal in the directory containing the Java source files.
Compile the Java files:
javac *.java

Run the main class:
java EVChargingSimulation


If your project uses a separate Main.java containing the main() method, run java Main instead.

Simulation Parameters
Number of vehicles: 5
Number of chargers: 2
Charger power: 50 (configured power units)
Maximum waiting patience: 10 simulated time units
Charging mode: Non-preemptive
Charging duration: Estimated using battery percentage, charging power, and a scaling factor.

The charging duration formula is:

Duration = (Target Battery % - Current Battery %) × k / Power

For the current configuration, k = 10 and Power = 50.

Performance Metrics

The simulation compares scheduling strategies using:

Vehicles served
Vehicles lost after exceeding the patience limit
Average waiting time
Maximum waiting time
Charger utilization

Results depend on the configured vehicle arrivals, charging durations, and scheduling strategy.

Learning Outcomes

This project provides practical experience with:

Object-Oriented Programming in Java
Classes, objects, constructors, and methods
Queue and heap implementation
Searching and sorting algorithms
Scheduling strategies and event-driven simulation
Performance analysis of competing algorithms
Repository

GitHub: https://github.com/Akshita-83/Smart-EV-Charging-Allocator