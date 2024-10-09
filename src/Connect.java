import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class Connect extends JFrame implements ActionListener {
    private JPanel p1;
    private JLabel l1, l2;
    private JTextField t1;
    private JPasswordField pf1;
    private JButton b1;
    private boolean authenticated = false;

    public Connect() {
        super("Authentification");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        p1 = new JPanel();
        p1.setLayout(new GridLayout(3, 2));

        l1 = new JLabel("Nom d'utilisateur : ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        p1.add(l1, gbc);

        t1 = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        p1.add(t1, gbc);

        l2 = new JLabel("Mot de passe : ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        p1.add(l2, gbc);

        pf1 = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        p1.add(pf1, gbc);

        b1 = new JButton("Se connecter");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        b1.addActionListener(this);
        p1.add(b1, gbc);

        add(p1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = t1.getText();
        String password = new String(pf1.getPassword());
        if (authenticate(username, password)) {
            authenticated = true;
            System.out.println("Authentification réussie");
            dispose(); // Fermer la fenêtre de connexion
        } else {
            System.out.println("Authentification échouée");
            JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean authenticate(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/vols";
        String user = "root";
        String pass = "";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT * FROM admins WHERE id = ? AND mdp = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true; // Authentication successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Authentication failed
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public static void main(String[] args) {
        Connect c = new Connect();
        c.setVisible(true);
    }
}