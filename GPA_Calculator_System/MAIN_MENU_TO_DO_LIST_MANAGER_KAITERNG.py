# --------------------------------------------------To-do List Manager--------------------------------------------------------------------------------

# GUI Setup for To-Do List Manager
def run_todo_manager():
    global root, tasks, canvas_frame, title_entry, description_entry, deadline_var  # Declare global variables

    import tkinter as tk  #
    # Import tkinter for GUI components
    from tkinter import ttk, messagebox  # Import ttk for styled widgets and messagebox for alerts
    import json  # Import json to handle tasks data in JSON format
    import os  # Import os for file and directory management

    # File path to store tasks data
    TASKS_FILE_PATH = r"C:\DCS1S2G2-BehKokWeiLohKaiTerngTanChiCai\tasks.txt"  # File where tasks are saved

    # Ensure the directory for the task file exists
    os.makedirs(os.path.dirname(TASKS_FILE_PATH), exist_ok=True)  # Create the directory if it doesn't exist

    # Function to load tasks from the file
    def load_tasks():
        print("Loading tasks from file...")  # Print message to show the loading process
        if os.path.exists(TASKS_FILE_PATH):  # Check if the file exists
            try:
                with open(TASKS_FILE_PATH, 'r') as file:  # Open the file in read mode
                    tasks = json.load(file)  # Load the task list from the JSON file
                    print(f"Loaded tasks: {tasks}")  # Print loaded tasks
                    return tasks  # Return the list of tasks
            except json.JSONDecodeError:  # If there's an error in JSON formatting
                print("Error decoding JSON. Returning empty task list.")  # Handle JSON error
                return []  # Return an empty list if file content is corrupted
        print("File doesn't exist, returning empty task list.")  # If file is missing, return empty list
        return []  # Return an empty list if the file doesn't exist

    # Function to save tasks to the file
    def save_tasks():
        print("Saving tasks to file...")  # Print message to indicate saving tasks
        with open(TASKS_FILE_PATH, 'w') as file:  # Open the file in write mode
            json.dump(tasks, file)  # Write the task list to the file in JSON format
        print(f"Tasks saved: {tasks}")  # Print saved tasks

    # Function to create a new task
    def create_task():
        title = title_entry.get()  # Get the task title from the input field
        description = description_entry.get("1.0", tk.END).strip()  # Get the task description
        deadline = deadline_var.get()  # Get the deadline from the dropdown

        # Check if the title is empty
        if title.strip() == "":  # If the title is empty or just whitespace
            messagebox.showerror("Invalid Title", "The task title cannot be empty.")  # Show an error message
            return  # Exit the function if there's no title

        # Create the task dictionary
        task = {
            'title': title,
            'description': description,
            'deadline': deadline,
            'completed': False  # Initially, the task is not completed
        }

        tasks.append(task)  # Add the new task to the tasks list
        print("Task added:", task)  # Print the task that was added

        title_entry.delete(0, tk.END)  # Clear the title input field
        description_entry.delete("1.0", tk.END)  # Clear the description input field

        save_tasks()  # Save the updated task list
        refresh_task_list()  # Refresh the task list display

    # Function to toggle task completion
    def toggle_complete(task):
        task['completed'] = not task['completed']  # Toggle the task's completion status
        print(f"Task '{task['title']}' completed: {task['completed']}")  # Print updated completion status
        save_tasks()  # Save updated tasks list
        refresh_task_list()  # Refresh the task list display

    # Function to save changes made to a task (title validation included)
    def save_changes(task, title_edit_entry, description_edit_entry, deadline_edit_var, edit_window):
        title = title_edit_entry.get().strip()  # Get the title from the edit entry field

        # Check if the title is empty
        if title == "":  # If title is empty
            edit_window.withdraw()  # Temporarily hide the edit window
            messagebox.showerror("Invalid Title", "The task title cannot be empty.")  # Show an error message
            edit_window.deiconify()  # Reopen the edit window after dismissing the error
            return  # Exit the function if no valid title is provided

        task['title'] = title  # Update the task's title
        task['description'] = description_edit_entry.get("1.0", tk.END).strip()  # Update the description
        task['deadline'] = deadline_edit_var.get()  # Update the deadline

        print(f"Task '{task['title']}' updated.")  # Print the updated task title
        save_tasks()  # Save updated tasks list
        refresh_task_list()  # Refresh the task list display
        edit_window.destroy()  # Close the edit window
        root.deiconify()  # Show the main window again

    # Function to delete a task with the edit window temporarily hidden
    def delete_task(task, edit_window, delete_button):
        # Hide the edit window temporarily to prevent further actions
        edit_window.withdraw()  # Temporarily hide the edit window

        # Disable the delete button to prevent multiple presses
        if delete_button.winfo_exists():  # Check if the button still exists in the widget tree
            delete_button.config(state=tk.DISABLED)  # Disable the delete button immediately when clicked

        # Show confirmation dialog for deletion
        result = messagebox.askyesno("Confirm Deletion", "Are you sure you want to delete this task?")  # Ask for user confirmation

        if result:  # If the user confirms deletion
            tasks.remove(task)  # Remove the task from the task list
            print(f"Task '{task['title']}' deleted.")  # Print a message confirming deletion
            save_tasks()  # Save the updated tasks list
            refresh_task_list()  # Refresh the task list display
            edit_window.destroy()  # Close the edit window
            root.deiconify()  # Show the main window again
        else:
            print("Task deletion cancelled.")  # Print message if deletion is cancelled

        # Re-enable the delete button after the process
        if delete_button.winfo_exists():  # Ensure the button still exists before trying to re-enable it
            delete_button.config(state=tk.NORMAL)  # Re-enable the delete button

        edit_window.deiconify()  # Re-open the edit window after the process

    # Function to open the task editing window
    def open_edit_window(task):
        print(f"Opening edit window for task '{task['title']}'...")  # Print message indicating the edit window is opened
        root.withdraw()  # Hide the main window

        # Create a new top-level window for editing the task
        edit_window = tk.Toplevel(root)
        edit_window.title("Edit Task")  # Set the window title

        # Add labels and input fields for task title, description, and deadline
        tk.Label(edit_window, text="Task Title").pack()
        title_edit_entry = tk.Entry(edit_window, width=30)
        title_edit_entry.insert(0, task['title'])  # Pre-fill the title field with the current task title
        title_edit_entry.pack()

        tk.Label(edit_window, text="Description").pack()
        description_edit_entry = tk.Text(edit_window, height=4, width=30)
        description_edit_entry.insert("1.0", task['description'])  # Pre-fill the description field
        description_edit_entry.pack()

        tk.Label(edit_window, text="Select Deadline").pack()
        deadline_edit_var = tk.StringVar()  # Create a StringVar for deadline
        deadline_options = ['Today', 'Tomorrow', 'Next Week']  # List of deadline options
        deadline_edit_dropdown = ttk.Combobox(edit_window, textvariable=deadline_edit_var, values=deadline_options)
        deadline_edit_dropdown.set(task['deadline'])  # Set the current deadline as the selected option
        deadline_edit_dropdown.pack()

        # Create a frame to hold the Done and Delete buttons
        button_frame = tk.Frame(edit_window)
        button_frame.pack(pady=10)

        # Done button to save the task changes
        done_button = tk.Button(button_frame, text="Done", command=lambda: save_changes(task, title_edit_entry, description_edit_entry, deadline_edit_var, edit_window))
        done_button.pack(side=tk.LEFT, padx=5)

        # Create a Delete button with a reference to the button itself to disable it during confirmation
        delete_button = tk.Button(button_frame, text="Delete", command=lambda: delete_task(task, edit_window, delete_button))
        delete_button.pack(side=tk.LEFT, padx=5)

        # Back button with error handling for empty title
        def go_back():
            title = title_edit_entry.get().strip()  # Get the current title from the input

            # Check if the title is empty
            if title == "":  # If title is empty
                edit_window.withdraw()  # Temporarily hide the edit window
                messagebox.showerror("Invalid Title", "The task title cannot be empty.")  # Show error
                edit_window.deiconify()  # Reopen the edit window after dismissing the error
                return  # Exit the function if no valid title

            # If the title is valid, save and close the edit window
            save_changes(task, title_edit_entry, description_edit_entry, deadline_edit_var, edit_window)  # Save before going back
            edit_window.destroy()  # Close the edit window
            root.deiconify()  # Show the main window again

        back_button = tk.Button(edit_window, text="← Back", command=go_back)  # Button to go back
        back_button.pack(pady=5)    # Function to display tasks in the main window

    def display_task(task, task_frame):
        task_frame.pack(side=tk.TOP, fill=tk.X, pady=5)  # Add each task frame to the window

        title_label = tk.Label(task_frame, text=task['title'], width=30, anchor="w")  # Display task title
        title_label.pack(side=tk.LEFT)

        complete_button = tk.Button(task_frame, text="Complete" if not task['completed'] else "Uncomplete", command=lambda: toggle_complete(task))
        complete_button.pack(side=tk.LEFT)  # Add a button to toggle completion status

        edit_button = tk.Button(task_frame, text="Edit", command=lambda: open_edit_window(task))
        edit_button.pack(side=tk.LEFT)  # Add an edit button to open the task editing window

        # Strike-through for completed tasks
        if task['completed']:
            title_label.config(font=("Arial", 10, "overstrike"))  # Apply strike-through to completed tasks


    # Function to refresh the task list in the main window based on filters and search term
    def refresh_task_list():
        print("Refreshing task list...")  # Print message for task list refresh
        search_term = search_entry.get().lower()  # Get search term from input field
        deadline_filter = deadline_filter_var.get()  # Get selected deadline filter

        # Clear the current task display
        for widget in canvas_frame.winfo_children():
            widget.destroy()  # Remove all widgets from the canvas

        # Add non-completed tasks
        non_completed_tasks = [task for task in tasks if not task['completed']]
        completed_tasks = [task for task in tasks if task['completed']]

        for task in non_completed_tasks:
            if search_term in task['title'].lower() and (task['deadline'] == deadline_filter or deadline_filter == "All"):
                task_frame = tk.Frame(canvas_frame)
                display_task(task, task_frame)  # Display non-completed tasks

        # Add completed tasks at the bottom
        for task in completed_tasks:  # Loop through each task in the completed tasks list
            if search_term in task['title'].lower() and (task['deadline'] == deadline_filter or deadline_filter == "All"):  # Check if task title matches search term and the deadline matches filter
                task_frame = tk.Frame(canvas_frame)  # Create a new frame for each completed task
                display_task(task, task_frame)  # Call the display_task function to show the task in the UI

    # Function to search tasks based on the search term
    def search_task():
        refresh_task_list()  # Refresh the task list when searching for tasks

    # Function to filter tasks by deadline
    def filter_tasks_by_deadline():
        refresh_task_list()  # Refresh the task list when filtering by deadline

    # Function to go back to the main menu (add this to the existing code)
    def go_back_to_main_menu():
        root.withdraw()  # Hide the current To-Do List Manager window
        open_main_menu()

    # GUI Setup
    root = tk.Tk()  # Create the main window for the application
    root.title("To-Do List Manager")  # Set the title of the main window

    # Task input section
    task_input_frame = tk.Frame(root)  # Create a frame to contain task input elements
    task_input_frame.pack(pady=10)  # Add the frame to the window with padding

    tk.Label(task_input_frame, text="Task Title").pack()  # Add a label for task title input
    title_entry = tk.Entry(task_input_frame, width=30)  # Create an entry widget for the user to input task title
    title_entry.pack()  # Pack the entry widget into the frame

    tk.Label(task_input_frame, text="Description").pack()  # Add a label for task description input
    description_entry = tk.Text(task_input_frame, height=4, width=30)  # Create a text box for task description
    description_entry.pack()  # Pack the text box into the frame

    tk.Label(task_input_frame, text="Select Deadline").pack()  # Add a label for the deadline selection
    deadline_var = tk.StringVar()  # Create a StringVar to store the selected deadline value
    deadline_options = ['Today', 'Tomorrow', 'Next Week']  # Define the options for the deadline
    deadline_dropdown = ttk.Combobox(task_input_frame, textvariable=deadline_var,values=deadline_options)  # Create a combobox for selecting deadline
    deadline_dropdown.set(deadline_options[0])  # Set default deadline to 'Today'
    deadline_dropdown.pack()  # Pack the combobox into the frame

    add_button = tk.Button(task_input_frame, text="Add Task", command=create_task)  # Create a button to add a new task
    add_button.pack(pady=10)  # Pack the button into the frame with padding

    # Add the "Go Back To Main Menu" button below the "Add Task" button
    go_back_button = tk.Button(task_input_frame, text="Go Back To Main Menu",command=go_back_to_main_menu)  # Create a button to go back to the main menu
    go_back_button.pack(pady=10)  # Pack the button below the "Add Task" button

    # Search section
    search_frame = tk.Frame(root)  # Create a frame to contain search-related widgets
    search_frame.pack(pady=10)  # Pack the frame with padding

    tk.Label(search_frame, text="Search by Title").pack(side=tk.LEFT)  # Add a label for search input
    search_entry = tk.Entry(search_frame, width=30)  # Create an entry widget for searching task titles
    search_entry.pack(side=tk.LEFT)  # Pack the entry widget to the left side

    search_button = tk.Button(search_frame, text="Search", command=search_task)  # Create a button to perform search
    search_button.pack(side=tk.LEFT, padx=5)  # Pack the button to the left side with padding

    # Deadline filter
    deadline_filter_frame = tk.Frame(root)  # Create a frame for the deadline filter
    deadline_filter_frame.pack(pady=10)  # Pack the frame with padding

    tk.Label(deadline_filter_frame, text="Filter by Deadline").pack(side=tk.LEFT)  # Add a label for filtering tasks by deadline
    deadline_filter_var = tk.StringVar()  # Create a StringVar to store the selected filter deadline value
    deadline_filter_options = ['All', 'Today', 'Tomorrow', 'Next Week']  # Define the options for deadline filtering
    deadline_filter_dropdown = ttk.Combobox(deadline_filter_frame, textvariable=deadline_filter_var,values=deadline_filter_options)  # Create a combobox for selecting the filter
    deadline_filter_dropdown.set('All')  # Set default filter to 'All'
    deadline_filter_dropdown.pack(side=tk.LEFT)  # Pack the combobox to the left side

    filter_button = tk.Button(deadline_filter_frame, text="Filter",command=filter_tasks_by_deadline)  # Create a button to apply the filter
    filter_button.pack(side=tk.LEFT, padx=5)  # Pack the button to the left side with padding

    # Creating a Canvas to hold tasks and make it scrollable
    canvas = tk.Canvas(root)  # Create a canvas to hold tasks and enable scrolling
    canvas.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)  # Pack the canvas and make it expand in both directions

    # Adding a Scrollbar to the Canvas
    scrollbar = tk.Scrollbar(root, orient="vertical",command=canvas.yview)  # Create a vertical scrollbar for the canvas
    scrollbar.pack(side=tk.RIGHT, fill="y")  # Pack the scrollbar on the right side

    # Configuring the canvas to use the scrollbar
    canvas.configure(yscrollcommand=scrollbar.set)  # Link the scrollbar to the canvas

    # Creating a frame inside the Canvas to hold the tasks
    canvas_frame = tk.Frame(canvas)  # Create a frame inside the canvas

    # Creating a window in the canvas to hold the frame (for scrolling)
    canvas.create_window((0, 0), window=canvas_frame, anchor="nw")  # Create a window in the canvas to hold the frame

    # Binding the scrollbar to the canvas for smooth scrolling
    canvas_frame.bind(
        "<Configure>",  # Bind the frame's configuration change event to update the canvas scroll region
        lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        # Update the scroll region whenever the frame's size changes
    )

    # Load tasks from file and display them
    tasks = load_tasks()  # Load existing tasks from the task file

    # Refresh the task list for the first time
    refresh_task_list()  # Call the function to display tasks

    root.mainloop()  # Start the Tkinter event loop to run the application


