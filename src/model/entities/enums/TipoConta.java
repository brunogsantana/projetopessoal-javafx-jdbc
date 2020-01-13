package model.entities.enums;

public enum TipoConta {
	
	CONTA_DINHEIRO (10),
	CONTA_INVESTIMENTO (20) ;

	private final int tipoConta;

	TipoConta(int tipoConta) {
		this.tipoConta = tipoConta;
	}

	public int getTipoContaAsInt() {
		return tipoConta;
	}

	public String getTipoContaAsString() {
		return String.valueOf(tipoConta);
	}

	public static TipoConta convertIntToTipoConta(int iTipoConta) {
		for (TipoConta tipoConta : TipoConta.values()) {
			if (tipoConta.getTipoContaAsInt() == iTipoConta) {
				return tipoConta;
			}
		}
		return null;
	}

	public static TipoConta convertStringToTipoConta(String inputTipoConta) {
		for (TipoConta tipoConta : TipoConta.values()) {
			if (tipoConta.getTipoContaAsString().equals(inputTipoConta)) {
				return tipoConta;
			}
		}
		return null;
	}

	public static int convertTipoContaToInt(TipoConta inputTipoConta) {
		for (TipoConta tipoConta : TipoConta.values()) {
			if (tipoConta.getTipoContaAsInt() == inputTipoConta.getTipoContaAsInt()) {
				return tipoConta.getTipoContaAsInt();
			}
		}
		return -1;
	}

	public static String convertTipoContaToString(TipoConta inputTipoConta) {
		for (TipoConta tipoConta : TipoConta.values()) {
			if (tipoConta.getTipoContaAsInt() == inputTipoConta.getTipoContaAsInt()) {
				return tipoConta.getTipoContaAsString();
			}
		}
		return null;
	}
}

