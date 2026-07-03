package com.vn.test.demob1.LAB.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Ung dung desktop Swing quan ly sinh vien (CRUD) thong qua Firebase Realtime Database REST API.
 */
public class StudentSwingApp extends JFrame {

    // Dia chi Firebase host
    private static final String HOST = "https://fpolyedu.firebaseio.com";

    // Cong cu chuyen doi JSON <-> Java
    private final ObjectMapper mapper = new ObjectMapper();

    // Cac o nhap lieu tren form
    private final JTextField txtId = new JTextField(15);
    private final JTextField txtName = new JTextField(15);
    private final JTextField txtMark = new JTextField(15);
    private final JRadioButton rdMale = new JRadioButton("Male");
    private final JRadioButton rdFemale = new JRadioButton("Female");
    private final ButtonGroup genderGroup = new ButtonGroup();

    // Cac nut chuc nang
    private final JButton btnCreate = new JButton("Create");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnReset = new JButton("Reset");

    // Bang hien thi danh sach sinh vien
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Id", "Full Name", "Gender", "Mark"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // khong cho phep sua truc tiep tren bang
        }
    };
    private final JTable table = new JTable(tableModel);

    // Danh sach song song: moi dong bang -> khoa (key) tren Firebase
    private final List<String> rowKeys = new ArrayList<>();

    // Khoa cua sinh vien dang duoc chon (dung cho update/delete)
    private String selectedKey = null;

    public StudentSwingApp() {
        super("Quản lý sinh viên");
        initComponents();
        fillTable(); // nap du lieu khi khoi dong
    }

    // Xay dung giao dien
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // ----- Vung form nhap lieu -----
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Id:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Everage Mark:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMark, gbc);

        // Nhom radio gioi tinh, mac dinh chon Female
        genderGroup.add(rdMale);
        genderGroup.add(rdFemale);
        rdFemale.setSelected(true);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.add(rdMale);
        genderPanel.add(rdFemale);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        formPanel.add(genderPanel, gbc);

        // ----- Vung nut chuc nang -----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnReset);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ----- Gan su kien -----
        btnCreate.addActionListener(e -> onCreate());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnReset.addActionListener(e -> resetForm());

        // Double-click tren dong de nap sinh vien vao form
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onRowDoubleClick();
                }
            }
        });
    }

    // Nap toan bo danh sach sinh vien vao bang
    private void fillTable() {
        try {
            HttpURLConnection conn = HttpClient.openConnection("GET", HOST + "/students.json");
            byte[] data = HttpClient.readData(conn);

            tableModel.setRowCount(0);
            rowKeys.clear();

            if (data != null && data.length > 0) {
                StudentMap students = mapper.readValue(data, StudentMap.class);
                if (students != null) {
                    for (Map.Entry<String, Student> entry : students.entrySet()) {
                        String key = entry.getKey();
                        Student s = entry.getValue();
                        if (s == null) {
                            continue;
                        }
                        rowKeys.add(key);
                        tableModel.addRow(new Object[]{
                                s.getId(),
                                s.getName(),
                                s.isGender() ? "Male" : "Female",
                                s.getMark()
                        });
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải danh sách sinh viên: " + ex.getMessage());
        }
    }

    // Xu ly su kien double-click: nap sinh vien vao form va ghi nho khoa
    private void onRowDoubleClick() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        if (modelRow < 0 || modelRow >= rowKeys.size()) {
            return;
        }
        String key = rowKeys.get(modelRow);
        try {
            HttpURLConnection conn = HttpClient.openConnection("GET",
                    HOST + "/students/" + key + ".json");
            byte[] data = HttpClient.readData(conn);
            if (data != null && data.length > 0) {
                Student s = mapper.readValue(data, Student.class);
                if (s != null) {
                    selectedKey = key;
                    fillForm(s);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải thông tin sinh viên: " + ex.getMessage());
        }
    }

    // Do du lieu sinh vien vao form
    private void fillForm(Student s) {
        txtId.setText(s.getId());
        txtName.setText(s.getName());
        txtMark.setText(String.valueOf(s.getMark()));
        if (s.isGender()) {
            rdMale.setSelected(true);
        } else {
            rdFemale.setSelected(true);
        }
    }

    // Doc du lieu tu form thanh doi tuong Student
    private Student readForm() {
        double mark;
        try {
            mark = Double.parseDouble(txtMark.getText().trim());
        } catch (NumberFormatException e) {
            mark = 0; // gia tri mac dinh khi nhap sai
        }
        boolean gender = rdMale.isSelected(); // true = Male
        return Student.builder()
                .id(txtId.getText().trim())
                .name(txtName.getText().trim())
                .mark(mark)
                .gender(gender)
                .build();
    }

    // Xoa trang form va bo chon khoa
    private void resetForm() {
        txtId.setText("");
        txtName.setText("");
        txtMark.setText("");
        rdFemale.setSelected(true);
        selectedKey = null;
    }

    // Them moi sinh vien (POST)
    private void onCreate() {
        try {
            Student s = readForm();
            byte[] body = mapper.writeValueAsBytes(s);
            HttpURLConnection conn = HttpClient.openConnection("POST", HOST + "/students.json");
            HttpClient.writeData(conn, body);
            resetForm();
            fillTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi thêm sinh viên: " + ex.getMessage());
        }
    }

    // Cap nhat sinh vien dang chon (PUT)
    private void onUpdate() {
        if (selectedKey == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một sinh viên (nhấp đúp vào dòng) trước khi cập nhật.");
            return;
        }
        try {
            Student s = readForm();
            byte[] body = mapper.writeValueAsBytes(s);
            HttpURLConnection conn = HttpClient.openConnection("PUT",
                    HOST + "/students/" + selectedKey + ".json");
            HttpClient.writeData(conn, body);
            fillTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi cập nhật sinh viên: " + ex.getMessage());
        }
    }

    // Xoa sinh vien dang chon (DELETE)
    private void onDelete() {
        if (selectedKey == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một sinh viên (nhấp đúp vào dòng) trước khi xóa.");
            return;
        }
        try {
            HttpURLConnection conn = HttpClient.openConnection("DELETE",
                    HOST + "/students/" + selectedKey + ".json");
            HttpClient.readData(conn); // thuc thi yeu cau
            resetForm();
            fillTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xóa sinh viên: " + ex.getMessage());
        }
    }

    // Ham main: khoi tao frame tren luong EDT
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentSwingApp app = new StudentSwingApp();
            app.setSize(700, 500);
            app.setLocationRelativeTo(null);
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            app.setVisible(true);
        });
    }
}
