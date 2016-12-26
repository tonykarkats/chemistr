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
import javax.swing.table.TableColumn;


public class CustomersSecure extends JFrame {

    private String[] columnNames = {"Κωδικός Πελάτη","Επωνυμο","Όνομα","Πατρώνυμο","Παρατσούκλι","Διεύθυνση","Σταθερό","Κινητό","Email",
                                   "ΑΦΜ","ΔΟΥ","Ποσο","Προκαταβολή","Υπόλοιπο"};
    private JTable table;
    private MyDefaultTableModel model;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");;
    private JButton search,editpay;
    private ImageIcon payI = new ImageIcon("icons/Paper-Money-icon.png");
    private ImageIcon searchI = new ImageIcon("icons/search-icon.png");
    private ImageIcon winesI = new ImageIcon("icons/add-wine.png");
    JPanel panel;
    JTextField text;
    String searchStr;
    String selected_id;
    boolean mustUpdate=false,enabledli=true;
    JScrollPane sc;




    public CustomersSecure (){

         Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        setSize(new Dimension(dim.width,dim.height-50)); // Change height- XX to fit fullscreen
        setLayout(new BorderLayout());
        setTitle("ΠΕΛΑΤΕΣ - ΑΣΦΑΛΗΣ ΛΕΙΤΟΥΡΓΙΑ");
        UIManager.put("Button.font", new FontUIResource("Dialog", Font.BOLD, 17));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        model = new MyDefaultTableModel();
        for(int i = 0 ; i < columnNames.length ; i++ )
            model.addColumn(columnNames[i]);
        initializeTable();

           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);


       // add(sc,BorderLayout.CENTER);

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        for(int i = 0 ; i < columnNames.length ; i++ ){
        TableColumn column = table.getColumn(columnNames[i]);
        column.setHeaderRenderer(new MyHeaderRenderer());
        }

      //  add(table.getTableHeader(),BorderLayout.PAGE_START);
       // add(table,BorderLayout.CENTER);
         sc = new JScrollPane(table);
        add(sc,BorderLayout.CENTER);

        ListSelectionModel cm = table.getSelectionModel();
        ListSelectionListener lsl = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && enabledli) {
                int index;
                Object idOb;
                index =  table.getSelectedRow();
                idOb = model.getValueAt(index, 0);
                selected_id = idOb.toString();
            }
            }

        };
        cm.addListSelectionListener(lsl);

        addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e) {
               if(mustUpdate==true) {
                   updateTable();
                   mustUpdate = false;
               }
            }

            public void windowLostFocus(WindowEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
            }
        });


        editpay = new JButton("Επεξεργασία Πληρωμών",payI);
        editpay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                UpdatePay up =new UpdatePay(selected_id);
                //System.out.println(selected_id);
                up.setVisible(true);
                if (up.mustUpdate == true) mustUpdate = true;

            }
        });


        text = new JTextField();
        text.setPreferredSize(new Dimension(200, 40));
        search = new JButton("Αναζήτηση",searchI);
        getRootPane().setDefaultButton(search);
        search.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                 searchStr = text.getText();
                 CustomerSecureSearchResults csr= new CustomerSecureSearchResults(searchStr);
                 csr.setVisible(true);
                 mustUpdate = true;
                //dispose();


            }
        });         


        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));      
       // panel.add(refresh);
        panel.add(text);
        panel.add(search);
        panel.add(editpay);      

        getContentPane().add(panel,BorderLayout.SOUTH);






    }

    private void initializeTable() {
        Vector<String> rowData = new Vector<String>();
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select * from Customer order by Surname");
                while (rs.next()) {
                    rowData.add(rs.getString("CustomerID"));
                    rowData.add(rs.getString("Surname"));
                    rowData.add(rs.getString("Name"));
                    rowData.add(rs.getString("FatherName"));
                    rowData.add(rs.getString("Nickname"));
                    rowData.add(rs.getString("Address"));
                    rowData.add(rs.getString("Phone1"));
                    rowData.add(rs.getString("Phone2"));
                    rowData.add(rs.getString("mail"));
                    rowData.add(rs.getString("afm"));
                    rowData.add(rs.getString("doy"));
                    rowData.add(rs.getString("Money"));
                    rowData.add(rs.getString("MoneyGiven"));
                    rowData.add(rs.getString("MoneyOwned"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
            } catch (SQLException ex) {

              //  Logger.getLogger(Musician.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

        private void updateTable() {
       enabledli = false;
        Vector<String> rowData = new Vector<String>();
        if(model.getRowCount() != 0) {
            model = (MyDefaultTableModel) table.getModel();
            model.getDataVector().removeAllElements();
        }
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select * from Customer order by Surname");
                while (rs.next()) {
                    rowData.add(rs.getString("CustomerID"));
                    rowData.add(rs.getString("Surname"));
                    rowData.add(rs.getString("Name"));
                    rowData.add(rs.getString("FatherName"));
                    rowData.add(rs.getString("Nickname"));
                    rowData.add(rs.getString("Address"));
                    rowData.add(rs.getString("Phone1"));
                    rowData.add(rs.getString("Phone2"));
                    rowData.add(rs.getString("mail"));
                    rowData.add(rs.getString("afm"));
                    rowData.add(rs.getString("doy"));
                    rowData.add(rs.getString("Money"));
                    rowData.add(rs.getString("MoneyGiven"));
                    rowData.add(rs.getString("MoneyOwned"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
                enabledli = true;
            } catch (SQLException ex) {

              //  Logger.getLogger(Musician.class.getName()).log(Level.SEVERE, null, ex);
            }

    }


}





