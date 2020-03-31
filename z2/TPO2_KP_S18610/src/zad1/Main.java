/**
 *
 *  @author Kamiński Patryk S18610
 *
 */

package zad1;

import java.awt.*;

import javax.swing.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();

    // część uruchamiająca GUI
    SwingUtilities.invokeLater(() -> initAndShowGUI(s));

  }

  private static void initAndShowGUI(Service frameService) {

    JFrame frame = new JFrame("TPO");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setMinimumSize(new Dimension(640, 480));
    frame.setLayout(new BorderLayout());

    //Weather
    JLabel weather = new JLabel(new Weather(frameService.getWeather(frameService.getCity()), frameService.getCity(), frameService.getCountry()).getHTML());

    //Currency Rate
    JPanel currency = new JPanel();
    currency.setLayout(new GridLayout(0, 3));
    currency.add(new JLabel("Currency:"));
    JTextField text = new JTextField(3);
    currency.add(text);
    JButton enterButton = new JButton("Enter");
    JLabel label1 = new JLabel("");
    JLabel label2 = new JLabel("");
    JLabel label3 = new JLabel("");

    enterButton.addActionListener(e -> {

      label1.setText("Your currency in " + text.getText().toUpperCase() + ":");

      try {
        label1.setForeground(Color.BLACK);
        label2.setText("" + frameService.getRateFor(text.getText().toUpperCase()));

      } catch(Exception e1) {
        label1.setForeground(Color.RED);
        label1.setText("Invalid currency!");
        label2.setText("");
      }

      currency.repaint();
    });

    currency.add(enterButton);
    currency.add(label1);
    currency.add(label2);
    currency.add(label3);
    currency.add(new JLabel("Your currency in PLN:"));
    currency.add(new JLabel("" + frameService.getNBPRate()));

    //Wikipedia
    JFXPanel fxPanel = new JFXPanel();

    frame.add(fxPanel, "Center");
    frame.add(currency, "North");
    frame.add(weather, "South");
    frame.setVisible(true);
    frame.setMinimumSize(new Dimension(800, 805));
    fxPanel.setLocation(new Point(0, 0));
    frame.setResizable(false);
    frame.pack();

    Platform.runLater(() -> initFX(fxPanel, frameService.getCity()));

  }

  private static void initFX(JFXPanel fxPanel, String city) {

    Group group = new Group();
    Scene scene = new Scene(group);
    fxPanel.setScene(scene);
    WebView webView = new WebView();
    group.getChildren().add(webView);
    WebEngine webEngine = webView.getEngine();
    webEngine.load("https://en.wikipedia.org/wiki/" + city);

  }

}

