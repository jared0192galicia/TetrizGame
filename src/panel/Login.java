package panel;

import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import clases.LoadData;
import clases.MainWindow;
import clases.MenuWindow;
import driver.Conexion;

public class Login extends JPanel implements ActionListener {
	
	// Components for Panel
	private JPanel VBox;
	private JLabel image;
	private JLabel title;
	private JButton access;
	private JTextField txtUser;
	private JPasswordField txtPass;
	private MainWindow window;
	
	// Constructor
	public Login(int width, int heigth, MainWindow window) {
		initComponents();
		this.setSize(width, heigth);
		this.setLayout(new GridLayout());
		this.window = window;
	}
	
	// Instance and puts components decorate
	public void initComponents() {
		// New JPanel
		VBox = new JPanel();
		VBox.setLayout(null);
		VBox.setAlignmentX(CENTER_ALIGNMENT);
		
		// Image of the right
		image = new JLabel();
		image.setBounds(image.getX(), image.getY(), 400, 500);
//		image.setPreferredSize(getMaximumSize());
		// Resize image 
		ImageIcon imagen = new ImageIcon("./src/images/imageLogin.png");
        ImageIcon icono = new ImageIcon(imagen.getImage().getScaledInstance(image.getWidth(),
                image.getHeight(), Image.SCALE_DEFAULT));
        image.setIcon(icono);
		
        // Button the access
		access = new JButton("Aceder");
		access.setFont(new Font(TOOL_TIP_TEXT_KEY, Font.BOLD, 22));
		access.setAlignmentX(CENTER_ALIGNMENT);
		access.addActionListener(this);
		access.setBounds(120, 310, 150, 25);
		
		// Set title in JLabel
		title = new JLabel("Login");
		title.setFont(new Font("Book Antiqua", Font.BOLD, 26));
		title.setBounds(150, 100, 210, 40);
		
		// Component the text for user
		txtUser = new JTextField();
		txtUser.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		txtUser.setForeground(Color.WHITE);
		txtUser.setFont(new Font("Areal", Font.ITALIC, 16));
		txtUser.setSize(210, 25);
		txtUser.setBounds(80, 150, 210, 30);
		txtUser.setBackground(new Color(102,153,255));

		// Component the text for password
		txtPass = new JPasswordField();
		txtPass.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		txtPass.setForeground(Color.WHITE);
		txtPass.setFont(new Font("Areal", Font.BOLD, 18));
		txtPass.setBounds(80, 220, 210, 30);
		txtPass.setBackground(new Color(102,153,255));

		VBox.add(title);
		VBox.add(txtUser);
		VBox.add(txtPass);
		VBox.add(access);
		
		this.add(VBox);
		this.add(image);
	}

	// Function for EVENT generated by pressed button 
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == access) {
			
			// valid textField the user
			String user = txtUser.getText().trim();
			String pass = String.valueOf(txtPass.getPassword())
					;
			// Continue 
			if (!user.equals("") && !pass.equals("")) {
				
				Connection cn = Conexion.getConnection();
				try {
					PreparedStatement pst = (PreparedStatement) cn.prepareStatement(
					        "SELECT alias, experience, CONVERT(AES_DECRYPT(pass, \"root\") \r\n"
					        + "    USING UTF8) FROM user WHERE username = '" + user + "'");
					
					ResultSet rs = pst.executeQuery();
					
					if (rs.next()) {
						String name = rs.getString("alias");
						String xp = rs.getString("experience");
						String password = rs.getString(3);
						
						if (pass.equals(password)) {
							MainWindow.name = name;
							txtUser.setBackground(Color.BLUE);
							LoadData.setIdUser(0);
							LoadData.load();
							window.setVisible(false);
							new MenuWindow();							
						}  else {
							JOptionPane.showMessageDialog(null, "Datos invalidos");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Datos invalidos");
					}
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			} else {
				txtUser.setBackground(Color.RED);
				JOptionPane.showMessageDialog(null, "Debe ingresar su nombre");
			}
		}
	}
}








