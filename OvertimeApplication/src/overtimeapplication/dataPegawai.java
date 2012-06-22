/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * dataPegawai.java
 *
 * Created on 04 Agu 11, 13:16:18
 */

package overtimeapplication;

/**
 *
 * @author MuhLiX-Tech
 */
import javax.swing.table.*;
import javax.swing.*;
import java.sql.*;
import java.awt.Dimension.*;

public class dataPegawai extends javax.swing.JInternalFrame {

    Connection kon;
    /** Creates new form dataPegawai */
    public dataPegawai() {
        initComponents();
         koneksiDatabase();
        tampilGolongan();
        tampilDepartemen();
        tampilJabatan();
        Baru();
    }
private void koneksiDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
            String url = "jdbc:mysql://localhost:3306/payroll_db?user=root&password=admin";
            kon = DriverManager.getConnection(url);
            }
            catch (SQLException se) {
            System.out.println("Koneksi Database Gagal" + se);
            }
        }
        catch (ClassNotFoundException cnfe) {
            System.out.println("Driver Tidak Ditemukan... Error :" + cnfe);
            System.exit(0);
        }
    }
 private void tampilGolongan() {
        try {
            String sql = "Select kd_gol from golongan";
            Statement stat = kon.createStatement();
            ResultSet set = stat.executeQuery(sql);
            while (set.next()) {
                String gol = set.getString("kd_gol");
                comboGolongan.addItem(gol);
            }
        }
        catch (Exception se) {
        comboGolongan.addItem("Pilih");
        }

    }
 private void tampilDepartemen() {
        try {
            String sql = "Select kd_dep from departemen";
            Statement stat = kon.createStatement();
            ResultSet set = stat.executeQuery(sql);
            while (set.next()) {
                String dep = set.getString("kd_dep");
                comboDepart.addItem(dep);
            }
        }
        catch (Exception se) {
        comboDepart.addItem("Pilih");
        }

    }
 private void tampilJabatan() {
        try {
            String sql = "Select kd_jab from jabatan";
            Statement stat = kon.createStatement();
            ResultSet set = stat.executeQuery(sql);
            while (set.next()) {
                String jab = set.getString("kd_jab");
                comboJab.addItem(jab);
            }
        }
        catch (Exception se) {
        comboJab.addItem("Pilih");
        }

    }
 private void Baru(){
        btnSave.setText("Save");
        txtNip.requestFocus();
        txtNip.setText("");
        txtNama.setText("");
        comboGolongan.setSelectedItem("Pilih");
        comboDepart.setSelectedItem("Pilih");
        comboJab.setSelectedItem("Pilih");
        comboJK.setSelectedItem("Pilih");
        comboPendidikan.setSelectedItem("Pilih");
        comboTglIn.setSelectedItem("Tanggal");
        comboBlnIn.setSelectedItem("Bulan");
        comboThnIn.setSelectedItem("Tahun");
        comboTglOut.setSelectedItem("Tanggal");
        comboBlnOut.setSelectedItem("Bulan");
        comboThnOut.setSelectedItem("Tahun");
        txtAlamat.setText("");
        txtTelp.setText("");
        txtHp.setText("");

        try {
            Class.forName(KoneksiDatabase.driver);
            java.sql.Connection c = DriverManager.getConnection(KoneksiDatabase.database, KoneksiDatabase.user, KoneksiDatabase.pass);
            Statement s = c.createStatement();
            String sql = "select * from Pegawai";
            ResultSet rs = s.executeQuery(sql);

            final String[] headers = {"NIP", "Nama", "Golongan", "Departemen", "Jabatan","JK","Pend. Akhir","Tgl Masuk","Tgl Keluar","Alamat","Telp Rumah","HP"};
            rs.last();

            int n = rs.getRow();
            Object[][] data = new Object[n][12];
            int p = 0;
            rs.beforeFirst();
            while (rs.next()) {
                data[p][0] = rs.getString(1);
                data[p][1] = rs.getString(2);
                data[p][2] = rs.getString(3);
                data[p][3] = rs.getString(4);
                data[p][4] = rs.getString(5);
                data[p][5] = rs.getString(6);
                data[p][6] = rs.getString(7);
                data[p][7] = rs.getString(8);
                data[p][8] = rs.getString(9);
                data[p][9] = rs.getString(10);
                data[p][10] = rs.getString(11);
                data[p][11] = rs.getString(12);
                p++;
            }
            tblPegawai.setModel(new DefaultTableModel(data, headers));
            tblPegawai.setAlignmentX(CENTER_ALIGNMENT);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Koneksi, Ada Kesalahan.", "Warning", JOptionPane.WARNING_MESSAGE);
        }




 }
  private void Simpan() {
        if (txtNip.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Isi Nomor Induk Pegawai !!!", "Peringatan", JOptionPane.ERROR_MESSAGE);
            txtNip.requestFocus();
        } else if (txtNama.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Nama Pegawai Harus Diisi !!!", "Peringatan", JOptionPane.ERROR_MESSAGE);
            txtNama.requestFocus();
        } else {
            try {
                Class.forName(KoneksiDatabase.driver);
                java.sql.Connection c = DriverManager.getConnection(KoneksiDatabase.database, KoneksiDatabase.user, KoneksiDatabase.pass);
                Statement s = c.createStatement();
                String sql;

                Object kodeGol="";
                Object kodeDep="";
                Object kodeJab="";
                Object kelamin="";
                Object pend="";
                String tanggalMasuk="";
                String tanggalKeluar="";

		kodeGol=comboGolongan.getSelectedItem();
                kodeGol=comboGolongan.getSelectedItem();
		kodeDep=comboDepart.getSelectedItem();
		kodeJab=comboJab.getSelectedItem();
		kelamin=comboJK.getSelectedItem();
		pend=comboPendidikan.getSelectedItem();
                tanggalMasuk = comboThnIn.getSelectedItem() + "-" + comboBlnIn.getSelectedItem() + "-" + comboTglIn.getSelectedItem();
                tanggalKeluar = comboThnOut.getSelectedItem() + "-" + comboBlnOut.getSelectedItem() + "-" + comboTglOut.getSelectedItem();
                if(tanggalKeluar.equals("Tahun-Bulan-Tanggal")){
                tanggalKeluar="";
                }
 else
                {
 tanggalKeluar = tanggalKeluar;
 }

                if (btnSave.getText().equals("Save")) {
                    sql = "insert into pegawai values ('";
                    sql += txtNip.getText() + "','";
                    sql += txtNama.getText() + "','";
                    sql += kodeGol + "','";
		    sql += kodeDep + "','";
		    sql += kodeJab + "','";
		    sql += kelamin + "','";
		    sql += pend + "','";
                    sql += tanggalMasuk + "','";
                    sql += tanggalKeluar + "','";
		    sql += txtAlamat.getText() + "','";
                    sql += txtTelp.getText() + "','";
                    sql += txtHp.getText() + "')";

                    JOptionPane.showConfirmDialog(null, "Simpan Data Pegawai ...?", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    sql = "update pegawai set nama = '";
                    sql += txtNama.getText() + "', kd_gol='";
                    sql += kodeGol + "', kd_dep='";
                    sql += kodeDep + "', kd_jab='";
		    sql += kodeJab + "', jk='";
                    sql += kelamin + "', pendidikan='";
		    sql += pend + "', tgl_masuk='";
                    sql += tanggalMasuk + "', tgl_keluar='";
                    sql += tanggalKeluar + "', alamat='";
		    sql += txtAlamat.getText() + "', telp_rumah='";
                    sql += txtTelp.getText() + "', hp='";
                    sql += txtHp.getText() + "'";
                    sql += "where nip = '" + txtNip.getText() + "'";
                }
                s.executeUpdate(sql);
                if (btnSave.getText().equals("Edit")){
                JOptionPane.showMessageDialog(null, "Update Data Berhasil !!!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal Disimpan, Data Harus Lengkap !!!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
            Baru();
        }

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNip = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        comboGolongan = new javax.swing.JComboBox();
        comboDepart = new javax.swing.JComboBox();
        comboJab = new javax.swing.JComboBox();
        comboJK = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        comboTglOut = new javax.swing.JComboBox();
        comboBlnOut = new javax.swing.JComboBox();
        comboThnOut = new javax.swing.JComboBox();
        comboPendidikan = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        txtTelp = new javax.swing.JTextField();
        txtHp = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        comboTglIn = new javax.swing.JComboBox();
        comboBlnIn = new javax.swing.JComboBox();
        comboThnIn = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPegawai = new javax.swing.JTable();

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gerffa_indonesia_logo.jpg"))); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel12.setFont(new java.awt.Font("Pristina", 1, 48));
        jLabel12.setText("  Data Pegawai PT ");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Olah Data Karyawan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jLabel1.setText("NIP");

        jLabel2.setText("Nama Pegawai");

        jLabel3.setText("Golongan");

        jLabel4.setText("Departemen");

        jLabel5.setText("Jabatan");

        jLabel6.setText("Jenis Kelamin");

        comboGolongan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih" }));
        comboGolongan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboGolonganActionPerformed(evt);
            }
        });

        comboDepart.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih" }));

        comboJab.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih" }));

        comboJK.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih", "L", "P" }));

        jLabel7.setText("Pend Terakhir");

        jLabel8.setText("Tanggal Keluar");

        jLabel10.setText("Alamat");

        jLabel11.setText("Telp. Rumah");

        comboTglOut.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tanggal", "01", "02", "02", "04", "05", "05", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        comboBlnOut.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bulan", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        comboThnOut.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tahun", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015" }));

        comboPendidikan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih", "SMA/SMK", "Diploma 1", "Diploma 2", "Diploma 3", "S1/D4", "Magister/S2 ", "Doktor/S3 " }));

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane1.setViewportView(txtAlamat);

        jLabel9.setText("Nomor HP");

        btnNew.setText("NEW");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jLabel13.setText("Tanggal Masuk");

        comboTglIn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tanggal", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        comboBlnIn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bulan", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        comboThnIn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tahun", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboPendidikan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboJK, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboJab, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboDepart, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboGolongan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNama)
                    .addComponent(txtNip, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                .addGap(95, 95, 95)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8))
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTelp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(txtHp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete)))
                        .addGap(18, 18, 18)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(comboTglIn, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboTglOut, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboBlnIn, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBlnOut, 0, 67, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboThnIn, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboThnOut, 0, 74, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(comboTglIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBlnIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboThnIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(comboGolongan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(comboDepart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboTglOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBlnOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboThnOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jScrollPane1, 0, 0, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(comboJab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboJK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSave)
                        .addComponent(btnNew)
                        .addComponent(btnDelete)
                        .addComponent(btnClose))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(comboPendidikan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data View | Tabel Karyawan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        tblPegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPegawaiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPegawai);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboGolonganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboGolonganActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_comboGolonganActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        Baru();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        Simpan();
}//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if (txtNip.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Isi NIP Pegawai yang Akan dihapus dari Database!!!", "Peringatan", JOptionPane.ERROR_MESSAGE);
            txtNip.requestFocus();
        }

        else {
            try {
                Class.forName(KoneksiDatabase.driver);
                java.sql.Connection c = DriverManager.getConnection(KoneksiDatabase.database, KoneksiDatabase.user, KoneksiDatabase.pass);
                Statement s = c.createStatement();
                String sql = "DELETE FROM Pegawai where nip ='"+txtNip.getText()+"'";
                s.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data pegawai dengan NIP : "+txtNip.getText()+" Berhasil Dihapus !");
                Baru();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Kemungkinan terjadi kegagalan koneksi", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:     
        dispose();
}//GEN-LAST:event_btnCloseActionPerformed

    private void tblPegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPegawaiMouseClicked
        // TODO add your handling code here:
        String tanggalMasuk = tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 7).toString();
        String tanggalKeluar = tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 8).toString();
        btnSave.setText("Edit");
        txtNip.setText(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 0).toString());
        txtNama.setText(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 1).toString());
        comboGolongan.setSelectedItem(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 2).toString());
        comboDepart.setSelectedItem(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 3).toString());
        comboJab.setSelectedItem(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 4).toString());
        comboJK.setSelectedItem(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 5).toString());
        comboPendidikan.setSelectedItem(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 6).toString());
        comboTglIn.setSelectedItem(tanggalMasuk.substring(8));
        comboBlnIn.setSelectedItem(tanggalMasuk.substring(5, 7));
        comboThnIn.setSelectedItem(tanggalMasuk.substring(0, 4));
        if(tanggalKeluar.equals("")){
            comboTglOut.setSelectedItem("Tanggal");
            comboBlnOut.setSelectedItem("Bulan");
            comboThnOut.setSelectedItem("Tahun");
        } else{
            comboTglOut.setSelectedItem(tanggalKeluar.substring(8));
            comboBlnOut.setSelectedItem(tanggalKeluar.substring(5, 7));
            comboThnOut.setSelectedItem(tanggalKeluar.substring(0, 4));
        }
        txtAlamat.setText(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 9).toString());
        txtTelp.setText(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 10).toString());
        txtHp.setText(tblPegawai.getValueAt(tblPegawai.getSelectedRow(), 11).toString());
    }//GEN-LAST:event_tblPegawaiMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox comboBlnIn;
    private javax.swing.JComboBox comboBlnOut;
    private javax.swing.JComboBox comboDepart;
    private javax.swing.JComboBox comboGolongan;
    private javax.swing.JComboBox comboJK;
    private javax.swing.JComboBox comboJab;
    private javax.swing.JComboBox comboPendidikan;
    private javax.swing.JComboBox comboTglIn;
    private javax.swing.JComboBox comboTglOut;
    private javax.swing.JComboBox comboThnIn;
    private javax.swing.JComboBox comboThnOut;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblPegawai;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtHp;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNip;
    private javax.swing.JTextField txtTelp;
    // End of variables declaration//GEN-END:variables

}
