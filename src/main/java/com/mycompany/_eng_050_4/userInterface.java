
package com.mycompany._eng_050_4;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

public class userInterface {
    private static JFrame mainFrame;
    private List<Train> trains;
    private List<ResultTrain> selectedTrains = new ArrayList<>();
    private List<ResultTrain> TrainList = new ArrayList<>();
    private Result result;
    
    // Additional instance variables
    private List<Character> path;
    private int index;
    private LocalTime nextFrom;
    private LocalTime intendedTime;

    private JTextArea tb;
    private JScrollPane tbScrollPane;
    
    String startStation;
    String endStation;
    
    private static final int[][] distances = {
        {0, 10, 22, -1, 8, -1},
        {10, 0, 15, 9, -1, 7},
        {22, 15, 0, 9, -1, -1},
        {-1, 9, 9, 0, 5, 12},
        {8, -1, -1, 5, 0, 16},
        {-1, 7, -1, 12, 16, 0}
    };

    public userInterface() {
        initWindow();
    }

    private void initWindow() {
        mainFrame = new JFrame("Find Train");
        mainFrame.setSize(400, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setLocationRelativeTo(null);

        JPanel searchPanel = createSearchPanel();
        mainFrame.add(searchPanel, BorderLayout.CENTER);

        mainFrame.setVisible(true);
        createTrains();
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Starting Station 
        JLabel startStationLabel = new JLabel("Starting Station (A-F):");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(startStationLabel, gbc);

        JTextField startStationField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(startStationField, gbc);

        // Intended Start Time 
        JLabel timeLabel = new JLabel("Intended Start Time (HH:MM):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(timeLabel, gbc);

        JTextField timeField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(timeField, gbc);

        // Destination Station
        JLabel endStationLabel = new JLabel("Destination Station (A-F):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(endStationLabel, gbc);

        JTextField endStationField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(endStationField, gbc);

        // Search Button
        JButton searchButton = new JButton("Find Trains");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(searchButton, gbc);

        // scrollable area (train options)
        JPanel pn = new JPanel();
        pn.setLayout(new BoxLayout(pn, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(pn);
        scrollPane.setPreferredSize(new Dimension(350, 100));
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(scrollPane, gbc);

        // Add Train Button
        JButton addTrainButton = new JButton("Add Train");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(addTrainButton, gbc);

        // Text Area
        JTextArea tb = new JTextArea(10, 30);
        tb.setLineWrap(true);
        tb.setWrapStyleWord(true);
        tb.setEditable(false);
        tb.setTabSize(4);
        JScrollPane tbScrollPane = new JScrollPane(tb);
        tbScrollPane.setPreferredSize(new Dimension(350, 100));

        // Set GridBagConstraints for JTextArea and add it to panel
        gbc.gridy = 6;
        panel.add(tbScrollPane, gbc);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTrainButton.setEnabled(true);
                selectedTrains.clear();
                TrainList.clear();
                tb.setText("");
                startStation = startStationField.getText().trim().toUpperCase();
                endStation = endStationField.getText().trim().toUpperCase();
                String timeText = timeField.getText().trim();

                if (isValidStation(startStation) && isValidStation(endStation) && isValidTime(timeText)) {
                    intendedTime = LocalTime.parse(timeText);
                    findTrains(startStation.charAt(0), endStation.charAt(0), intendedTime);
                    path = result.getPath();
                    index = 0;

                    pn.removeAll();
                    pn.revalidate();
                    pn.repaint();

                    List<ResultTrain> availableTrains = getList(path, index, intendedTime.minusMinutes(10));
                    ButtonGroup gp = new ButtonGroup();

                    for (ResultTrain train : availableTrains) {
                        JRadioButton newTrain = new JRadioButton(train.toString());
                        gp.add(newTrain);
                        pn.add(newTrain);

                        newTrain.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                selectedTrains.clear();
                                selectedTrains.add(train);
                            }
                        });
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please enter valid input (Stations A-F, Time in HH:MM format).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addTrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedTrains.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "Please select a train to add.", "No Train Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                ResultTrain addedTrain = null;
                if(!selectedTrains.isEmpty()){
                    addedTrain = selectedTrains.removeLast();
                    TrainList.add(addedTrain);
                    index++;
                    nextFrom = addedTrain.depatureTime.plusMinutes(addedTrain.TravelDuration.plusMinutes(5).toMinutes());
                    System.out.println("Next: " + nextFrom.toString() + "Train: " +TrainList);
                    JOptionPane.showMessageDialog(mainFrame, "Added train: " + addedTrain, "Train Added", JOptionPane.INFORMATION_MESSAGE);
                }

                pn.removeAll();
                pn.revalidate();
                pn.repaint();
                
                if(path.size()-1 > index ){
                    List<ResultTrain> availableTrains = getList(path, index, nextFrom);
                    ButtonGroup gp = new ButtonGroup();
                    for (ResultTrain train : availableTrains) {
                        JRadioButton newTrain = new JRadioButton(train.toString());
                        gp.add(newTrain);
                        pn.add(newTrain);

                        newTrain.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                selectedTrains.add(train);
                            }
                        });
                    }
                }else{
                    addTrainButton.setEnabled(false);
                }
                tb.setText(formatTrainList());
            }
        });

        return panel;
    }

    private String formatTrainList(){
        String s= "Trip "+ this.startStation +" to "+this.endStation+"\n"+"------------"+"\n"; 
        LocalTime startTime = TrainList.getFirst().depatureTime;
        LocalTime stopTime = null;
        for(ResultTrain train:TrainList){
            stopTime = train.depatureTime.plusMinutes(train.TravelDuration.toMinutes());
            s = s + train.startSt +" to "+train.destinationSt+" :\tStart at "+train.depatureTime.toString()+"hrs\t-\tStops at "+stopTime.toString()+"hrs\n";
        }

        s = s+"\nTotal time = "+ Duration.between(startTime, stopTime).toMinutes() + " minutes\n";
        return s;
    }
    
    private List<ResultTrain> getList(List<Character> path, int index, LocalTime time) {
        char startSt = path.get(index);
        char destinationSt = path.get(index + 1);
        List<ResultTrain> availableTrains = new ArrayList<>();

        for (Train train : trains) {
            if (train.thisTrain(startSt, destinationSt) || train.thisTrain(destinationSt, startSt)) {
                availableTrains = train.CheckAvailableTrain(time, startSt);
                break;
            }
        }
        return availableTrains;
    }

    private static boolean isValidStation(String station) {
        return station.length() == 1 && station.charAt(0) >= 'A' && station.charAt(0) <= 'F';
    }

    private static boolean isValidTime(String timeText) {
        try {
            LocalTime.parse(timeText);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void findTrains(char start, char end, LocalTime time) {
        result = Dijkstra.dijkstra(distances, start - 'A', end - 'A');
    }

    private void createTrains() {
        trains = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = i; j < 6; j++) {
                if (i != j && distances[i][j] != -1) {
                    Train temp = new Train((char) ('A' + i), (char) ('A' + j), distances[i][j]);
                    trains.add(temp);
                }
            }
        }
    }
}
