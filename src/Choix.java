import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Choix extends JFrame implements ActionListener {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton enreg, affi, modif ;

    public Choix() {
        super("Java - SQL");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Panel d'accueil
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("Choisissez une action");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        homePanel.add(label, gbc);

        enreg = new JButton("Enregistrer un vol");
        enreg.setFont(new Font("Arial", Font.PLAIN, 18));
        enreg.setPreferredSize(new Dimension(200, 50));
        enreg.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        homePanel.add(enreg, gbc);

        affi = new JButton("Afficher les vols");
        affi.setFont(new Font("Arial", Font.PLAIN, 18));
        affi.setPreferredSize(new Dimension(200, 50));
        affi.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 2;
        homePanel.add(affi, gbc);

        modif = new JButton("Modifier / supprimer un vol");
        modif.setFont(new Font("Arial", Font.PLAIN, 18));
        modif.setPreferredSize(new Dimension(200, 50));
        modif.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 3;
        homePanel.add(modif, gbc);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(homePanel, "Home");

        cardPanel.add(new Inserer(this), "Interface"); 
        cardPanel.add(new Afficher(this), "Afficher");
        cardPanel.add(new Modifier(this), "Modifier");

        add(cardPanel, BorderLayout.CENTER);
        add(cardPanel, BorderLayout.CENTER);
        setVisible(true);
        }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enreg) {
            cardLayout.show(cardPanel, "Interface");
        } else if (e.getSource() == affi) {
            cardLayout.show(cardPanel, "Afficher");
        } else if (e.getSource() == modif) {
            // afficher l'interface de connexion avant d'accéder à Modifier
            Connect connect = new Connect();
            connect.setVisible(true);
            
            // ajouter un listener pour vérifier l'authentification
            connect.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    if (connect.isAuthenticated()) {
                        // si l'utilisateur est authentifié, afficher l'interface Modifier
                        cardLayout.show(cardPanel, "Modifier");
                    } else {
                        JOptionPane.showMessageDialog(null, "Accès refusé : authentification requise.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
    }

    public void showHomePage() {
        cardLayout.show(cardPanel, "Home");
    }
}
