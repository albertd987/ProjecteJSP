package test;

import dao.DAOComponent;
import model.Component;

public class TestDAOComponent {
    public static void main(String[] args) {
        DAOComponent dao = new DAOComponent();
        
        System.out.println("📊 Total components: " + dao.countTotal());
        System.out.println("\n📋 Tots els components:");
        
        for (Component c : dao.findAll()) {
            System.out.println("   " + c.getCmCodi() + " - " + c.getItNom() + 
                             " | Preu: " + c.getCmPreuMig() + " €");
        }
        
        System.out.println("\n🔍 Buscar C001:");
        Component c001 = dao.findById("C001");
        if (c001 != null) {
            System.out.println("   Trobat: " + c001);
        }
    }
}