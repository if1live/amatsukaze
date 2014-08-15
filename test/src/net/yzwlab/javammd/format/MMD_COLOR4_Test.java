package net.yzwlab.javammd.format;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.graphics.Color;

public class MMD_COLOR4_Test {
	@Test
	public void testToColor() {
		MMD_COLOR4 orig = null;
		Color retval = null;
		
		orig = new MMD_COLOR4();
		orig.r = (byte)255;
		retval = orig.toColor();
		Assert.assertEquals(new Color(1.0f, 0, 0, 0), retval);
		
		orig = new MMD_COLOR4();
		orig.g = (byte)255;
		retval = orig.toColor();
		Assert.assertEquals(new Color(0, 1.0f, 0, 0), retval);
		
		orig = new MMD_COLOR4();
		orig.b = (byte)255;
		retval = orig.toColor();
		Assert.assertEquals(new Color(0, 0, 1.0f, 0), retval);
		
		orig = new MMD_COLOR4();
		orig.a = (byte)255;
		retval = orig.toColor();
		Assert.assertEquals(new Color(0, 0, 0, 1.0f), retval);
	}
}
