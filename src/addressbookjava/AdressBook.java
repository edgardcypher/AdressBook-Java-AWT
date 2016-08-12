/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addressbookjava;

/**
 *
 * @author abcd
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class AdressBook extends Frame {

    Label Lname,//label name 
            Lsurname, //label surname
            Laddress,// label adress
            Ltelephone, //label tel
            Lemail, //label email
            Lrecord; //label record display
    TextField[] tf = new TextField[5];
    Button add, next, previous;
    Dialog dialogBox;
    File file;//variable of type File
    RandomAccessFile randFile;//variable of type randomAccesFile
    String str;// variable containing the string from the file
    long l = 0;
    int i = 0, j = 0, record = 0;
    java.util.List list = new LinkedList();

    public AdressBook() {
        list = new ArrayList(); //to hold the file pointers

        file = new File("adresbook.txt");

        Lname = new Label("Name         : ");
        Lsurname = new Label("Surname   : ");
        Laddress = new Label("Address     : ");
        Ltelephone = new Label("Telephone : ");
        Lemail = new Label("E-mail         : ");

        tf[0] = new TextField(20);
        tf[1] = new TextField(20);
        tf[2] = new TextField(40);
        tf[3] = new TextField(20);
        tf[4] = new TextField(20);

        add = new Button("Add");
        previous = new Button("Previous");
        next = new Button("Next");
        Lrecord = new Label("          ");

        Panel p1 = new Panel(new FlowLayout(FlowLayout.LEFT));//contains the name label and its textfield
        Panel p2 = new Panel(new FlowLayout(FlowLayout.LEFT));
        Panel p3 = new Panel(new FlowLayout(FlowLayout.LEFT));
        Panel p4 = new Panel(new FlowLayout(FlowLayout.LEFT));
        Panel p5 = new Panel(new FlowLayout(FlowLayout.LEFT));

        Panel p6 = new Panel(new GridLayout(5, 1));//create the big panel to contain the 5 panel above

        Panel p7 = new Panel(new FlowLayout(FlowLayout.CENTER));//panel for the button

        p1.add(Lname);
        p1.add(tf[0]);
        p2.add(Lsurname);
        p2.add(tf[1]);
        p3.add(Laddress);
        p3.add(tf[2]);
        p4.add(Ltelephone);
        p4.add(tf[3]);
        p5.add(Lemail);
        p5.add(tf[4]);

        p6.add(p1);
        p6.add(p2);
        p6.add(p3);
        p6.add(p4);
        p6.add(p5);

        p7.add(add);
        p7.add(previous);
        p7.add(next);
        p7.add(Lrecord);

        setLayout(new BorderLayout());
        add(p6, BorderLayout.CENTER);
        add(p7, BorderLayout.SOUTH);
        previous.setEnabled(false);// desactivate this button when lauching  the app
        readFile();//open the file	
        pack();
        setTitle("AdressBook");
        setVisible(true);

        /**
         * *********************WindowListener**********************
         */
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                try {
                    randFile.close();
                } catch (Exception e) {
                    System.out.println("Can't close the file");
                }
                dispose();
                System.exit(0);
            }
        });

        /**
         * *********************Action listener add button*******************
         */
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (tf[0].getText().equals("")) //check the fields are full
                {
                    dialogMesaj("Name required..."); //you can remove any of you want
                } else if (tf[1].getText().equals("")) {
                    dialogMesaj("Surname required...");
                } else if (tf[2].getText().equals("")) {
                    dialogMesaj("Address required...");
                } else if (tf[3].getText().equals("")) {
                    dialogMesaj("Telephone required...");
                } else if (tf[4].getText().equals("")) {
                    dialogMesaj("E-mail required...");
                } else { //create the kisi(person) object
                    Display k = new Display(tf[0].getText(), tf[1].getText(), tf[2].getText(), tf[3].getText(), tf[4].getText());
                    System.out.println(k);
                    try {
                        randFile = new RandomAccessFile(file, "rw");
                        randFile.seek(file.length()); //go to the end of the file
                        randFile.writeBytes(k.toString());
                        randFile.close();
                        //dosyaAc(); //reopen the file
                    } catch (Exception e) {
                        System.out.println("Can't write to file");
                    }
                }
            }
        });
       
        previous.addActionListener(new ActionListener() { //previous button
            public void actionPerformed(ActionEvent ae) {
                try {
                    if (i > 0) {
                        i--;  //to get the previous file pointer from arraylist
                        randFile.seek(list.get(i).hashCode()); //because of the araylist holding the Object Long, we have to get the long type of the Long object
                        str = randFile.readLine();
                        StringTokenizer strtk = new StringTokenizer(str, ">"); //parse the line 
                        while (strtk.hasMoreElements()) {
                            tf[j].setText(strtk.nextToken()); //write to the textfields
                            j++; //next token
                        }
                        j = 0;
                    }
                } catch (Exception e) {
                    System.out.println("Can't read from file");
                }
            }
        });

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                previous.setEnabled(true);//activate the previous button now
                try {
                    if (i < list.size() - 1) {
                        i++; //to get the next file pointer from arraylist
                        randFile.seek(list.get(i).hashCode()); //because of the araylist holding the Object Long, we have to get the long type of the Long object
                        str = randFile.readLine();
                        StringTokenizer strtk = new StringTokenizer(str, ">");//parse the line 
                        while (strtk.hasMoreElements()) {
                            tf[j].setText(strtk.nextToken()); //write to the textfields
                            j++; //next token
                        }
                        j = 0;
                    }
                } catch (Exception e) {
                    System.out.println("Can't read from file");
                }
            }
        });

    }

    /**
     * ***********open the file****************
     */
    public void readFile() {
        try {
            randFile = new RandomAccessFile(file, "rw");
            while ((str = randFile.readLine()) != null) {
                record++; //record count
                list.add(new Long(l)); //add the file pointer to the arraylist
                l += str.length() + 1; //+1 for the \n (newline) character
                randFile.seek(l);
            }
            randFile.seek(0);
            System.out.println(list);
            Lrecord.setText("     Record count : " + record);

            /**
             * ******write the first record to the textfields********
             
            str = randFile.readLine();
            StringTokenizer strtk = new StringTokenizer(str, ">");
            while (strtk.hasMoreElements()) {
                tf[j].setText(strtk.nextToken());
                j++;
            }*/
            j = 0;
            randFile.seek(0);
        } catch (Exception e) {
            System.out.println("Can't read from file");
        }

    }

    /**
     * ********************************MESAJ**********************
     */
    public void dialogMesaj(String str) {

        dialogBox = new Dialog(new Frame(), "Warning ");
        dialogBox.setLayout(new BorderLayout());

        Panel p1 = new Panel(new FlowLayout());
        Button okBtn = new Button("OK");
        okBtn.setForeground(Color.black);
        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogBox.setVisible(false);
            }
        });

        p1.add(okBtn);
        Label l = new Label(str, Label.CENTER);
        l.setFont(new Font("Courier", Font.PLAIN, 14));
        l.setForeground(Color.black);

        dialogBox.add(l, BorderLayout.CENTER);
        dialogBox.add(p1, BorderLayout.SOUTH);
        dialogBox.setResizable(false);
        dialogBox.setLocation(270, 200);
        dialogBox.pack();
        dialogBox.show();
    }

    /**
     * ************************************MAIN*********************
     */
    public static void main(String args[]) {
        AdressBook ab = new AdressBook();
    }
}

class Display {

    String name, surname, adresss, telephone, email;

    public Display(String a, String s, String ad, String t, String e) {
        this.name = a; //name
        this.surname = s; //surname
        this.adresss = ad;
        this.telephone = t;
        this.email = e;
    }

    public String toString() { //override the toString method to write the record to the file
        return (this.name + ">" + this.surname + ">" + this.adresss + ">"
                + this.telephone + ">" + this.email + "\n");
    }

}
