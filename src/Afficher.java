import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Afficher extends JPanel implements ActionListener {
    private JLabel labelDepart, labelArrivee, titre;
    private JTextField textDepart, textArrivee;
    private JButton btnRechercher;
    private JTable tableResultats;
    private DefaultTableModel tableModel;
    private Choix choix; 
    private JCheckBox checkPlusRapide; 

    public Afficher(Choix choix) {
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

        JLabel titleLabel = new JLabel("Affichage des vols enregistrés", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Panel pour les champs de recherche
        JPanel panelRecherche = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement
        gbc.fill = GridBagConstraints.HORIZONTAL;

        labelDepart = new JLabel("Ville de départ :");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelRecherche.add(labelDepart, gbc);

        textDepart = new JTextField(15);
        gbc.gridx = 1;
        panelRecherche.add(textDepart, gbc);

        labelArrivee = new JLabel("Ville d'arrivée :");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelRecherche.add(labelArrivee, gbc);

        textArrivee = new JTextField(15);
        gbc.gridx = 1;
        panelRecherche.add(textArrivee, gbc);

        // Case à cocher pour le vol le plus rapide
        checkPlusRapide = new JCheckBox("Afficher seulement le vol le plus rapide");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelRecherche.add(checkPlusRapide, gbc);

        btnRechercher = new JButton("Rechercher");
        btnRechercher.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelRecherche.add(btnRechercher, gbc);

        add(panelRecherche, BorderLayout.WEST);

        // Table pour afficher les résultats
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"N° de Vol", "Heure Départ", "Heure Arrivée", "Ville Départ", "Ville Arrivée"});
        tableResultats = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableResultats);
        add(scrollPane, BorderLayout.CENTER);
    }

    //affiche le tableau quand bouton appuyé
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRechercher) {
            rechercherVols();
        }
    }

    private void rechercherVols() {
        String villeDepart = textDepart.getText().trim();
        String villeArrivee = textArrivee.getText().trim();
        boolean afficherPlusRapide = checkPlusRapide.isSelected();

        // Validation des entrées
        if (villeDepart.isEmpty() && villeArrivee.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer au moins une ville de départ ou d'arrivée.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Connexion à la base de données et récupération des vols
        String url = "jdbc:mysql://localhost:3306/vols";
        String user = "root";
        String password = ""; 

        String query = "SELECT numvol, heure_depart, heure_arrive, ville_depart, ville_arrivee FROM vol WHERE 1=1";

        if (!villeDepart.isEmpty()) {
            query += " AND ville_depart LIKE ?";
        }
        if (!villeArrivee.isEmpty()) {
            query += " AND ville_arrivee LIKE ?";
        }

        if (afficherPlusRapide) {
            query += " ORDER BY TIMEDIFF(heure_arrive, heure_depart) LIMIT 1";
        }

        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Driver JDBC non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = con.prepareStatement(query)) {

            int paramIndex = 1;
            if(!villeDepart.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + villeDepart + "%");
            }
            if (!villeArrivee.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + villeArrivee + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            // Vider le tableau avant d'ajouter de nouveaux résultats
            tableModel.setRowCount(0);
            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                String numvol = rs.getString("numvol");
                String heureDepart = rs.getString("heure_depart");
                String heureArrivee = rs.getString("heure_arrive");
                String vDepart = rs.getString("ville_depart");
                String vArrivee = rs.getString("ville_arrivee");

                tableModel.addRow(new Object[]{numvol, heureDepart, heureArrivee, vDepart, vArrivee});
            }

            if (!hasResults) {
                JOptionPane.showMessageDialog(this, "Aucun vol trouvé pour les critères spécifiés.", "Résultats", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche des vols.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
