import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Main extends JFrame {
    private JTextArea textArea;
    private JLabel fileLabel;
    private File textFile;
    private File stopFile;
    private TreeMap<String, Integer> wordMap = new TreeMap<>();

    public Main() {
        setTitle("Tag Extractor");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JButton textButton = new JButton("Choose Text File");
        textButton.setBounds(20, 20, 160, 30);
        add(textButton);

        JButton stopButton = new JButton("Choose Stopword File");
        stopButton.setBounds(200, 20, 180, 30);
        add(stopButton);

        JButton processButton = new JButton("Process");
        processButton.setBounds(400, 20, 100, 30);
        add(processButton);

        JButton saveButton = new JButton("Save Results");
        saveButton.setBounds(510, 20, 120, 30);
        add(saveButton);

        fileLabel = new JLabel("No file selected");
        fileLabel.setBounds(20, 60, 500, 20);
        add(fileLabel);

        textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBounds(20, 90, 550, 250);
        add(scroll);

        textButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                textFile = chooser.getSelectedFile();
                fileLabel.setText("Text File: " + textFile.getName());
            }
        });

        stopButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                stopFile = chooser.getSelectedFile();
            }
        });

        processButton.addActionListener(e -> {
            if (textFile == null || stopFile == null) {
                JOptionPane.showMessageDialog(this, "Please select both files first.");
                return;
            }
            processFiles();
        });

        saveButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File saveFile = chooser.getSelectedFile();
                try (PrintWriter pw = new PrintWriter(saveFile)) {
                    pw.print(textArea.getText());
                    JOptionPane.showMessageDialog(this, "Results saved!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file.");
                }
            }
        });

        setVisible(true);
    }

    private void processFiles() {
        try {
            Set<String> stopWords = new HashSet<>();
            Scanner stopScanner = new Scanner(stopFile);
            while (stopScanner.hasNextLine()) {
                stopWords.add(stopScanner.nextLine().trim().toLowerCase());
            }
            stopScanner.close();

            wordMap.clear();
            Scanner textScanner = new Scanner(textFile);
            while (textScanner.hasNext()) {
                String word = textScanner.next().toLowerCase().replaceAll("[^a-z]", "");
                if (!word.isEmpty() && !stopWords.contains(word)) {
                    wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
                }
            }
            textScanner.close();

            // Display results
            textArea.setText("");
            for (String word : wordMap.keySet()) {
                textArea.append(word + ": " + wordMap.get(word) + "\n");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error reading files.");
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
