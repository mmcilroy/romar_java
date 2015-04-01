package gui;

import gui.common.Gbc;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import romar.RomarMonth;

@SuppressWarnings("serial")
public class WorkMonthPanel extends JPanel
{
	public WorkMonthPanel()
	{
		dataLabels = new JLabel[cols.length];
		for( int i=0; i<cols.length; i++ ) {
			dataLabels[i] = new JLabel();
		}

		initLayout();
		initControls();
	}

	public void display( RomarMonth month )
	{
		this.month = month;

		dataLabels[0].setText( month.letters.toString() );
		dataLabels[1].setText( month.appt.toString() );
		dataLabels[2].setText( month.meeting.toString() );
		dataLabels[3].setText( month.sale.toString() );
	}

	private void initLayout()
	{
		setLayout( new GridBagLayout() );

		int i;
		for( i=0; i<cols.length; i++ )
		{
			add( new JLabel( "<html><b>" + cols[i] ), new Gbc( Gbc.NONE, i, 0 ) );
			add( dataLabels[i], new Gbc( Gbc.NONE, i, 1 ).XY( 0, 0 ) );
		}

		add( editButton, new Gbc( Gbc.NONE, i+1, 1 ).a( Gbc.EAST ) );
	}

	private void initControls()
	{
		editButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onEdit();
			} } );
	}

	private void onEdit()
	{
		if( month != null )
		{
			EditWorkDialog dialog = new EditWorkDialog( month );
			if( dialog.isOk() )
			{
				month.year.calc();
				display( month );
			}
		}
	}

	private RomarMonth month;

	private JLabel dataLabels[];
	private JButton editButton = new JButton( "Edit" );

	String cols[] = { "Contacts", "Appt", "Meeting", "Sale" };
}
