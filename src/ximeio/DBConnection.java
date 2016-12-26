package ximeio;

import java.sql.*;

public class DBConnection {
    private String dbhost,dbuser,dbpass,dbname;
    private boolean isConnected;
    private Connection myconn;
    protected boolean connError=false;

    public DBConnection(String dbhost,String dbuser,String dbpass,String dbname) {
        this.dbhost = dbhost;
        this.dbuser = dbuser;
        this.dbpass = dbpass;
        this.dbname = dbname;
        this.isConnected = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.setLoginTimeout(1);
            myconn = DriverManager.getConnection("jdbc:mysql://"+dbhost+"/"+dbname+"?user="+dbuser+"&password="+dbpass);
            isConnected = true;
        }
        catch (Exception e) {
            connError = true;
            System.out.println("conneror");
        }
    }

    public Statement createStatement(boolean updateable){
        Statement stmt = null;
        try {
            if(!updateable){
                stmt = myconn.createStatement();
            }
            else {
                stmt = myconn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CLOSE_CURSORS_AT_COMMIT);
            }
        }
        catch (Exception e) {
            System.exit(1);
        }
        return stmt;
    }

    public Connection getConnection(){
        return myconn;
    }

    public void close(ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (Exception e) {}
        }
    }

    public void close(Statement st){
        if(st!=null){
            try {
                st.close();
            } catch (Exception e) {}
        }
    }

    public boolean isConnected(){
        return isConnected;
    }

    public void destroy(){
        if(myconn!=null){
            try {
                myconn.close();
                isConnected = false;
            }
            catch(Exception e){}
        }
    }
    protected void setConnection(boolean status) {
        this.isConnected = status;
    }
}