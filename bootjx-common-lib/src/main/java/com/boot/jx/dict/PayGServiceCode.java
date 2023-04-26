package com.boot.jx.dict;

public enum PayGServiceCode {
	// Full Code
	KNET("knet"), BENEFIT("benefit"), OMANNET("omannet"), KOMANNET("komannet"), BENEFIT_UPGRADE("benefit_upgrade")

	, WT("wt"), KNET2("knet2"), KNET2_INQUIRY("knet2_inquiry"), PB("pb"),

	// Short Code - Max 3 Chars
	K(KNET), BOD(BENEFIT),
	
	CASH("CASH"), LOCAL("LOCAL"),IPOS("ipos");

	public static final PayGServiceCode DEFAULT = KNET;

	private String pgEnum;

	private PayGServiceCode longEnum;
	private PayGServiceCode shortEnum;

	PayGServiceCode(String pgEnum) {
		this.pgEnum = pgEnum;
		this.longEnum = this;
		this.shortEnum = this;
	}

	PayGServiceCode(PayGServiceCode longEnum) {
		this.longEnum = longEnum;
		this.shortEnum = this;
	}

	public String getPgEnum() {
		return this.pgEnum;
	}


	public PayGServiceCode longEnum() {
		return this.longEnum;
	}

	public PayGServiceCode shortEnum() {
		return this.shortEnum;
	}

	public void setShortEnum(PayGServiceCode shortEnum) {
		this.shortEnum = shortEnum;
	}

	static {
		KNET.setShortEnum(K);
		KNET2.setShortEnum(K);
		BENEFIT.setShortEnum(BOD);
	}

}



