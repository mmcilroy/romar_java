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
import romar.RomarPipeline;
import base.Util;

@SuppressWarnings("serial")
public class EditPipelineDialog extends OkCancelDialog
{
	public EditPipelineDialog()
	{
		super( "Add Pipeline" );
		this.panel = new EditPipelinePanel( null );

		init();
	}

	public EditPipelineDialog( RomarPipeline pipeline )
	{
		super( "Edit Pipeline" );
		this.panel = new EditPipelinePanel( pipeline );

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
			RomarPipeline p = panel.toItem();
			String err;
			if( ( err = Util.isValid( p ) ) == null )
			{
				Romar.edit( p );
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

	private EditPipelinePanel panel;

	private class EditPipelinePanel extends JPanel
	{
		public EditPipelinePanel( RomarPipeline pipeline )
		{
			this.pipeline = pipeline;

			KeyValuePanelBuilder b = new KeyValuePanelBuilder( this );
			b.add( "Date", dateField );
			b.add( "#", clientNumField );
			b.add( "Surname", surnameField );
			b.add( "Initial", initialField );
			b.add( "Type", typeField );
			b.add( "Prem/Loan", premiumField );
			b.add( "TBC", tbcField );
			b.add( "Status", statusField );
			b.gap();

			if( pipeline == null ) {
				this.pipeline = new RomarPipeline();
			} else {
				fromItem( pipeline );
			}
		}
	
		public RomarPipeline toItem()
		{
			pipeline.date = dateField.getDate();
			
			String cnf = clientNumField.getText();
			if( cnf != null && cnf.length() > 0 ) {
				pipeline.clientNum = Integer.parseInt( cnf );
			} else {
				pipeline.clientNum = 0;
			}

			pipeline.surname = surnameField.getText();
			pipeline.initial = initialField.getText();
			pipeline.type = (String)typeField.getSelectedItem();
			pipeline.premium = new BigDecimal( premiumField.toFloat() );
			pipeline.tbc = new BigDecimal( tbcField.toFloat() );
			pipeline.status = (String)statusField.getSelectedItem();
			return pipeline;
		}
	
		public void fromItem( RomarPipeline p )
		{
			dateField.setDate( p.date );
			clientNumField.setText( p.clientNum.toString() );
			surnameField.setText( p.surname );
			initialField.setText( p.initial );
			typeField.setSelectedItem( p.type );
			premiumField.fromFloat( p.premium.floatValue() );
			tbcField.fromFloat( p.tbc.floatValue() );
			statusField.setSelectedItem( p.status );
		}

		private JXDatePicker dateField = PipelineComponentFactory.newDateComponent();
		private CustomTextField clientNumField = BusinessComponentFactory.newClientNumComponent();
		private CustomTextField initialField = BusinessComponentFactory.newInitialComponent();
		private CustomTextField surnameField = BusinessComponentFactory.newSurnameComponent();
		private CustomComboBox typeField = PipelineComponentFactory.newTypeComponent();
		private FloatPanel premiumField = PipelineComponentFactory.newPremiumComponent();
		private FloatPanel tbcField = PipelineComponentFactory.newTbcComponent();
		private CustomComboBox statusField = PipelineComponentFactory.newStatusComponent();

		private RomarPipeline pipeline;
	}
}
