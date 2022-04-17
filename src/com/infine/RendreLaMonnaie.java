package com.infine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RendreLaMonnaie {
	
	int nbPieces;
	Map<Integer, Integer> montantPieces = new HashMap<>();
	
	private int[] pieces;
    private Map<Integer, Map<Integer, RenduMonnaie>> resultatsIntermediaires = new HashMap<>();

    
	public RendreLaMonnaie(int[] args) {
		try {
			this.nbPieces = args[0];
			pieces = new int[this.nbPieces];
			if(args.length >= (args[0]+2)) {
				for(int i = 1; i <= args[0] ; i++) {
					pieces[i-1] = args[i];
				}
				for(int j = args[0]+1; j < args.length ; j++) {
					montantPieces.put(j-(args[0]+1), args[j]);
				}
			}
			else System.out.print("Format non respecter");
		}catch(IndexOutOfBoundsException e) {
			System.out.print(e);
		}
	}
	
	public int[] getPieces() {
		return pieces;
	}

	public void setPieces(int[] pieces) {
		this.pieces = pieces;
	}
	
	public int getNbPieces() {
		return nbPieces;
	}

	public void setNbPieces(int nbPieces) {
		this.nbPieces = nbPieces;
	}

	public Map<Integer, Integer> getMontantPieces() {
		return montantPieces;
	}

	public void setMontantPieces(Map<Integer, Integer> montantPieces) {
		this.montantPieces = montantPieces;
	}
	
	public static String toString(Map<Integer, Integer> montantPieces) {
		String list ="";
		for(int i = 0 ; i < montantPieces.size() ; i++) {
			list += montantPieces.get(i) + " ";
		}
		return list;
	}
	
	private Integer getIndexPieceMax(long montant) {
        Integer pieceMax = null;
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] <= montant) {
                pieceMax = i;
            }
        }
        return pieceMax;
    }
	
	private void initResultatsIntermediaires() {
        RenduMonnaie renduZero = new RenduMonnaie();
        Map<Integer, RenduMonnaie> zeroMap = new HashMap<>();
        for (int i = 0; i < pieces.length; i++) {
            zeroMap.put(i, renduZero);
        }
        resultatsIntermediaires.put(0, zeroMap);
    }
	
	public RenduMonnaie calculerRenduMonnaieOptimal(int montant) {
        initResultatsIntermediaires();
        return calculeMonnaie(montant);
    }
	
	private RenduMonnaie calculeMonnaie(int montant) {
        Integer indexPieceMax = getIndexPieceMax(montant);
        if (indexPieceMax == null) {
            return null;
        }
        resultatsIntermediaires.putIfAbsent(montant, new HashMap<>());
        RenduMonnaie meilleurRendu = null;
        int meilleurePiece = -1;
        for (int indexPiece = indexPieceMax; indexPiece >= 0; indexPiece--) {
            int nouveauMontant = montant - pieces[indexPiece];
            resultatsIntermediaires.putIfAbsent(nouveauMontant, new HashMap<>());
            RenduMonnaie renduOptimal = resultatsIntermediaires.get(nouveauMontant).get(indexPiece);
            if (renduOptimal == null) {
                renduOptimal = calculeMonnaie(nouveauMontant);
            }
            if (renduOptimal != null) {
                if ((meilleurRendu == null) || (meilleurRendu.nbPieces() > renduOptimal.nbPieces())) {
                    meilleurRendu = renduOptimal;
                    meilleurePiece = indexPiece;
                }
            }
        }
        if (meilleurRendu != null) {
            meilleurRendu = new RenduMonnaie(montant, meilleurRendu, meilleurePiece);
            resultatsIntermediaires.get(montant).put(meilleurePiece, meilleurRendu);
        }
        return meilleurRendu;
    }
	
	public class RenduMonnaie {
		
        private final int montant;
        private final int[] nbPiecesARendre;
        RenduMonnaie() {
            this.montant = 0;
            this.nbPiecesARendre = new int[pieces.length];
        }
        RenduMonnaie(int montant, RenduMonnaie precedent, int indexPiece) {
            this.montant = montant;
            int length = precedent.nbPiecesARendre.length;
            nbPiecesARendre = new int[length];
            System.arraycopy(precedent.nbPiecesARendre, 0, nbPiecesARendre, 0, length);
            nbPiecesARendre[indexPiece]++;
        }
        public int[] getNbPiecesARendre() {
            return nbPiecesARendre;
        }
        public long getMontant() {
            return montant;
        }
        public int nbPieces() {
            int nbPieces = 0;
            for (int i = 0; i < pieces.length; i++) {
                nbPieces += nbPiecesARendre[i];
            }
            return nbPieces;
        }
    }
	
	public static List<Integer> calculerMonnaie(RendreLaMonnaie rendreLaMonnaie) {
		String text ="Nombre des valeurs est : "+rendreLaMonnaie.getNbPieces()+" sont : ";
		List<Integer> result = new ArrayList<Integer>();
		for(int i = 0 ; i < rendreLaMonnaie.getPieces().length ; i++) {
			text+= rendreLaMonnaie.getPieces()[i] + " ";
		}
		System.out.println(text);
		
		Map<Integer, Integer> montants = rendreLaMonnaie.getMontantPieces();
        for(int index = 0 ; index <montants.size() ; index++) {
        	RendreLaMonnaie.RenduMonnaie rendu = rendreLaMonnaie.calculerRenduMonnaieOptimal(montants.get(index));
        	System.out.println("--------- La valeur "+montants.get(index)+" contient ---------");
        	if(rendu!=null) { 
        		for (int i = 0; i < rendreLaMonnaie.getPieces().length; i++) {
	                if (rendu.getNbPiecesARendre()[i] != 0) {
                		System.out.println(rendu.getNbPiecesARendre()[i] + " pièces de " + rendreLaMonnaie.getPieces()[i] + " €");
                		result.add(rendu.getNbPiecesARendre()[i]);
                		result.add(rendreLaMonnaie.getPieces()[i]);
                	}
                }System.out.println("");
            }else 
            	{
            		result.add(-1);
            		System.out.println("-1\n");
            	}
        }
        return result;
    }
	

	public static void main(String[] args) {
		RendreLaMonnaie rlm = new RendreLaMonnaie(new int[] {2,9,5,1,15,32,1});
		calculerMonnaie(rlm);
	}

}
