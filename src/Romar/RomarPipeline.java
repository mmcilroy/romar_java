package romar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class RomarPipeline implements Serializable
{
	public RomarPipeline()
	{
		this.id = nextId++;
	}

	public Long id;
	public Date date = new Date();

	public Integer clientNum = new Integer( 0 );
	public String surname = new String();
	public String initial = new String();

	public String type = new String();
	public BigDecimal premium = new BigDecimal( 0 );
	public BigDecimal tbc = new BigDecimal( 0 );
	public String status = new String();

	public static Long nextId = System.currentTimeMillis();
}
