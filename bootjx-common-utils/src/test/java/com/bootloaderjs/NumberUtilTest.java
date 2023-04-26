package com.bootloaderjs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.boot.utils.NumberUtil;

@RunWith(SpringRunner.class)
public class NumberUtilTest {

	NumberUtil util = new NumberUtil();

	@Test
	public void testisNumber() {
		assertFalse(util.isIntegerValue(null));
		assertFalse(util.isIntegerValue(new BigDecimal(9.30)));
		assertTrue(util.isIntegerValue(new BigDecimal(9)));
	}
}
