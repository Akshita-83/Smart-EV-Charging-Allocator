# Smart EV Charging Allocator

## Project Overview

Smart EV Charging Allocator is a C-based simulation project that demonstrates how electric vehicles can be assigned to charging stations using different scheduling strategies.

The project compares scheduling methods and shows how data structures and algorithms can help manage waiting vehicles, charging priorities, and limited charging resources.

## Objectives

- Simulate an EV charging environment with limited chargers.
- Compare different vehicle scheduling strategies.
- Demonstrate the use of data structures and algorithms in a practical problem.
- Compare results using waiting time, vehicles served, lost vehicles, and charger utilization.

## Features

- Simulates vehicle arrivals and charging durations.
- Uses two chargers in the configured simulation.
- Implements FIFO, Shortest Charge First, Lowest Battery First, and Emergency + Aging strategies.
- Applies a maximum waiting patience of 10 simulated time units.
- Treats charging as non-preemptive, meaning an ongoing charging session is not interrupted.
- Displays and compares the results of the scheduling strategies.

## Technologies Used

- **Programming Language:** C
- **Compiler:** GCC
- **Concepts:** Data Structures and Algorithms

## Data Structures and Algorithms

The project uses the following concepts:

- **Queue:** Helps manage waiting vehicles.
- **Heap / Priority Queue:** Supports priority-based selection.
- **Sorting and Comparisons:** Used to compare vehicle scheduling strategies.
- **Searching:** Used to locate vehicle information.
- **Greedy Scheduling:** Selects vehicles according to the rules of a scheduling strategy.
- **Time Complexity Analysis:** Used to discuss the efficiency of individual operations.

## Scheduling Strategies

1. **FIFO (First In, First Out):** Vehicles are considered in arrival order.
2. **Shortest Charge First:** Gives preference to vehicles with shorter estimated charging durations.
3. **Lowest Battery First:** Gives preference to vehicles with lower current battery percentages.
4. **Emergency + Aging:** Gives priority to emergency vehicles while considering arrival order and vehicle ID for tie-breaking.

## Project Structure

- `main.c` – Main program and execution flow.
- `data.c` – Vehicle data and related operations.
- `queue.c` – Queue-related operations.
- `heap.c` – Heap-related operations.
- `simulation.c` – Charging simulation and strategy comparison.

## How to Run

### Prerequisites

Install GCC and make sure it is available in your terminal.

### Steps

1. Clone or download this repository.
2. Open the project folder in a terminal.
3. Compile the C files:

   ```bash
   gcc -Wall -Wextra *.c -o main.exe
   ```

4. Run the program on Windows:

   ```powershell
   .\main.exe
   ```

   On Linux, compile with `-o main` and run using `./main`.

## Simulation Parameters

- Number of chargers: 2
- Maximum waiting patience: 10 simulated time units
- Charging mode: Non-preemptive
- Charging duration is estimated using the configured battery percentage, charging power, and scaling factor.

The duration formula used is:

`Duration = (Target Battery % - Current Battery %) × k / Power`

For the current configuration, `k = 10` and `Power = 50`.

## Results

The program compares the scheduling strategies using:

- Vehicles served
- Vehicles lost
- Average waiting time
- Maximum waiting time
- Charger utilization

The results depend on the configured simulation scenario. Run the program to view the comparison produced by the current configuration.

## Learning Outcomes

This project provides practical experience with queues, heaps, searching, sorting, scheduling logic, simulation, and time complexity analysis using C.

## Repository

GitHub: https://github.com/aayusimishra1-cmd/Smart_EV_Charging_Allocator