# ----------------------------------------------------------END To-do List Manager---------------------------------------------------------------------------------

# --------------------------------------------------------Main Menu--------------------------------------------------------------------------------------------------

import tkinter as tk  # Import tkinter library for GUI components
from tkinter import messagebox  # Import messagebox for showing dialog boxes

# Function to display "Under Maintenance" message
def under_maintenance():
    messagebox.showinfo("Under Maintenance","This application is under maintenance.")  # Show a message box with maintenance information

# Function to open the To-do List Manager
def open_todo_manager():
    main_menu_window.destroy()  # Close the main menu window
    run_todo_manager()  # Call the To-Do List Manager application function (assuming it's defined elsewhere)

def open_gpa_calculator():
    main_menu_window.destroy()  # Close the main menu window
    from GPA_CALCULATOR_CHICAI import GPA_Calculator
    if not GPA_Calculator.is_running:


    # If you want to use the current window (`main_menu_window`) as the container
        gpa_calculator_instance = GPA_Calculator()  # Pass the existing window to GPA_Calculator

        # Create the GPA Calculator interface within the current window
        gpa_calculator_instance.create_main_page()  # Add the GPA calculator interface to the current window

        # Run the main loop for the GPA Calculator window (use the existing window's loop)
        main_menu_window.mainloop()

