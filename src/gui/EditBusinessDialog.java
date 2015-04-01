package gui;

import gui.common.CustomComboBox;
import gui.common.CustomTextField;
import gui.common.ErrorDialog;
import gui.common.FloatPanel;
import gui.common.KeyValuePanelBuilder;
import gui.common.OkCancelDialog;

import java.math.BigDecimal;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXDatePicker;

import romar.Romar;
import romar.RomarBusiness;
import romar.RomarMonth;
import base.Util;

@SuppressWarnings("serial")
public class EditBusinessDialog extends OkCancelDialog
{
	public EditBusinessDialog( RomarMonth month )
	{
		super( "Add Business" );
		this.month = month;
		this.panel = new EditBusinessPanel();

		init();
	}

	public EditBusinessDialog( RomarBusiness biz )
	{
		super( "Edit Business" );
		this.month = biz.month;
		this.panel = new EditBusinessPanel( biz );
		this.edit = true;

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
			RomarBusiness biz = panel.toItem();
			String err;
			if( ( err = Util.isValid( biz ) ) == null )
			{
				if( edit ) {
					month.edit( panel.toItem() );
				} else {
					month.add( panel.toItem() );
				}

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
	private EditBusinessPanel panel;
	private boolean edit = false;

	private class EditBusinessPanel extends JPanel
	{
		public EditBusinessPanel()
		{
			this( null );
		}

		@SuppressWarnings("deprecation")
		public EditBusinessPanel( RomarBusiness biz )
		{
			this.biz = biz;

			KeyValuePanelBuilder b = new KeyValuePanelBuilder( this );

			if( biz == null || ( biz != null && !biz.locked && !biz.npwd ) )
			{
				b.add( "Date", dateField );
				b.add( "#", clientNumField );
				b.add( "Surname", surnameField );
				b.add( "Initial", initialField );
				b.add( "Company", companyField );
				b.add( "Type", typeField );
				b.add( "Prem/Loan", amountField );
				b.add( "Term", termField );
				b.add( "Source", sourceField );
				b.add( "TBC", tbcField );
				b.add( "RI 2", ri2Field );
			}

			b.add( "Paid", paidField );
			b.gap();

			if( biz == null )
			{
				this.biz = new RomarBusiness( month );
				if( Romar.selectedMonth != null && Romar.selectedYear != null )
				{
					this.biz.date.setDate( 1 );
					this.biz.date.setMonth( Romar.selectedMonth.ordinal() );
					this.biz.date.setYear( Romar.selectedYear-1900 );
					dateField.setDate( this.biz.date );
				}
			}
			else
			{
				fromItem( biz );
			}
		}

		public RomarBusiness toItem()
		{
			biz.date = dateField.getDate();
			
			String cnf = clientNumField.getText();
			if( cnf != null && cnf.length() > 0 ) {
				biz.clientNum = Integer.parseInt( cnf );
			} else {
				biz.clientNum = 0;
			}

			biz.surname = surnameField.getText();
			biz.initial = initialField.getText();
			biz.company = (String)companyField.getSelectedItem();
			biz.type = (String)typeField.getSelectedItem();
			biz.amount = new BigDecimal( amountField.toFloat() );

			String tf = termField.getText();
			if( tf != null && tf.length() > 0 ) {
				biz.term = Integer.parseInt( tf );
			} else {
				biz.term = 0;
			}

			biz.source = (String)sourceField.getSelectedItem();
			biz.tbc = new BigDecimal( tbcField.toFloat() );
			biz.ri2 = (String)ri2Field.getSelectedItem();
			biz.paid = new BigDecimal( paidField.toFloat() );

			return biz;
		}
	
		public void fromItem( RomarBusiness item )
		{
			dateField.setDate( item.date );
			clientNumField.setText( item.clientNum.toString() );
			surnameField.setText( item.surname );
			initialField.setText( item.initial );
			companyField.setSelectedItem( item.company );
			typeField.setSelectedItem( item.type );
			amountField.fromFloat( item.amount.floatValue() );
			termField.setText( item.term.toString() );
			sourceField.setSelectedItem( item.source );
			tbcField.fromFloat( item.tbc.floatValue() );
			ri2Field.setSelectedItem( item.ri2 );
			paidField.fromFloat( item.paid.floatValue() );
		}

		private JXDatePicker dateField = BusinessComponentFactory.newDateComponent();
		private CustomTextField clientNumField = BusinessComponentFactory.newClientNumComponent();
		private CustomTextField initialField = BusinessComponentFactory.newInitialComponent();
		private CustomTextField surnameField = BusinessComponentFactory.newSurnameComponent();
		private CustomComboBox companyField = BusinessComponentFactory.newCompanyComponent();
		private CustomComboBox typeField = BusinessComponentFactory.newTypeComponent();
		private FloatPanel amountField = BusinessComponentFactory.newAmountComponent();
		private CustomTextField termField = BusinessComponentFactory.newTermComponent();
		private CustomComboBox sourceField = BusinessComponentFactory.newSourceComponent();
		private FloatPanel tbcField = BusinessComponentFactory.newTbcComponent();
		private CustomComboBox ri2Field = BusinessComponentFactory.newRi2Component();
		private FloatPanel paidField = BusinessComponentFactory.newPaidComponent();

		private RomarBusiness biz;
	}
}
