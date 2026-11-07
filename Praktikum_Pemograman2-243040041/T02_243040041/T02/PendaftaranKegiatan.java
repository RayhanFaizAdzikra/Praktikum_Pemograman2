package T02_243040041.T02;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

/**
 * Tugas 2 - Event handling, validasi, dan navigasi Swing
 * IF21W0508 - Praktikum Pemrograman II
 *
 * Aplikasi pendaftaran kegiatan dengan tiga halaman (CardLayout):
 * Formulir, Ringkasan, dan Bantuan. Hanya ada satu JFrame.
 */
public class PendaftaranKegiatan extends JFrame {

    // ===== Nama kartu untuk CardLayout =====
    private static final String CARD_FORM = "FORM";
    private static final String CARD_RINGKASAN = "RINGKASAN";
    private static final String CARD_BANTUAN = "BANTUAN";

    private static final String PLACEHOLDER_KEGIATAN = "-- Pilih kegiatan --";
    private static final String[] DAFTAR_KEGIATAN = {
            PLACEHOLDER_KEGIATAN, "Seminar Teknologi", "Workshop Java",
            "Lomba Coding", "Bakti Sosial", "Pelatihan Kepemimpinan"
    };

    private static final Pattern POLA_EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");

    // ===== Komponen utama =====
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel kartuPanel = new JPanel(cardLayout);

    // ===== Komponen formulir =====
    private final JTextField txtNama = new JTextField(22);
    private final JTextField txtEmail = new JTextField(22);
    private final JTextField txtUmur = new JTextField(8);
    private final JComboBox<String> cmbKegiatan = new JComboBox<>(DAFTAR_KEGIATAN);
    private final JLabel lblStatus = new JLabel(" ");

    // ===== Komponen ringkasan =====
    private final JTextArea areaRingkasan = new JTextArea(10, 30);

    // ===== Keadaan data =====
    // Snapshot data yang terakhir disimpan: {nama, email, umur, kegiatan}
    private String[] dataTersimpan = {"", "", "", PLACEHOLDER_KEGIATAN};
    private boolean sudahPernahSimpan = false;

