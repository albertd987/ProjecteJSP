package model;


/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:38:03
 */
public class Component extends Item {

	private String cmCodi;
	private String cmCodiFabricant;
	private Double cmPreuMig;
	private String cmUmCodi;
	public ProvComp m_ProvComp;

	public Component(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end Component