import copy
import os
import random
import time
import psutil
import heapq

class GreedyBestFirstSearch:
    def __init__(self, puzzle_folder='data', solution_folder='solutions'):
        self.puzzle_folder = puzzle_folder
        self.solution_folder = os.path.join(os.path.dirname(__file__), '..', solution_folder)
        self.puzzles = []
        self.solution_count = 0

    def read_sudoku_file(self):
        folder_path = 'data'   
        boards = []
         

        try:
            file_names = [file for file in os.listdir(folder_path) if file.endswith('.txt')]
            
            # Sort file names numerically, assuming filenames like 'puzzle1.txt', 'puzzle2.txt', etc.
            file_names.sort(key=lambda x: int(x.split('puzzle')[1].split('.txt')[0]))

            for file_name in file_names:
                file_path = os.path.join(folder_path, file_name)
                with open(file_path, 'r') as file:
                    lines = file.readlines()
                    if not lines:
                        print(f"Skipping empty file: {file_name}")
                        continue

                    board = []
                    for line in lines:
                        line = line.strip()
                        if line and all(char.isdigit() for char in line):
                            board.append([int(num) for num in line])

                    if len(board) == 9 and all(len(row) == 9 for row in board):
                        boards.append(board)
                        print(f"Added board from {file_name}")
                    else:
                        print(f"Skipping malformed board from {file_name}")
        except FileNotFoundError:
            print(f"Error: The folder {folder_path} was not found.")
        
        print(f"Loaded {len(boards)} puzzles.")
        self.puzzles = boards

 


    def validate_board(self,board):
        for i in range(9):
            if len(set(board[i]) - {0}) != len([num for num in board[i] if num != 0]):
                raise ValueError(f"Conflict found in row {i + 1}")
            col = [board[j][i] for j in range(9)]
            if len(set(col) - {0}) != len([num for num in col if num != 0]):
                raise ValueError(f"Conflict found in column {i + 1}")
        for box_row in range(0, 9, 3):
            for box_col in range(0, 9, 3):
                subgrid = []
                for i in range(3):
                    for j in range(3):
                        subgrid.append(board[box_row + i][box_col + j])
                if len(set(subgrid) - {0}) != len([num for num in subgrid if num != 0]):
                    raise ValueError(f"Conflict in 3x3 subgrid at ({box_row + 1}, {box_col + 1})")
        return True

    def heuristic(self,board):
        return sum(row.count(0) for row in board)  # Fewer empty cells = better

    def find_empty(self,board):
        for i in range(9):
            for j in range(9):
                if board[i][j] == 0:
                    return i, j
        return None

    def get_valid_numbers(self,board, row, col):
        nums = set(range(1, 10))  #all numbers 1-9 are possible candidates for the cell.
        nums -= set(board[row])   #Remove numbers that already appear in the same row
        nums -= {board[i][col] for i in range(9)}  #Remove numbers that already appear in the same column.

        #Remove numbers already present in the 3x3 subgrid from nums.
        box_row, box_col = 3 * (row // 3), 3 * (col // 3)  #finds which 3x3 box the cell belongs to.
        nums -= {
            board[i][j]
            for i in range(box_row, box_row + 3)
            for j in range(box_col, box_col + 3)
        }
        return list(nums)

    # Greedy Best-First Search will:

    # Pick the board with the lowest heuristic value (i.e., closest to being solved)

    # Try the next empty cell

    # Repeat the process

    # Summary:

    # For each valid number:

    # Try it on a copy of the board

    # Evaluate how good the new board is

    # Put it in priority queue (frontier) to try later
    def solve(self,start_board):
        frontier = []
        visited = set()
        heapq.heappush(frontier, (self.heuristic(start_board), start_board)) #adds an item into a priority queue (min-heap), while maintaining the heap order   reordered them based on priority (smallest heuristic value = highest priority).Put smallest heuristic value in frontier first element and gradually increase
        iterations = 0
         
        while frontier:
            #_ → ignored (priority value)

            # current → the board state you're going to work on
            _, current = heapq.heappop(frontier)
            iterations += 1

            board_key = tuple(tuple(row) for row in current)
            if board_key in visited:
                continue
            visited.add(board_key)

             

            if self.heuristic(current) == 0:
                 return current, 0, iterations  # Solved

            empty = self.find_empty(current)  #find next one
            if not empty:
                continue   

            row, col = empty
            for num in self.get_valid_numbers(current, row, col):  # if num is empty list,The current board state is a dead end
                #all possible candidate number in the board may find others 0 that have only one candidate
                new_board = copy.deepcopy(current)  #Creates a completely independent copy of the current Sudoku board.
                new_board[row][col] = num
                h = self.heuristic(new_board)
                heapq.heappush(frontier, (h, new_board))

        return current, self.heuristic(current), iterations  # Unsolved

    def save_solution(self,solution, puzzle_index):
        folder_path = os.path.join(os.path.dirname(__file__), 'solutions')
        
         
        try:
            os.makedirs(folder_path, exist_ok=True)
        except Exception as e:
             
            return   
        
        file_path = os.path.join(folder_path, f"solution_{puzzle_index + 1}.txt")
        
         
        
        try:
            with open(file_path, 'w') as file:
                for row in solution:
                    file.write(''.join(map(str, row)) + '\n')
             
        except Exception as e:
            print(f"Error writing to file {file_path}: {e}")

    def solve_all(self):
    
        self.read_sudoku_file()
        if not self.puzzles:
            print("No puzzles to solve.")
            return

        start_time = time.time()
        process = psutil.Process()
        start_memory = process.memory_info().rss

        for i, puzzle in enumerate(self.puzzles):
            print(f"\nValidating Puzzle {i + 1}...")
            try:
                self.validate_board(puzzle)
            except ValueError as e:
                print(f"Puzzle {i + 1} is invalid: {e}\nSkipping...")
                continue
            initial_h = self.heuristic(puzzle)
            print(f"Solving puzzle {i + 1} using GBFS...")
            print(f"Initial h(n): {initial_h} empty cells")

            puzzle_start = time.time()
            solution, h, iterations = self.solve(puzzle)
            puzzle_end = time.time()

            if h == 0:
                memory_used = (process.memory_info().rss - start_memory) / (1024 * 1024)
                duration = puzzle_end - puzzle_start
                print(f"Solution found in {iterations} iterations.")
                print(f"Time: {duration:.3f}s | Memory: {memory_used:.4f} MB")
                for row in solution:
                    print(row)
                self.save_solution(solution, i)
                self.solution_count += 1
            else:
                print(f"No solution found for puzzle {i + 1}.")

        total_time = time.time() - start_time
        total_memory = (process.memory_info().rss - start_memory) / (1024 * 1024)

        print(f"\nTotal Time: {total_time:.2f} seconds")
        print(f"Total Memory Used: {total_memory:.3f} MB")
        print(f"Total Solutions Found: {self.solution_count}")
        if self.solution_count == 0:
            print("No solutions found.")
        elif self.solution_count == len(self.puzzles):
            print("All puzzles solved!")


if __name__ == "__main__":
    GBFS = GreedyBestFirstSearch()
    GBFS.solve_all()