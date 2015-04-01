package gui;

import gui.common.CustomTextField;
import gui.common.ErrorDialog;
import gui.common.KeyValuePanelBuilder;
import gui.common.OkCancelDialog;

import javax.swing.JPanel;

import romar.Romar;
import romar.RomarMonth;
import base.Util;

@SuppressWarnings("serial")
public class EditWorkDialog extends OkCancelDialog
{
	public EditWorkDialog( RomarMonth month )
	{
		super( "Edit Work" );
		this.month = month;
		this.panel = new EditWorkPanel( month );

		init();
	}

	protected JPanel initContent()
	{
		return panel;
	}

	protected void onOk()
	{
		try
		{
			String err;
			if( ( err = Util.isValid( month ) ) == null )
			{
				panel.toItem();
				Romar.save( month.year );
				isOk( true );
				dispose();
			}
			else
			{
				ErrorDialog.show( this, "Error", err );
			}
		}
		catch ( Exception e )
		{
			ErrorDialog.show( this, "Save failed!", e );
		}
	}

	private RomarMonth month;
	private EditWorkPanel panel;

	private class EditWorkPanel extends JPanel
	{
		public EditWorkPanel( RomarMonth work )
		{
	    	KeyValuePanelBuilder b = new KeyValuePanelBuilder( this );
			b.add( "Contacts", contactsField );
			b.add( "Appt", apptField );
			b.add( "Meeting", meetingField );
			b.add( "Sale", saleField );
			b.gap();

			fromItem( work );
		}
	
		public RomarMonth toItem()
		{
			month.letters = Integer.parseInt( contactsField.getText() );
			month.appt = Integer.parseInt( apptField.getText() );
			month.meeting = Integer.parseInt( meetingField.getText() );
			month.sale = Integer.parseInt( saleField.getText() );

			return month;
		}
	
		public void fromItem( RomarMonth work )
		{
			contactsField.setText( work.letters.toString() );
			apptField.setText( work.appt.toString() );
			meetingField.setText( work.meeting.toString() );
			saleField.setText( work.sale.toString() );
		}

		private CustomTextField newInputField()
		{
			CustomTextField ctf = new CustomTextField( 8 );
	        ctf.setAllowDigit( true );
	        return ctf;
		}
		
		private CustomTextField contactsField = newInputField();
		private CustomTextField apptField = newInputField();
		private CustomTextField meetingField = newInputField();
		private CustomTextField saleField = newInputField();
	}
}
