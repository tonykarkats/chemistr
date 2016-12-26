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


public class WineOld extends JFrame {

    private String[] columnNames = {"Κωδικός Κρασιού","Χρονιά","No Βαρελιού","Κιλα","Χρώμα","Me","Τυπος Βαρελιου","Πηγη Σταφυλιών"};
    private JTable table;
    private MyDefaultTableModel model;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");
    private JButton add,delete,ok,analysis,update;
    private ImageIcon addI = new ImageIcon("icons/add-2-icon.png");
    private ImageIcon deleteI = new ImageIcon("icons/delete-icon.png");
    private ImageIcon okI = new ImageIcon("icons/Ok-icon.png");
    private ImageIcon analysisI = new ImageIcon("icons/analysis.png");
    private ImageIcon editI = new ImageIcon("icons/Edit-Document-icon.png");


    JPanel panel;
    String selected_id;
    boolean mustUpdate=false,enabledli=true;
    String cid,y;

    public WineOld (final String customerID,final String year){

        cid = customerID;
        y = year;
        setSize(new Dimension(1000,300));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



         try{
                    Statement stmt = conn.createStatement(true);
                    ResultSet rs = stmt.executeQuery("select * from customer where CustomerID="+customerID);
                    rs.next();
                    String customerName = rs.getString("Surname");
                    customerName +=" "+ rs.getString("Name");

        setTitle(customerName+" - ΠΡΟΒΟΛΗ ΚΡΑΣΙΩΝ");
        }
        catch(SQLException ex) {

        } ;

        model = new MyDefaultTableModel();
        for(int i = 0 ; i < columnNames.length ; i++ )
            model.addColumn(columnNames[i]);
        initializeTable(customerID);

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        add(table.getTableHeader(),BorderLayout.PAGE_START);
        add(table,BorderLayout.CENTER);

        ListSelectionModel cm = table.getSelectionModel();
        cm.addListSelectionListener(new ListSelectionListener() {

           public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && enabledli) {
                int index;
                Object idOb;
                index =  table.getSelectedRow();
                idOb = model.getValueAt(index, 0);
                selected_id = idOb.toString();
            }
            }
        });


        add= new JButton("Καταχώρηση Κρασιου",addI);

        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AddWine aw = new AddWine(customerID);
                aw.setVisible(true);
                if (aw.mustUpdate == true) mustUpdate = true;
            }
        });

        addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e) {
               if(mustUpdate==true) {
                   updateTable(customerID);
                   mustUpdate = false;
               }
            }

            public void windowLostFocus(WindowEvent e) {
            }
        });


        delete = new JButton("Διαγραφη Κρασιου",deleteI);
        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                    deleteWine(selected_id);
            }
        });

        update = new JButton("Επεξεργασία",editI);
        update.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                UpdateWine uw = new UpdateWine(selected_id);
                uw.setVisible(true);
                if (uw.mustUpdate == true) mustUpdate = true;


            }
        });

        analysis = new JButton("Αναλύσεις",analysisI);
        analysis.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               Analysis a = new Analysis(selected_id);
               a.setVisible(true);


            }
        });

        ok = new JButton("OK",okI);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                dispose();
                try{
                    int money=0;
                    Statement stmt = conn.createStatement(true);
                    ResultSet rs = stmt.executeQuery("select Money from Wine where CustomerID="+customerID);
                    while(rs.next()) {
                        money+=Integer.parseInt(rs.getString("Money"));

                    }

                    stmt.executeUpdate("update Customer set Money="+Integer.toString(money)+",MoneyOwned="+Integer.toString(money)+" where CustomerID="+customerID);
                }
                catch(SQLException ex) {

                }

            }
        });



        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(add);
        panel.add(delete);
        panel.add(update);
        panel.add(analysis);
        panel.add(ok);

        add(panel,BorderLayout.SOUTH);
    }

    private void initializeTable(String CustomerID) {
        Vector<String> rowData = new Vector<String>();
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select WineID,extract(year from Added) as Added,NrBarrel,Kilos,Colour,Me,BarrelType,GrapeSource from Wine where CustomerID="+CustomerID+" and extract(year from Added)="+y+" ORDER BY NrBarrel");
                while (rs.next()) {
                    rowData.add(rs.getString("WineID"));
                    rowData.add(rs.getString("Added"));
                    rowData.add(rs.getString("NrBarrel"));
                    rowData.add(rs.getString("Kilos"));
                    rowData.add(rs.getString("Colour"));
                    rowData.add(rs.getString("Me"));
                    rowData.add(rs.getString("BarrelType"));
                    rowData.add(rs.getString("GrapeSource"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
            } catch (SQLException ex) {
            }

    }


     private void updateTable(String CustomerID) {
         enabledli = false;
        Vector<String> rowData = new Vector<String>();
        if(model.getRowCount() != 0) {
            model = (MyDefaultTableModel) table.getModel();
            model.getDataVector().removeAllElements();
        }
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select WineID,NrBarrel,Kilos,Colour,Me,BarrelType,GrapeSource from Wine where CustomerID="+CustomerID+" and extract(year from Added)="+y+" ORDER BY NrBarrel");
                while (rs.next()) {
                    rowData.add(rs.getString("WineID"));
                    rowData.add(rs.getString("NrBarrel"));
                    rowData.add(rs.getString("Kilos"));
                    rowData.add(rs.getString("Colour"));
                    rowData.add(rs.getString("Me"));
                    rowData.add(rs.getString("BarrelType"));
                    rowData.add(rs.getString("GrapeSource"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
                enabledli = true;
            } catch (SQLException ex) {
            }

    }


    public void deleteWine(String id){
        JDialog warnDialog = new JDialog();
                warnDialog.setBounds(500,400,200,200);
               int warning = JOptionPane.showConfirmDialog(null, "Είστε βέβαιος?", "Διαγραφή Κρασιού", JOptionPane.YES_NO_CANCEL_OPTION);
        if (warning == 0) {
        try {

                Statement stmt = conn.createStatement(true);
                stmt.executeUpdate("delete from Wine where WineID="+id);

               //System.out.println(cid+"eeeee");
                updateTable(cid);
                table.revalidate();
                table.repaint();

        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }

    }


    }
}





