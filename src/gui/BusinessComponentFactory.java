package gui;

import gui.common.CustomComboBox;
import gui.common.CustomTextField;
import gui.common.FloatPanel;

import org.jdesktop.swingx.JXDatePicker;

import romar.Romar;

public class BusinessComponentFactory
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

	public static CustomTextField newInitialComponent()
	{
		CustomTextField ctf = new CustomTextField( 2 );
        ctf.setAllowAlpha( true );
        ctf.setConvertToUpper( true );
        ctf.setMaximumLength( 1 );
        return ctf;
	}

	public static CustomTextField newSurnameComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setAllowSpace( true );
        ctf.setAllowSpecial( true );
        ctf.setConvertToUpper( true );
        return ctf;
	}

	public static CustomTextField newAccountNumberComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setAllowDigit( true );
        ctf.setAllowSpecial( true );
        ctf.setConvertToUpper( true );

        return ctf;
	}

	public static CustomComboBox newCompanyComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		if( Romar.companyStrings != null ) {
			for( String s : Romar.companyStrings ) {
				ccb.addItem( s );
			}
		}

		return ccb;
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

	public static FloatPanel newAmountComponent()
	{		
		return new FloatPanel();
	}

	public static CustomTextField newTermComponent()
	{
		CustomTextField ctf = new CustomTextField( 8 );
        ctf.setAllowDigit( true );
        return ctf;
	}

	public static CustomComboBox newSourceComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		if( Romar.sourceStrings != null ) {
			for( String s : Romar.sourceStrings ) {
				ccb.addItem( s );
			}
		}

		return ccb;
	}

	public static FloatPanel newTbcComponent()
	{		
		return new FloatPanel();
	}

	public static FloatPanel newPaidComponent()
	{		
		return new FloatPanel();
	}

	public static FloatPanel newRi1Component()
	{		
		return new FloatPanel();
	}

	public static CustomComboBox newRi2Component()
	{		
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "" );

		for( String user : Romar.availableUsers ) {
			ccb.addItem( user );
		}

        return ccb;
	}

	public static JXDatePicker newPostedComponent()
	{
		JXDatePicker dp = new JXDatePicker();
		dp.setFormats( new String[] { "dd/MM/yyyy" } );
		return dp;
	}
}
