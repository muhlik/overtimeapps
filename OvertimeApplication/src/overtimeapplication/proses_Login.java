/*
 * proses_Login.java
 *
 * Created on 11 Januari 2011, 11:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package overtimeapplication;

/**
 *
 * @author MuhliX-Tech
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class proses_Login extends JWindow {
    JLabel jLabel1 = new JLabel();
    JProgressBar prosesBar = new JProgressBar();
    JLabel statusProses = new JLabel();
    Connection konek;
    javax.swing.Timer time;
    ActionListener task1, task2, task3, task4, task5, task6;
    String dataUser, dataPassword;
    boolean proses = true;
    String statusGagal;
    
    /** Creates a new instance of proses_Login */
    public proses_Login(String user, String password) {
        dataUser = user;
        dataPassword = password;
        tampilan_GUI();
        tahapan_prosesLogin();
    }
    public void tampilan_GUI() {
        this.getContentPane().setBackground(Color.yellow);
        this.getContentPane().setLayout(null);
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel1.setText("Please Waiting...");
        jLabel1.setBounds(new Rectangle(14, 5, 187, 30));
        prosesBar.setBounds(new Rectangle(14, 40, 311, 16));
        prosesBar.setStringPainted(true);
        statusProses.setText("");
        statusProses.setBounds(new Rectangle(14, 60, 311, 16));
        this.getContentPane().add(jLabel1);
        this.getContentPane().add(prosesBar);
        this.getContentPane().add(statusProses);  
    }
    public void load_DriverJDBC() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            status_Proses(true, "Sukses!!!Driver JDBC ditemukan...", 20);
        }
        catch (ClassNotFoundException cnfe) {
            status_Proses(false, "Gagal!!!Driver JDBC tidak ditemukan...", 20);
        }
    }
    public void koneksiDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/payroll_db";
            Properties prop = new Properties();
            prop.put("user", "root");
            prop.put("password", "admin");
            konek = DriverManager.getConnection(url, prop);
            status_Proses(true, "Sukses!!!Berhasil Terhubung dengan Database...", 20);
        }
        catch (SQLException se) {
            status_Proses(false, "Gagal!!!Tidak terhubung \nKarena : "+se, 20); 
        }
    }
    public void check_User() {
        String user = "";
        String sql = "Select * from user";
        boolean user_valid = false;
        try {
            PreparedStatement stat = konek.prepareStatement(sql);
            ResultSet rset = stat.executeQuery();
            while (rset.next()) {
                user = rset.getString("username");
                if (user.equalsIgnoreCase(dataUser)) {
                    user_valid = true;
                    status_Proses(true, "Sukses!!!User Name Valid...", 20);
                }
            }
            if (user_valid == false) {
                status_Proses(false, "Gagal!!!User yang Anda Masukan Salah...",20);
            }
        }
            catch (SQLException se) {}
        }
    public void check_Password() {
        String password = "admin";
        String sql = "Select * from user where user_id = '"+dataUser+ "'";
        boolean password_valid = false;
        try {
            PreparedStatement stat = konek.prepareStatement(sql);
            ResultSet rset = stat.executeQuery();
            while (rset.next()) {
                password = rset.getString("psw");
                if (password.equalsIgnoreCase(dataPassword)) {
                password_valid = true;
                status_Proses(true, "Sukses!!!Password Valid...", 20);
            }
        }
        if (password_valid == false) {
               status_Proses(false, "Gagal!!!Password yang Anda Masukan Salah...",20); 
            }
        }
        catch (SQLException se) {}          
    }
    public void status_Proses(boolean sukses, String status, int nilaiProsesBar) {
        statusProses.setText(status);
        prosesBar.setValue(prosesBar.getValue() + nilaiProsesBar);
        if (proses == false) {
            JOptionPane.showMessageDialog(null, "Gagal Login\nKarena"+statusGagal);
            removeAll();
            dispose();
            new LoginDialog_Database().show();
            }
            proses = sukses;
            statusGagal = status;
        }
    public void tahapan_prosesLogin() {
        task1 = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                load_DriverJDBC();
                time.stop();
                if (proses == true) {
                    time = new javax.swing.Timer(2000, task2);
                    time.start();
                }
                else {
                    status_Proses(false, "Error", 20);
                }
            }
        };
        time = new javax.swing.Timer(1000, task1);
        time.start();
        
        task2 = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                koneksiDatabase();
                time.stop();
                if (proses == true) {
                    time = new javax.swing.Timer(2000, task3);
                    time.start();
                }
                else {
                    status_Proses(false, "Error", 20);
                }
            }
        };
        task3 = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                check_User();
                time.stop();
                if (proses == true) {
                    time = new javax.swing.Timer(2000, task4);
                    time.start();
                }
                else {
                    status_Proses(false, "Error", 20);
                }
            }
        };
        task4 = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                check_Password();
                time.stop();
                if (proses == true) {
                    time = new javax.swing.Timer(2000, task5);
                    time.start();
                }
                else {
                    status_Proses(false, "Error", 20);
                }
            }
        };
        task5 = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                status_Proses(true, "Proses Login Berhasil...", 20);
                time.stop();
                if (proses == true) {
                    time = new javax.swing.Timer(2000, task6);
                   time.start();
                }
                else {
                   status_Proses(false, "Error", 20);
                }
            }
        };
        task6 = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (prosesBar.getPercentComplete() == 1.0) {
                    new formUtama().show();
                    time.stop();
                    dispose();
                }
                else {
                    status_Proses(true, "Welcome !", 20);
                }
            }
        };
    }
}

