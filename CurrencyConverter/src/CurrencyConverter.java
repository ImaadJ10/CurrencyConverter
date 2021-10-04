import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class CurrencyConverter extends Container implements ActionListener {

    private JFrame frame;
    private JTextField inputMoney;
    private JComboBox<String> currencyFrom;
    private JLabel to;
    private JLabel dollar;
    private JComboBox<String> currencyTo;
    private JButton button;
    private JLabel converted;
    private JLabel inputText;
    private JPanel panel;
    private String[] countries = {"", "US Dollar (USD)", "Canadian Dollar (CAD)", "Euro (EUR)", "UAE Dirham (AED)", "Afghani (AFN)",
            "Argentine Peso (ARS)", "Australian Dollar (AUD)", "Bangladeshi Taka (BDT)", "Brazilian Real (BRL)",
            "Swiss Franc (CHF)", "Chilean Peso (CLP)", "Chinese Yuan (CNY)", "Colombian Peso (COP)", "Czech Koruna (CZK)",
            "Danish Krone (DKK)", "British Pound Sterling (GBP)", "Hong Kong Dollar (HKD)", "Hungarian Forint (HUF)",
            "Indonesian Rupiah (IDR)", "Indian Rupee (INR)", "New Israeli Sheqel (ILS)", "Japanese Yen (JPY)",
            "South Korean Won (KRW)", "Mexican Peso (MXN)", "Malaysian Ringgit (MYR)", "Norwegian Krone (NOK)",
            "New Zealand Dollar (NZD)", "Philippine Peso (PHP)", "Pakistani Rupee (PKR)", "Polish Zloty (PLN)",
            "Romanian Leu (RON)", "Russian Ruble (RUB)", "Saudi Riyal (SAR)", "Swedish Krona (SEK)",
            "Singapore Dollar (SGD)", "Thai Baht (THB)", "Turkish Lira (TRY)", "New Taiwan Dollar (TWD)",
            "Venezuelan Bolivar (VEF)", "South African Rand (ZAR)"};
    private double newCurrency;
    private double currencyScale;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private static final String API_KEY
            = "YOUR API KEY";
    private static final String USER_AGENT_ID = "Java/"
            + System.getProperty("java.version");

    public CurrencyConverter(){

        frame = new JFrame();

        inputMoney = new JTextField();

        DefaultComboBoxModel<String> fromModel = new DefaultComboBoxModel<String>(countries);
        currencyFrom = new JComboBox(fromModel);

        to = new JLabel("To");
        dollar = new JLabel("$");
        inputText = new JLabel("Input Money Here");

        DefaultComboBoxModel<String> toModel = new DefaultComboBoxModel<String>(countries);
        currencyTo = new JComboBox(toModel);

        button = new JButton("Convert Currency");
        button.addActionListener(this);

        converted = new JLabel("Converted Currency: $                                     ");

        panel = new JPanel();

        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                        .addComponent(converted)
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                                                        .addComponent(inputText)
                                                        .addGroup(gl_panel.createSequentialGroup()
                                                                .addComponent(dollar)
                                                                .addGap(2)
                                                                .addComponent(inputMoney, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(18)
                                                .addComponent(currencyFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(10)
                                                .addComponent(to)
                                                .addGap(10)
                                                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(button)
                                                        .addComponent(currencyTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                .addGap(30))
        );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(inputText)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(inputMoney)
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addGap(3)
                                                .addComponent(dollar))
                                        .addComponent(currencyFrom)
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addGap(3)
                                                .addComponent(to))
                                        .addComponent(currencyTo))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(button)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(converted))
        );
        panel.setLayout(gl_panel);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Currency Converter");
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new CurrencyConverter();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String currencyFromText = currencyFrom.getSelectedItem().toString();
        String currencyToText = currencyTo.getSelectedItem().toString();
        String currencyFromCode = currencyFromText.substring(currencyFromText.length() - 4, currencyFromText.length() - 1);
        String currencyToCode = currencyToText.substring(currencyToText.length() - 4, currencyToText.length() - 1);

        try {
            currencyScale = rate(currencyFromCode, currencyToCode);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String textMoney = inputMoney.getText();
        double floatMoney = Double.parseDouble(textMoney);
        newCurrency = floatMoney * currencyScale;
        converted.setText("Converted Currency: $" + df2.format(newCurrency));
    }

    static double rate(String from, String to)
            throws IOException {
        String queryPath
                = "https://free.currconv.com/api/v7/convert?q="
                + from + "_"
                + to
                + "&compact=ultra&apiKey=" + API_KEY;
        URL queryURL = new URL(queryPath);
        HttpURLConnection connection
                = (HttpURLConnection) queryURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT_ID);
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) { // 200 is HTTP status OK
            InputStream stream
                    = (InputStream) connection.getContent();
            Scanner scanner = new Scanner(stream);
            String quote = scanner.nextLine();
            String number = quote.substring(quote.indexOf(':') + 1,
                    quote.indexOf('}'));
            return Double.parseDouble(number);
        } else {
            String excMsg = "Query " + queryPath
                    + " returned status " + responseCode;
            throw new RuntimeException(excMsg);
        }
    }

}

