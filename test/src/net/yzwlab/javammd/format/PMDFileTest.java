package net.yzwlab.javammd.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.yzwlab.javammd.ReadException;
import net.yzwlab.javammd.io.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

public class PMDFileTest {
	@Test
	public void testOpen() throws IOException, ReadException {
		String testFile = "test.pmd";
		Path path = Paths.get(testFile);
		byte[] data = Files.readAllBytes(path);
		ByteBuffer buffer = new ByteBuffer(data);
		
		PMDFile pmd = new PMDFile();
		boolean success = pmd.open(buffer);
		Assert.assertEquals(true, success);
		
	}
}