def open_expenses_tracker():

    main_menu_window.destroy()  # Close the main menu window
    from EXPENSES_TRACKER_KOKWEI import main
    from EXPENSES_TRACKER_KOKWEI import ExpenseTrackerApp

    if not ExpenseTrackerApp.is_running:
        # If you want to use the current window (`main_menu_window`) as the container
        ExpenseTrackerApp_instance = main()  # Pass the existing window to Expenses Tracker

        # Create the Expenses Tracker interface within the current window
        ExpenseTrackerApp_instance.ExpenseTrackerApp()  # Add the Expenses Tracker interface to the current window

        # Run the main loop for the Expenses Tracker window (use the existing window's loop)
        main_menu_window.mainloop()



def open_main_menu():
    global main_menu_window

    main_menu_window = tk.Tk()
    main_menu_window.title("Main Menu")
    main_menu_window.geometry("300x310")
    main_menu_window.tkraise()  # bring the window in front
    main_menu_window.attributes("-topmost", 1)  # always stay on top of all other windows  1 is True

    title_label = tk.Label(main_menu_window, text="Please select an application", font=("Helvetica", 16))
    title_label.pack(pady=20)

    # Button for the GPA Calculator
    todo_button = tk.Button(main_menu_window, text="To-do List Manager", width=30, height=2, font=("Helvetica", 12),command=open_todo_manager)  # Create a button for "To-do List Manager" with specified dimensions and font
    todo_button.pack(pady=10)  # Pack the button into the window with 10 pixels of vertical padding

    gpa_button = tk.Button(main_menu_window, text="GPA Calculator", width=30, height=2, font=("Helvetica", 12),command=open_gpa_calculator)
    gpa_button.pack(pady=10)

    expense_button = tk.Button(main_menu_window, text="Expense Tracker", width=30, height=2, font=("Helvetica", 12),command=open_expenses_tracker)  # Create a button for "Expense Tracker" that shows an under maintenance message
    expense_button.pack(pady=10)  # Pack the button into the window with 10 pixels of vertical padding

    main_menu_window.mainloop()  # Run the main loop for the main menu window

# -------------------------------------------------------------------------END Main Menu--------------------------------------------------------------------------------

# Start the application by opening the main menu
if __name__ == "__main__":
    open_main_menu()

