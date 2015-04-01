package gui;

import gui.common.Gbc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SetupDialog extends JDialog
{
	public static void main( String[] args )
	{
		new SetupDialog().init();
	}

	public void init()
	{
		initComponents();
		initLayout();
		setLocationRelativeTo( null );
		setVisible( true );
	}

	private void initComponents()
	{
		setTitle( "Setup" );
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setModalityType( ModalityType.APPLICATION_MODAL );
		setLayout( new GridBagLayout() );
		setResizable( false );
	}

	private void initLayout()
	{
		JPanel buttonPanel = new JPanel();
		buttonPanel.add( addButton, new Gbc( Gbc.NONE, 1, 0 ).XY( 0, 0 ).i( 10, 0, 10, 10 ) );
		buttonPanel.add( deleteButton, new Gbc( Gbc.NONE, 1, 1 ).XY( 0, 0 ).i( 5, 0, 0, 10 ) );
		buttonPanel.add( pwButton, new Gbc( Gbc.NONE, 1, 2 ).XY( 0, 0 ).i( 5, 0, 0, 10 ) );

		Container content = getContentPane();
		content.setLayout( new BorderLayout() );
		content.add( userList, BorderLayout.CENTER );
		content.add( buttonPanel, BorderLayout.SOUTH );
		
		setSize( 600, 600 );
	}

	private JList<String> userList = new JList<String>();
	private JButton addButton = new JButton( "Add" );
	private JButton deleteButton = new JButton( "Delete" );
	private JButton pwButton = new JButton( "Set Password" );
}
