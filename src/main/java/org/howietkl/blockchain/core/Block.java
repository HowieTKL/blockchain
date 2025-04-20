package org.howietkl.blockchain.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Formatter;

public class Block {
  private static final Logger LOG = LoggerFactory.getLogger(Block.class);
  static final int DEFAULT_DIFFICULTY = 3;
  private static final SecureRandom rand = new SecureRandom();
  private static MessageDigest digest;
  private final String data;
  private final long timeStamp;
  private byte[] hash;
  private byte[] previousHash;
  private long nonce;

  public Block(String data, int difficulty) throws IOException, NoSuchAlgorithmException {
    this(data);
    if (difficulty < 0) {
      difficulty = DEFAULT_DIFFICULTY;
    }
    mineBlock(this, difficulty);
  }

  public Block(String data, Block previous) throws IllegalAccessException, IOException, NoSuchAlgorithmException {
    this(data);
    if (previous == null) {
      throw new IllegalAccessException("Previous block must not be null");
    }
    this.previousHash = previous.hash;
    mineBlock(this, getDifficulty(getHashChars(previousHash)));
  }

  private Block(String data) throws NoSuchAlgorithmException {
    digest = MessageDigest.getInstance("SHA3-256");
    this.data = data;
    this.timeStamp = System.currentTimeMillis();
    this.nonce = rand.nextLong();
  }

  public static int benchmark(int upperMillis) {
    long start, end, elapsed;
    Block b;
    int tmpDiff = 0;

    try {
      LOG.info("Benchmark - looking for difficulty within {} ms", upperMillis);
      outer:
      for (; tmpDiff < 10; ++tmpDiff) {
        for (int j = 0; j < 100; ++j) {
          b = new Block("test");
          start = System.currentTimeMillis();
          Block.mineBlock(b, tmpDiff);
          end = System.currentTimeMillis();
          elapsed = end - start;
          if (elapsed > upperMillis) {
            break outer;
          }
        }
      }
      LOG.info("Difficulty={}", tmpDiff);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return tmpDiff;
  }

  static char[] getEmptyData(int len) {
    char[] result = new char[len];
    Arrays.fill(result, '0');
    return result;
  }

  static char[] getHashChars(byte[] hash) {
    CharBuffer buffer = CharBuffer.allocate(digest.getDigestLength() * 2);
    try (Formatter formatter = new Formatter(buffer)) {
      for (byte b : hash) {
        formatter.format("%02x", b);
      }
    }
    return buffer.array();
  }

  static boolean match(char[] data, int difficulty) {
    int tmpDiff = 0;
    for (char datum : data) {
      if (datum == '0') {
        ++tmpDiff;
      } else {
        break;
      }
    }
    return tmpDiff == difficulty;
  }

  static int getDifficulty(char[] hash) {
    int diff = 0;
    if (hash != null) {
      while (diff < hash.length) {
        if (hash[diff] == '0') {
          ++diff;
        } else {
          break;
        }
      }
    }
    return diff;
  }

  private static byte[] computeHash(Block b) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    bytes.writeBytes(long2Bytes(b.nonce));
    if (b.previousHash != null) {
      bytes.write(b.previousHash);
    }
    bytes.write(long2Bytes(b.timeStamp));
    bytes.write(b.data.getBytes(StandardCharsets.UTF_8));
    return digest.digest(bytes.toByteArray());
  }

  private static byte[] long2Bytes(long x) {
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.putLong(x);
    return buffer.array();
  }

  private static void mineBlock(Block b, int difficulty) throws IOException {
    byte[] tmpHash = computeHash(b);
    while (!match(getHashChars(tmpHash), difficulty)) {
      ++b.nonce;
      tmpHash = computeHash(b);
    }
    b.hash = tmpHash;
  }

  public String getHash() {
    return new String(getHashChars(hash));
  }

  byte[] getHashBytes() {
    return hash;
  }

}
