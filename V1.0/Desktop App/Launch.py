import firebase_admin
from firebase_admin import db, credentials
import os
import time
import tkinter as tk
from tkinter import ttk

class App(ttk.Frame):
    def __init__(self, parent):
        ttk.Frame.__init__(self)

        db_url_file_name = open("db_url.txt", "r")
        db_url = db_url_file_name.read()
        db_url_file_name.close()
        cred = credentials.Certificate("firebase key.json")
        firebase_admin.initialize_app(cred, {"databaseURL": db_url})
        # Make the app responsive
        for index in [0, 1, 2]:
            self.columnconfigure(index=index, weight=1)
            self.rowconfigure(index=index, weight=1)

        # Create control variables
        self.var_1 = tk.BooleanVar(value=True)
        self.var_2 = tk.BooleanVar()
        self.var_3 = tk.BooleanVar()

        # Create widgets :)
        self.setup_widgets()
        
        
    def set_data(self):
        file1 = open('plaintext', 'w')
        if len(self.entry1.get()) == 0:
            file1.write(" ")
        else:
            file1.write(self.entry1.get())
        file1.close()
        os.startfile("encrypt_data.jar")
        time.sleep(5)
        ref = db.reference("/")
        line1data = open("ciphertext", "r")
        db.reference("/").update({self.id_entry.get() + "_line1" : line1data.read()})
        line1data.close()
        
        file2 = open('plaintext', 'w')
        if len(self.entry2.get()) == 0:
            file2.write(" ")
        else:
            file2.write(self.entry2.get())
        file2.close()
        os.startfile("encrypt_data.jar")
        time.sleep(5)
        ref = db.reference("/")
        line2data = open("ciphertext", "r")
        db.reference("/").update({self.id_entry.get() + "_line2" : line2data.read()})
        line2data.close()
        
        file3 = open('plaintext', 'w')
        if len(self.entry3.get()) == 0:
            file3.write(" ")
        else:
            file3.write(self.entry3.get())
        file3.close()
        os.startfile("encrypt_data.jar")
        time.sleep(5)
        ref = db.reference("/")
        line3data = open("ciphertext", "r")
        db.reference("/").update({self.id_entry.get() + "_line3" : line3data.read()})
        line3data.close()
        
        
        file4 = open('plaintext', 'w')
        if len(self.entry4.get()) == 0:
            file4.write(" ")
        else:
            file4.write(self.entry4.get())
        file4.close()
        os.startfile("encrypt_data.jar")
        time.sleep(5)
        ref = db.reference("/")
        line4data = open("ciphertext", "r")
        db.reference("/").update({self.id_entry.get() + "_line4" : line4data.read()})
        line4data.close()
        
        
        file5 = open('plaintext', 'w')
        if len(self.entry5.get()) == 0:
            file5.write(" ")
        else:
            file5.write(self.entry5.get())
        file5.close()
        os.startfile("encrypt_data.jar")
        time.sleep(5)
        ref = db.reference("/")
        line5data = open("ciphertext", "r")
        db.reference("/").update({self.id_entry.get() + "_line5" : line5data.read()})
        line5data.close()
        
        file6 = open('plaintext', 'w')
        if len(self.entry6.get()) == 0:
            file6.write(" ")
        else:
            file6.write(self.entry6.get())
        file6.close()
        os.startfile("encrypt_data.jar")
        time.sleep(5)
        ref = db.reference("/")
        line6data = open("ciphertext", "r")
        db.reference("/").update({self.id_entry.get() + "_line6" : line6data.read()})
        line6data.close()
        
        file7 = open('plaintext', 'w')
        if len(self.entry7.get()) == 0:
            file7.write(" ")
        else:
            file7.write(self.entry7.get())
        file7.close()
        os.startfile("encrypt_data.jar")
        time.sleep(5)
        ref = db.reference("/")
        line7data = open("ciphertext", "r")
        db.reference("/").update({self.id_entry.get() + "_line7" : line7data.read()})
        line7data.close()
        
        file8 = open('plaintext', 'w')
        if len(self.price_entry.get()) == 0:
            file8.write(" ")
        else:
            file8.write(self.price_entry.get())
        file8.close()
        os.startfile("encrypt_data.jar")
        time.sleep(5)
        ref = db.reference("/")
        line8data = open("ciphertext", "r")
        db.reference("/").update({self.id_entry.get() + "_price" : line8data.read()})
        line8data.close()

        os.remove("plaintext")
        os.remove("ciphertext")

    def setup_widgets(self):
        # Panedwindow
        self.paned = ttk.PanedWindow(self)
        self.paned.grid(row=0, column=0, pady=(25, 5), sticky="nsew", rowspan=3)

        # Pane #1
        self.pane_1 = ttk.Frame(self.paned, padding=5)
        self.paned.add(self.pane_1, weight=1)


        # Notebook, pane #2
        self.pane_2 = ttk.Frame(self.paned, padding=5)
        self.paned.add(self.pane_2, weight=3)

        # Notebook, pane #2
        self.notebook = ttk.Notebook(self.pane_2)
        self.notebook.pack(fill="both", expand=True)

        # Tab #1
        self.tab_1 = ttk.Frame(self.notebook)
        for index in [0, 1]:
            self.tab_1.columnconfigure(index=index, weight=1)
            self.tab_1.rowconfigure(index=index, weight=1)
        self.notebook.add(self.tab_1, text="Electronic Shelf Label Client Application")
        
        # Create a Frame for input widgets
        self.widgets_frame = ttk.Frame(self.tab_1, padding=(0, 0, 0, 10))
        self.widgets_frame.grid(
            row=0, column=0, padx=10, pady=(30, 10), sticky="nsew", rowspan=3
        )
        self.widgets_frame.columnconfigure(index=0, weight=1)

        # Entry
        self.id_entry = ttk.Entry(self.widgets_frame)
        self.id_entry.insert(0, "ID")
        self.id_entry.grid(row=0, column=0, padx=5, pady=(0, 10), sticky="ew")
        
        self.entry1 = ttk.Entry(self.widgets_frame)
        self.entry1.insert(0, "Line 1")
        self.entry1.grid(row=1, column=0, padx=5, pady=(0, 10), sticky="ew")
        
        self.entry2 = ttk.Entry(self.widgets_frame)
        self.entry2.insert(0, "Line 2")
        self.entry2.grid(row=2, column=0, padx=5, pady=(0, 10), sticky="ew")
        
        self.entry3 = ttk.Entry(self.widgets_frame)
        self.entry3.insert(0, "Line 3")
        self.entry3.grid(row=3, column=0, padx=5, pady=(0, 10), sticky="ew")
        
        self.entry4 = ttk.Entry(self.widgets_frame)
        self.entry4.insert(0, "Line 4")
        self.entry4.grid(row=4, column=0, padx=5, pady=(0, 10), sticky="ew")
        
        self.entry5 = ttk.Entry(self.widgets_frame)
        self.entry5.insert(0, "Line 5")
        self.entry5.grid(row=5, column=0, padx=5, pady=(0, 10), sticky="ew")
        
        self.entry6 = ttk.Entry(self.widgets_frame)
        self.entry6.insert(0, "Line 6")
        self.entry6.grid(row=6, column=0, padx=5, pady=(0, 10), sticky="ew")
        
        self.entry7 = ttk.Entry(self.widgets_frame)
        self.entry7.insert(0, "Line 7")
        self.entry7.grid(row=7, column=0, padx=5, pady=(0, 10), sticky="ew")
        
        self.price_entry = ttk.Entry(self.widgets_frame)
        self.price_entry.insert(0, "Price")
        self.price_entry.grid(row=8, column=0, padx=5, pady=(0, 10), sticky="ew")

        # Accentbutton
        self.accentbutton = ttk.Button(
            self.widgets_frame, text="Set Data to ESL", command=self.set_data, style="Accent.TButton"
        )
        self.accentbutton.grid(row=9, column=0, padx=5, pady=10, sticky="nsew")
        

if __name__ == "__main__":
    root = tk.Tk()
    root.title("")

    # Simply set the theme
    root.tk.call("source", "azure.tcl")
    root.tk.call("set_theme", "dark")

    app = App(root)
    app.pack(fill="both", expand=True)

    # Set a minsize for the window, and place it in the middle
    root.update()
    root.minsize(root.winfo_width(), root.winfo_height())
    x_cordinate = int((root.winfo_screenwidth() / 2) - (root.winfo_width() / 2))
    y_cordinate = int((root.winfo_screenheight() / 2) - (root.winfo_height() / 2))
    root.geometry("+{}+{}".format(x_cordinate, y_cordinate-20))

    root.mainloop()