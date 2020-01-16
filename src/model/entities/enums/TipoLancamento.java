package model.entities.enums;

public enum TipoLancamento {
	
	CONTA_DINHEIRO ("Conta Dinheiro"),
	CONTA_INVESTIMENTO ("Conta Investimento"),
	CONTA_CORRENTE ("Conta Corrente"),
	CONTA_EMPRESARIAL ("Conta Empresarial");

	private final String tipoConta;

	TipoLancamento(String tipoConta) {
		this.tipoConta = tipoConta;
	}
	
	
	public String toString() {
		return tipoConta;
	}
	


	/* 
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
	} */
}

