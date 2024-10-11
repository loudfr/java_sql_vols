import javax.swing.*;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Inserer extends JPanel implements ActionListener {
    private JLabel numvol, hdep, harr, adep, aarr, titre;
    private JTextField num_vol, h_dep, h_arr, a_dep, a_arr, affichage;
    private JButton btn;
    private Choix choix; // Référence au cadre principal pour navigation

    public Inserer(Choix choix) {
        this.choix = choix;
        setLayout(new BorderLayout());

        // Panneau supérieur avec bouton Menu et titre
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton menuButton = new JButton("Retour");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 18));
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choix.showHomePage();
            }
        });
        topPanel.add(menuButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Enregistrement d'un nouveau vol", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // création des champs de texte et des labels 
        num_vol = new JTextField(10);
        numvol = new JLabel("Entrer numéro de vol");
        h_dep = new JTextField(10);
        hdep = new JLabel("Entrer Heure de départ HH:MM");
        h_arr = new JTextField(10);
        harr = new JLabel("Entrer heure d'arrivée HH:MM");
        a_dep = new JTextField(10);
        adep = new JLabel("Entrer l'aéroport de départ");
        a_arr = new JTextField(10);
        aarr = new JLabel("Entrer l'aéroport d'arrivée");
        
        // affichage du message de confirmation
        affichage = new JTextField(20);
        affichage.setEditable(false);
        
        btn = new JButton("Enregistrer le vol");
        btn.addActionListener(this);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new java.awt.Insets(10, 10, 10, 10);
        
        c.gridx = 0;
        c.gridy = 0;
        p.add(numvol, c);
        c.gridx = 1;
        p.add(num_vol, c);

        c.gridx = 0;
        c.gridy = 1;
        p.add(hdep, c);
        c.gridx = 1;
        p.add(h_dep, c);

        c.gridx = 0;
        c.gridy = 2;
        p.add(harr, c);
        c.gridx = 1;
        p.add(h_arr, c);

        c.gridx = 0;
        c.gridy = 3;
        p.add(adep, c);
        c.gridx = 1;
        p.add(a_dep, c);

        c.gridx = 0;
        c.gridy = 4;
        p.add(aarr, c);
        c.gridx = 1;
        p.add(a_arr, c);

        c.gridx = 0;
        c.gridy = 5;
        p.add(btn, c);

        c.gridx = 1;
        p.add(affichage, c);

        // Ajout du panel au centre
        add(p, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String numvol = num_vol.getText();
        String heure_depart = h_dep.getText();
        String heure_arrive = h_arr.getText();
        String ville_depart = a_dep.getText();
        String ville_arrivee = a_arr.getText();
        
        if (e.getSource() == btn) {
            // validation des heures sinon message d'erreur
            if (!isValidTimeFormat(heure_depart) || !isValidTimeFormat(heure_arrive)) {
                JOptionPane.showMessageDialog(this, "Format invalide (HH:MM) OU valeurs erronées (24h)", "Erreur de format", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // enregistrement dans la base de données
            enregistrerVol(numvol, heure_depart, heure_arrive, ville_depart, ville_arrivee);
            affichage.setText("Le vol " + numvol + " est bien enregistré !");
        }
    }

    public boolean isValidTimeFormat(String time) {
        String timePattern = "^([01]\\d|2[0-3]):([0-5]\\d)$"; // vérifie si l'heure est entre 0 et 19 puis 20 et 23 et ensuite les min de 0 à 59
        return Pattern.matches(timePattern, time);
    }

    public void enregistrerVol(String numvol, String heure_depart, String heure_arrive, String ville_depart, String ville_arrivee) {
        String url = "jdbc:mysql://localhost:3306/vols";
        Connection con = null;
        Statement stmt = null;
        String query = "INSERT INTO vol (numvol, heure_depart, heure_arrive, ville_depart, ville_arrivee) VALUES ('" 
                       + numvol + "', '" + heure_depart + "', '" + heure_arrive + "', '" 
                       + ville_depart + "', '" + ville_arrivee + "')";

        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Driver JDBC non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        try {
            // Connexion à la base de données
            con = DriverManager.getConnection(url, "root", "");
            stmt = con.createStatement();
            // Exécution de la requête
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println("Nombre de rangs affectés: " + rowsAffected);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
