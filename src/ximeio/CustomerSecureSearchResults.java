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


public class CustomerSecureSearchResults extends JFrame {

    private String[] columnNames = {"Κωδικός Πελάτη","Επωνυμο","Όνομα","Πατρώνυμο","Παρατσούκλι","Διεύθυνση","Σταθερό","Κινητό","Email",
                                   "ΑΦΜ","ΔΟΥ","Ποσο","Προκαταβολή","Υπόλοιπο"};
    private JTable table;
    private MyDefaultTableModel model;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");;
    private JButton editpay;   
    JPanel panel;

    String selected_id,key1;
    boolean mustUpdate=false,enabledli=true;



    public CustomerSecureSearchResults (String key){


        key1=key;
        setSize(new Dimension(1280,960));
        setLayout(new BorderLayout());
        setTitle("ΑΠΟΤΕΛΕΣΜΑΤΑ ΑΝΑΖΗΤΗΣΗΣ");
        UIManager.put("Button.font", new FontUIResource("Dialog", Font.BOLD, 17));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        model = new MyDefaultTableModel();
        for(int i = 0 ; i < columnNames.length ; i++ )
            model.addColumn(columnNames[i]);
        initializeTable(key);

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        add(table.getTableHeader(),BorderLayout.PAGE_START);
        add(table,BorderLayout.CENTER);

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
                   updateTable(key1);
                   mustUpdate = false;
               }
            }

            public void windowLostFocus(WindowEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        editpay = new JButton("Επεξεργασία Πληρωμών");
        editpay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                UpdatePay up =new UpdatePay(selected_id);
                //System.out.println(selected_id);
                up.setVisible(true);
                if (up.mustUpdate == true) mustUpdate = true;

            }
        });








        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(editpay);
        getContentPane().add(panel,BorderLayout.SOUTH);






    }

   private void initializeTable(String key) {
        Vector<String> rowData = new Vector<String>();
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select * from Customer where Surname like '%"+key+"%' or Nickname like '%"+key+"%' or Address like '%"+key+"%' ORDER BY Surname");
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

      private void updateTable(String key) {
        enabledli = false;
        Vector<String> rowData = new Vector<String>();
        if(model.getRowCount() != 0) {
            model = (MyDefaultTableModel) table.getModel();
            model.getDataVector().removeAllElements();
        }
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select * from Customer where Surname like '%"+key+"%' or Nickname like '%"+key+"%' or Address like '%"+key+"%' ORDER BY Surname");
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

}





