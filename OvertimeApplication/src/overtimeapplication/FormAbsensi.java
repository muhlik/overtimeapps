/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FormAbsensi.java
 *
 * Created on 16 Agu 11, 12:28:37
 */

package overtimeapplication;

/**
 *
 * @author MuhLix-Tech
 */
import javax.swing.table.*;
import javax.swing.*;
import java.lang.Integer.*;
import java.sql.*;
import java.awt.Dimension.*;

public class FormAbsensi extends javax.swing.JInternalFrame {

    /** Creates new form FormAbsensi */
    public FormAbsensi() {
        initComponents();
        Baru();
    }

    private void Baru(){
        btnSave.setText("Save");
        txtNip.requestFocus();
        txtNip.setText("");

        cmbJam_in.setSelectedItem("08");
        cmbMenit_in.setSelectedItem("00");
        cmbJam_out.setSelectedItem("17");
	cmbMenit_out.setSelectedItem("00");
        cmbTgl_in.setSelectedItem("Tgl");
        cmbBln_in.setSelectedItem("Bln");
        cmbThn_in.setSelectedItem("Thn");

        try {
            Class.forName(KoneksiDatabase.driver);
            java.sql.Connection c = DriverManager.getConnection(KoneksiDatabase.database, KoneksiDatabase.user, KoneksiDatabase.pass);
            Statement s = c.createStatement();
            String sql = "select * from absensi_tmp";
            ResultSet rs = s.executeQuery(sql);

            final String[] headers = {"NIP", "Tanggal Absensi", "Jam Masuk", "Jam Pulang"};
            rs.last();

            int n = rs.getRow();
            Object[][] data = new Object[n][4];
            int p = 0;
            rs.beforeFirst();
            while (rs.next()) {
                data[p][0] = rs.getString(1);
                data[p][1] = rs.getString(2);
                data[p][2] = rs.getString(3);
                data[p][3] = rs.getString(4);
                p++;
            }
            tblAbsensi.setModel(new DefaultTableModel(data, headers));
            tblAbsensi.setAlignmentX(CENTER_ALIGNMENT);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Koneksi, Ada Kesalahan.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
 }
    private void HitungLembur(){
        String settingMasuk ="";
        String settingPulang = "";
        String jamMasuk="";
        Object jamKeluar="";
        Object tanggalAbsen="";
        Object kodeAbsen="";
        String uangMakan,uangTransport,hari="",tipe_hari="",tipelembur_pegawai;

        jamMasuk = cmbJam_in.getSelectedItem() + ":" + cmbMenit_in.getSelectedItem() + ":00";
        jamKeluar = cmbJam_out.getSelectedItem() + ":" + cmbMenit_out.getSelectedItem() + ":00";
        tanggalAbsen = cmbThn_in.getSelectedItem() + "-" + cmbBln_in.getSelectedItem() + "-" + cmbTgl_in.getSelectedItem();
        kodeAbsen = cmbTgl_in.getSelectedItem() +""+ cmbBln_in.getSelectedItem() + "" + cmbThn_in.getSelectedItem() +"" +txtNip.getText();
        try {
                Class.forName(KoneksiDatabase.driver);
                java.sql.Connection c = DriverManager.getConnection(KoneksiDatabase.database, KoneksiDatabase.user, KoneksiDatabase.pass);
                Statement s = c.createStatement();
                String sql;
                String jenisLembur=""; String telat=""; String Lembur="00:00:00"; String total_lembur="00:00:00";
                ResultSet rs;

                rs = s.executeQuery("select DAYNAME('"+ tanggalAbsen + "') as a; ");if(rs.next()) hari = rs.getString("a");

                if (hari.equals("Sunday")){
                hari = "Minggu"; tipe_hari="Hari Libur";
                }else if(hari.equals("Saturday")){
                hari = "Sabtu"; tipe_hari="Hari Libur";
                }else if(hari.equals("Friday")){
                hari = "Jumat"; tipe_hari="Regular";
                }else if(hari.equals("Thursday")){
                hari = "Kamis"; tipe_hari="Regular";
                }else if(hari.equals("Wednesday")){
                hari = "Rabu"; tipe_hari="Regular";
                }else if(hari.equals("Tuesday")){
                hari = "Selasa"; tipe_hari="Regular";
                }else{
                hari = "Senin"; tipe_hari="Regular";
                }

                rs = s.executeQuery("select * from setting_waktu ");if(rs.next()) settingMasuk = rs.getString("start_worktime");
                rs = s.executeQuery("select * from setting_waktu ");if(rs.next()) settingPulang = rs.getString("end_worktime");
                String x = cmbJam_in.getSelectedItem().toString();
                int y = Integer.parseInt(x);
                if(y >= 8){
                rs = s.executeQuery("SELECT TIMEDIFF('"+ jamMasuk + "','"+ settingMasuk + "')");if(rs.next()) telat = rs.getTime(1).toString();
                }else{
                telat="00:00:00";
                }
                if (tipe_hari.equals("Hari Libur")){
                    rs = s.executeQuery("SELECT TIMEDIFF('"+ jamKeluar + "','"+ jamMasuk + "')");if(rs.next()) Lembur = rs.getTime(1).toString();
                }
                else
                {
                String a = cmbJam_out.getSelectedItem().toString();
                int b = Integer.parseInt(a);
                if(b>=17){
                rs = s.executeQuery("SELECT TIMEDIFF('"+ jamKeluar + "','"+ settingPulang + "')");if(rs.next()) Lembur = rs.getTime(1).toString();
                }
                }
               //ini masih salah --> rs = s.executeQuery("SELECT jenis_lembur FROM setting_tarif_lembur where setting_tarif_lembur.nip = '"+ txtNip.getText() +"' ");if(rs.next()) jenisLembur = rs.getTime(1).toString();
                
                rs = s.executeQuery("SELECT SUBTIME('"+ Lembur + "','"+ telat + "')");if(rs.next()) total_lembur = rs.getTime(1).toString();

                if (btnSave.getText().equals("Save")) {
                  sql = "insert into absensi_lembur values('"+ kodeAbsen +"','"+ txtNip.getText() +"','"+ tanggalAbsen +"','"+ jamMasuk +"','"+ jamKeluar +"','"+ hari +"','"+ tipe_hari +"','"+ telat +"','"+ Lembur +"','"+ jenisLembur +"','"+ total_lembur +"','0','0')";
                   s.executeUpdate(sql);
                }   
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Data Absen Gagal Diproses !!!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
    }
    private void Simpan() {
        if (txtNip.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Isi Nomor Induk Pegawai !!!", "Peringatan", JOptionPane.ERROR_MESSAGE);
            txtNip.requestFocus();
        }  else {
            try {
                Class.forName(KoneksiDatabase.driver);
                java.sql.Connection c = DriverManager.getConnection(KoneksiDatabase.database, KoneksiDatabase.user, KoneksiDatabase.pass);
                Statement s = c.createStatement();
                String sql;
                ResultSet rs;

                Object jamMasuk="";
                Object jamKeluar="";
                Object tanggalAbsen="";
               
                jamMasuk = cmbJam_in.getSelectedItem() + ":" + cmbMenit_in.getSelectedItem() + ":00";
		jamKeluar = cmbJam_out.getSelectedItem() + ":" + cmbMenit_out.getSelectedItem() + ":00";
               	tanggalAbsen = cmbThn_in.getSelectedItem() + "-" + cmbBln_in.getSelectedItem() + "-" + cmbTgl_in.getSelectedItem();
                if (btnSave.getText().equals("Save")) {
                    
                    HitungLembur();

                    sql = "insert into absensi_tmp values ('";
                    sql += txtNip.getText() + "','";
                    sql += tanggalAbsen + "','";
		    sql += jamMasuk + "','";
		    sql += jamKeluar + "')";
                } else {
                    sql = "update absensi_tmp set time_in = '";
                    sql += jamMasuk + "', time_out='";
                    sql += jamKeluar + "'";
                    sql += "where tgl_absen = '"+ tanggalAbsen + "' AND nip = '" + txtNip.getText() + "'";
                }
                s.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan!!!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
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

        personPanel = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblCategory = new javax.swing.JLabel();
        txtNip = new javax.swing.JTextField();
        cmbJam_in = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        cmbJam_out = new javax.swing.JComboBox();
        cmbMenit_in = new javax.swing.JComboBox();
        cmbMenit_out = new javax.swing.JComboBox();
        cmbTgl_in = new javax.swing.JComboBox();
        cmbBln_in = new javax.swing.JComboBox();
        cmbThn_in = new javax.swing.JComboBox();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        tbPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        tblAbsensi = new javax.swing.JTable();

        personPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Absensi"));

        lblName.setText("NIP");

        lblAddress.setText("Tanggal Absensi");

        lblCategory.setText("Jam Masuk");

        cmbJam_in.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));

        jLabel1.setText("Jam Pulang");

        cmbJam_out.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        cmbMenit_in.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        cmbMenit_out.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        cmbTgl_in.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tgl", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        cmbBln_in.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bln", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        cmbThn_in.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Thn", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" }));

