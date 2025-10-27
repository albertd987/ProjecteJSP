package model;


/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:38:04
 */
public class Item {

	private String itCodi;
	private String itDesc;
	private String itFoto;
	private String itNom;
	private Integer itStock;
	private String itTipus;
	public ProdItem m_ProdItem;

	public Item(){

	}

	public void finalize() throws Throwable {

	}
}//end Item