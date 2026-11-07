package T01_243040041.T01;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;

public class FormPendaftaranVoli {

    public static void main(String[] args) {
        // 1. Inisialisasi antarmuka secara aman di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(FormPendaftaranVoli::buatDanTampilkanGUI);
    }

    private static void buatDanTampilkanGUI() {
        JFrame frame = new JFrame("Formulir Pendaftaran Turnamen Bola Voli");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Container utama menggunakan BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Area Atas (NORTH): Judul Form
        JLabel labelJudul = new JLabel("PENDAFTARAN TURNAMEN BOLA VOLI KAMPUS", JLabel.CENTER);
        labelJudul.setFont(labelJudul.getFont().deriveFont(16.0f));
        mainPanel.add(labelJudul, BorderLayout.NORTH);

        // Area Tengah (CENTER): Penampung 3 grup form (GridLayout 3x1)
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // =========================================================================
        // GRUP 1: IDENTITAS TIM & KAPTEN (GridLayout 3 Baris, 2 Kolom)
        // =========================================================================
        JPanel panelIdentitas = new JPanel(new GridLayout(3, 2, 8, 8));
        panelIdentitas.setBorder(BorderFactory.createTitledBorder("1. Identitas Tim & Kapten"));
        
        JTextField txtNamaTim = new JTextField(20);
        JTextField txtKapten = new JTextField(20);
        JTextField txtKontak = new JTextField(20);

        panelIdentitas.add(new JLabel("Nama Tim Voli:"));
        panelIdentitas.add(txtNamaTim);
        
        panelIdentitas.add(new JLabel("Nama Kapten:"));
        panelIdentitas.add(txtKapten);
        
        panelIdentitas.add(new JLabel("No. WhatsApp Kapten:"));
        panelIdentitas.add(txtKontak);
        
        contentPanel.add(panelIdentitas);

        // =========================================================================
        // GRUP 2: PILIHAN KATEGORI & FASILITAS (GridLayout & FlowLayout)
        // =========================================================================
        JPanel panelKegiatan = new JPanel(new GridLayout(3, 1, 6, 6));
        panelKegiatan.setBorder(BorderFactory.createTitledBorder("2. Pilihan Turnamen & Fasilitas"));

        // Baris A: JComboBox (Divisi Turnamen)
        JPanel panelDivisi = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelDivisi.add(new JLabel("Divisi Turnamen: "));
        String[] daftarDivisi = {"-- Pilih Divisi Turnamen --", "Antar Mahasiswa / Fakultas", "Pelajar / SMA", "Umum / Klub Regional"};
        JComboBox<String> comboDivisi = new JComboBox<>(daftarDivisi);
        panelDivisi.add(comboDivisi);
        panelKegiatan.add(panelDivisi);

        // Baris B: JRadioButton & ButtonGroup (Eksklusif)
        JPanel panelKategori = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelKategori.add(new JLabel("Kategori Tim: "));
        JRadioButton radioPutra = new JRadioButton("Putra", true);
        JRadioButton radioPutri = new JRadioButton("Putri");
        JRadioButton radioCampuran = new JRadioButton("Campuran");
        ButtonGroup grupKategori = new ButtonGroup();
        grupKategori.add(radioPutra);
        grupKategori.add(radioPutri);
        grupKategori.add(radioCampuran);
        panelKategori.add(radioPutra);
        panelKategori.add(radioPutri);
        panelKategori.add(radioCampuran);
        panelKegiatan.add(panelKategori);

        // Baris C: JCheckBox (Persyaratan & Fasilitas Ekstra)
        JPanel panelFasilitas = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelFasilitas.add(new JLabel("Persyaratan: "));
        JCheckBox cbKostum = new JCheckBox("Kostum Resmi");
        JCheckBox cbSuratSehat = new JCheckBox("Surat Sehat Tim");
        panelFasilitas.add(cbKostum);
        panelFasilitas.add(cbSuratSehat);
        panelKegiatan.add(panelFasilitas);

        contentPanel.add(panelKegiatan);

        // =========================================================================
        // GRUP 3: CATATAN TAMBAHAN (BorderLayout & JScrollPane)
        // =========================================================================
        JPanel panelCatatan = new JPanel(new BorderLayout(5, 5));
        panelCatatan.setBorder(BorderFactory.createTitledBorder("3. Catatan Tambahan"));
        panelCatatan.add(new JLabel("Daftar Nama Pemain Inti & Cadangan (Posisi):"), BorderLayout.NORTH);
        
        JTextArea areaCatatan = new JTextArea(4, 25);
        areaCatatan.setLineWrap(true);
        areaCatatan.setWrapStyleWord(true);
        panelCatatan.add(new JScrollPane(areaCatatan), BorderLayout.CENTER);

        contentPanel.add(panelCatatan);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // =========================================================================
        // AREA BAWAH (SOUTH): TOMBOL AKSI (FlowLayout)
        // =========================================================================
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnReset = new JButton("Reset Form");
        JButton btnDaftar = new JButton("Daftar Tim");
        
        panelAksi.add(btnReset);
        panelAksi.add(btnDaftar);
        mainPanel.add(panelAksi, BorderLayout.SOUTH);

        // =========================================================================
        // EVENT HANDLING (Aksi Tombol)
        // =========================================================================
        
        // Aksi Tombol Reset
        btnReset.addActionListener(e -> {
            txtNamaTim.setText("");
            txtKapten.setText("");
            txtKontak.setText("");
            comboDivisi.setSelectedIndex(0);
            radioPutra.setSelected(true);
            cbKostum.setSelected(false);
            cbSuratSehat.setSelected(false);
            areaCatatan.setText("");
        });

        // Aksi Tombol Daftar (Memunculkan Notifikasi JOptionPane)
        btnDaftar.addActionListener(e -> {
            String namaTim = txtNamaTim.getText();
            String namaKapten = txtKapten.getText();
            String divisi = comboDivisi.getSelectedItem().toString();
            
            // Cek kategori yang dipilih
            String kategori = "Putra";
            if (radioPutri.isSelected()) kategori = "Putri";
            else if (radioCampuran.isSelected()) kategori = "Campuran";

            if (namaTim.isEmpty() || namaKapten.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nama Tim dan Nama Kapten wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                String pesan = "Pendaftaran Berhasil Dikirim!\n" +
                               "Nama Tim: " + namaTim + "\n" +
                               "Kapten: " + namaKapten + "\n" +
                               "Divisi: " + divisi + "\n" +
                               "Kategori: " + kategori;
                JOptionPane.showMessageDialog(frame, pesan, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Penentuan Ukuran Otomatis & Penempatan di Tengah Layar
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}