package gui;

import gui.common.CustomComboBox;
import gui.common.CustomTextField;
import gui.common.FloatPanel;

import org.jdesktop.swingx.JXDatePicker;

import romar.Romar;

public class PipelineComponentFactory
{
	public static JXDatePicker newDateComponent()
	{
		JXDatePicker dp = new JXDatePicker();
		dp.setFormats( new String[] { "dd/MM/yyyy" } );
		return dp;
	}

	public static CustomTextField newClientNumComponent()
	{
		CustomTextField ctf = new CustomTextField( 8 );
        ctf.setAllowDigit( true );
        return ctf;
	}

	public static CustomTextField newClientNameComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setAllowSpace( true );
        ctf.setConvertToUpper( true );
        return ctf;
	}

	public static CustomComboBox newTypeComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		if( Romar.typeStrings != null ) {
			for( String s : Romar.typeStrings ) {
				ccb.addItem( s );
			}
		}

		return ccb;
	}

	public static FloatPanel newPremiumComponent()
	{
		return new FloatPanel();
	}

	public static FloatPanel newTbcComponent()
	{		
		return new FloatPanel();
	}

	public static CustomComboBox newStatusComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		if( Romar.statusStrings != null ) {
			for( String s : Romar.statusStrings ) {
				ccb.addItem( s );
			}
		}

		return ccb;
	}
}
