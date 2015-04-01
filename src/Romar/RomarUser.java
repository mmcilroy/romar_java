package romar;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RomarUser implements Serializable
{
	public String id = "";
	public String pass = "";
	public Float riPct = 1.0f;
	public Boolean admin = false;
}