        btnNew.setText("NEW");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSave.setText("SAVE");
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

        javax.swing.GroupLayout personPanelLayout = new javax.swing.GroupLayout(personPanel);
        personPanel.setLayout(personPanelLayout);
        personPanelLayout.setHorizontalGroup(
            personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(personPanelLayout.createSequentialGroup()
                        .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName)
                            .addComponent(lblCategory))
                        .addGap(34, 34, 34)
                        .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(personPanelLayout.createSequentialGroup()
                                .addComponent(cmbJam_in, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbMenit_in, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtNip, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(personPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(32, 32, 32)
                        .addComponent(cmbJam_out, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMenit_out, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80)
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAddress)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbTgl_in, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(personPanelLayout.createSequentialGroup()
                        .addComponent(cmbBln_in, 0, 72, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbThn_in, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personPanelLayout.createSequentialGroup()
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClose)))
                .addContainerGap())
        );
        personPanelLayout.setVerticalGroup(
            personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txtNip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbThn_in, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAddress)
                    .addComponent(cmbTgl_in, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBln_in, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbJam_in, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCategory)
                    .addComponent(cmbMenit_in, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbJam_out, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(cmbMenit_out, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose)
                    .addComponent(btnDelete)
                    .addComponent(btnSave)
                    .addComponent(btnNew)))
        );

        tbPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("View Absensi"));

        tblAbsensi.setModel(new javax.swing.table.DefaultTableModel(
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
        tblAbsensi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAbsensiMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(tblAbsensi);

        javax.swing.GroupLayout tbPanelLayout = new javax.swing.GroupLayout(tbPanel);
        tbPanel.setLayout(tbPanelLayout);
        tbPanelLayout.setHorizontalGroup(
            tbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
        );
        tbPanelLayout.setVerticalGroup(
            tbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tbPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tbPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(personPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(personPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tbPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:

        Baru();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        Simpan();
}//GEN-LAST:event_btnSaveActionPerformed

    private void tblAbsensiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAbsensiMouseClicked
        // TODO add your handling code here:
        String tanggalAbsen = tblAbsensi.getValueAt(tblAbsensi.getSelectedRow(), 1).toString();
        String jamMasuk = tblAbsensi.getValueAt(tblAbsensi.getSelectedRow(), 2).toString();
        String jamKeluar = tblAbsensi.getValueAt(tblAbsensi.getSelectedRow(), 3).toString();
        btnSave.setText("Edit");
        txtNip.setText(tblAbsensi.getValueAt(tblAbsensi.getSelectedRow(), 0).toString());

        cmbTgl_in.setSelectedItem(tanggalAbsen.substring(8));
        cmbBln_in.setSelectedItem(tanggalAbsen.substring(5, 7));
        cmbThn_in.setSelectedItem(tanggalAbsen.substring(0, 4));

        cmbJam_in.setSelectedItem(jamMasuk.substring(0, 2));
        cmbMenit_out.setSelectedItem(jamMasuk.substring(2, 4));

        cmbJam_out.setSelectedItem(jamKeluar.substring(0, 2));
        cmbMenit_out.setSelectedItem(jamKeluar.substring(2, 4));

}//GEN-LAST:event_tblAbsensiMouseClicked

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        String tanggalAbsen = cmbThn_in.getSelectedItem() + "-" + cmbBln_in.getSelectedItem() + "-" + cmbTgl_in.getSelectedItem();
        if (txtNip.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Isi NIP Pegawai yang Akan dihapus dari Data Absensi!!!", "Peringatan", JOptionPane.ERROR_MESSAGE);
            txtNip.requestFocus();
        }

        else {
            try {
                Class.forName(KoneksiDatabase.driver);
                java.sql.Connection c = DriverManager.getConnection(KoneksiDatabase.database, KoneksiDatabase.user, KoneksiDatabase.pass);
                Statement s = c.createStatement();
                String sql = "DELETE FROM absensi_tmp where nip ='"+txtNip.getText()+"' AND tgl_absen = '"+ tanggalAbsen + "'";
                s.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus !");
                Baru();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Kemungkinan terjadi kegagalan koneksi", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbBln_in;
    private javax.swing.JComboBox cmbJam_in;
    private javax.swing.JComboBox cmbJam_out;
    private javax.swing.JComboBox cmbMenit_in;
    private javax.swing.JComboBox cmbMenit_out;
    private javax.swing.JComboBox cmbTgl_in;
    private javax.swing.JComboBox cmbThn_in;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblName;
    private javax.swing.JPanel personPanel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel tbPanel;
    private javax.swing.JTable tblAbsensi;
    private javax.swing.JTextField txtNip;
    // End of variables declaration//GEN-END:variables

}
