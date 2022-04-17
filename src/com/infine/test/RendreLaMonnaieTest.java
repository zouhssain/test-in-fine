package com.infine.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.infine.RendreLaMonnaie;

public class RendreLaMonnaieTest {

	@Test
    public void monnaie_2_9_val_24_15_montant_1_9_3_5_3_5() {
		RendreLaMonnaie rlm = new RendreLaMonnaie(new int[] {2,9,5,24,15});
		int[] resultatAttendues = new int[]{1,9,3,5,3,5};
        this.calculerMonnaieTest(rlm,resultatAttendues);
    }
	
	@Test
    public void monnaie_2_9_val_24_montant_1_9_3_5() {
		RendreLaMonnaie rlm = new RendreLaMonnaie(new int[] {2,9,5,24});
		int[] resultatAttendues = new int[]{1,9,3,5};
        this.calculerMonnaieTest(rlm,resultatAttendues);
    }
	
	@Test
    public void monnaie_2_9_val_1_montant_moins1() {
		RendreLaMonnaie rlm = new RendreLaMonnaie(new int[] {2,9,5,1});
		int[] resultatAttendues = new int[]{-1};
        this.calculerMonnaieTest(rlm,resultatAttendues);
    }
    
    private void calculerMonnaieTest(RendreLaMonnaie rendreLaMonnaie, int[] nbPiecesAttendues) {
    	
    	List<Integer> toCompare=null;
    	for(int i = 0 ; i < rendreLaMonnaie.getMontantPieces().size() ; i++) {
    		rendreLaMonnaie.calculerRenduMonnaieOptimal(rendreLaMonnaie.getMontantPieces().get(i));
            toCompare = new ArrayList<Integer>();
            for(Integer valeur:nbPiecesAttendues) {
            	toCompare.add(valeur);
            }
    	}
		assertEquals(toCompare ,RendreLaMonnaie.calculerMonnaie(rendreLaMonnaie));
    }

}
