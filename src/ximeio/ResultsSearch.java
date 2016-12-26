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


public class ResultsSearch extends JFrame {

    private String[] columnNames = {"Κωδικός Πελάτη","Επωνυμο","Όνομα","Πατρώνυμο","Παρώνυμο","Διεύθυνση","Σταθερό","Κινητό","Email","ΑΦΜ","ΔΟΥ"};
    private JTable table;
    private MyDefaultTableModel model;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");
    JPanel panel;
    String selected_id,keyS;
    JScrollPane sc;




    public ResultsSearch (String day,String month){

        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        setSize(new Dimension(dim.width,dim.height-50)); // Change height- XX to fit fullscreen
        setLayout(new BorderLayout());
        setTitle("ΑΠΟΤΕΛΕΣΜΑΤΑ ΑΝΑΖΗΤΗΣΗΣ");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

       model = new MyDefaultTableModel();
        for(int i = 0 ; i < columnNames.length ; i++ ){
            model.addColumn(columnNames[i]);

        }
        initializeTable(day,month);
           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);


        table = new JTable(model);

        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(400);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);



        table.setFont(new Font("Arial", Font.PLAIN, 14));
        for(int i = 0 ; i < columnNames.length ; i++ ){
        TableColumn column = table.getColumn(columnNames[i]);
        column.setHeaderRenderer(new MyHeaderRenderer());
        }



         sc = new JScrollPane(table);
        add(sc,BorderLayout.CENTER);
    }

    private void initializeTable(String day, String month) {
        Vector<String> rowData = new Vector<String>();
            try {
                String analysisID,kilos = null,name = null,barrel = null,analysisNr=null,address=null,analysisDate=null;
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select AnalysisID,AnalysisNr,DATE_FORMAT(AnalysisDate,'%d %b %Y') as AnalysisDate,AddWater,AddSugar,TAcid,CAcid,Me,Lux,Caco3,Tanine,Vitamin from Analysis where extract(day from AnalysisDate) = "+day+" and extract(month from AnalysisDate)="+month);
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





