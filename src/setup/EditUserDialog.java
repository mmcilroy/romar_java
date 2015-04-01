package setup;

import gui.common.CustomComboBox;
import gui.common.CustomTextField;
import gui.common.ErrorDialog;
import gui.common.FloatPanel;
import gui.common.KeyValuePanelBuilder;
import gui.common.OkCancelDialog;

import javax.swing.JPanel;
import javax.swing.JPasswordField;

import romar.Romar;
import romar.RomarUser;

@SuppressWarnings("serial")
public class EditUserDialog extends OkCancelDialog
{
	public EditUserDialog( RomarUser user )
	{
		super( "Add User" );

		this.user = user;
		this.panel = new AddUserPanel();

		init();
	}

	public EditUserDialog()
	{
		this( null );
	}

	protected JPanel initContent()
	{
		return panel;
	}

	protected void onOk()
	{
		try
		{
			String user = panel.getUser();
			if( user.length() <= 0 ) {
				throw new Exception( "Invalid user id! Cannot be blank" );
			}

			String pass1 = panel.getPass1();
			String pass2 = panel.getPass2();

			if( pass1.length() <= 0 || pass2.length() <= 0 ) {
				throw new Exception( "Invalid password! Cannot be blank" );
			}

			if( pass1.compareTo( pass2 ) != 0 ) {
				throw new Exception( "Passwords do not match!" );
			}

			float ri = panel.getRI();
			if( ri < 0.1f ) {
				throw new Exception( "Invalid RI! Must be greater than 0.0" );
			}

			if( this.user == null ) {
				Romar.addUser( panel.getUser(), pass1, panel.isAdmin(), panel.getRI() );
			} else {
				Romar.editUser( panel.getUser(), pass1, panel.isAdmin(), panel.getRI() );
			}

			isOk( true );
			dispose();
		}
		catch ( Exception e )
		{
			ErrorDialog.show( this, "Add user failed!", e );
		}
	}

	private RomarUser user;
	private AddUserPanel panel;

	@SuppressWarnings("deprecation")
	private class AddUserPanel extends JPanel
	{
		public AddUserPanel()
		{
			userField.setAllowAlpha( true );
			userField.setConvertToLower( true );

			adminField.addItem( "N" );
			adminField.addItem( "Y" );

	    	KeyValuePanelBuilder b = new KeyValuePanelBuilder( this );
			b.add( "User", userField );
			b.add( "Password", pass1Field );
			b.add( "Confirm Password", pass2Field );
			b.add( "Admin", adminField );
			b.add( "RI", riField );
			b.gap();

			if( user != null )
			{
				userField.setText( user.id );
				pass1Field.setText( user.pass );
				pass2Field.setText( user.pass );
				riField.fromFloat( user.riPct );
				if( user.admin ) {
					adminField.setSelectedItem( "Y" );
				} else {
					adminField.setSelectedItem( "N" );
				}
			}
		}

		public String getUser()
		{
			return userField.getText();
		}
	
		public String getPass1()
		{
			return pass1Field.getText();
		}

		public String getPass2()
		{
			return pass2Field.getText();
		}

		public boolean isAdmin()
		{
			return ((String)adminField.getSelectedItem()).compareTo( "Y" ) == 0;
		}

		public float getRI()
		{
			return riField.toFloat();
		}

		private CustomTextField userField = new CustomTextField( 10 );
		private JPasswordField pass1Field = new JPasswordField( 10 );
		private JPasswordField pass2Field = new JPasswordField( 10 );
		private CustomComboBox adminField = new CustomComboBox();
		private FloatPanel riField = new FloatPanel();
	}
}
