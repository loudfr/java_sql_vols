import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Modifier extends JPanel implements ActionListener {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    @SuppressWarnings("unused")
    private Choix choix;

    // Composants pour le panneau de modification/suppression
    private JTextField searchNumVolField;
    private JButton btnSearch, btnUpdate, btnDelete;
    private JTextField numVolField, heureDepField, heureArrField, villeDepField, villeArrField;
    private JLabel statusLabel;

    public Modifier(Choix choix) {
        //this.choix = choix;
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Panneau de modification/suppression
        JPanel modifyPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcMod = new GridBagConstraints();
        gbcMod.insets = new Insets(10,10,10,10);
        gbcMod.fill = GridBagConstraints.HORIZONTAL;

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

        JLabel titleLabel = new JLabel("Modification/suppression des vols", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JLabel searchLabel = new JLabel("Numéro de Vol:");
        gbcMod.gridx = 0;
        gbcMod.gridy = 1;
        modifyPanel.add(searchLabel, gbcMod);

        searchNumVolField = new JTextField(10);
        gbcMod.gridx = 1;
        modifyPanel.add(searchNumVolField, gbcMod);

        btnSearch = new JButton("Rechercher");
        btnSearch.addActionListener(this);
        gbcMod.gridx = 2;
        modifyPanel.add(btnSearch, gbcMod);

        // Champs pour afficher et modifier les détails du vol
        JLabel numVolLabel = new JLabel("Numéro de vol:");
        gbcMod.gridx = 0;
        gbcMod.gridy = 2;
        modifyPanel.add(numVolLabel, gbcMod);

        numVolField = new JTextField(10);
        numVolField.setEditable(false);
        gbcMod.gridx = 1;
        modifyPanel.add(numVolField, gbcMod);

        JLabel heureDepLabel = new JLabel("Heure de départ (HH:MM):");
        gbcMod.gridx = 0;
        gbcMod.gridy = 3;
        modifyPanel.add(heureDepLabel, gbcMod);

        heureDepField = new JTextField(10);
        gbcMod.gridx = 1;
        modifyPanel.add(heureDepField, gbcMod);

        JLabel heureArrLabel = new JLabel("Heure d'arrivée (HH:MM):");
        gbcMod.gridx = 0;
        gbcMod.gridy = 4;
        modifyPanel.add(heureArrLabel, gbcMod);

        heureArrField = new JTextField(10);
        gbcMod.gridx = 1;
        modifyPanel.add(heureArrField, gbcMod);

        JLabel villeDepLabel = new JLabel("Ville de départ:");
        gbcMod.gridx = 0;
        gbcMod.gridy = 5;
        modifyPanel.add(villeDepLabel, gbcMod);

        villeDepField = new JTextField(10);
        gbcMod.gridx = 1;
        modifyPanel.add(villeDepField, gbcMod);

        JLabel villeArrLabel = new JLabel("Ville d'arrivée:");
        gbcMod.gridx = 0;
        gbcMod.gridy = 6;
        modifyPanel.add(villeArrLabel, gbcMod);

        villeArrField = new JTextField(10);
        gbcMod.gridx = 1;
        modifyPanel.add(villeArrField, gbcMod);

        btnUpdate = new JButton("Modifier le Vol");
        btnUpdate.addActionListener(this);
        gbcMod.gridx = 0;
        gbcMod.gridy = 7;
        modifyPanel.add(btnUpdate, gbcMod);

        btnDelete = new JButton("Supprimer le Vol");
        btnDelete.addActionListener(this);
        gbcMod.gridx = 1;
        modifyPanel.add(btnDelete, gbcMod);

        statusLabel = new JLabel("", JLabel.CENTER);
        gbcMod.gridx = 0;
        gbcMod.gridy = 8;
        gbcMod.gridwidth = 3;
        modifyPanel.add(statusLabel, gbcMod);

        // Ajouter le panneau de modification/suppression au CardLayout
        /*cardPanel.add(loginPanel, "Login");*/
        cardPanel.add(modifyPanel, "Modify");

        add(cardPanel, BorderLayout.CENTER);

        // Afficher le panneau de modification/suppression par défaut
        cardLayout.show(cardPanel, "Modify"); /* name:Login */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*if (e.getSource() == btnLogin) {
            handleLogin();
        } else*/ if (e.getSource() == btnSearch) {
            handleSearch();
        } else if (e.getSource() == btnUpdate) {
            handleUpdate();
        } else if (e.getSource() == btnDelete) {
            handleDelete();
        } 
    }

    private void handleSearch() {
        String numVol = searchNumVolField.getText().trim();

        if (numVol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer le numéro de vol à rechercher.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/vols";
        String user = "root";
        String dbPassword = ""; // Mettez ici votre mot de passe MySQL si nécessaire

        String query = "SELECT * FROM vol WHERE numvol = ?";

        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Driver JDBC non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        try (Connection con = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, numVol);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Afficher les détails du vol
                numVolField.setText(rs.getString("numvol"));
                heureDepField.setText(rs.getString("heure_depart"));
                heureArrField.setText(rs.getString("heure_arrive"));
                villeDepField.setText(rs.getString("ville_depart"));
                villeArrField.setText(rs.getString("ville_arrivee"));
                statusLabel.setText("Vol trouvé. Vous pouvez le modifier ou le supprimer.");
            } else {
                JOptionPane.showMessageDialog(this, "Aucun vol trouvé avec ce numéro.", "Résultat", JOptionPane.INFORMATION_MESSAGE);
                clearModifyFields();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche du vol.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleUpdate() {
        String numVol = numVolField.getText().trim();
        String heureDep = heureDepField.getText().trim();
        String heureArr = heureArrField.getText().trim();
        String villeDep = villeDepField.getText().trim();
        String villeArr = villeArrField.getText().trim();

        if (heureDep.isEmpty() || heureArr.isEmpty() || villeDep.isEmpty() || villeArr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validation des heures
        if (!isValidTimeFormat(heureDep) || !isValidTimeFormat(heureArr)) {
            JOptionPane.showMessageDialog(this, "Format invalide pour les heures (HH:MM).", "Erreur de format", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/vols";
        String user = "root";
        String dbPassword = ""; // Mettez ici votre mot de passe MySQL si nécessaire

        String query = "UPDATE vol SET heure_depart = ?, heure_arrive = ?, ville_depart = ?, ville_arrivee = ? WHERE numvol = ?";

        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Driver JDBC non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        try (Connection con = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, heureDep);
            pstmt.setString(2, heureArr);
            pstmt.setString(3, villeDep);
            pstmt.setString(4, villeArr);
            pstmt.setString(5, numVol);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                statusLabel.setText("Vol mis à jour avec succès.");
            } else {
                JOptionPane.showMessageDialog(this, "Aucun vol mis à jour.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du vol.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleDelete() {
        String numVol = numVolField.getText().trim();

        if (numVol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer le numéro de vol à supprimer.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer le vol " + numVol + " ?", "Confirmer Suppression", JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        String url = "jdbc:mysql://localhost:3306/vols";
        String user = "root";
        String dbPassword = ""; // Mettez ici votre mot de passe MySQL si nécessaire

        String query = "DELETE FROM vol WHERE numvol = ?";

        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Driver JDBC non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        try (Connection con = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, numVol);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                statusLabel.setText("Vol supprimé avec succès.");
                clearModifyFields();
            } else {
                JOptionPane.showMessageDialog(this, "Aucun vol supprimé.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du vol.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearModifyFields() {
        searchNumVolField.setText("");
        numVolField.setText("");
        heureDepField.setText("");
        heureArrField.setText("");
        villeDepField.setText("");
        villeArrField.setText("");
        statusLabel.setText("");
    }

    private boolean isValidTimeFormat(String time) {
        String timePattern = "^([01]\\d|2[0-3]):([0-5]\\d)$";
        return time.matches(timePattern);
    }
}
