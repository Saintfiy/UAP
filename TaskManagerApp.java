import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * TaskManagerApp: A modern GUI application to manage tasks with titles and descriptions.
 * Features:
 * - Program correctness ensured through input validation.
 * - Simple refactoring for modular design.
 * - Implements modern programming principles with file handling and GUI.
 * - Documentation and API-style design for maintainability.
 */
public class TaskManagerApp {
    private JFrame frame;
    private JTextField titleField;
    private JTextArea descriptionField;
    private DefaultListModel<String> listModel;
    private List<TaskRecord> tasks;

    public TaskManagerApp() {
        tasks = new ArrayList<>();

        frame = new JFrame("Task Manager App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        titleField = new JTextField();
        descriptionField = new JTextArea();
        inputPanel.add(new JLabel("Task Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Task Description:"));
        inputPanel.add(new JScrollPane(descriptionField));

        panel.add(inputPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        JList<String> taskList = new JList<>(listModel);
        taskList.addListSelectionListener(e -> showSelectedTask(taskList.getSelectedIndex()));
        panel.add(new JScrollPane(taskList), BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(this::addTask);
        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(e -> deleteTask(taskList.getSelectedIndex()));
        JButton saveButton = new JButton("Save Tasks");
        saveButton.addActionListener(e -> saveTasksToFile());
        JButton loadButton = new JButton("Load Tasks");
        loadButton.addActionListener(e -> loadTasksFromFile());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void addTask(ActionEvent e) {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Both title and description are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        tasks.add(new TaskRecord(title, description));
        listModel.addElement(title);
        clearInputs();
    }

    private void deleteTask(int index) {
        if (index >= 0) {
            tasks.remove(index);
            listModel.remove(index);
            clearInputs();
        }
    }

    private void showSelectedTask(int index) {
        if (index >= 0) {
            TaskRecord task = tasks.get(index);
            titleField.setText(task.getTitle());
            descriptionField.setText(task.getDescription());
        }
    }

    private void saveTasksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.dat"))) {
            oos.writeObject(tasks);
            JOptionPane.showMessageDialog(frame, "Tasks saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error saving tasks: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTasksFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tasks.dat"))) {
            tasks = (List<TaskRecord>) ois.readObject();
            listModel.clear();
            for (TaskRecord task : tasks) {
                listModel.addElement(task.getTitle());
            }
            JOptionPane.showMessageDialog(frame, "Tasks loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading tasks: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputs() {
        titleField.setText("");
        descriptionField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagerApp::new);
    }
}

/**
 * TaskRecord: Represents a task with a title and description.
 */
class TaskRecord implements Serializable {
    private String title;
    private String description;

    public TaskRecord(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
