import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.HashMap;

public class AutoFillGUI {

    static HashMap<Integer, String> dataMap = new HashMap<>();

    public static void main(String[] args) {

        JFrame frame = new JFrame("Smart Autofill Assistant");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(12, 2, 10, 10));

        JLabel label = new JLabel("Enter number of inputs (max 10):");
        JTextField countField = new JTextField();

        inputPanel.add(label);
        inputPanel.add(countField);

        JButton generateBtn = new JButton("Generate Fields");

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(10, 2, 10, 10));

        JButton saveBtn = new JButton("Save Data");

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(fieldsPanel), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(saveBtn, BorderLayout.NORTH);
        bottom.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        frame.add(bottom, BorderLayout.SOUTH);

        inputPanel.add(generateBtn);

        generateBtn.addActionListener(e -> {
            fieldsPanel.removeAll();

            int count = Integer.parseInt(countField.getText());

            for (int i = 1; i <= count; i++) {
                fieldsPanel.add(new JLabel("Field " + i + " (Ctrl+" + i + "):"));
                JTextField field = new JTextField();
                field.setName("field" + i);
                fieldsPanel.add(field);
            }

            frame.revalidate();
            frame.repaint();
        });

        saveBtn.addActionListener(e -> {
            dataMap.clear();
            Component[] comps = fieldsPanel.getComponents();

            int index = 1;

            for (int i = 1; i < comps.length; i += 2) {
                JTextField tf = (JTextField) comps[i];
                dataMap.put(index, tf.getText());
                index++;
            }

            outputArea.setText("Saved Data:\n");

            for (int key : dataMap.keySet()) {
                outputArea.append("Ctrl + " + key + " → " + dataMap.get(key) + "\n");
            }

            outputArea.append("\nClick below box and press Ctrl + number\n");
        });

        // Key listener for shortcuts
        JTextArea keyListenerArea = new JTextArea("Click here and press Ctrl + number");
        keyListenerArea.setFocusable(true);

        keyListenerArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                if (e.isControlDown()) {
                    int key = e.getKeyCode() - KeyEvent.VK_0;

                    if (dataMap.containsKey(key)) {
                        pasteText(dataMap.get(key));
                    }
                }
            }
        });

        bottom.add(keyListenerArea, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Paste using clipboard
    public static void pasteText(String text) {
        try {
            StringSelection selection = new StringSelection(text);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
