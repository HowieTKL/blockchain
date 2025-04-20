package org.howietkl.blockchain.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockTest {
	private static final Logger LOG = LoggerFactory.getLogger(BlockTest.class);

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	public void testGetDifficulty() {
		assertEquals(0, Block.getDifficulty(null));
		assertEquals(0, Block.getDifficulty(new char[0]));
		assertEquals(0, Block.getDifficulty(new char[] {'1'}));
		assertEquals(0, Block.getDifficulty(new char[] {'1', '1'}));
		assertEquals(1, Block.getDifficulty(new char[] {'0'}));
		assertEquals(1, Block.getDifficulty(new char[] {'0', '1', '0'}));
		assertEquals(2, Block.getDifficulty(new char[] {'0', '0'}));
		assertEquals(2, Block.getDifficulty(new char[] {'0', '0', '1'}));
		assertEquals(3, Block.getDifficulty(new char[] {'0', '0', '0', '2'}));
	}

	@Test
	public void testMatch() {
		char[] data;
		
		data = Block.getEmptyData(10);
		data[0] = '1';
    assertFalse(Block.match(data, 1));

		data = Block.getEmptyData(10);
		data[0] = '1';
    assertFalse(Block.match(data, 2));

		data = Block.getEmptyData(10);
		data[1] = '1';
    assertFalse(Block.match(data, 2));

		data = Block.getEmptyData(10);
		data[5] = '1';
		assertTrue(Block.match(data, 5));

		data = Block.getEmptyData(10);
		data[0] = '1';
    assertFalse(Block.match(data, 5));

		data = Block.getEmptyData(10);
		data[4] = '1';
    assertFalse(Block.match(data, 5));
		
		data = Block.getEmptyData(10);
		assertTrue(Block.match(data, 10));

		data = Block.getEmptyData(10);
    assertFalse(Block.match(data, 0));

		data = Block.getEmptyData(10);
    assertFalse(Block.match(data, 1));

		data = Block.getEmptyData(10);
    assertFalse(Block.match(data, 2));

		data = Block.getEmptyData(10);
    assertFalse(Block.match(data, 11));
	}
	
	@Test
	public void testBlock0() {
		createBlock(0);
	}
	
	@Test
	public void testBlock1() {
		createBlock(1);
	}
	
	@Test
	public void testBlock2() {
		createBlock(2);
	}
	
	@Test
	public void testBlock3() {
		createBlock(3);
	}
	
	@Test
	public void testBlock4() {
		createBlock(4);
	}

	@Test
	public void testBlock() {
		try {
			Block b = new Block("test", -1);
			char[] hash = Block.getHashChars(b.getHashBytes());
			assertEquals(Block.DEFAULT_DIFFICULTY, Block.getDifficulty(hash));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Test
	public void testBenchmark() {
		Block.benchmark(300);
	}

	private static void createBlock(int difficulty) {
		try {
			Block b = new Block("test", difficulty);
			char[] hash = Block.getHashChars(b.getHashBytes());
			assertEquals(difficulty, Block.getDifficulty(hash));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
