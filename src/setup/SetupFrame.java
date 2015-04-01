package setup;

import gui.LoginDialog;
import gui.common.ErrorDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import romar.Romar;
import romar.RomarUser;

@SuppressWarnings("serial")
public class SetupFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main( String[] args )
	{
		{
			try {
				UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
			} catch( Exception e ) {
				;
			}
		}

		LoginDialog dialog = new LoginDialog();

		if( dialog.isOk() && Romar.user.admin )
		{
			EventQueue.invokeLater( new Runnable()
			{
				public void run()
				{
					try
					{
						SetupFrame frame = new SetupFrame();
						frame.displayUsers();
						frame.setLocationRelativeTo( null );
						frame.setVisible( true );
					}
					catch( Exception e )
					{
						e.printStackTrace();
					}
				}
			} );
		}
	}

	private JButton addUserButton;
	private JButton deleteUserButton;
	private JButton editUserButton;

	private JList<String> userList;
	private DefaultListModel<String> userListModel = new DefaultListModel<String>();

	public SetupFrame()
	{
		setTitle( "Romar Admin Console" );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWeights = new double[]{1.0};
		gbl_panel.rowWeights = new double[]{1.0};
		panel.setLayout(gbl_panel);
		
		BufferedImage img = null;

		try {
			img = ImageIO.read( new File( "logo.jpg" ) );
		} catch( IOException e ) {
			;
		}

		JLabel picLabel;

		if( img != null ) {
			picLabel = new JLabel( new ImageIcon( img ) );
		} else {
			picLabel = new JLabel( "err" );
		}

		GridBagConstraints gbc_imagePanel = new GridBagConstraints();
		gbc_imagePanel.anchor = GridBagConstraints.NORTHWEST;
		gbc_imagePanel.gridx = 1;
		gbc_imagePanel.gridy = 0;
		panel.add(picLabel, gbc_imagePanel);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(0,42,92));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(panel_2, BorderLayout.CENTER);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWeights = new double[]{1.0};
		gbl_panel_2.rowWeights = new double[]{1.0};
		panel_2.setLayout(gbl_panel_2);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 0;
		panel_2.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWeights = new double[]{1.0, 0.0};
		gbl_panel_5.rowWeights = new double[]{0.0, 1.0};
		panel_5.setLayout(gbl_panel_5);
		
		JLabel lblNewLabel = new JLabel("Users");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_5.add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.insets = new Insets(0, 0, 0, 5);
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 1;
		panel_5.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWeights = new double[]{1.0};
		gbl_panel_4.rowWeights = new double[]{1.0};
		panel_4.setLayout(gbl_panel_4);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_4.add(scrollPane, gbc_scrollPane);
		
		userList = new JList<String>();
		scrollPane.setViewportView(userList);
		userList.setModel( userListModel );
		userList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.anchor = GridBagConstraints.NORTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 1;
		panel_5.add(panel_3, gbc_panel_3);
		panel_3.setLayout(new GridLayout(3, 1, 0, 4));

		addUserButton = new JButton( "Add User" );
		addUserButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				new EditUserDialog(); displayUsers();
			} } );

		deleteUserButton = new JButton( "Delete User" );
		deleteUserButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				try
				{
					Romar.deleteUser( userList.getSelectedValue() );
					displayUsers();
				}
				catch( IOException x )
				{
					ErrorDialog.show( SetupFrame.this, "Delete user failed!", x );
				} } } );

		panel_3.add( addUserButton );
		
				editUserButton = new JButton( "Edit User" );
				editUserButton.addActionListener( new ActionListener() {
					public void actionPerformed( ActionEvent e ) {
						RomarUser user = Romar.loadUser( userList.getSelectedValue() );
						if( user != null ) {
							new EditUserDialog( user );
						} } } );
				panel_3.add( editUserButton );
		panel_3.add( deleteUserButton );

		setSize( 500, 500 );
	}

	public void displayUsers()
	{
		userListModel.removeAllElements();

		try {
			Romar.load();
		} catch ( IOException e ) {
			e.printStackTrace();
		}

		for( String user : Romar.availableUsers ) {
			userListModel.addElement( user );
		}
	}
}
