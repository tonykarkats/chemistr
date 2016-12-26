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
import javax.swing.table.TableColumn;


public class Results extends JFrame {

    private String[] columnNames = {"Κωδ","Ανάλ.","Ημερομηνία","Ονοματεπωνυμο","Διεύθυνση","Αρ. Βαρελιου","Κιλα","Water","Sugar","Tartaric Acid","Citric Acid","Me","Lux","Caco3","Tanine","Vitamin"};

    private JTable table,table1;
    private MyDefaultTableModel model,model1;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");
    JScrollPane sc;
    JPanel panel;
    JButton print,printAnalysisButt;
    JTextField text;
    String searchStr,selected_id;
    int selectedRows[];
    boolean enabledli=true;
    private ImageIcon printI = new ImageIcon("icons/print-icon.png");
    private ImageIcon printA = new ImageIcon("icons/report-icon.png");




    public Results (){

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        setSize(new Dimension(dim.width,dim.height-50)); // Change height- XX to fit fullscreen
        setLayout(new BorderLayout());
        setTitle("ΑΠΟΤΕΛΕΣΜΑΤΑ");

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
       

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(400);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);


        
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        for(int i = 0 ; i < columnNames.length ; i++ ){
        TableColumn column = table.getColumn(columnNames[i]);
        column.setHeaderRenderer(new MyHeaderRenderer());
        }
         ListSelectionModel cm = table.getSelectionModel();
        ListSelectionListener lsl = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {

                selectedRows =  table.getSelectedRows();

            }
            }

        };
        cm.addListSelectionListener(lsl);

        ListSelectionModel cm1 = table.getSelectionModel();
        ListSelectionListener lsl1 = new ListSelectionListener() {
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
        cm.addListSelectionListener(lsl1);

       

         sc = new JScrollPane(table);
        add(sc,BorderLayout.CENTER);

         panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        print = new JButton("Εκτύπωση Επιλεγμένων",printI);

        print.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                    model1 = new MyDefaultTableModel();
                    for(int i = 0 ; i < columnNames.length ; i++ ){
                        model1.addColumn(columnNames[i]);

                    }
                     
                    model1.addRow(columnNames);

                    for(int j=0;j<selectedRows.length;j++){
                        Vector<String> rowData = new Vector<String>();
                        rowData = (Vector<String>)model.getDataVector().elementAt(selectedRows[j]);
                       // System.out.println(rowData);
                        model1.addRow(rowData);
                    }


                    table1 = new JTable(model1); 

                   // table1.getColumnModel().getColumn(0).setPreferredWidth(50);
                   // table1.getColumnModel().getColumn(1).setPreferredWidth(100);
                   // table1.getColumnModel().getColumn(3).setPreferredWidth(400);
                   // table1.getColumnModel().getColumn(2).setPreferredWidth(150);

                    table1.setFont(new Font("Arial", Font.PLAIN, 14));
                    for(int i = 0 ; i < columnNames.length ; i++ ){
                    TableColumn column = table1.getColumn(columnNames[i]);
                    column.setHeaderRenderer(new MyHeaderRenderer());
                    }
                    JFrame print = new JFrame();
                    print.setLayout(new BorderLayout());
                    print.add(table1);
                    print.setVisible(true);
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    Dimension dim = toolkit.getScreenSize();
                    print.setSize(new Dimension(dim.width,dim.height-50));

                    PrintableDocument.printComponent(table1);
                    print.dispose();
            }
        });
        panel.add(print);

        printAnalysisButt = new JButton("Εκτύπωση Ανάλυσης",printA);
        getRootPane().setDefaultButton(printAnalysisButt);
        printAnalysisButt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               
                String analysisID = selected_id;
               // System.out.println(analysisID);
                PrintDialog pd = new PrintDialog(analysisID);
                pd.setVisible(true);
            }
        });
        panel.add(printAnalysisButt);

        getContentPane().add(panel,BorderLayout.SOUTH);

    }

    private void initializeTable() {
        Vector<String> rowData = new Vector<String>();
            try {
                String analysisID,kilos = null,name = null,barrel = null,analysisNr=null,address=null,analysisDate=null;
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select AnalysisID,AnalysisNr,DATE_FORMAT(AnalysisDate,'%d / %m / %Y') as AnalysisDate,AddWater,AddSugar,TAcid,CAcid,Me,Lux,Caco3,Tanine,Vitamin from Analysis where extract(year from AnalysisDate) = extract(year from CURDATE()) ORDER BY AnalysisDate");
                while (rs.next()) {
                    analysisID = rs.getString("AnalysisID");
                    analysisNr = rs.getString("AnalysisNr");
                    analysisDate = rs.getString("AnalysisDate");
                    Statement stmt2 = conn.createStatement(true);
                    try{
                    ResultSet rs2 = stmt2.executeQuery("select NrBarrel,kilos from Wine,Analysis where Analysis.WineID = Wine.WineID and AnalysisID="+analysisID);
                   rs2.next();
                    kilos = rs2.getString("kilos");
                    barrel = rs2.getString("NrBarrel");
                    }
                    catch(SQLException ex){
                        System.out.println("Error @ query2: "+ex.getMessage());
                    }

                    Statement stmt3 = conn.createStatement(true);
                    try{
                    ResultSet rs3 = stmt3.executeQuery("select Surname,Name,Nickname,Address from Customer,Wine,Analysis where Analysis.WineID = Wine.WineID and Wine.CustomerID = Customer.CustomerID and Analysis.AnalysisID="+analysisID);
                    rs3.next();
                    name=rs3.getString("Surname");
                    name += " "+rs3.getString("Name");
                    name += " "+rs3.getString("Nickname");
                    address = rs3.getString("Address");
                    }
                    catch(SQLException ex){
                         System.out.println("Error @ query3: "+ex.getMessage());
                    }

                    rowData.add(analysisID);
                    rowData.add(analysisNr);
                    rowData.add(analysisDate);
                    rowData.add(name);
                    rowData.add(address);
                    rowData.add(barrel);
                    rowData.add(kilos);
                    rowData.add(rs.getString("Addwater"));
                    rowData.add(rs.getString("AddSugar"));
                    rowData.add(rs.getString("TAcid"));
                    rowData.add(rs.getString("CAcid"));
                    rowData.add(rs.getString("Me"));
                    rowData.add(rs.getString("Lux"));
                    rowData.add(rs.getString("Caco3"));
                    rowData.add(rs.getString("Tanine"));
                    rowData.add(rs.getString("Vitamin"));                    
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
            } catch (SQLException ex) {

              System.out.println(ex.getMessage());

            }

    }



    }





