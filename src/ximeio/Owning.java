package ximeio;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowFocusListener;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
//import sun.swing.table.DefaultTableCellHeaderRenderer;


public class Owning extends JFrame {

    private String[] columnNames = {"Κωδικός","Επωνυμο","Όνομα","Πατρώνυμο","Παρώνυμο","Διεύθυνση","Σταθερό","Κινητό","Ποσό","Υπόλοιπο"
                                  };
    private JTable table;
    private MyDefaultTableModel model;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");
    private ImageIcon printI = new ImageIcon("icons/print-icon.png");
    JPanel panel;
    JTextField text;
    String searchStr;
    String selected_id;
    boolean mustUpdate=false;
    JScrollPane sc;
    boolean enabledli=true;
    JButton print;




    public Owning (){


       //UIManager.put("Text.font", new FontUIResource("Dialog", Font.BOLD, 24));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        setSize(new Dimension(dim.width,dim.height-50)); // Change height- XX to fit fullscreen
        setLayout(new BorderLayout());
        setTitle("ΧΡΩΣΤΟΥΝ");
          ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        model = new MyDefaultTableModel();
        for(int i = 0 ; i < columnNames.length ; i++ ){
            model.addColumn(columnNames[i]);

        }
        initializeTable();


        table = new JTable(model);
        for(int i = 0 ; i < columnNames.length ; i++ ){
        TableColumn column = table.getColumn(columnNames[i]);
        column.setHeaderRenderer(new MyHeaderRenderer());
        }

        TableColumn column = table.getColumn(columnNames[0]);
        column.setPreferredWidth(5);

        TableColumn column1 = table.getColumn(columnNames[1]);
        column1.setPreferredWidth(150);

        table.setFont(new Font("Arial", Font.PLAIN, 14));


         sc = new JScrollPane(table);
        add(sc,BorderLayout.CENTER);


        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        getContentPane().add(panel,BorderLayout.SOUTH);

        print = new JButton("Εκτύπωση",printI);
        panel.add(print);
        print.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PrintableDocument.printComponent(sc);
            }
        });
    }

    private void initializeTable() {


        Vector<String> rowData = new Vector<String>();
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select Customer.CustomerID,Surname,Name,FatherName,Nickname,Address,Phone1,Phone2,Money,MoneyOwned from Customer,CurrentCustomers where Customer.CustomerID=CurrentCustomers.CustomerID and MoneyOwned>0");
                while (rs.next()) {
                    rowData.add(rs.getString("CustomerID"));
                    rowData.add(rs.getString("Surname"));
                    rowData.add(rs.getString("Name"));
                    rowData.add(rs.getString("FatherName"));
                    rowData.add(rs.getString("Nickname"));
                    rowData.add(rs.getString("Address"));
                    rowData.add(rs.getString("Phone1"));
                    rowData.add(rs.getString("Phone2"));
                    rowData.add(rs.getString("Money"));
                    rowData.add(rs.getString("MoneyOwned"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
            } catch (SQLException ex) {

              //  Logger.getLogger(Musician.class.getName()).log(Level.SEVERE, null, ex);
            }

    }




}





