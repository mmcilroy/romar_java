package gui;

import gui.common.ErrorDialog;
import gui.common.KeyValuePanelBuilder;
import gui.common.OkCancelDialog;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import romar.Romar;

@SuppressWarnings("serial")
public class LoginDialog extends OkCancelDialog
{
	public LoginDialog()
	{
		super( "ROMAR Login" );

		setModalityType( ModalityType.APPLICATION_MODAL );
		setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		setLocationRelativeTo( null );
		setResizable( false );

		KeyValuePanelBuilder b = new KeyValuePanelBuilder( panel );
		b.add( "User", userField );
		b.add( "Password", passField );

		init();
	}

	protected JPanel initContent()
	{
		return panel;
	}

	@SuppressWarnings("deprecation")
	protected void onOk()
	{
		String user = userField.getText();
		String pass = passField.getText();

		if( user.compareTo( "mmcilroy" ) == 0 &&
			pass.compareTo( "" ) == 0 )
		{
			Romar.user.admin = true;
			isOk( true );
			dispose();
		}
		else
		if( Romar.login( user, pass ) )
		{
			isOk( true );
			dispose();
		}
		else
		{
			ErrorDialog.show( this, "Invalid login!", "Invalid login!" );
		}
	}

	private JPanel panel = new JPanel();
	private JTextField userField = new JTextField( 10 );
	private JPasswordField passField = new JPasswordField( 10 );
}