    public PendaftaranKegiatan() {
        super("Pendaftaran Kegiatan");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                keluar();
            }
        });

        setLayout(new BorderLayout());
        add(buatBarNavigasi(), BorderLayout.NORTH);

        kartuPanel.add(buatPanelFormulir(), CARD_FORM);
        kartuPanel.add(buatPanelRingkasan(), CARD_RINGKASAN);
        kartuPanel.add(buatPanelBantuan(), CARD_BANTUAN);
        add(kartuPanel, BorderLayout.CENTER);

        pasangPemantauPerubahan();

        pack();
        setMinimumSize(new Dimension(480, 380));
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------
    // Pembuatan UI
    // ------------------------------------------------------------------

    private JPanel buatBarNavigasi() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        JButton btnForm = new JButton("Formulir");
        JButton btnRingkasan = new JButton("Ringkasan");
        JButton btnBantuan = new JButton("Bantuan");
        btnForm.addActionListener(e -> tampilkanKartu(CARD_FORM));
        btnRingkasan.addActionListener(e -> {
            isiRingkasan();
            tampilkanKartu(CARD_RINGKASAN);
        });
        btnBantuan.addActionListener(e -> tampilkanKartu(CARD_BANTUAN));
        bar.add(btnForm);
        bar.add(btnRingkasan);
        bar.add(btnBantuan);
        return bar;
    }

    private JPanel buatPanelFormulir() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JPanel isian = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 4, 5, 4);
        gbc.anchor = GridBagConstraints.WEST;

        tambahBaris(isian, gbc, 0, "Nama:", txtNama);
        tambahBaris(isian, gbc, 1, "Email:", txtEmail);
        tambahBaris(isian, gbc, 2, "Umur:", txtUmur);
        tambahBaris(isian, gbc, 3, "Kegiatan:", cmbKegiatan);

        lblStatus.setForeground(new Color(0xB00020));

        JButton btnSimpan = new JButton("Simpan");
        JButton btnTampilkan = new JButton("Tampilkan");
        JButton btnReset = new JButton("Reset");
        JButton btnKeluar = new JButton("Keluar");
        btnSimpan.addActionListener(e -> simpan());
        btnTampilkan.addActionListener(e -> tampilkan());
        btnReset.addActionListener(e -> reset());
        btnKeluar.addActionListener(e -> keluar());

        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        tombol.add(btnSimpan);
        tombol.add(btnTampilkan);
        tombol.add(btnReset);
        tombol.add(btnKeluar);

        JPanel bawah = new JPanel(new BorderLayout());
        bawah.add(lblStatus, BorderLayout.NORTH);
        bawah.add(tombol, BorderLayout.SOUTH);

        panel.add(isian, BorderLayout.CENTER);
        panel.add(bawah, BorderLayout.SOUTH);
        return panel;
    }

    private void tambahBaris(JPanel p, GridBagConstraints gbc, int baris, String label, JComponent komponen) {
        gbc.gridy = baris;
        gbc.gridx = 0;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(komponen, gbc);
    }

    private JPanel buatPanelRingkasan() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        areaRingkasan.setEditable(false);
        areaRingkasan.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        panel.add(new JLabel("Ringkasan Pendaftaran"), BorderLayout.NORTH);
        panel.add(new JScrollPane(areaRingkasan), BorderLayout.CENTER);

        JButton btnKembali = new JButton("Kembali");
        JButton btnKeluar = new JButton("Keluar");
        btnKembali.addActionListener(e -> tampilkanKartu(CARD_FORM)); // data form tetap utuh
        btnKeluar.addActionListener(e -> keluar());
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        tombol.add(btnKembali);
        tombol.add(btnKeluar);
        panel.add(tombol, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buatPanelBantuan() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JTextArea teks = new JTextArea(
                "BANTUAN\n\n"
                + "Aturan pengisian:\n"
                + "1. Nama wajib diisi.\n"
                + "2. Email harus berformat wajar, contoh: nama@domain.com.\n"
                + "3. Umur berupa bilangan bulat antara 15 dan 100.\n"
                + "4. Kegiatan wajib dipilih.\n\n"
                + "Tombol:\n"
                + "- Simpan: memvalidasi lalu menyimpan data.\n"
                + "- Tampilkan: memvalidasi lalu membuka halaman Ringkasan.\n"
                + "- Reset: mengosongkan seluruh isian formulir.\n"
                + "- Kembali: pindah ke formulir tanpa menghapus isian.\n"
                + "- Keluar: menutup aplikasi (ada konfirmasi bila ada\n"
                + "  perubahan yang belum disimpan).");
        teks.setEditable(false);
        teks.setOpaque(false);
        panel.add(teks, BorderLayout.CENTER);

        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> tampilkanKartu(CARD_FORM));
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tombol.add(btnKembali);
        panel.add(tombol, BorderLayout.SOUTH);
        return panel;
    }

    // ------------------------------------------------------------------
    // Event handler
    // ------------------------------------------------------------------

    private void tampilkanKartu(String nama) {
        cardLayout.show(kartuPanel, nama);
    }

    private void simpan() {
        if (!validasi()) {
            return;
        }
        dataTersimpan = ambilDataForm();
        sudahPernahSimpan = true;
        lblStatus.setText(" ");
        JOptionPane.showMessageDialog(this, "Data berhasil disimpan.", "Berhasil",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void tampilkan() {
        if (!validasi()) {
            return;
        }
        lblStatus.setText(" ");
        isiRingkasan();
        tampilkanKartu(CARD_RINGKASAN);
    }

    private void reset() {
        txtNama.setText("");
        txtEmail.setText("");
        txtUmur.setText("");
        cmbKegiatan.setSelectedIndex(0);
        lblStatus.setText(" ");
        txtNama.requestFocusInWindow();
    }

    private void keluar() {
        if (adaPerubahanBelumDisimpan()) {
            int pilihan = JOptionPane.showConfirmDialog(this,
                    "Ada perubahan yang belum disimpan.\nTetap keluar dari aplikasi?",
                    "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (pilihan != JOptionPane.YES_OPTION) {
                return; // pembatalan: tetap di aplikasi, data tidak berubah
            }
        }
        dispose();
        System.exit(0);
    }

    // ------------------------------------------------------------------
    // Validasi
    // ------------------------------------------------------------------

    /** Memvalidasi satu per satu. Kesalahan pertama ditampilkan dan kontrolnya difokuskan. */
    private boolean validasi() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) {
            return gagal("Nama wajib diisi.", txtNama);
        }

        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            return gagal("Email wajib diisi.", txtEmail);
        }
        if (!POLA_EMAIL.matcher(email).matches()) {
            return gagal("Format email tidak valid. Contoh: nama@domain.com", txtEmail);
        }

        String umurTeks = txtUmur.getText().trim();
        if (umurTeks.isEmpty()) {
            return gagal("Umur wajib diisi.", txtUmur);
        }
        int umur;
        try {
            umur = Integer.parseInt(umurTeks);
        } catch (NumberFormatException ex) {
            return gagal("Umur harus berupa bilangan bulat (tanpa huruf atau desimal).", txtUmur);
        }
        if (umur < 15 || umur > 100) {
            return gagal("Umur harus antara 15 dan 100 tahun.", txtUmur);
        }

        if (cmbKegiatan.getSelectedIndex() <= 0) {
            return gagal("Silakan pilih salah satu kegiatan.", cmbKegiatan);
        }
        return true;
    }

    private boolean gagal(String pesan, JComponent kontrol) {
        lblStatus.setText(pesan);
        JOptionPane.showMessageDialog(this, pesan, "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
        kontrol.requestFocusInWindow();
        if (kontrol instanceof JTextField) {
            ((JTextField) kontrol).selectAll();
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Data & keadaan
    // ------------------------------------------------------------------

    private String[] ambilDataForm() {
        return new String[]{
                txtNama.getText().trim(),
                txtEmail.getText().trim(),
                txtUmur.getText().trim(),
                String.valueOf(cmbKegiatan.getSelectedItem())
        };
    }

    private boolean adaPerubahanBelumDisimpan() {
        return !java.util.Arrays.equals(ambilDataForm(), dataTersimpan);
    }

    private void isiRingkasan() {
        String[] d = ambilDataForm();
        boolean tersimpan = !adaPerubahanBelumDisimpan() && sudahPernahSimpan;
        StringBuilder sb = new StringBuilder();
        sb.append("Nama     : ").append(d[0].isEmpty() ? "-" : d[0]).append('\n');
        sb.append("Email    : ").append(d[1].isEmpty() ? "-" : d[1]).append('\n');
        sb.append("Umur     : ").append(d[2].isEmpty() ? "-" : d[2]).append('\n');
        sb.append("Kegiatan : ").append(PLACEHOLDER_KEGIATAN.equals(d[3]) ? "-" : d[3]).append('\n');
        sb.append('\n').append("Status   : ")
          .append(tersimpan ? "Sudah disimpan" : "Belum disimpan");
        areaRingkasan.setText(sb.toString());
    }

    private void pasangPemantauPerubahan() {
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { lblStatus.setText(" "); }
            public void removeUpdate(DocumentEvent e) { lblStatus.setText(" "); }
            public void changedUpdate(DocumentEvent e) { lblStatus.setText(" "); }
        };
        txtNama.getDocument().addDocumentListener(dl);
        txtEmail.getDocument().addDocumentListener(dl);
        txtUmur.getDocument().addDocumentListener(dl);
    }

    // ------------------------------------------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PendaftaranKegiatan().setVisible(true));
    }
}