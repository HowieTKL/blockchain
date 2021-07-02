package blockchain.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BlockTest {
	
	@Test
	public void getDifficulty() {
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
	public void match() {
		char[] data;
		
		data = Block.getEmptyData(10);
		data[0] = '1';
		assertTrue(!Block.match(data, 1));

		data = Block.getEmptyData(10);
		data[0] = '1';
		assertTrue(!Block.match(data, 2));

		data = Block.getEmptyData(10);
		data[1] = '1';
		assertTrue(!Block.match(data, 2));

		data = Block.getEmptyData(10);
		data[5] = '1';
		assertTrue(Block.match(data, 5));

		data = Block.getEmptyData(10);
		data[0] = '1';
		assertTrue(!Block.match(data, 5));

		data = Block.getEmptyData(10);
		data[4] = '1';
		assertTrue(!Block.match(data, 5));
		
		data = Block.getEmptyData(10);
		assertTrue(Block.match(data, 10));

		data = Block.getEmptyData(10);
		assertTrue(!Block.match(data, 0));

		data = Block.getEmptyData(10);
		assertTrue(!Block.match(data, 1));

		data = Block.getEmptyData(10);
		assertTrue(!Block.match(data, 2));

		data = Block.getEmptyData(10);
		assertTrue(!Block.match(data, 11));
	}
	
	@Test
	public void Block0() {
		createBlock(0);
	}
	
	@Test
	public void Block1() {
		createBlock(1);
	}
	
	@Test
	public void Block2() {
		createBlock(2);
	}
	
	@Test
	public void Block3() {
		createBlock(3);
	}
	
	@Test
	public void Block4() {
		createBlock(4);
	}

	@Test
	public void Block() {
		try {
			Block b = new Block("test", -1);
			char[] hash = Block.getHashChars(b.getHashBytes());
			assertEquals(new String(hash), Block.DEFAULT_DIFFICULTY, Block.getDifficulty(hash));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void benchmark() {
		Block.benchmark(300);
	}

	private static void createBlock(int difficulty) {
		try {
			Block b = new Block("test", difficulty);
			char[] hash = Block.getHashChars(b.getHashBytes());
			assertEquals(new String(hash), difficulty, Block.getDifficulty(hash));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
