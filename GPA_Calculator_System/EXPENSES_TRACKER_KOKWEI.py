import tkinter as tk
from tkinter import messagebox
import calendar
import datetime
from expense import Expense

class ExpenseTrackerApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Expense Tracker")

        self.budget = 0
        # Define the full path for the expense file
        self.expense_file_path = r"C:\DCS1S2G2-BehKokWeiLohKaiTerngTanChiCai\expenses.csv"

        self.budget_label = tk.Label(root, text="Enter your monthly budget (RM):")
        self.budget_label.grid(row=0, column=0, padx=10, pady=5)

        self.budget_entry = tk.Entry(root)
        self.budget_entry.grid(row=0, column=1, padx=10, pady=5)

        self.budget_button = tk.Button(root, text="Set Budget", command=self.set_budget)
        self.budget_button.grid(row=0, column=2, padx=10, pady=5)

        self.expense_name_label = tk.Label(root, text="Expense Name:")
        self.expense_name_label.grid(row=1, column=0, padx=10, pady=5)

        self.expense_name_entry = tk.Entry(root)
        self.expense_name_entry.grid(row=1, column=1, padx=10, pady=5)

        self.expense_amount_label = tk.Label(root, text="Expense Amount (RM):")
        self.expense_amount_label.grid(row=2, column=0, padx=10, pady=5)

        self.expense_amount_entry = tk.Entry(root)
        self.expense_amount_entry.grid(row=2, column=1, padx=10, pady=5)

        self.category_label = tk.Label(root, text="Select Category:")
        self.category_label.grid(row=3, column=0, padx=10, pady=5)

        self.category_var = tk.StringVar(root)
        self.category_var.set("Food")

        self.category_dropdown = tk.OptionMenu(root, self.category_var, "Food", "Home", "Work", "Entertainment", "Other")
        self.category_dropdown.grid(row=3, column=1, padx=10, pady=5)

        self.add_expense_button = tk.Button(root, text="Add Expense", command=self.add_expense)
        self.add_expense_button.grid(row=4, column=0, columnspan=3, pady=10)

        self.Back_to_main_menu = tk.Button(root, text="Back", command=self.Back_to_main_menu,
                                           font=("Arial", 11))
        self.Back_to_main_menu.grid(row=5, column=0, columnspan=3, pady=10)

        self.text_display = tk.Text(root, height=15, width=50)
        self.text_display.grid(row=6, column=0, columnspan=3, padx=10, pady=10)

    def Back_to_main_menu(self):
        import MAIN_MENU_TO_DO_LIST_MANAGER_KAITERNG  # Import the main menu to show it again
        self.root.withdraw()  # Hide the GPA Calculator window
        print("rtg")
        MAIN_MENU_TO_DO_LIST_MANAGER_KAITERNG.open_main_menu()  # Call the main menu function to display it

    def set_budget(self):
        try:
            self.budget = float(self.budget_entry.get())
            if self.budget <= 0:
                messagebox.showerror("Invalid Input", "Budget must be greater than 0")
            else:
                messagebox.showinfo("Success", f"Budget of RM{self.budget:.2f} set!")
        except ValueError:
            messagebox.showerror("Invalid Input", "Please enter a valid number for the budget.")

    def add_expense(self):
        expense_name = self.expense_name_entry.get()
        try:
            expense_amount = float(self.expense_amount_entry.get())
            selected_category = self.category_var.get()

            if not expense_name or expense_amount <= 0:
                messagebox.showerror("Invalid Input", "Please provide valid expense name and amount.")
                return

            expense = Expense(name=expense_name, category=selected_category, amount=expense_amount)
            self.save_expense_to_file(expense)
            self.calculate_expenses()
            self.clear_inputs()

        except ValueError:
            messagebox.showerror("Invalid Input", "Please enter a valid number for the expense amount.")

    def clear_inputs(self):
        self.expense_name_entry.delete(0, tk.END)
        self.expense_amount_entry.delete(0, tk.END)
        self.category_var.set("Food")

    def save_expense_to_file(self, expense: Expense):
        try:
            with open(self.expense_file_path, "a", encoding="utf-8") as f:
                f.write(f"{expense.name}, {expense.category}, {expense.amount}\n")
        except IOError as e:
            messagebox.showerror("File Error", f"Error saving the expense to file: {e}")

    def calculate_expenses(self):
        expenses = []
        try:
            with open(self.expense_file_path, "r", encoding="utf-8") as f:
                lines = f.readlines()
                for line in lines:
                    stripped_line = line.strip()
                    parts = stripped_line.split(",")
                    if len(parts) != 3:
                        continue
                    try:
                        expense_name = parts[0].strip()
                        expense_amount = float(parts[2].strip())
                        expense_category = parts[1].strip()
                        expenses.append(Expense(name=expense_name, amount=expense_amount, category=expense_category))
                    except ValueError:
                        continue
        except IOError as e:
            messagebox.showerror("File Error", f"Error reading the expense file: {e}")
            return

        self.text_display.delete(1.0, tk.END)
        self.text_display.insert(tk.END, "Expenses Used:\n")
        for expense in expenses:
            self.text_display.insert(tk.END, f"  Name: {expense.name}, Category: {expense.category}, Amount: RM{expense.amount:.2f}\n")

        amount_by_category = {}
        for expense in expenses:
            key = expense.category
            amount_by_category[key] = amount_by_category.get(key, 0) + expense.amount

        self.text_display.insert(tk.END, "\nExpense By Category📈:\n")
        for key, amount in amount_by_category.items():
            self.text_display.insert(tk.END, f"  {key}: RM{amount:.2f}\n")

        total_spent = sum(expense.amount for expense in expenses)
        self.text_display.insert(tk.END, f"\nYou've spent RM{total_spent:.2f} this month!\n")

        remain_budget = self.budget - total_spent
        self.text_display.insert(tk.END, f"Budget remaining: RM{remain_budget:.2f}\n")

        now = datetime.datetime.now()
        days_in_month = calendar.monthrange(now.year, now.month)[1]
        remaining_days = days_in_month - now.day
        if remaining_days > 0:
            daily_budget = remain_budget / remaining_days
            self.text_display.insert(tk.END, f"👉 Budget Per Day: RM{daily_budget:.2f}\n")
        else:
            self.text_display.insert(tk.END, "No more days left in the month!\n")

def main():
    root = tk.Tk()
    app = ExpenseTrackerApp(root)
    root.mainloop()

main()